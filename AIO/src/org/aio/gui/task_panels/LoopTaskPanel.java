package org.aio.gui.task_panels;

import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.LoopTask;
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
    private JCheckBox loopforeverBox;

    LoopTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Add counter of previous tasks
        controls.add(new JLabel("Num Previous Tasks:"));
        taskCountField = new JTextField();
        taskCountField.setColumns(4);
        ((AbstractDocument) taskCountField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(taskCountField);

        controls.add(new JLabel("Num Iterations:"));
        iterationCountField = new JTextField();
        iterationCountField.setColumns(4);
        ((AbstractDocument) iterationCountField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        controls.add(iterationCountField);

        loopforeverBox = new JCheckBox("Loop forever");
        controls.add(loopforeverBox);

        loopforeverBox.addActionListener(e -> {
            if (loopforeverBox.isSelected()) {
                iterationCountField.setEnabled(false);
            } else {
                iterationCountField.setEnabled(true);
            }
        });

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Loop Task"));

        mainPanel.add(controls, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        int numIterations = loopforeverBox.isSelected() ? LoopTask.INFINITE_ITERATIONS : Integer.parseInt(iterationCountField.getText());

        return new LoopTask(Integer.parseInt(taskCountField.getText()), numIterations);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.LOOP.name());
        jsonObject.put("taskCount", taskCountField.getText());
        jsonObject.put("iterationCount", iterationCountField.getText());
        jsonObject.put("loopForever", loopforeverBox.isSelected());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        taskCountField.setText((String) jsonObject.get("taskCount"));
        iterationCountField.setText((String) jsonObject.get("iterationCount"));
        loopforeverBox.setSelected((boolean) jsonObject.get("loopForever"));
    }
}

