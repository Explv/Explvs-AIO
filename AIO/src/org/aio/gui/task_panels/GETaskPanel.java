package org.aio.gui.task_panels;

import org.aio.activities.grand_exchange.*;
import org.aio.gui.utils.AutoCompleteTextField;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.grand_exchange_task.GrandExchangeTask;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class GETaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JComboBox<GEMode> typeSelector;
    private AutoCompleteTextField itemNameField;
    private JTextField itemQuantityField;
    private JTextField itemPriceField;
    private JCheckBox waitForCompletion;

    public GETaskPanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        controls.add(new JLabel("Type:"));

        typeSelector = new JComboBox<>();
        controls.add(typeSelector);

        controls.add(new JLabel("Item Name:"));

        itemNameField = new AutoCompleteTextField();
        itemNameField.setColumns(20);
        itemNameField.addPosibilities(GrandExchangeHelper.getAllGEItems().keySet());
        itemNameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {

            }

            @Override
            public void keyReleased(final KeyEvent e) {
                validateItemNameField();
                updatePriceField();
            }
        });
        controls.add(itemNameField);

        controls.add(new JLabel("Quantity:"));

        itemQuantityField = new JTextField();
        itemQuantityField.setColumns(5);
        ((AbstractDocument) itemQuantityField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(itemQuantityField);

        controls.add(new JLabel("Price:"));

        itemPriceField = new JTextField();
        itemPriceField.setColumns(10);
        ((AbstractDocument) itemPriceField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(itemPriceField);

        waitForCompletion = new JCheckBox("Wait for completion");
        waitForCompletion.setSelected(true);
        controls.add(waitForCompletion);

        mainPanel.add(controls, BorderLayout.CENTER);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Grand Exchange Task"));
        typeSelector.setModel(new DefaultComboBoxModel<>(GEMode.values()));
    }

    private void validateItemNameField() {
        if (!GrandExchangeHelper.getAllGEItems().containsKey(itemNameField.getText())) {
            itemNameField.setForeground(Color.RED);
        } else {
            itemNameField.setForeground(Color.BLACK);
        }
    }

    private void updatePriceField() {
        String itemName = itemNameField.getText();
        if (itemName.isEmpty()) {
            return;
        }

        Map<String, Integer> allGEItems = GrandExchangeHelper.getAllGEItems();

        if (!allGEItems.containsKey(itemName)) {
            return;
        }

        if (typeSelector.getSelectedItem() == GEMode.BUY) {
            GrandExchangeHelper.getBuyPrice(allGEItems.get(itemName)).ifPresent(price -> {
                SwingUtilities.invokeLater(() -> itemPriceField.setText(String.valueOf(price)));
            });
        } else {
            GrandExchangeHelper.getSellPrice(allGEItems.get(itemName)).ifPresent(price -> {
                SwingUtilities.invokeLater(() -> itemPriceField.setText(String.valueOf(price)));
            });
        }
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
