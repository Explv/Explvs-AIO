package org.aio.activities.skills.magic;

import org.aio.activities.activity.Activity;

public class MagicActivity extends Activity{

    private final Spell spell;

    public MagicActivity(final Spell spell) {
        super(null);//ActivityType.MAGIC);
        this.spell = spell;
    }

    @Override
    public void runActivity() throws InterruptedException {
    }
}
