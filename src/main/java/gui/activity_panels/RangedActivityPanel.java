package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.ranged.Bow;
import activities.skills.ranged.RangeGuildActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class RangedActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Bow> bowSelector;

    public RangedActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Bow:"));

        bowSelector = new StyledJComboBox<>(Bow.values());
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
