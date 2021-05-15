package activities.skills.woodcutting;

import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.ui.Skill;
import util.Tool;

public enum Axe implements Tool {

    BRONZE("Bronze axe", 1, 1),
    IRON("Iron axe", 1, 1),
    STEEL("Steel axe", 6, 5),
    BLACK("Black axe", 11, 10),
    MITHRIL("Mithril axe", 21, 20),
    ADAMANT("Adamant axe", 31, 30),
    RUNE("Rune axe", 41, 40),
    DRAGON("Dragon axe", 61, 60, true),
    INFERNAL("Infernal axe", 61, 60, true);

    String name;
    int wcLevelRequired;
    int attLevelRequired;
    boolean membersOnly;

    Axe(final String name, final int wcLevelRequired, final int attLevelRequired) {
        this.name = name;
        this.wcLevelRequired = wcLevelRequired;
        this.attLevelRequired = attLevelRequired;
    }

    Axe(final String name, final int wcLevelRequired, final int attLevelRequired, final boolean membersOnly) {
        this(name, wcLevelRequired, attLevelRequired);
        this.membersOnly = membersOnly;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMembersOnly() {
        return membersOnly;
    }

    @Override
    public int getLevelRequired() {
        return wcLevelRequired;
    }

    @Override
    public boolean canUse(Skills skills) {
        return skills.getStatic(Skill.WOODCUTTING) >= wcLevelRequired;
    }

    @Override
    public boolean canEquip(Skills skills) {
        return skills.getStatic(Skill.ATTACK) >= attLevelRequired;
    }

    @Override
    public String toString() {
        return name;
    }
}
