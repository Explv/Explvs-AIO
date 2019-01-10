package org.aio.activities.quests;

import org.osbot.rs07.api.Configs;

public enum Quest {

    SHHEP_SHEARER ("Sheep Shearer", 179, 21),
    RUNE_MYSTERIES ("Rune Mysteries", 63, 6),
    COOKS_ASSISTANT ("Cooks Assistant", 29, 2),
	ROMEO_AND_JULIET ("Romeo & Juliet", 144, 100);
    //ABYSS ("Abyss Mini Quest", 492, 4);

    final String name;
    final int configID;
    final int completedConfigVal;

    Quest(final String name, final int configID, final int completedConfigVal){
        this.name = name;
        this.configID = configID;
        this.completedConfigVal = completedConfigVal;
    }

    public boolean isComplete(final Configs configs){
        return configs.get(configID) == completedConfigVal;
    }

    @Override
    public String toString(){
        return name;
    }
}
