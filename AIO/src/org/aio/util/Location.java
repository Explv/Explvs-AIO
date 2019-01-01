package org.aio.util;

import org.osbot.rs07.api.map.Area;

public class Location {

    private final String name;
    private final Area area;

    public Location(final String name, final Area area) {
        this.name = name;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public Area getArea() {
        return area;
    }

    @Override
    public String toString(){
        return name;
    }
}
