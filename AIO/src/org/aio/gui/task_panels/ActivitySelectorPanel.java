package org.aio.gui.task_panels;

import org.aio.activities.activity.ActivityType;
import org.aio.gui.JSONSerializable;
import org.aio.gui.activity_panels.*;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class ActivitySelectorPanel implements JSONSerializable {

    private JPanel mainPanel;
    private ActivityPanel activityPanel;
    private JComboBox<ActivityType> activitySelector;

    public JComboBox<ActivityType> getActivitySelector() {
        return activitySelector;
    }

    public ActivitySelectorPanel() {
        mainPanel = new JPanel(new BorderLayout());

        activitySelector = new JComboBox<>(new DefaultComboBoxModel<>(ActivityType.values()));

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel activityLabel = new JLabel("Activity:");
        selectorPanel.add(activityLabel);

        selectorPanel.add(activitySelector);

        mainPanel.add(selectorPanel, BorderLayout.NORTH);

        activityPanel = ActivityPanelFactory.createActivityPanel(ActivityType.values()[0]);

        mainPanel.add(activityPanel.getPanel(), BorderLayout.SOUTH);

        mainPanel.setMinimumSize(mainPanel.getPreferredSize());

        activitySelector.addActionListener(e -> {

            ActivityType selectedActivityType = (ActivityType) activitySelector.getSelectedItem();

            if(activityPanel != null){
                mainPanel.remove(activityPanel.getPanel());
            }

            activityPanel = ActivityPanelFactory.createActivityPanel(selectedActivityType);
            setActivityPanel(activityPanel);
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void setActivityPanel(final ActivityPanel activityPanel) {
        mainPanel.add(activityPanel.getPanel(), BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
        mainPanel.setMinimumSize(mainPanel.getPreferredSize());
    }

    public ActivityPanel getActivityPanel() {
        return activityPanel;
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
}
