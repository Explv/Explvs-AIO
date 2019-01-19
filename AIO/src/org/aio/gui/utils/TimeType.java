package org.aio.gui.utils;

public enum TimeType {
    DURATION("Duration"),
    DATE_TIME("Date / Time");

    private String name;

    TimeType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
