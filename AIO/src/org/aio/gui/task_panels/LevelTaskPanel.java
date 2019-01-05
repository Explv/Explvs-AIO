package org.aio.gui.task_panels;

import org.aio.activities.activity.ActivityType;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.LevelTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Skill;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class LevelTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JComboBox<Skill> skillSelector;
    private JTextField levelField;
    private ActivitySelectorPanel activitySelectorPanel;

    LevelTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        activitySelectorPanel = new ActivitySelectorPanel(this);

        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        controls.add(new JLabel("Skill:"));

        skillSelector = new JComboBox<>();
        controls.add(skillSelector);

        controls.add(new JLabel("Level:"));

        levelField = new JTextField();
        levelField.setColumns(2);
        ((AbstractDocument) levelField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(levelField);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Level Task"));

        mainPanel.add(controls, BorderLayout.SOUTH);

        skillSelector.setModel(new DefaultComboBoxModel<>(((ActivityType)  activitySelectorPanel.getActivitySelector().getSelectedItem()).gainedXPSkills));
        activitySelectorPanel.getActivitySelector().addActionListener(e -> {
            skillSelector.setModel(new DefaultComboBoxModel<>(((ActivityType)  activitySelectorPanel.getActivitySelector().getSelectedItem()).gainedXPSkills));
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        return new LevelTask(activitySelectorPanel.getActivityPanel().toActivity(), (Skill) skillSelector.getSelectedItem(), Integer.parseInt(levelField.getText()));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.LEVEL.name());
        jsonObject.put("skill", ((Skill) skillSelector.getSelectedItem()).name());
        jsonObject.put("level", levelField.getText());
        jsonObject.put("activity", activitySelectorPanel.toJSON());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        skillSelector.setSelectedItem(Skill.valueOf((String) jsonObject.get("skill")));
        levelField.setText((String) jsonObject.get("level"));
        activitySelectorPanel.fromJSON((JSONObject) jsonObject.get("activity"));
    }
}
