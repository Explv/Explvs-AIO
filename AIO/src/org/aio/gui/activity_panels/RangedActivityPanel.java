package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.ranged.Bow;
import org.aio.activities.skills.ranged.RangeGuildActivity;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class RangedActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Bow> bowSelector;

    public RangedActivityPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new JLabel("Bow:"));

        bowSelector = new JComboBox<>(Bow.values());
        mainPanel.add(bowSelector);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new RangeGuildActivity((Bow) bowSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bow", ((Bow) bowSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        bowSelector.setSelectedItem(Bow.valueOf((String) jsonObject.get("bow")));
    }
}
