package org.aio.gui.task_panels;

import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.loop_task.LoopTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class LoopTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JTextField iterationCountField;
    private JTextField taskCountField;

    LoopTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Add counter of previous tasks
        controls.add(new JLabel("# Previous Tasks:"));
        taskCountField = new JTextField();
        taskCountField.setColumns(2);
        ((AbstractDocument) taskCountField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(taskCountField);

        // Add counter of previous tasks
        controls.add(new JLabel("# Iterations:"));
        controls.add(new JLabel("(-1 to loop forever)"));
        iterationCountField = new JTextField();
        iterationCountField.setColumns(2);
        ((AbstractDocument) iterationCountField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(iterationCountField);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Loop Task"));

        mainPanel.add(controls, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        return new LoopTask(Integer.parseInt(taskCountField.getText()), Integer.parseInt(iterationCountField.getText()));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.LOOP.name());
        jsonObject.put("taskCount", taskCountField.getText());
        jsonObject.put("iterationCount", iterationCountField.getText());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        taskCountField.setText((String) jsonObject.get("taskCount"));
        iterationCountField.setText((String) jsonObject.get("iterationCount"));
    }
}

