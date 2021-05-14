package util.custom_method_provider;

import org.osbot.rs07.api.Camera;
import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.MethodProvider;

import java.util.function.Supplier;

public class ExtendedCamera extends Camera {

    private static final int MAX_PITCH_ANGLE = 67;
    private static final int MAX_ZOOM_OUT_SCALE = 181;

    public boolean isAtTop() {
        return getCamera().getPitchAngle() >= MAX_PITCH_ANGLE;
    }

    public int clampAngle(final int angle) {
        if (angle < 0) {
            return angle + 360;
        } else if (angle > 360) {
            return angle - 360;
        }
        return angle;
    }

    public void moveYawUntilObjectVisible(final Supplier<Entity> entitySupplier) {
        execute(new Event() {
            @Override
            public int execute() {
                if (entitySupplier.get().isVisible()) {
                    setFinished();
                } else {
                    int nextYaw = clampAngle(getCamera().getYawAngle() + random(5, 10));
                    getCamera().moveYaw(nextYaw);
                }
                return 0;
            }
        });
    }

    public boolean isZoomedOut() {
        return getCamera().getScaleZ() == MAX_ZOOM_OUT_SCALE;
    }

    public void zoomOut() {
        if (isZoomedOut()) {
            log("IS zoomed out");
            return;
        }
        execute(new Event() {
            @Override
            public int execute() {
                if (isZoomedOut()) {
                    setFinished();
                } else if (getTabs().getOpen() != Tab.SETTINGS) {
                    getTabs().open(Tab.SETTINGS);
                } else if (getSettings().getCurrentBasicSettingsTab() != Settings.BasicSettingsTab.CONTROLS) {
                    log("Opening tab");
                    getSettings().open(Settings.BasicSettingsTab.CONTROLS);
                } else {
                    RS2Widget maxZoomOutWidget = getWidgets().get(116, 51);
                    if (maxZoomOutWidget != null) {
                        maxZoomOutWidget.interact();
                    }
                }
                return 0;
            }
        });
    }
}
