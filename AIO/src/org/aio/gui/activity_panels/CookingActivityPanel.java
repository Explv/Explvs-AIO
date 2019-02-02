package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.cooking.CookingActivity;
import org.aio.activities.skills.cooking.CookingItem;
import org.aio.activities.skills.cooking.CookingLocation;
import org.aio.activities.skills.cooking.CookingType;
import org.aio.gui.styled_components.StyledJComboBox;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class CookingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<CookingLocation> locationSelector;
    private JComboBox<CookingType> typeSelector;
    private JComboBox<CookingItem> itemSelector;

    public CookingActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Location:"));
        locationSelector = new StyledJComboBox<>(CookingLocation.values());
        mainPanel.add(locationSelector);

        mainPanel.add(new StyledJLabel("Type:"));
        typeSelector = new StyledJComboBox<>(CookingLocation.values()[0].cookingObject.allowedCookingTypes);
        mainPanel.add(typeSelector);

        mainPanel.add(new StyledJLabel("Item:"));
        itemSelector = new StyledJComboBox<>(CookingItem.getAllWithType(CookingLocation.values()[0].cookingObject.allowedCookingTypes[0]));
        mainPanel.add(itemSelector);

        locationSelector.addActionListener(e -> {
            typeSelector.setModel(new DefaultComboBoxModel<>(((CookingLocation) locationSelector.getSelectedItem()).cookingObject.allowedCookingTypes));
            itemSelector.setModel(new DefaultComboBoxModel<>(CookingItem.getAllWithType(CookingLocation.values()[0].cookingObject.allowedCookingTypes[0])));
        });

        typeSelector.addActionListener(e -> itemSelector.setModel(new DefaultComboBoxModel<>(CookingItem.getAllWithType((CookingType) typeSelector.getSelectedItem()))));
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new CookingActivity((CookingItem) itemSelector.getSelectedItem(), (CookingLocation) locationSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("location", ((CookingLocation) locationSelector.getSelectedItem()).name());
        jsonObject.put("type", ((CookingType) typeSelector.getSelectedItem()).name());
        jsonObject.put("item", ((CookingItem) itemSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        locationSelector.setSelectedItem(CookingLocation.valueOf((String) jsonObject.get("location")));
        typeSelector.setSelectedItem(CookingType.valueOf((String) jsonObject.get("type")));
        itemSelector.setSelectedItem(CookingItem.valueOf((String) jsonObject.get("item")));
    }
}
