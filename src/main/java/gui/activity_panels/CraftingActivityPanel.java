package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.crafting.CraftingActivity;
import activities.skills.crafting.CraftingItem;
import activities.skills.crafting.CraftingType;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;
import util.Location;

import javax.swing.*;
import java.awt.*;

public class CraftingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<CraftingType> typeSelector;
    private JComboBox<Location> locationSelector;
    private JComboBox<CraftingItem> itemSelector;

    public CraftingActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Type:"));
        typeSelector = new StyledJComboBox<>(CraftingType.values());
        mainPanel.add(typeSelector);

        mainPanel.add(new StyledJLabel("Location:"));
        locationSelector = new StyledJComboBox<>(CraftingType.values()[0].locations);
        mainPanel.add(locationSelector);

        mainPanel.add(new StyledJLabel("Item:"));
        itemSelector = new StyledJComboBox<>(CraftingItem.getItemsWithType(CraftingType.values()[0]));
        mainPanel.add(itemSelector);

        typeSelector.addActionListener(e -> {
            locationSelector.setModel(new DefaultComboBoxModel<>(((CraftingType) typeSelector.getSelectedItem()).locations));
            itemSelector.setModel(new DefaultComboBoxModel<>(CraftingItem.getItemsWithType(((CraftingType) typeSelector.getSelectedItem()))));
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new CraftingActivity((CraftingItem) itemSelector.getSelectedItem(), (Location) locationSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", ((CraftingType) typeSelector.getSelectedItem()).name());
        jsonObject.put("item", ((CraftingItem) itemSelector.getSelectedItem()).name());
        jsonObject.put("location_index", locationSelector.getSelectedIndex());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        typeSelector.setSelectedItem(CraftingType.valueOf((String) jsonObject.get("type")));
        itemSelector.setSelectedItem(CraftingItem.valueOf((String) jsonObject.get("item")));
        locationSelector.setSelectedIndex(new Long((long) jsonObject.get("location_index")).intValue());
    }
}
