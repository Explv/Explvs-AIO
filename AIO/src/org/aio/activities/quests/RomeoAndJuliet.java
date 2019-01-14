package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.DepositAllBanking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Tab;

import java.util.*;

public class RomeoAndJuliet extends QuestActivity {

    private static final Area ROMEO_AREA = new Area(3205, 3431, 3220, 3415);
    private static final Area JULIET_AREA = new Area(new Position(3155, 3425, 1), new Position(3161, 3426, 1));
    private static final Area LAWRENCE_AREA = new Area(3252, 3486, 3259, 3472);
    private static final Area APOTHECARY_AREA = new Area(3197, 3406, 3192, 3402);
    private static final Area BERRIES_AREA = new Area(3263, 3365, 3278, 3375);

    private static final List<Position> BERRIES_PATH = Arrays.asList(new Position[]{new Position(3193,3403,0),new Position(3193,3403,0),new Position(3191,3404,0),new Position(3191,3407,0),new Position(3194,3407,0),new Position(3197,3407,0),new Position(3200,3407,0),new Position(3200,3407,0),new Position(3203,3407,0),new Position(3206,3410,0),new Position(3209,3410,0),new Position(3210,3413,0),new Position(3210,3413,0),new Position(3210,3416,0),new Position(3211,3419,0),new Position(3212,3420,0),new Position(3212,3420,0),new Position(3215,3423,0),new Position(3218,3426,0),new Position(3221,3427,0),new Position(3224,3427,0),new Position(3227,3427,0),new Position(3230,3428,0),new Position(3230,3428,0),new Position(3233,3428,0),new Position(3236,3428,0),new Position(3239,3428,0),new Position(3242,3428,0),new Position(3245,3428,0),new Position(3248,3428,0),new Position(3251,3428,0),new Position(3254,3428,0),new Position(3257,3428,0),new Position(3260,3428,0),new Position(3263,3428,0),new Position(3266,3428,0),new Position(3269,3428,0),new Position(3272,3428,0),new Position(3275,3426,0),new Position(3275,3426,0),new Position(3277,3423,0),new Position(3278,3420,0),new Position(3280,3418,0),new Position(3281,3415,0),new Position(3284,3413,0),new Position(3286,3410,0),new Position(3288,3407,0),new Position(3289,3404,0),new Position(3290,3401,0),new Position(3290,3398,0),new Position(3290,3398,0),new Position(3290,3395,0),new Position(3290,3392,0),new Position(3290,3389,0),new Position(3290,3386,0),new Position(3290,3383,0),new Position(3290,3380,0),new Position(3290,3377,0),new Position(3287,3375,0),new Position(3284,3375,0),new Position(3281,3372,0),new Position(3281,3372,0),new Position(3278,3370,0),new Position(3275,3369,0),new Position(3275,3369,0),new Position(3272,3369,0)});
    private static final List<Position> APOTHECARY_PATH = Arrays.asList(new Position[]{new Position(3256,3371,0),new Position(3256,3371,0),new Position(3260,3371,0),new Position(3264,3371,0),new Position(3268,3371,0),new Position(3272,3374,0),new Position(3276,3375,0),new Position(3280,3375,0),new Position(3284,3375,0),new Position(3288,3375,0),new Position(3288,3375,0),new Position(3290,3379,0),new Position(3290,3383,0),new Position(3290,3387,0),new Position(3290,3391,0),new Position(3290,3395,0),new Position(3290,3399,0),new Position(3290,3403,0),new Position(3288,3407,0),new Position(3288,3407,0),new Position(3286,3411,0),new Position(3282,3414,0),new Position(3281,3418,0),new Position(3279,3421,0),new Position(3276,3424,0),new Position(3276,3424,0),new Position(3272,3426,0),new Position(3268,3426,0),new Position(3264,3426,0),new Position(3260,3427,0),new Position(3256,3428,0),new Position(3252,3428,0),new Position(3248,3428,0),new Position(3244,3428,0),new Position(3240,3428,0),new Position(3236,3428,0),new Position(3232,3428,0),new Position(3228,3428,0),new Position(3224,3428,0),new Position(3220,3428,0),new Position(3216,3424,0),new Position(3216,3424,0),new Position(3212,3423,0),new Position(3208,3421,0),new Position(3204,3420,0),new Position(3200,3418,0),new Position(3200,3418,0),new Position(3200,3414,0),new Position(3198,3410,0),new Position(3198,3410,0),new Position(3195,3409,0),new Position(3191,3407,0),new Position(3191,3403,0),new Position(3193,3403,0)});


