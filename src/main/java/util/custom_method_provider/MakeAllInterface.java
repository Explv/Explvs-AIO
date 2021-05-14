package util.custom_method_provider;

import org.osbot.rs07.api.ui.RS2Widget;
import util.executable.BlockingExecutable;
import util.executable.ExecutionFailedException;
import util.widget.CachedWidget;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MakeAllInterface extends CustomMethodProvider {

    private static final Map<String, Integer> widgetOptionNumbers = new ConcurrentHashMap<>();

    private CachedWidget titleWidget = new CachedWidget(270, "Choose a quantity, then click an item to begin");

    public boolean isOpen() {
        return titleWidget.isVisible(getWidgets());
    }

    public void makeAll(int itemOptionNumber) throws InterruptedException {
        execute(new BlockingExecutable() {
            @Override
            protected void blockingRun() {
                if (!isOpen()) {
                    throw new ExecutionFailedException("Make all interface is not open");
                }

                if (getKeyboard().typeString(String.valueOf(itemOptionNumber), false)) {
                    setFinished();
                }
            }
        });
    }

    public void makeAll(String widgetText) throws InterruptedException {
        execute(new BlockingExecutable() {
            @Override
            protected void blockingRun() {
                if (!isOpen()) {
                    throw new ExecutionFailedException("Make all interface is not open");
                }

                int itemOptionNumber = getItemOptionNumber(widgetText);

                if (getKeyboard().typeString(String.valueOf(itemOptionNumber), false)) {
                    setFinished();
                }
            }
        });
    }

    private int getItemOptionNumber(String widgetText) {
        if (widgetOptionNumbers.containsKey(widgetText)) {
            return widgetOptionNumbers.get(widgetText);
        }

        RS2Widget makeWidget = getWidgets().singleFilter(
                270,
                widget -> widget.getSpellName().contains(widgetText)
        );

        if (makeWidget == null) {
            throw new ExecutionFailedException("Could not find item widget");
        }

        RS2Widget optionNumberWidget = getWidgets().singleFilter(270,
                widget -> widget.getMessage() != null &&
                        widget.getMessage().matches("\\d+") &&
                        makeWidget.getBounds().contains(
                                new Point2D.Double(
                                        widget.getBounds().getCenterX(),
                                        makeWidget.getBounds().getCenterY()
                                )
                        ) &&
                        widget.getAbsY() > makeWidget.getAbsY()
        );

        int optionNumber = optionNumberWidget != null ? Integer.parseInt(optionNumberWidget.getMessage()) : 1;

        widgetOptionNumbers.put(widgetText, optionNumber);

        return optionNumber;
    }
}
