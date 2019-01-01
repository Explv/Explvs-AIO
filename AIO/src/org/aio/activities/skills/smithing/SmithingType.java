package org.aio.activities.skills.smithing;

public enum SmithingType {

    SMELT_BAR ("Smelt Bar"),
    SMITH_ITEM ("Smith Item"),
    CANNONBALLS ("Cannonballs");

    String name;

    SmithingType(final String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