    private final DialogueCompleter romeoDialogueCompleter = new DialogueCompleter(
            "Romeo",
            ROMEO_AREA,
            "Perhaps I could help to find her for you?",
            "Yes, ok, I'll let her know.",
            "Ok, thanks."
    );

    private final DialogueCompleter apothecaryDialogueCompleter = new DialogueCompleter(
            "Apothecary",
            APOTHECARY_AREA,
            APOTHECARY_PATH,
            "Talk about something else.",
            "Talk about Romeo & Juliet.",
            "Ok, thanks.﻿﻿﻿"
    );

    private final DialogueCompleter lawrenceDialogueCompleter = new DialogueCompleter(
            "Father Lawrence",
            LAWRENCE_AREA
    );

    private final DialogueCompleter julietDialogueCompleter = new DialogueCompleter(
            "Juliet",
            JULIET_AREA
    );

    private static final String[] QUEST_ITEMS = {
            "Cadava potion",
            "Cadava berries",
            "Message"
    };

    private final ItemCompleter berryItemCompleter = new ItemCompleter(
            "Cadava bush",
            "Cadava berries",
            "Pick-from",
            BERRIES_AREA,
            BERRIES_PATH,
            ItemCompleter.EntitySelector.RANDOM
    );

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(QUEST_ITEMS);

    public RomeoAndJuliet() {
        super(Quest.ROMEO_AND_JULIET);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());
        romeoDialogueCompleter.exchangeContext(getBot());
        apothecaryDialogueCompleter.exchangeContext(getBot());
        lawrenceDialogueCompleter.exchangeContext(getBot());
        julietDialogueCompleter.exchangeContext(getBot());
        berryItemCompleter.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().getEmptySlotCount() < 5) {
            depositAllBanking.run();
            return;
        }

        if (!Tab.INVENTORY.isDisabled(bot) && getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
            return;
        }
        int progress = getProgress();
        switch (progress) {
            case 0:
                romeoDialogueCompleter.run();
                break;
            case 10:
                julietDialogueCompleter.run();
                break;
            case 20:
                romeoDialogueCompleter.run();
                break;
            case 30:
                lawrenceDialogueCompleter.run();
                break;
            case 40:
                apothecaryDialogueCompleter.run(DialogueCompleter.WalkType.WEBWALK);
                break;
            case 50:
                // Make sure we are not in the cut scene
                if (Tab.INVENTORY.isDisabled(bot)) {
                    if (getDialogues().isPendingContinuation()) {
                        getDialogues().clickContinue();
                    }
                } else {
                    if (getInventory().contains("Cadava potion")) {
                        julietDialogueCompleter.run();
                    } else if (getInventory().contains("Cadava berries")) {
                        apothecaryDialogueCompleter.run();
                    } else {
                        berryItemCompleter.run();
                    }
                }
                break;
            case 60:
                // Make sure we are not in the cut scene
                if (Tab.INVENTORY.isDisabled(bot)) {
                    if (getDialogues().isPendingContinuation()) {
                        getDialogues().clickContinue();
                    }
                } else {
                    romeoDialogueCompleter.run();
                }
                break;
            case 100:
                if (getDialogues().inDialogue()) {
                    getDialogues().clickContinue();
                }
                break;
            default:
                log("Unknown Romeo&Juliet Quest state ["+progress+"]");
                break;
        }
    }

    @Override
    public Activity copy() {
        return new RomeoAndJuliet();
    }
}
