package activities.banking;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.Tool;
import util.executable.ExecutionFailedException;

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
                .filter(tool -> !tool.isMembersOnly() || getWorlds().isMembersWorld())
                .filter(tool -> tool.canUse(getSkills()))
                .filter(tool -> getInventory().contains(tool.getName()) ||
                        getEquipment().isWearingItem(EquipmentSlot.WEAPON, tool.getName()))
                // NOTE: Do not replace this with a method reference Tool::getLevelRequired
                // There is a bug in the Java compiler which causes the code to crash.
                .max(Comparator.comparingInt(tool -> tool.getLevelRequired()))
                .orElse(null);
    }

    public boolean toolUpgradeAvailable() {
        return currentTool == null ||
                !checkedForTools ||
                bankContainsBetterTool();
    }

    @Override
    protected void bank(final BankType currentBankType) {
        if (toolsInBank.isEmpty()) {
            for (T tool : toolClass.getEnumConstants()) {
                if (!getWorlds().isMembersWorld() && tool.isMembersOnly()) {
                    log(String.format("Skipping tool '%s' as not in members world", tool.getName()));
                    continue;
                }
                if (getBank().contains(tool.getName())) {
                    log(String.format("Found tool '%s' in the bank", tool.getName()));
                    toolsInBank.add(tool);
                }
            }
            checkedForTools = true;
        }

        if (currentTool == null || bankContainsBetterTool()) {
            if (toolsInBank.isEmpty()) {
                throw new ExecutionFailedException(
                        String.format("No tools of class '%s' found in bank", toolClass.getName())
                );
            }

            if (!getInventory().isEmpty()) {
                getBank().depositAll();
            } else if (getBank().getWithdrawMode() != Bank.BankMode.WITHDRAW_ITEM) {
                getBank().enableMode(Bank.BankMode.WITHDRAW_ITEM);
            } else {
                Optional<T> bestTool = getBestUsableTool();
                if (bestTool.isPresent()) {
                    if (getBank().withdraw(bestTool.get().getName(), 1)) {
                        currentTool = bestTool.get();
                    }
                } else {
                    throw new ExecutionFailedException(
                            String.format("No usable tools of class '%s' found in bank", toolClass.getName())
                    );
                }
            }
        } else if (!getInventory().isEmptyExcept(currentTool.getName())) {
            getBank().depositAllExcept(currentTool.getName());
        } else {
            setFinished();
        }
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
                // NOTE: Do not replace this with a method reference Tool::getLevelRequired
                // There is a bug in the Java compiler which causes the code to crash.
                .max(Comparator.comparingInt(tool -> tool.getLevelRequired()));
    }
}
