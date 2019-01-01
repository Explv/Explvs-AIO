package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.crafting.CraftingActivity;
import org.aio.activities.skills.crafting.CraftingItem;
import org.aio.activities.skills.crafting.CraftingType;
import org.aio.util.Location;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class CraftingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<CraftingType> typeSelector;
    private JComboBox<Location> locationSelector;
    private JComboBox<CraftingItem> itemSelector;

    public CraftingActivityPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new JLabel("Type:"));
        typeSelector = new JComboBox<>(CraftingType.values());
        mainPanel.add(typeSelector);

        mainPanel.add(new JLabel("Location:"));
        locationSelector = new JComboBox<>(CraftingType.values()[0].locations);
        mainPanel.add(locationSelector);

        mainPanel.add(new JLabel("Item:"));
        itemSelector = new JComboBox<>(CraftingItem.getItemsWithType(CraftingType.values()[0]));
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
