package gui.activity_panels;

import activities.activity.Activity;
import activities.money_making.MoneyMakingType;
import activities.money_making.flax_picking.FlaxPickingActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class MoneyMakingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<MoneyMakingType> typeSelector;

    public MoneyMakingActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Type:"));
        typeSelector = new StyledJComboBox<>(MoneyMakingType.values());
        mainPanel.add(typeSelector);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        switch ((MoneyMakingType) typeSelector.getSelectedItem()) {
            case FLAX_PICKING:
                return new FlaxPickingActivity();
            case VIAL_FILLING:
                return null;
        }
        return null;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", ((MoneyMakingType) typeSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        typeSelector.setSelectedItem(MoneyMakingType.valueOf((String) jsonObject.get("type")));
    }
}
