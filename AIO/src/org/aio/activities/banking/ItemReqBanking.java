package org.aio.activities.banking;

import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemReqBanking extends Banking {

    private final ItemReq[] itemReqs;
    private final Map<ItemReq, Integer> reqTargetAmountMap = new HashMap<>();
    private final Filter<Item> itemReqFilter;

    public ItemReqBanking(final ItemReq... itemReqs) {
        this.itemReqs = itemReqs;
        itemReqFilter = item -> Stream.of(itemReqs).anyMatch(req -> req.isRequirementItem(item));
        loadItemReqTargetAmounts();
    }

    @Override
    public void run() throws InterruptedException {
        if (!Bank.inAnyBank(myPosition())) {
            getWalking().webWalk(bankAreas);
        } else {
            bank();
        }
    }

    private void loadItemReqTargetAmounts() {
        int slotsRemaining = 28;

        for (ItemReq itemReq : itemReqs) {
            if (itemReq.isStackable()) {
                reqTargetAmountMap.put(itemReq, itemReq.getMaxQuantity());

                // If the item is equipable it won't take any slots in the inventory
                if (!itemReq.isEquipable()) {
                    slotsRemaining--;
                }
            }
        }

        while (slotsRemaining > 0) {
            boolean noChange = true;

            for (final ItemReq itemReq : itemReqs) {
                if (!itemReq.isStackable()) {
                    if (itemReq.getMinQuantity() > slotsRemaining) {
                        break;
                    }
                    Integer existingAmount = reqTargetAmountMap.get(itemReq);

                    if (existingAmount == null) {
                        reqTargetAmountMap.put(itemReq, itemReq.getMinQuantity());

                        // If the item is equipable it won't take any slots in the inventory
                        if (!itemReq.isEquipable()) {
                            slotsRemaining -= itemReq.getMinQuantity();
                        }
                        noChange = false;
                    } else if (itemReq.getMaxQuantity() == ItemReq.QUANTITY_ALL || existingAmount < itemReq.getMaxQuantity()) {
                        reqTargetAmountMap.put(itemReq, existingAmount + itemReq.getMinQuantity());

                        // If the item is equipable it won't take any slots in the inventory
                        if (!itemReq.isEquipable()) {
                            slotsRemaining -= itemReq.getMinQuantity();
                        }
                        noChange = false;
                    }
                }
            }

            if (noChange) {
                break;
            }
        }
    }

    @Override
    protected boolean bank() {
        reqTargetAmountMap.forEach((itemRequirement, integer) -> {
            if (itemRequirement != null && integer != null) {
                log(itemRequirement.getName() + ": " + integer);
            }
        });

        if (getInventory().contains(item -> !itemReqFilter.match(item))) {
            depositNonItemReqs();
        } else if (!ItemReq.hasItemRequirements(itemReqs, getInventory(), getEquipment())) {
            getItemRequirements(itemReqs);
        }

        return true;
    }

    private boolean depositNonItemReqs() {
        return execute(new Event() {
            @Override
            public int execute() throws InterruptedException {
                if (!getBank().isOpen()) {
                    getBank().open();
                } else if (getInventory().contains(item -> !itemReqFilter.match(item))) {
                    getBank().depositAll(item -> !itemReqFilter.match(item));
                } else {
                    setFinished();
                }
                return 600;
            }
        }).hasFinished();
    }

    private void getItemRequirements(final ItemReq... itemReqs) {
        final List<ItemReq> itemReqList = Arrays.asList(itemReqs);

        List<ItemReq> equipableItemReqs = itemReqList.stream().filter(ItemReq::isEquipable).collect(Collectors.toList());
        Filter<Item> equipableItemReqFilter = item -> equipableItemReqs.stream().anyMatch(req -> req.isRequirementItem(item));

        if (!equipableItemReqs.isEmpty()) {
            // First we want to withdraw any equipable item reqs
            Event withdrawEquipables = execute(new Event() {
                Queue<ItemReq> equipableItemReqQueue = new LinkedList<>(equipableItemReqs);

                @Override

                public int execute() throws InterruptedException {
                    if (equipableItemReqQueue.isEmpty()) {
                        setFinished();
                    } else if (!getBank().isOpen()) {
                        getBank().open();
                    } else if (getInventory().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositAllExcept(equipableItemReqFilter);
                    } else if (getEquipment().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositWornItems();
                    } else {
                        ItemReq itemReq = equipableItemReqQueue.peek();
                        if (!itemReq.hasRequirement(getInventory(), getEquipment())) {
                            if (!getItemReq(itemReq)) {
                                setFailed();
                            }
                        } else {
                            equipableItemReqQueue.poll();
                        }
                    }
                    return 600;
                }
            });

            if (withdrawEquipables.hasFailed()) {
                setFailed();
                return;
            }

            // Now we want to equip the item reqs
            execute(new Event() {
                Queue<ItemReq> equipableItemReqQueue = new LinkedList<>(equipableItemReqs);

                @Override
                public int execute() throws InterruptedException {
                    if (equipableItemReqQueue.isEmpty()) {
                        setFinished();
                    } else if (getBank().isOpen()) {
                        getBank().close();
                    } else {
                        ItemReq itemReq = equipableItemReqQueue.peek();
                        if (getInventory().contains(itemReq.getName())) {
                            getInventory().getItem(itemReq.getName()).interact();
                        } else {
                            equipableItemReqQueue.poll();
                        }
                    }
                    return 600;
                }
            });
        }

        final List<ItemReq> nonEquipableItemReqsList = itemReqList.stream().filter(req -> !req.isEquipable()).collect(Collectors.toList());

        // Finally we want to withdraw any remaining item reqs
        Event withdrawNonEquipableEvent = execute(new Event() {
            Queue<ItemReq> nonEquipableItemReqs = new LinkedList<>(nonEquipableItemReqsList);

            @Override
            public int execute() throws InterruptedException {
                if (!getBank().isOpen()) {
                    getBank().open();
                } else if (getInventory().contains(item -> !itemReqFilter.match(item))) {
                    getBank().depositAllExcept(itemReqFilter);
                } else if (nonEquipableItemReqs.isEmpty()) {
                    setFinished();
                } else {
                    ItemReq itemReq = nonEquipableItemReqs.peek();

                    if (!itemReq.hasRequirement(getInventory(), getEquipment())) {
                        if (!getItemReq(itemReq)) {
                            setFailed();
                        }
                    } else {
                        nonEquipableItemReqs.poll();
                    }
                }
                return 600;
            }
        });
        if (withdrawNonEquipableEvent.hasFailed()) {
            setFailed();
        }
    }

    private boolean getItemReq(final ItemReq itemReq) {
        int amountOnPlayer = (int) getAmountOnPlayer(itemReq);
        int targetAmount = reqTargetAmountMap.get(itemReq);

        if (itemReq.isNoted() && getBank().getWithdrawMode() != org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE) {
            getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
        } else if (!itemReq.isNoted() && getBank().getWithdrawMode() != org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM) {
            getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM);
        } else if (targetAmount != ItemReq.QUANTITY_ALL && amountOnPlayer > targetAmount) {
            int excessAmount = amountOnPlayer - reqTargetAmountMap.get(itemReq);
            if (getBank().deposit(itemReq.getName(), excessAmount)) {
                Sleep.sleepUntil(() -> getAmountOnPlayer(itemReq) < amountOnPlayer, 1200, 600);
            }
        } else if (targetAmount == ItemReq.QUANTITY_ALL || amountOnPlayer < reqTargetAmountMap.get(itemReq)) {
            if (amountOnPlayer < itemReq.getMinQuantity()) {
                int requiredAmountForMinQuantity = Math.max(0, itemReq.getMinQuantity() - amountOnPlayer);
                int bankAmount = (int) itemReq.getAmount(getBank());

                if (bankAmount < requiredAmountForMinQuantity) {
                    log("Not enough " + itemReq.getName() + " in bank. Required: " + requiredAmountForMinQuantity + ", Found: " + bankAmount);
                    return false;
                }
            }
            if (targetAmount == ItemReq.QUANTITY_ALL) {
                getBank().withdrawAll(itemReq.getName());
            } else {
                int requiredTargetAmount = reqTargetAmountMap.get(itemReq) - amountOnPlayer;
                if (getBank().withdraw(itemReq.getName(), requiredTargetAmount)) {
                    Sleep.sleepUntil(() -> getAmountOnPlayer(itemReq) > amountOnPlayer, 1200, 600);
                }
            }
        }
        return true;
    }

    private long getAmountOnPlayer(final ItemReq itemReq) {
        return itemReq.getAmount(getInventory(), getEquipment());
    }
}
