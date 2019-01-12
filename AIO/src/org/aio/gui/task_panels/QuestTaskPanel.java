package org.aio.gui.task_panels;

import org.aio.activities.quests.*;
import org.aio.tasks.QuestTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class QuestTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JComboBox<Quest> questSelector;

    QuestTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        final JLabel label1 = new JLabel();
        label1.setText("Quest:");
        controls.add(label1);

        questSelector = new JComboBox<>();
        controls.add(questSelector);

        mainPanel.add(controls, BorderLayout.CENTER);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Quest Task"));
        questSelector.setModel(new DefaultComboBoxModel<>(Quest.values()));
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        switch ((Quest) questSelector.getSelectedItem()) {
	        case RUNE_MYSTERIES:
	            return new QuestTask(new RuneMysteries(), (Quest) questSelector.getSelectedItem());
	        case COOKS_ASSISTANT:
	            return new QuestTask(new CooksAssistant(), (Quest) questSelector.getSelectedItem());
            case ROMEO_AND_JULIET:
                return new QuestTask(new RomeoAndJuliet(), (Quest) questSelector.getSelectedItem());
            case THE_RESTLESS_GHOST:
                return new QuestTask(new TheRestlessGhost(), (Quest) questSelector.getSelectedItem());
        }
        return null;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.QUEST.name());
        jsonObject.put("quest", ((Quest) questSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        questSelector.setSelectedItem(Quest.valueOf((String) jsonObject.get("quest")));
    }
}
