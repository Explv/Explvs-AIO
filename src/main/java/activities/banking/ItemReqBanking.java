package activities.banking;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;
import util.Sleep;
import util.item_requirement.ItemReq;

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
            getWalking().webWalk(Bank.AREAS);
        } else {
            // We can never withdraw item requirements from a deposit box
            // so we hardcode to BankType.BANK
            bank(BankType.BANK);
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
    protected boolean bank(final BankType currentBankType) {
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

        final List<ItemReq> equipableItemReqs = itemReqList.stream().filter(ItemReq::isEquipable).collect(Collectors.toList());
        final Filter<Item> equipableItemReqFilter = item -> equipableItemReqs.stream().anyMatch(req -> req.isRequirementItem(item));
        final List<ItemReq> nonEquipableItemReqsList = itemReqList.stream().filter(req -> !req.isEquipable()).collect(Collectors.toList());

        // First we deposit any items that are not a requirement
        execute(new Event() {
            @Override
            public int execute() throws InterruptedException {
                if (!getInventory().contains(item -> !itemReqFilter.match(item))) {
                    setFinished();
                    return 0;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                } else {
                    getBank().depositAllExcept(itemReqFilter);
                }
                return 600;
            }
        });

        // Then we deposit any excess item reqs we have
        execute(new Event() {
            Queue<ItemReq> nonEquipableItemReqs = new LinkedList<>(nonEquipableItemReqsList);

            @Override
            public int execute() throws InterruptedException {
                if (nonEquipableItemReqs.isEmpty()) {
                    setFinished();
                    return 0;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                } else {
                    ItemReq itemReq = nonEquipableItemReqs.peek();

                    int targetAmount = reqTargetAmountMap.get(itemReq);
                    long amountOnPlayer = itemReq.getAmount(getInventory(), getEquipment());

                    if (amountOnPlayer > targetAmount && targetAmount != ItemReq.QUANTITY_ALL) {
                        depositExcess(itemReq);
                    } else {
                        nonEquipableItemReqs.poll();
                    }
                }
                return 600;
            }
        });


        if (!equipableItemReqs.isEmpty()) {
            // Now we want to withdraw any equipable item reqs
            Event withdrawEquipables = execute(new Event() {
                Queue<ItemReq> equipableItemReqQueue = new LinkedList<>(equipableItemReqs);

                @Override

                public int execute() throws InterruptedException {
                    if (equipableItemReqQueue.isEmpty()) {
                        setFinished();
                        return 0;
                    }

                    if (!getBank().isOpen()) {
                        getBank().open();
                    } else if (getInventory().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositAllExcept(equipableItemReqFilter);
                    } else if (getEquipment().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositWornItems();
                    } else {
                        ItemReq itemReq = equipableItemReqQueue.peek();
                        if (!itemReq.hasRequirement(getInventory(), getEquipment())) {
                            if (!withdrawItemReq(itemReq)) {
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

        // Finally we want to withdraw any remaining item reqs
        Event withdrawNonEquipableEvent = execute(new Event() {
            Queue<ItemReq> nonEquipableItemReqs = new LinkedList<>(nonEquipableItemReqsList);

            @Override
            public int execute() throws InterruptedException {
                if (nonEquipableItemReqs.isEmpty()) {
                    setFinished();
                    return 0;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                    return 0;
                }

                ItemReq itemReq = nonEquipableItemReqs.peek();

                int targetAmount = reqTargetAmountMap.get(itemReq);
                long amountOnPlayer = itemReq.getAmount(getInventory(), getEquipment());

                if (amountOnPlayer < targetAmount) {
                    if (!withdrawItemReq(itemReq)) {
                        setFailed();
                    }
                } else {
                    nonEquipableItemReqs.poll();
                }

                return 600;
            }
        });

        if (withdrawNonEquipableEvent.hasFailed()) {
            setFailed();
        }
    }

    private boolean depositExcess(final ItemReq itemReq) {
        int amountOnPlayer = (int) getAmountOnPlayer(itemReq);

        int excessAmount = amountOnPlayer - reqTargetAmountMap.get(itemReq);
        if (getBank().deposit(itemReq.getName(), excessAmount)) {
            Sleep.sleepUntil(() -> getAmountOnPlayer(itemReq) < amountOnPlayer, 1200, 600);
            return true;
        }

        return false;
    }

    private boolean withdrawItemReq(final ItemReq itemReq) {
        if (itemReq.isNoted() && getBank().getWithdrawMode() != org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE) {
            getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
        } else if (!itemReq.isNoted() && getBank().getWithdrawMode() != org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM) {
            getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM);
        } else {
            int amountOnPlayer = (int) getAmountOnPlayer(itemReq);
            int targetAmount = reqTargetAmountMap.get(itemReq);

            if (amountOnPlayer < itemReq.getMinQuantity()) {
                int requiredAmountForMinQuantity = Math.max(0, itemReq.getMinQuantity() - amountOnPlayer);
                int bankAmount = (int) itemReq.getAmount(getBank());

                if (bankAmount < requiredAmountForMinQuantity) {
                    log("Not enough " + itemReq.getName() + " in bank. Required: " + requiredAmountForMinQuantity + ", Found: " + bankAmount);
                    return false;
                }
            }
            if (targetAmount == getInventory().getEmptySlots()) {
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
