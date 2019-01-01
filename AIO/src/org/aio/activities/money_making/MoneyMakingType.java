package org.aio.activities.money_making;

public enum MoneyMakingType {

    FLAX_PICKING ("Flax picking"),
    VIAL_FILLING ("Vial filling");

    String name;

    MoneyMakingType(final String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
