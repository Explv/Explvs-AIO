package org.aio.util;

import org.osbot.rs07.api.Widgets;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.ui.RS2Widget;

import java.util.Optional;

public class CachedWidget {

    private int parentID = -1, childID = -1, subChildID = -1;
    private String[] widgetTexts;
    private Filter<RS2Widget> filter;

    public CachedWidget(final int parentID, final int childID){
        this.parentID = parentID;
        this.childID = childID;
    }

    public CachedWidget(final int parentID, final int childID, final int subChildID){
        this.parentID = parentID;
        this.childID = childID;
        this.subChildID = subChildID;
    }

    public CachedWidget(final int parentID, final String... widgetTexts) {
        this.parentID = parentID;
        this.widgetTexts = widgetTexts;
    }

    public CachedWidget(final String... widgetTexts){
        this.widgetTexts = widgetTexts;
    }

    public CachedWidget(final int parentID, final Filter<RS2Widget> filter) {
        this.parentID = parentID;
        this.filter = filter;
    }

    public CachedWidget(final Filter<RS2Widget> filter) {
        this.filter = filter;
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
        if(parentID != -1 && childID != -1 && subChildID != -1) {
            return Optional.ofNullable(widgets.get(parentID, childID, subChildID));
        } else if(parentID != -1 && childID != -1) {
            return getSecondLevelWidget(widgets);
        } else if (widgetTexts != null) {
            return getWidgetWithText(widgets);
        } else {
            return getWidgetUsingFilter(widgets);
        }
    }

    private Optional<RS2Widget> getSecondLevelWidget(final Widgets widgets){
        RS2Widget rs2Widget = widgets.get(parentID, childID);
        if(rs2Widget != null && rs2Widget.isThirdLevel()){
            subChildID = rs2Widget.getThirdLevelId();
        }
        return Optional.ofNullable(rs2Widget);
    }

    private Optional<RS2Widget> getWidgetWithText(final Widgets widgets){
        RS2Widget rs2Widget;
        if (parentID != -1) {
            rs2Widget = widgets.getWidgetContainingText(parentID, widgetTexts);
        } else {
            rs2Widget = widgets.getWidgetContainingText(widgetTexts);
        }
        setWidgetIDs(rs2Widget);
        return Optional.ofNullable(rs2Widget);
    }

    private Optional<RS2Widget> getWidgetUsingFilter(final Widgets widgets) {
        RS2Widget rs2Widget;
        if (parentID != -1) {
            rs2Widget = widgets.singleFilter(parentID, filter);
        } else {
            rs2Widget = widgets.singleFilter(widgets.getAll(), filter);
        }
        setWidgetIDs(rs2Widget);
        return Optional.ofNullable(rs2Widget);
    }

    private void setWidgetIDs(final RS2Widget rs2Widget) {
        if (rs2Widget == null) {
            return;
        }
        parentID = rs2Widget.getRootId();
        childID = rs2Widget.getSecondLevelId();
        if (rs2Widget.isThirdLevel()) {
            subChildID = rs2Widget.getThirdLevelId();
        }
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

    @Override
    public String toString() {
        return parentID + ", " + childID + ", " + subChildID;
    }
} 