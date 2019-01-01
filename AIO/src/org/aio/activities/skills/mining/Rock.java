package org.aio.activities.skills.mining;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

import java.util.stream.Stream;

public enum Rock {

    CLAY(new short[]{6705}, "Clay"),
    COPPER(new short[]{4645, 4510}, "Copper ore"),
    TIN(new short[]{53}, "Tin ore"),
    IRON(new short[]{2576}, "Iron ore"),
    SILVER(new short[]{74}, "Silver"),
    COAL(new short[]{10508}, "Coal"),
    GOLD(new short[]{8885}, "Gold"),
    MITHRIL(new short[]{-22239}, "Mithril ore"),
    ADAMANTITE(new short[]{21662}, "Adamantite ore"),
    RUNITE(new short[]{-31437}, "Runite ore"),
    RUNE_ESSENCE(null, "Rune essence");

    public String ORE;
    public short[] COLOURS;

    Rock(final short[] COLOURS, final String ORE) {
        this.COLOURS = COLOURS;
        this.ORE = ORE;
    }

    public RS2Object getClosestWithOre(final MethodProvider methods, final Filter<RS2Object>... filters) {
        //noinspection unchecked
        return methods.getObjects().closest(
                obj -> Stream.of(filters).allMatch(f -> f.match(obj))
                       && hasOre(obj)
        );
    }

    public boolean hasOre(final Entity rockEntity) {
        if (rockEntity.getDefinition() == null) {
            return false;
        }

        short[] colours = rockEntity.getDefinition().getModifiedModelColors();

        if (colours == null) {
            return false;
        }

        for (short rockColour : this.COLOURS) {
            for (short entityColour : colours) {
                if (rockColour == entityColour) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString(){
        char[] name = name().toLowerCase().replace("_", " ").toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}