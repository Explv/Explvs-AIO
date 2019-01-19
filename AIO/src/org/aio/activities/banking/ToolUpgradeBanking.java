package org.aio.activities.banking;

import org.aio.util.Tool;
import org.osbot.rs07.api.ui.EquipmentSlot;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public class ToolUpgradeBanking<T extends Enum<T> & Tool> extends Banking {

    private final Class<T> toolClass;
    private final EnumSet<T> toolsInBank;
    private boolean checkedForTools;
    private T currentTool;

    public ToolUpgradeBanking(final Class<T> toolClass) {
        this.toolClass = toolClass;
        toolsInBank = EnumSet.noneOf(toolClass);
    }

    public T getCurrentTool() {
        return currentTool;
    }

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
        currentTool = Stream.of(toolClass.getEnumConstants())
                            .filter(tool -> tool.canUse(getSkills()))
                            .filter(tool -> getInventory().contains(tool.getName()) ||
                                            getEquipment().isWearingItem(EquipmentSlot.WEAPON, tool.getName()))
                            .max(Comparator.comparingInt(tool -> tool.getLevelRequired()))
                            .orElse(null);
    }

    public boolean toolUpgradeAvailable() {
        return currentTool == null ||
               !checkedForTools ||
               bankContainsBetterTool();
    }

    @Override
    protected boolean bank() {
        if (toolsInBank.isEmpty()) {
            for (T tool : toolClass.getEnumConstants()) {
                if (getBank().contains(tool.getName())) {
                    toolsInBank.add(tool);
                }
            }
            checkedForTools = true;
        }

        if (currentTool == null || bankContainsBetterTool()) {
            if (toolsInBank.isEmpty()) {
                setFailed();
                return false;
            }

            if (!getInventory().isEmpty()) {
                getBank().depositAll();
            } else {
                Optional<T> bestTool = getBestUsableTool();
                if (bestTool.isPresent()) {
                    if (getBank().withdraw(bestTool.get().getName(), 1)) {
                        currentTool = bestTool.get();
                    }
                } else {
                    setFailed();
                    return false;
                }
            }
        } else if (!getInventory().isEmptyExcept(currentTool.getName())) {
            getBank().depositAllExcept(currentTool.getName());
        }

        return true;
    }

    private boolean bankContainsBetterTool() {
        return toolsInBank.stream()
                .anyMatch(axe ->
                        axe.canUse(getSkills()) &&
                        axe.getLevelRequired() > currentTool.getLevelRequired()
                );
    }

    private Optional<T> getBestUsableTool() {
       return toolsInBank.stream()
                         .filter(tool -> tool.canUse(getSkills()))
                         .max(Comparator.comparingInt(tool -> tool.getLevelRequired()));
    }
}
