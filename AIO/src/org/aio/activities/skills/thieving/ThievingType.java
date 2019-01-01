package org.aio.activities.skills.thieving;

public enum ThievingType {

    NPC,
    STALL;
    //CHEST;

    @Override
    public String toString(){
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
