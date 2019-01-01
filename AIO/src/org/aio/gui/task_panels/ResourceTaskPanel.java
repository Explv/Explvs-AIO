package org.aio.gui.task_panels;

import org.aio.activities.grand_exchange.GrandExchangeHelper;
import org.aio.gui.utils.AutoCompleteTextField;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.resource_task.ResourceTask;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ResourceTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private AutoCompleteTextField resourceField;
    private JTextField quantityField;
    private ActivitySelectorPanel activitySelectorPanel;

    public ResourceTaskPanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel bottomControls = new JPanel();
        bottomControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        bottomControls.add(new JLabel("Name of item:"));

        resourceField = new AutoCompleteTextField();
        resourceField.addPosibilities(GrandExchangeHelper.getAllGEItems().keySet());
        resourceField.setColumns(15);
        bottomControls.add(resourceField);

        final JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        bottomControls.add(panel1);

        bottomControls.add(new JLabel("Quantity of item:"));

        quantityField = new JTextField();
        quantityField.setColumns(5);
        ((AbstractDocument) quantityField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        bottomControls.add(quantityField);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Resource Task"));

        mainPanel.add(bottomControls, BorderLayout.SOUTH);

        activitySelectorPanel = new ActivitySelectorPanel();
        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        return new ResourceTask(activitySelectorPanel.getActivityPanel().toActivity(), resourceField.getText(), Integer.parseInt(quantityField.getText()));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.RESOURCE.name());
        jsonObject.put("resource_name", resourceField.getText());
        jsonObject.put("resource_quantity", quantityField.getText());
        jsonObject.put("activity", activitySelectorPanel.toJSON());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        resourceField.setText((String) jsonObject.get("resource_name"));
        quantityField.setText((String) jsonObject.get("resource_quantity"));
        activitySelectorPanel.fromJSON((JSONObject) jsonObject.get("activity"));
    }
}
