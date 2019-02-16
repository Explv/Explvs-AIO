package gui.task_panels;

import activities.activity.ActivityType;
import gui.fields.IntegerField;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Skill;
import tasks.LevelTask;
import tasks.Task;
import tasks.TaskType;

import javax.swing.*;
import java.awt.*;

public class LevelTaskPanel extends TaskPanel {

    private JComboBox<Skill> skillSelector;
    private JTextField levelField;
    private ActivitySelectorPanel activitySelectorPanel;

    LevelTaskPanel() {
        super(TaskType.LEVEL);

        JPanel contentPanel = new StyledJPanel(new BorderLayout());

        activitySelectorPanel = new ActivitySelectorPanel(this);

        contentPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);

        JPanel controls = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        controls.add(new StyledJLabel("Skill:"));

        skillSelector = new StyledJComboBox<>();
        controls.add(skillSelector);

        controls.add(new StyledJLabel("Level:"));

        levelField = new IntegerField();
        levelField.setColumns(2);
        controls.add(levelField);

        contentPanel.add(controls, BorderLayout.SOUTH);

        skillSelector.setModel(new DefaultComboBoxModel<>(((ActivityType) activitySelectorPanel.getActivitySelector().getSelectedItem()).gainedXPSkills));
        activitySelectorPanel.getActivitySelector().addActionListener(e -> {
            skillSelector.setModel(new DefaultComboBoxModel<>(((ActivityType) activitySelectorPanel.getActivitySelector().getSelectedItem()).gainedXPSkills));
        });

        setContentPanel(contentPanel);
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
