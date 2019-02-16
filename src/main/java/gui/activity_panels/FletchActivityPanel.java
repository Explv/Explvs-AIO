package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.fletching.FletchItem;
import activities.skills.fletching.FletchItemType;
import activities.skills.fletching.FletchingActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class FletchActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<FletchItemType> typeSelector;
    private JComboBox<FletchItem> itemSelector;

    public FletchActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Type:"));
        typeSelector = new StyledJComboBox<>(FletchItemType.values());
        mainPanel.add(typeSelector);

        mainPanel.add(new StyledJLabel("Item:"));
        itemSelector = new StyledJComboBox<>(FletchItem.getAllWithType(FletchItemType.values()[0]));
        mainPanel.add(itemSelector);

        typeSelector.addActionListener(e ->
                itemSelector.setModel(new DefaultComboBoxModel<>(FletchItem.getAllWithType((FletchItemType) typeSelector.getSelectedItem())))
        );
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new FletchingActivity((FletchItem) itemSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type", ((FletchItemType) typeSelector.getSelectedItem()).name());
        jsonObject.put("item", ((FletchItem) itemSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        typeSelector.setSelectedItem(FletchItemType.valueOf((String) jsonObject.get("item_type")));
        itemSelector.setSelectedItem(FletchItem.valueOf((String) jsonObject.get("item")));
    }
}
