package activities.tutorial_island;

import activities.activity.Activity;
import util.Sleep;

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
        Sleep.sleepUntil(() -> getClient().isLoggedIn() && myPlayer().isVisible(), 6000, 500);
    }

    @Override
    public void runActivity() throws InterruptedException {
        switch (getTutorialSection()) {
            case 0:
            case 1:
                execute(rsGuideSection);
                break;
            case 2:
            case 3:
                execute(survivalSection);
                break;
            case 4:
            case 5:
                execute(cookingSection);
                break;
            case 6:
            case 7:
                execute(questSection);
                break;
            case 8:
            case 9:
                execute(miningSection);
                break;
            case 10:
            case 11:
            case 12:
                execute(fightingSection);
                break;
            case 14:
            case 15:
                execute(bankSection);
                break;
            case 16:
            case 17:
                execute(priestSection);
                break;
            case 18:
            case 19:
            case 20:
                execute(wizardSection);
                break;
        }
    }

    private int getTutorialSection() {
        return getConfigs().get(406);
    }

    @Override
    public TutorialIsland copy() {
        return new TutorialIsland();
    }
}
