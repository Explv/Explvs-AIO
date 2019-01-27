package org.aio.gui.task_panels;

import org.aio.activities.grand_exchange.GEBuyActivity;
import org.aio.activities.grand_exchange.GEItem;
import org.aio.activities.grand_exchange.GEMode;
import org.aio.activities.grand_exchange.GESellActivity;
import org.aio.activities.grand_exchange.item_guide.ItemGuide;
import org.aio.activities.grand_exchange.price_guide.OSRSPriceGuide;
import org.aio.activities.grand_exchange.price_guide.RSBuddyPriceGuide;
import org.aio.gui.fields.ItemField;
import org.aio.gui.fields.NumberField;
import org.aio.tasks.GrandExchangeTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

public class GETaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JComboBox<GEMode> typeSelector;
    private ItemField itemNameField;
    private JTextField itemQuantityField;
    private JTextField itemPriceField;
    private JCheckBox waitForCompletion;

    GETaskPanel() {

        mainPanel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        controls.add(new JLabel("Type:"));

        typeSelector = new JComboBox<>();
        typeSelector.addActionListener(e -> {
            if (itemNameField.validateItemNameField()) {
                updatePriceField();
            }
        });
        controls.add(typeSelector);

        controls.add(new JLabel("Item Name:"));

        itemNameField = new ItemField();
        itemNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                if (itemNameField.validateItemNameField()) {
                    updatePriceField();
                }
            }
        });
        controls.add(itemNameField);

        controls.add(new JLabel("Quantity:"));

        itemQuantityField = new NumberField();
        itemQuantityField.setColumns(5);
        controls.add(itemQuantityField);

        controls.add(new JLabel("Price:"));

        itemPriceField = new NumberField();
        itemPriceField.setColumns(10);
        controls.add(itemPriceField);

        waitForCompletion = new JCheckBox("Wait for completion");
        waitForCompletion.setSelected(true);
        controls.add(waitForCompletion);

        mainPanel.add(controls, BorderLayout.CENTER);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Grand Exchange Task"));
        typeSelector.setModel(new DefaultComboBoxModel<>(GEMode.values()));
    }

    private void updatePriceField() {
        String itemName = itemNameField.getText().trim();

        if (itemName.isEmpty()) {
            return;
        }

        GEMode geMode = (GEMode) typeSelector.getSelectedItem();

        final Optional<Integer> price = getPrice(itemName, geMode);

        if (price.isPresent()) {
            SwingUtilities.invokeLater(() -> itemPriceField.setText(String.valueOf(price.get())));
        } else {
            SwingUtilities.invokeLater(() -> itemPriceField.setText(""));
        }
    }

    private Optional<Integer> getPrice(final String itemName, final GEMode geMode) {
        Map<String, Integer> allGEItems = ItemGuide.getAllGEItems();

        if (!allGEItems.containsKey(itemName)) {
            return Optional.empty();
        }

        final int itemID = allGEItems.get(itemName);

        Optional<Integer> price;

        if (geMode == GEMode.BUY) {
            price = RSBuddyPriceGuide.getBuyPrice(itemID);
        } else {
            price = RSBuddyPriceGuide.getSellPrice(itemID);
        }

        if (!price.isPresent()) {
            price = OSRSPriceGuide.getPrice(itemID);
        }

        return price;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        GEMode geMode = (GEMode) typeSelector.getSelectedItem();
        GEItem geItem = new GEItem(itemNameField.getText(), Integer.parseInt(itemQuantityField.getText()), Integer.parseInt(itemPriceField.getText()));
        if (geMode == GEMode.BUY) {
            return new GrandExchangeTask(new GEBuyActivity(geItem), geMode, geItem, waitForCompletion.isSelected());
        }
        return new GrandExchangeTask(new GESellActivity(geItem), geMode, geItem, waitForCompletion.isSelected());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskJSONObject = new JSONObject();
        taskJSONObject.put("type", TaskType.GRAND_EXCHANGE.name());
        taskJSONObject.put("mode", ((GEMode) typeSelector.getSelectedItem()).name());
        taskJSONObject.put("item_name", itemNameField.getText());
        taskJSONObject.put("item_quantity", itemQuantityField.getText());
        taskJSONObject.put("item_price", itemPriceField.getText());
        taskJSONObject.put("wait_for_completion", waitForCompletion.isSelected());
        return taskJSONObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        typeSelector.setSelectedItem(GEMode.valueOf((String) jsonObject.get("mode")));
        itemNameField.setText((String) jsonObject.get("item_name"));
        itemQuantityField.setText((String) jsonObject.get("item_quantity"));
        itemPriceField.setText((String) jsonObject.get("item_price"));

        if (jsonObject.containsKey("wait_for_completion")) {
            waitForCompletion.setSelected((boolean) jsonObject.get("wait_for_completion"));
        }
    }
}
