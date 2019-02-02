package org.aio.gui.task_panels;

import org.aio.gui.fields.ItemField;
import org.aio.gui.fields.NumberField;
import org.aio.gui.fields.RSUnitField;
import org.aio.tasks.ResourceTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ResourceTaskPanel extends TaskPanel {

    private ItemField resourceField;
    private RSUnitField quantityField;
    private ActivitySelectorPanel activitySelectorPanel;

    ResourceTaskPanel(){
        super(TaskType.RESOURCE);

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel bottomControls = new JPanel();
        bottomControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        bottomControls.add(new JLabel("Name of item:"));

        resourceField = new ItemField();
        bottomControls.add(resourceField);

        final JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        bottomControls.add(panel1);

        bottomControls.add(new JLabel("Quantity of item:"));

        quantityField = new RSUnitField();
        quantityField.setColumns(5);
        bottomControls.add(quantityField);

        contentPanel.add(bottomControls, BorderLayout.SOUTH);

        activitySelectorPanel = new ActivitySelectorPanel(this);
        contentPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);

        setContentPanel(contentPanel);
    }

    @Override
    public Task toTask() {
        return new ResourceTask(
                activitySelectorPanel.getActivityPanel().toActivity(),
                resourceField.getText(),
                (int) quantityField.getValue()
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
