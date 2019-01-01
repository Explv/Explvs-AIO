package org.aio.activities.skills.cooking;

public enum CookingType {

    MEAT,
    PIE,
    PIZZA,
    CAKE,
    DRINK;

    @Override
    public String toString(){
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
