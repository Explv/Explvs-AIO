package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.eating.Food;
import org.aio.activities.skills.thieving.ThievingActivity;
import org.aio.activities.skills.thieving.ThievingObject;
import org.aio.activities.skills.thieving.ThievingType;
import org.aio.util.Location;
import org.aio.util.ResourceMode;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class ThievingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<ThievingType> typeSelector;
    private JComboBox<ThievingObject> objectSelector;
    private JComboBox<Food> foodSelector;
    private JComboBox<ResourceMode> resourceModeSelector;
    private JTextField hpPercentField;
    private JComboBox<Location> locationSelector;

    public ThievingActivityPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(panel1, BorderLayout.NORTH);

        final JLabel label1 = new JLabel();
        label1.setText("Type:");
        panel1.add(label1);

        typeSelector = new JComboBox<>();
        panel1.add(typeSelector);

        final JLabel label2 = new JLabel();
        label2.setText("Object:");
        panel1.add(label2);

        objectSelector = new JComboBox<>();
        panel1.add(objectSelector);

        final JLabel label3 = new JLabel();
        label3.setText("Location:");
        panel1.add(label3);

        locationSelector = new JComboBox<>();
        panel1.add(locationSelector);

        final JLabel collectionLabel = new JLabel("Collection Mode:");
        mainPanel.add(collectionLabel);

        resourceModeSelector = new JComboBox<>();
        mainPanel.add(resourceModeSelector);
        resourceModeSelector.setModel(new DefaultComboBoxModel<>(ResourceMode.values()));

        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(panel4, BorderLayout.SOUTH);

        final JLabel label4 = new JLabel();
        label4.setText("Food:");
        panel4.add(label4);

        foodSelector = new JComboBox<>();
        panel4.add(foodSelector);

        JLabel hpPercentLabel = new JLabel();
        hpPercentLabel.setText("HP % To Eat:");
        panel4.add(hpPercentLabel);

        hpPercentField = new JTextField();
        hpPercentField.setColumns(2);
        hpPercentField.setEditable(true);
        hpPercentField.setEnabled(true);
        panel4.add(hpPercentField);

        typeSelector.setModel(new DefaultComboBoxModel<>(ThievingType.values()));
        objectSelector.setModel(new DefaultComboBoxModel<>(ThievingObject.getAllObjectsWithType(ThievingType.values()[0])));
        locationSelector.setModel(new DefaultComboBoxModel<>(objectSelector.getModel().getElementAt(0).locations));
        foodSelector.setModel(new DefaultComboBoxModel<>(Food.values()));
        hpPercentField.setVisible(false);
        hpPercentLabel.setVisible(false);
        foodSelector.addActionListener(e -> {
            Food selectedFood = (Food) foodSelector.getSelectedItem();
            if (selectedFood == Food.NONE) {
                hpPercentLabel.setVisible(false);
                hpPercentField.setVisible(false);
            } else {
                hpPercentLabel.setVisible(true);
                hpPercentField.setVisible(true);
            }
        });
        typeSelector.addActionListener(e -> {
            objectSelector.setModel(new DefaultComboBoxModel<>(ThievingObject.getAllObjectsWithType((ThievingType) typeSelector.getSelectedItem())));
            locationSelector.setModel(new DefaultComboBoxModel<>(((ThievingObject) objectSelector.getSelectedItem()).locations));
        });

        objectSelector.addActionListener(e -> {
            locationSelector.setModel(new DefaultComboBoxModel<>(((ThievingObject) objectSelector.getSelectedItem()).locations));
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {

        if (!hpPercentField.getText().isEmpty() && (foodSelector.getSelectedItem()) != Food.NONE) {
            return new ThievingActivity(
                    (ThievingObject) objectSelector.getSelectedItem(),
                    (Food) foodSelector.getSelectedItem(),
                    Integer.parseInt(hpPercentField.getText()),
                    (Location) locationSelector.getSelectedItem(),
                    (ResourceMode) resourceModeSelector.getSelectedItem());

        }
        return new ThievingActivity((ThievingObject) objectSelector.getSelectedItem(), (Location) locationSelector.getSelectedItem(), (ResourceMode) resourceModeSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", ((ThievingType) typeSelector.getSelectedItem()).name());
        jsonObject.put("object", ((ThievingObject) objectSelector.getSelectedItem()).name());
        jsonObject.put("food", ((Food) foodSelector.getSelectedItem()).name());
        jsonObject.put("hp_percent_to_eat", hpPercentField.getText());
        jsonObject.put("location_index", locationSelector.getSelectedIndex());
        jsonObject.put("resource_mode", ((ResourceMode) resourceModeSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        typeSelector.setSelectedItem(ThievingType.valueOf((String) jsonObject.get("type")));
        objectSelector.setSelectedItem(ThievingObject.valueOf((String) jsonObject.get("object")));
        foodSelector.setSelectedItem(Food.valueOf((String) jsonObject.get("food")));
        hpPercentField.setText((String) jsonObject.get("hp_percent_to_eat"));
        locationSelector.setSelectedIndex(new Long((long) jsonObject.get("location_index")).intValue());
        resourceModeSelector.setSelectedItem(ResourceMode.valueOf((String) jsonObject.get("resource_mode")));
    }
}
