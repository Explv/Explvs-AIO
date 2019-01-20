package org.aio.gui.task_panels;

import org.aio.gui.fields.ItemField;
import org.aio.gui.fields.NumberField;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.ResourceTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ResourceTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private ItemField resourceField;
    private JTextField quantityField;
    private ActivitySelectorPanel activitySelectorPanel;

    ResourceTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        JPanel bottomControls = new JPanel();
        bottomControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        bottomControls.add(new JLabel("Name of item:"));

        resourceField = new ItemField();
        bottomControls.add(resourceField);

        final JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        bottomControls.add(panel1);

        bottomControls.add(new JLabel("Quantity of item:"));

        quantityField = new NumberField();
        quantityField.setColumns(5);
        bottomControls.add(quantityField);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Resource Task"));

        mainPanel.add(bottomControls, BorderLayout.SOUTH);

        activitySelectorPanel = new ActivitySelectorPanel(this);
        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        return new ResourceTask(
                activitySelectorPanel.getActivityPanel().toActivity(),
                resourceField.getText(),
                Integer.parseInt(quantityField.getText())
        );
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
