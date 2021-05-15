package util;

import org.osbot.rs07.api.Skills;

public interface Tool {
    String getName();

    boolean isMembersOnly();

    int getLevelRequired();

    boolean canUse(final Skills skills);

    boolean canEquip(final Skills skills);
}
