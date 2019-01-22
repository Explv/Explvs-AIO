package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.fishing.Fish;
import org.aio.activities.skills.fishing.FishingActivity;
import org.aio.activities.skills.fishing.FishingLocation;
import org.aio.util.ResourceMode;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class FishingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Fish> fishSelector;
    private JComboBox<FishingLocation> locationSelector;
    private JComboBox<ResourceMode> resourceModeSelector;

    public FishingActivityPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new JLabel("Fish:"));

        fishSelector = new JComboBox<>(Fish.values());
        mainPanel. add(fishSelector);

        mainPanel.add(new JLabel("Location:"));
        locationSelector = new JComboBox<>(Fish.values()[0].locations);
        mainPanel.add(locationSelector);

        mainPanel.add(new JLabel("Collection Mode:"));
        resourceModeSelector = new JComboBox<>(ResourceMode.values());
        mainPanel.add(resourceModeSelector);

        fishSelector.addActionListener(e -> {
            Fish selectedFish = (Fish) fishSelector.getSelectedItem();
            locationSelector.setModel(new DefaultComboBoxModel<>(selectedFish.locations));
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new FishingActivity((Fish) fishSelector.getSelectedItem(), ((FishingLocation) locationSelector.getSelectedItem()), (ResourceMode) resourceModeSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("location", ((FishingLocation) locationSelector.getSelectedItem()).name());
        jsonObject.put("fish", ((Fish) fishSelector.getSelectedItem()).name());
        jsonObject.put("resource_mode", ((ResourceMode) resourceModeSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        fishSelector.setSelectedItem(Fish.valueOf((String) jsonObject.get("fish")));
        locationSelector.setSelectedItem(FishingLocation.valueOf((String) jsonObject.get("location")));
        resourceModeSelector.setSelectedItem(ResourceMode.valueOf((String) jsonObject.get("resource_mode")));
    }
}
