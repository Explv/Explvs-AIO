package util.custom_method_provider;

import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import util.widget.filters.WidgetActionFilter;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Optional;

public class ExtendedSettings extends Settings {

    public enum Setting {
        HIDE_ROOFS("Hide roofs", "Hide roofs"),
        SHIFT_CLICK_TO_DROP_ITEMS("Shift click to drop items", "Shift click");

        String name;
        String searchString;

        Setting(final String name, final String searchString) {
            this.name = name;
            this.searchString = searchString;
        }
    }

    public enum AllSettingsTab {
        SEARCH("Search", 0xb, 0x1b, 0x2b, 0x3b, 0x4b, 0x5b, 0x70b),
        DISPLAY("Display", 0x8),
        AUDIO("Audio", 0x18),
        CHAT("Chat", 0x28),
        CONTROLS("Controls", 0x38),
        KEYBINDS("Keybinds", 0x48),
        GAMEPLAY("Gameplay", 0x58);

        static final int CONFIG_ID = 2855;
        String tabText;
        int[] configValues;

        AllSettingsTab(final String text, final int... configValues) {
            this.tabText = text;
            this.configValues = configValues;
        }
    }

    public enum BasicSettingsTab {
        CONTROLS("Controls", 0x0),
        AUDIO("Audio", 0x400),
        DISPLAY("Display", 0x800);

        static final int CONFIG_ID = 1795;
        String actionText;
        int configValue;

        BasicSettingsTab(String actionText, int configValue) {
            this.actionText = actionText;
            this.configValue = configValue;
        }
    }

    public boolean isAllSettingsTabOpen(final AllSettingsTab allSettingsTab) {
        final int configValue =  getConfigs().get(AllSettingsTab.CONFIG_ID);
        return Arrays.stream(allSettingsTab.configValues).anyMatch(value -> value == configValue);
    }

    public boolean isBasicSettingsTabOpen(final BasicSettingsTab basicSettingsTab) {
        return getConfigs().get(BasicSettingsTab.CONFIG_ID) == basicSettingsTab.configValue;
    }

    public boolean toggleSetting(final Setting setting) {
        return execute(new Event() {
            private boolean clearedSearch;
            private boolean typedString;

            @Override
            public int execute() throws InterruptedException {

                if (Tab.SETTINGS.isDisabled(getBot())) {
                    setFailed();
                } else if (getTabs().getOpen() != Tab.SETTINGS) {
                    getTabs().open(Tab.SETTINGS);
                } else if (!isAllSettingsWidgetVisible()) {
                    RS2Widget allSettingsWidget = getWidgets().getWidgetContainingText("All Settings");
                    if (allSettingsWidget != null) {
                        allSettingsWidget.interact();
                    }
                } else if (!isAllSettingsTabOpen(AllSettingsTab.SEARCH)) {
                    RS2Widget searchWidget = getWidgets().singleFilter(134, new WidgetActionFilter("Search"));
                    if (searchWidget != null) {
                        searchWidget.interact();
                    }
                } else if (!clearedSearch) {
                    getKeyboard().pressKey(KeyEvent.VK_DELETE);
                    sleep(random(1000, 2000));
                    clearedSearch = true;
                } else if (!typedString) {
                    typedString = getKeyboard().typeString(setting.searchString);
                } else{
                    RS2Widget firstSearchResult = getFirstSearchResult();
                    if (firstSearchResult != null) {
                        if (firstSearchResult.interact()) {
                            sleep(random(1000, 2000));
                            setFinished();
                        }
                    } else {
                        setFailed();
                    }
                }
                return 0;
            }

            private RS2Widget getFirstSearchResult() {
                RS2Widget[] searchResults = getWidgets().get(134, 17).getChildWidgets();
                Optional<RS2Widget> targetWidget = Arrays.stream(searchResults).filter(RS2Widget::isVisible).findFirst();
                return targetWidget.orElse(null);
            }
        }).hasFinished();
    }

    public boolean openAllSettingsTab(final AllSettingsTab allSettingsTab) {
        if (isAllSettingsTabOpen(allSettingsTab)) {
            return true;
        }
        RS2Widget targetWidget = getWidgets().singleFilter(widget -> widget.getMessage().equals(allSettingsTab.tabText));
        if (targetWidget != null) {
            return targetWidget.interact();
        }
        return false;
    }

    public boolean openBasicSettingsTab(final BasicSettingsTab basicSettingsTab) {
        if (isBasicSettingsTabOpen(basicSettingsTab)) {
            return true;
        }
        RS2Widget targetWidget = getWidgets().singleFilter(116, new WidgetActionFilter(basicSettingsTab.actionText));
        if (targetWidget != null) {
            return targetWidget.interact();
        }
        return false;
    }

    public boolean isAllSettingsWidgetVisible() {
        return getWidgets().singleFilter(widget -> widget.getMessage().equals("Settings")) != null;
    }
}
