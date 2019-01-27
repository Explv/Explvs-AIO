package org.aio.util.widget;

import org.osbot.rs07.api.Widgets;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.ui.RS2Widget;

import java.util.Optional;

/**
 * This class provides a clean and efficient way of accessing RS2Widgets.
 * Widgets can be found using IDs, the message, or any other Filter<RS2Widget>
 * Once the widget is found, it's IDs are stored, so any future lookup will be performed in O(1) time.
 *
 * Note that the class is thread safe, and so instances of this class may be static.
 */
public class CachedWidget {

    private int rootID = -1, secondLevelID = -1, thirdLevelID = -1;
    private String[] widgetTexts;
    private Filter<RS2Widget> filter;

    public CachedWidget(final int rootID, final int secondLevelID){
        this.rootID = rootID;
        this.secondLevelID = secondLevelID;
    }

    public CachedWidget(final int rootID, final int secondLevelID, final int thirdLevelID){
        this.rootID = rootID;
        this.secondLevelID = secondLevelID;
        this.thirdLevelID = thirdLevelID;
    }

    public CachedWidget(final int rootID, final String... widgetTexts) {
        this.rootID = rootID;
        this.widgetTexts = widgetTexts;
    }

    public CachedWidget(final String... widgetTexts){
        this.widgetTexts = widgetTexts;
    }

    public CachedWidget(final int rootID, final Filter<RS2Widget> filter) {
        this.rootID = rootID;
        this.filter = filter;
    }

    public CachedWidget(final Filter<RS2Widget> filter) {
        this.filter = filter;
    }

    public CachedWidget(final RS2Widget rs2Widget) {
        setWidgetIDs(rs2Widget);
    }

    public boolean isVisible(final Widgets widgets) {
        return get(widgets).map(RS2Widget::isVisible)
                           .orElse(false);
    }

    public boolean interact(final Widgets widgets, final String interaction) {
        return get(widgets).filter(RS2Widget::isVisible)
                           .map(w -> w.interact(interaction))
                           .orElse(false);
    }

    public Optional<RS2Widget> getParent(final Widgets widgets) {
        return get(widgets).map(widget -> {
            if (widget.isSecondLevel()) {
                return widget;
            }
            return widgets.get(widget.getRootId(), widget.getSecondLevelId());
        });
    }

    public Optional<RS2Widget> get(final Widgets widgets){
        if(rootID != -1 && secondLevelID != -1 && thirdLevelID != -1) {
            return Optional.ofNullable(widgets.get(rootID, secondLevelID, thirdLevelID));
        } else if(rootID != -1 && secondLevelID != -1) {
            return getSecondLevelWidget(widgets);
        } else if (widgetTexts != null) {
            return getWidgetWithText(widgets);
        } else {
            return getWidgetUsingFilter(widgets);
        }
    }

    private Optional<RS2Widget> getSecondLevelWidget(final Widgets widgets){
        RS2Widget rs2Widget = widgets.get(rootID, secondLevelID);
        if(rs2Widget != null && rs2Widget.isThirdLevel()){
            setWidgetIDs(rs2Widget);
        }
        return Optional.ofNullable(rs2Widget);
    }

    private Optional<RS2Widget> getWidgetWithText(final Widgets widgets){
        RS2Widget rs2Widget;
        if (rootID != -1) {
            rs2Widget = widgets.getWidgetContainingText(rootID, widgetTexts);
        } else {
            rs2Widget = widgets.getWidgetContainingText(widgetTexts);
        }
        setWidgetIDs(rs2Widget);
        return Optional.ofNullable(rs2Widget);
    }

    private Optional<RS2Widget> getWidgetUsingFilter(final Widgets widgets) {
        RS2Widget rs2Widget;
        if (rootID != -1) {
            rs2Widget = widgets.singleFilter(rootID, filter);
        } else {
            rs2Widget = widgets.singleFilter(widgets.getAll(), filter);
        }
        setWidgetIDs(rs2Widget);
        return Optional.ofNullable(rs2Widget);
    }

    private synchronized void setWidgetIDs(final RS2Widget rs2Widget) {
        if (rs2Widget == null) {
            return;
        }
        rootID = rs2Widget.getRootId();
        secondLevelID = rs2Widget.getSecondLevelId();
        if (rs2Widget.isThirdLevel()) {
            thirdLevelID = rs2Widget.getThirdLevelId();
        }
    }

    @Override
    public String toString() {
        return rootID + ", " + secondLevelID + ", " + thirdLevelID;
    }
} 