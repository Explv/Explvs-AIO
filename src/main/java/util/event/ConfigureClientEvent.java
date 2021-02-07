package util.event;

import org.osbot.rs07.api.Display;
import org.osbot.rs07.api.Settings;
import org.osbot.rs07.event.Event;

public class ConfigureClientEvent extends Event {

    private boolean isAudioDisabled = false;

    @Override
    public int execute() throws InterruptedException {
        if (getDisplay().isResizableMode()) {
            getDisplay().setDisplayMode(Display.DisplayMode.FIXED);
        } if (!getSettings().areRoofsEnabled()) {
            getSettings().setSetting(Settings.AllSettingsTab.DISPLAY, "Hide roofs", true);
        } else if (!getSettings().isShiftDropActive()) {
            getSettings().setSetting(Settings.AllSettingsTab.CONTROLS, "Shift click to drop items", true);
        } else if (getSettings().isAllSettingsWidgetVisible()){
            getWidgets().closeOpenInterface();
        } else if (!isAudioDisabled) {
            execute(new DisableAudioEvent());
            isAudioDisabled = true;
        } else {
            setFinished();
        }
        return 600;
    }


}
