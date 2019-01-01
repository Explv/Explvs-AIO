package org.aio.util;

import org.osbot.rs07.api.Skills;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Tool{

    int getLevelRequired();
    boolean canUse(final Skills skills);
    boolean canEquip(final Skills skills);

    static Optional<Tool> getBestInBank(final Tool[] tools, final org.osbot.rs07.api.Bank bank, final Skills skills){
        return Arrays.stream(tools)
                     .filter(tool -> tool.canUse(skills) && bank.contains(tool.toString()))
                     .max((t1, t2) -> Integer.compare(t1.getLevelRequired(), t2.getLevelRequired()));
    }

    static List<String> getNames(Tool[] tools){
        return Arrays.stream(tools).map(Tool::toString).collect(Collectors.toList());
    }
}
