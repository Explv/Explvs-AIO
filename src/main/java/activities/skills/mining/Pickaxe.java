package activities.skills.mining;

import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.ui.Skill;
import util.Tool;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Pickaxe implements Tool {

    BRONZE("Bronze pickaxe", 1, 1),
    IRON("Iron pickaxe", 1, 1),
    STEEL("Steel pickaxe", 6, 5),
    BLACK("Black pickaxe", 11, 10),
    MITHRIL("Mithril pickaxe", 21, 20),
    ADAMANT("Adamant pickaxe", 31, 30),
    RUNE("Rune pickaxe", 41, 40),
    DRAGON("Dragon pickaxe", 61, 60, true),
    INFERNAL("Infernal pickaxe", 61, 60, true);

    String name;
    int miningLevelRequired;
    int attackLevelRequired;
    boolean membersOnly;

    Pickaxe(final String name, final int miningLevelRequired, final int attackLevelRequired) {
        this.name = name;
        this.miningLevelRequired = miningLevelRequired;
        this.attackLevelRequired = attackLevelRequired;
    }

    Pickaxe(final String name, final int miningLevelRequired, final int attackLevelRequired, final boolean membersOnly) {
        this(name, miningLevelRequired, attackLevelRequired);
        this.membersOnly = membersOnly;
    }

    public static List<String> getNames() {
        return Arrays.stream(values()).map(Pickaxe::toString).collect(Collectors.toList());
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
        return miningLevelRequired;
    }

    @Override
    public boolean canUse(Skills skills) {
        return skills.getStatic(Skill.MINING) >= miningLevelRequired;
    }

    @Override
    public boolean canEquip(Skills skills) {
        return skills.getStatic(Skill.ATTACK) >= attackLevelRequired;
    }

    @Override
    public String toString() {
        return name;
    }
}
