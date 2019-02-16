package util;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.MethodProvider;
import util.widget.CachedWidget;

import java.awt.geom.Point2D;
import java.util.Optional;

public class MakeAllInterface extends MethodProvider {

    private CachedWidget itemWidget;
    private CachedWidget titleWidget = new CachedWidget(270, "Choose a quantity, then click an item to begin");
    private int itemOptionNumber = -1;

    public MakeAllInterface(final int itemOptionNumber) {
        this.itemOptionNumber = itemOptionNumber;
    }

    public MakeAllInterface(final String itemWidgetText) {
        itemWidget = new CachedWidget(270, widget -> widget.getSpellName().contains(itemWidgetText));
    }

    public boolean makeAll() {
        if (!isMakeAllScreenOpen()) {
            return false;
        }

        if (itemOptionNumber == -1) {
            itemOptionNumber = getItemOptionNumber();

            if (itemOptionNumber == -1) {
                log("Could not find option number for item: " + itemOptionNumber);
                return false;
            }
        }

        return execute(new Event() {
            @Override
            public int execute() throws InterruptedException {
                if (getKeyboard().typeString(String.valueOf(itemOptionNumber), false)) {
                    setFinished();
                }
                return 200;
            }
        }).hasFinished();
    }

    public boolean isMakeAllScreenOpen() {
        return titleWidget.isVisible(getWidgets());
    }

    private int getItemOptionNumber() {
        Optional<RS2Widget> makeWidget = itemWidget.get(getWidgets());

        if (!makeWidget.isPresent()) {
            log("Could not find item widget");
            return -1;
        }

        RS2Widget optionNumberWidget = getWidgets().singleFilter(270,
                widget -> widget.getMessage() != null &&
                        widget.getMessage().matches("\\d+") &&
                        makeWidget.get().getBounds().contains(
                                new Point2D.Double(
                                        widget.getBounds().getCenterX(),
                                        makeWidget.get().getBounds().getCenterY()
                                )
                        ) &&
                        widget.getAbsY() > makeWidget.get().getAbsY()
        );

        return optionNumberWidget != null ? Integer.parseInt(optionNumberWidget.getMessage()) : 1;
    }
}
