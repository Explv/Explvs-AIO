package util.event;

import org.osbot.rs07.api.Display;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import util.custom_method_provider.ExtendedSettings;
import util.executable.BlockingExecutable;

public class ConfigureClientEvent extends BlockingExecutable {

    private boolean isAudioDisabled = false;

    @Override
    protected void blockingRun() throws InterruptedException {
        if (getDisplay().isResizableMode()) {
            getDisplay().setDisplayMode(Display.DisplayMode.FIXED);
        } else if (!getSettings().areRoofsEnabled()) {
            getSettings().toggleSetting(ExtendedSettings.Setting.HIDE_ROOFS);
        } else if (!getSettings().isShiftDropActive()) {
            getSettings().toggleSetting(ExtendedSettings.Setting.SHIFT_CLICK_TO_DROP_ITEMS);
        } else if (getSettings().isAllSettingsWidgetVisible()) {
            getWidgets().closeOpenInterface();
        } else if (!isAudioDisabled) {
            execute(new DisableAudioEvent());
            isAudioDisabled = true;
        } else {
            setFinished();
        }
    }
}
