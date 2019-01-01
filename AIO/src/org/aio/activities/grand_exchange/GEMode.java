package org.aio.activities.grand_exchange;

public enum GEMode {

    BUY,
    SELL;

    @Override
    public String toString(){
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
