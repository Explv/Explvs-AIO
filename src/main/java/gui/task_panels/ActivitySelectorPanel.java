package gui.task_panels;

import activities.activity.ActivityType;
import gui.activity_panels.ActivityPanel;
import gui.activity_panels.ActivityPanelFactory;
import gui.interfaces.JSONSerializable;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivitySelectorPanel implements JSONSerializable {

    private JPanel mainPanel;
    private ActivityPanel activityPanel;
    private JComboBox<ActivityType> activitySelector;

    public ActivitySelectorPanel(TaskPanel panel) {
        mainPanel = new StyledJPanel(new BorderLayout());

        activitySelector = new StyledJComboBox<>(new DefaultComboBoxModel<>(getValidActivitiesForPanel(panel)));

        JPanel selectorPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel activityLabel = new StyledJLabel("Activity:");
        selectorPanel.add(activityLabel);

        selectorPanel.add(activitySelector);

        mainPanel.add(selectorPanel, BorderLayout.NORTH);

        activityPanel = ActivityPanelFactory.createActivityPanel(ActivityType.values()[0]);

        mainPanel.add(activityPanel.getPanel(), BorderLayout.SOUTH);

        mainPanel.setMinimumSize(mainPanel.getPreferredSize());

        activitySelector.addActionListener(e -> {

            ActivityType selectedActivityType = (ActivityType) activitySelector.getSelectedItem();

            if (activityPanel != null) {
                mainPanel.remove(activityPanel.getPanel());
            }

            activityPanel = ActivityPanelFactory.createActivityPanel(selectedActivityType);
            setActivityPanel(activityPanel);
        });
    }

    public JComboBox<ActivityType> getActivitySelector() {
        return activitySelector;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public ActivityPanel getActivityPanel() {
        return activityPanel;
    }

    private void setActivityPanel(final ActivityPanel activityPanel) {
        mainPanel.add(activityPanel.getPanel(), BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
        mainPanel.setMinimumSize(mainPanel.getPreferredSize());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activity_type", ((ActivityType) activitySelector.getSelectedItem()).name());
        jsonObject.put("activity", activityPanel.toJSON());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        activitySelector.setSelectedItem(ActivityType.valueOf((String) jsonObject.get("activity_type")));
        activityPanel.fromJSON((JSONObject) jsonObject.get("activity"));
        setActivityPanel(activityPanel);
    }

    /**
     * Filter out any invalid activities for the given panel class
     *
     * @param panel To populate activities for
     * @return Valid activities for this panel
     */
    private ActivityType[] getValidActivitiesForPanel(TaskPanel panel) {
        ArrayList<ActivityType> allOptions = new ArrayList<>(Arrays.asList(ActivityType.values()));

        if (panel instanceof LevelTaskPanel || panel instanceof ResourceTaskPanel) {
            allOptions.removeIf(type -> type == ActivityType.MONEY_MAKING);
        }

        return allOptions.toArray(new ActivityType[allOptions.size()]);
    }
}
