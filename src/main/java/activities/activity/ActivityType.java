package activities.activity;

import org.osbot.rs07.api.ui.Skill;

public enum ActivityType {

    AGILITY(Skill.AGILITY),
    //COMBAT (Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS, Skill.RANGED, Skill.MAGIC),
    //CONSTRUCTION (Skill.CONSTRUCTION),
    COOKING(Skill.COOKING),
    CRAFTING(Skill.CRAFTING),
    //FARMING (Skill.FARMING),
    FIREMAKING(Skill.FIREMAKING),
    FISHING(Skill.FISHING),
    FLETCHING(Skill.FLETCHING),
    HERBLORE(Skill.HERBLORE),
    //HUNTER (Skill.HUNTER),
    //MAGIC (Skill.MAGIC),
    MINING(Skill.MINING),
    //PRAYER (Skill.PRAYER),
    RUNECRAFTING(Skill.RUNECRAFTING),
    //SLAYER (Skill.SLAYER, Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS, Skill.RANGED, Skill.MAGIC),
    SMITHING(Skill.SMITHING),
    THIEVING(Skill.THIEVING),
    WOODCUTTING(Skill.WOODCUTTING),
    RANGED(Skill.RANGED),
    MONEY_MAKING;

    public Skill[] gainedXPSkills;

    ActivityType(final Skill... gainedXPSkills) {
        this.gainedXPSkills = gainedXPSkills;
    }

    @Override
    public String toString() {
        char[] name = name().toLowerCase().replace('_', ' ').toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
