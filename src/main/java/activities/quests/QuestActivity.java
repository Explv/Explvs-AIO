package activities.quests;

import activities.activity.Activity;

public abstract class QuestActivity extends Activity {

    private final Quest quest;

    public QuestActivity(final Quest quest) {
        super(null);
        this.quest = quest;
    }

    protected int getProgress() {
        return getConfigs().get(quest.configID);
    }

    @Override
    public String toString() {
        return quest.name;
    }
}
