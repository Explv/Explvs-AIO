package org.aio.activities.tutorial_island;

import org.aio.activities.activity.Activity;
import org.aio.util.Sleep;

public final class TutorialIsland extends Activity {

    private final TutorialSection rsGuideSection = new RuneScapeGuideSection();
    private final TutorialSection survivalSection = new SurvivalSection();
    private final TutorialSection cookingSection = new CookingSection();
    private final TutorialSection questSection = new QuestSection();
    private final TutorialSection miningSection = new MiningSection();
    private final TutorialSection fightingSection = new FightingSection();
    private final TutorialSection bankSection = new BankSection();
    private final TutorialSection priestSection = new PriestSection();
    private final TutorialSection wizardSection = new WizardSection();

    public TutorialIsland() {
        super(null);
    }

    @Override
    public void onStart() throws InterruptedException {
        rsGuideSection.exchangeContext(getBot());
        survivalSection.exchangeContext(getBot());
        cookingSection.exchangeContext(getBot());
        questSection.exchangeContext(getBot());
        miningSection.exchangeContext(getBot());
        fightingSection.exchangeContext(getBot());
        bankSection.exchangeContext(getBot());
        priestSection.exchangeContext(getBot());
        wizardSection.exchangeContext(getBot());

        Sleep.sleepUntil(() -> getClient().isLoggedIn() && myPlayer().isVisible() && myPlayer().isOnScreen(), 6000, 500);
    }

    @Override
    public void runActivity() throws InterruptedException {
        switch (getTutorialSection()) {
            case 0:
            case 1:
                rsGuideSection.onLoop();
                break;
            case 2:
            case 3:
                survivalSection.onLoop();
                break;
            case 4:
            case 5:
                cookingSection.onLoop();
                break;
            case 6:
            case 7:
                questSection.onLoop();
                break;
            case 8:
            case 9:
                miningSection.onLoop();
                break;
            case 10:
            case 11:
            case 12:
                fightingSection.onLoop();
                break;
            case 14:
            case 15:
                bankSection.onLoop();
                break;
            case 16:
            case 17:
                priestSection.onLoop();
                break;
            case 18:
            case 19:
            case 20:
                wizardSection.onLoop();
                break;
        }
    }

    private int getTutorialSection() {
        return getConfigs().get(406);
    }
}
