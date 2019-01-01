package org.aio.util;

public enum ResourceMode {

    BANK,
    DROP;

    @Override
    public String toString(){
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
