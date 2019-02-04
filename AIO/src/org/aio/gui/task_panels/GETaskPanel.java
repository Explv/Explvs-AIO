package org.aio.gui.task_panels;

import org.aio.activities.grand_exchange.GEBuyActivity;
import org.aio.activities.grand_exchange.GEItem;
import org.aio.activities.grand_exchange.GEMode;
import org.aio.activities.grand_exchange.GESellActivity;
import org.aio.activities.grand_exchange.item_guide.ItemGuide;
import org.aio.activities.grand_exchange.price_guide.OSRSPriceGuide;
import org.aio.activities.grand_exchange.price_guide.RSBuddyPriceGuide;
import org.aio.gui.fields.ItemField;
import org.aio.gui.fields.RSUnitField;
import org.aio.gui.styled_components.StyledJCheckBox;
import org.aio.gui.styled_components.StyledJComboBox;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;
import org.aio.tasks.GrandExchangeTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.util.RSUnits;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

public class GETaskPanel extends TaskPanel {

    private JComboBox<GEMode> typeSelector;
    private ItemField itemNameField;
    private RSUnitField itemQuantityField;
    private RSUnitField itemPriceField;
    private JCheckBox waitForCompletion;

    GETaskPanel() {
        super(TaskType.GRAND_EXCHANGE);

        JPanel contentPanel = new StyledJPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel typePanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        typePanel.add(new StyledJLabel("Type:"));

        typeSelector = new StyledJComboBox<>(GEMode.values());
        typeSelector.addActionListener(e -> {
            if (itemNameField.validateItemNameField()) {
                updatePriceField();
            }
        });
        typePanel.add(typeSelector);

        contentPanel.add(typePanel);

        JPanel itemPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT));

        itemPanel.add(new StyledJLabel("Item Name:"));

        itemNameField = new ItemField();
        itemNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                if (itemNameField.validateItemNameField()) {
                    updatePriceField();
                }
            }
        });
        itemPanel.add(itemNameField);

        contentPanel.add(itemPanel);

        JPanel priceQuantityPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT));

        priceQuantityPanel.add(new StyledJLabel("Quantity:"));

        itemQuantityField = new RSUnitField();
        itemQuantityField.setColumns(10);
        priceQuantityPanel.add(itemQuantityField);

        priceQuantityPanel.add(new StyledJLabel("Price:"));

        itemPriceField = new RSUnitField();
        itemPriceField.setColumns(10);
        priceQuantityPanel.add(itemPriceField);

        contentPanel.add(priceQuantityPanel);

        JPanel waitCompletionPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT));

        waitForCompletion = new StyledJCheckBox("Wait for completion");
        waitForCompletion.setSelected(true);
        waitCompletionPanel.add(waitForCompletion);

        contentPanel.add(waitCompletionPanel);

        setContentPanel(contentPanel);
    }

    private void updatePriceField() {
        String itemName = itemNameField.getText().trim();

        if (itemName.isEmpty()) {
            return;
        }

        GEMode geMode = (GEMode) typeSelector.getSelectedItem();

        final Optional<Integer> price = getPrice(itemName, geMode);

        if (price.isPresent()) {
            SwingUtilities.invokeLater(() -> itemPriceField.setText(RSUnits.valueToFormatted(price.get())));
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

    @Override
    public Task toTask() {
        GEMode geMode = (GEMode) typeSelector.getSelectedItem();

        GEItem geItem = new GEItem(
                itemNameField.getText(),
                (int) itemQuantityField.getValue(),
                (int) itemPriceField.getValue()
        );

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
