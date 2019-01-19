package org.aio.util;

import org.osbot.rs07.api.Skills;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Tool{
    String getName();
    int getLevelRequired();
    boolean canUse(final Skills skills);
    boolean canEquip(final Skills skills);
}
