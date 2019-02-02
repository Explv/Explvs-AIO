package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.firemaking.FMLocation;
import org.aio.activities.skills.firemaking.FiremakingActivity;
import org.aio.activities.skills.firemaking.Log;
import org.aio.gui.styled_components.StyledJComboBox;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class FMActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Log> logSelector;
    private JComboBox<FMLocation> locationSelector;

    public FMActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Log Type:"));
        logSelector = new StyledJComboBox<>(Log.values());
        mainPanel.add(logSelector);

        mainPanel.add(new StyledJLabel("Location:"));
        locationSelector = new StyledJComboBox<>(FMLocation.values());
        mainPanel.add(locationSelector);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new FiremakingActivity((Log) logSelector.getSelectedItem(), ((FMLocation) locationSelector.getSelectedItem()).LOCATION);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("log", ((Log) logSelector.getSelectedItem()).name());
        jsonObject.put("location", ((FMLocation) locationSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        logSelector.setSelectedItem(Log.valueOf((String) jsonObject.get("log")));
        locationSelector.setSelectedItem(FMLocation.valueOf((String) jsonObject.get("location")));
    }
}
