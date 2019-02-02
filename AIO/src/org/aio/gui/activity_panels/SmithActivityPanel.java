package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.smithing.Bar;
import org.aio.activities.skills.smithing.SmithingType;
import org.aio.activities.skills.smithing.cannonballs.CannonballActivity;
import org.aio.activities.skills.smithing.item_making.SmithItem;
import org.aio.activities.skills.smithing.item_making.SmithItemMakingActivity;
import org.aio.activities.skills.smithing.item_making.SmithLocation;
import org.aio.activities.skills.smithing.smelting.SmeltLocation;
import org.aio.activities.skills.smithing.smelting.SmeltingActivity;
import org.aio.gui.styled_components.StyledJComboBox;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SmithActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<SmithingType> smithingTypeSelector;
    private JComboBox<SmithItem> smithItemSelector;
    private JComboBox<SmithLocation> smithLocationSelector;
    private JComboBox<Bar> smithBarSelector;
    private JComboBox<Bar> smeltBarSelector;
    private JComboBox<SmeltLocation> smeltLocationSelector;
    private JComboBox<SmeltLocation> cannonballLocationSelector;

    public SmithActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Smithing Type:"));

        smithingTypeSelector = new StyledJComboBox<>(SmithingType.values());
        mainPanel.add(smithingTypeSelector);

        JPanel cannonballPanel = new StyledJPanel();
        cannonballPanel.add(new StyledJLabel("Location:"));
        cannonballLocationSelector = new StyledJComboBox<>(SmeltLocation.values());
        cannonballPanel.add(cannonballLocationSelector);
        mainPanel.add(cannonballPanel);

        JPanel smithItemPanel = new StyledJPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(smithItemPanel);

        smithItemPanel.add(new StyledJLabel("Bar:"));
        smithBarSelector = new StyledJComboBox<>(Arrays.stream(Bar.values()).filter(bar -> bar.smithable).toArray(Bar[]::new));
        smithItemPanel.add(smithBarSelector);

        smithItemPanel.add(new StyledJLabel("Item:"));
        smithItemSelector = new StyledJComboBox<>(SmithItem.values());
        smithItemPanel.add(smithItemSelector);

        smithItemPanel.add(new StyledJLabel("Location:"));
        smithLocationSelector = new StyledJComboBox<>(SmithLocation.values());
        smithItemPanel.add(smithLocationSelector);

        JPanel smeltBarPanel = new StyledJPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(smeltBarPanel);

        smeltBarPanel.add(new StyledJLabel("Bar:"));
        smeltBarSelector = new StyledJComboBox<>(Bar.values());
        smeltBarPanel.add(smeltBarSelector);

        smeltBarPanel.add(new StyledJLabel("Location:"));
        smeltLocationSelector = new StyledJComboBox<>(SmeltLocation.values());
        smeltBarPanel.add(smeltLocationSelector);

        smeltBarPanel.setVisible(true);
        smithItemPanel.setVisible(false);
        cannonballPanel.setVisible(false);

        smithingTypeSelector.addActionListener(e -> {
            SmithingType selectedSmithingType = (SmithingType) smithingTypeSelector.getSelectedItem();
            if (selectedSmithingType == SmithingType.CANNONBALLS) {
                smeltBarPanel.setVisible(false);
                smithItemPanel.setVisible(false);
                cannonballPanel.setVisible(true);
            } else if (selectedSmithingType == SmithingType.SMELT_BAR) {
                smeltBarPanel.setVisible(true);
                smithItemPanel.setVisible(false);
                cannonballPanel.setVisible(false);
            } else if (selectedSmithingType == SmithingType.SMITH_ITEM){
                smeltBarPanel.setVisible(false);
                cannonballPanel.setVisible(false);
                smithItemPanel.setVisible(true);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        if (smithingTypeSelector.getSelectedItem() == SmithingType.CANNONBALLS) {
            return new CannonballActivity((SmeltLocation) cannonballLocationSelector.getSelectedItem());
        }
        if (smithingTypeSelector.getSelectedItem() == SmithingType.SMITH_ITEM) {
            return new SmithItemMakingActivity((Bar) smithBarSelector.getSelectedItem(), (SmithItem) smithItemSelector.getSelectedItem(), (SmithLocation) smithLocationSelector.getSelectedItem());
        }
        return new SmeltingActivity((Bar) smeltBarSelector.getSelectedItem(), (SmeltLocation) smeltLocationSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        SmithingType smithingType = (SmithingType) smithingTypeSelector.getSelectedItem();

        jsonObject.put("type", smithingType.name());

        if (smithingType == SmithingType.CANNONBALLS) {
            jsonObject.put("location", ((SmeltLocation) cannonballLocationSelector.getSelectedItem()).name());
        } else if (smithingType == SmithingType.SMITH_ITEM) {
            jsonObject.put("item", ((SmithItem) smithItemSelector.getSelectedItem()).name());
            jsonObject.put("location", ((SmithLocation) smithLocationSelector.getSelectedItem()).name());
            jsonObject.put("bar", ((Bar) smithBarSelector.getSelectedItem()).name());
        } else {
            jsonObject.put("location", ((SmeltLocation) smeltLocationSelector.getSelectedItem()).name());
            jsonObject.put("bar", ((Bar) smeltBarSelector.getSelectedItem()).name());
        }

        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        SmithingType smithingType = SmithingType.valueOf((String) jsonObject.get("type"));

        smithingTypeSelector.setSelectedItem(smithingType);

        if (smithingType == SmithingType.CANNONBALLS) {
            cannonballLocationSelector.setSelectedItem(SmeltLocation.valueOf((String) jsonObject.get("location")));
        } else if (smithingType == SmithingType.SMITH_ITEM) {
            smithItemSelector.setSelectedItem(SmithItem.valueOf((String) jsonObject.get("item")));
            smithLocationSelector.setSelectedItem(SmithLocation.valueOf((String) jsonObject.get("location")));
            smithBarSelector.setSelectedItem(Bar.valueOf((String) jsonObject.get("bar")));
        } else {
            smeltBarSelector.setSelectedItem(Bar.valueOf((String) jsonObject.get("bar")));
            smeltLocationSelector.setSelectedItem(SmeltLocation.valueOf((String) jsonObject.get("location")));
        }
    }
}
