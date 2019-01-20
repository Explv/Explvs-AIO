package org.aio.gui.task_panels;

import org.aio.gui.fields.NumberField;
import org.aio.gui.utils.DurationPanel;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.LoopTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class LoopTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JTextField taskCountField;
    private JComboBox<LoopDurationType> loopDurationTypeSelector;
    private JTextField iterationCountField;
    private DurationPanel durationPanel;

    LoopTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Loop Task"));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Add counter of previous tasks
        controls.add(new JLabel("Num Previous Tasks:"));
        taskCountField = new NumberField();
        taskCountField.setColumns(4);
        controls.add(taskCountField);

        controls.add(new JLabel("Duration:"));

        loopDurationTypeSelector = new JComboBox<>(LoopDurationType.values());
        controls.add(loopDurationTypeSelector);

        JPanel iterationsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        iterationsPanel.add(new JLabel("Num Iterations:"));
        iterationCountField = new NumberField();
        iterationCountField.setColumns(4);
        iterationsPanel.add(iterationCountField);
        controls.add(iterationsPanel);

        durationPanel = new DurationPanel();
        durationPanel.setVisible(false);
        controls.add(durationPanel);

        mainPanel.add(controls, BorderLayout.SOUTH);

        loopDurationTypeSelector.addActionListener(e -> {
            LoopDurationType loopDurationType = (LoopDurationType) loopDurationTypeSelector.getSelectedItem();
            if (loopDurationType == LoopDurationType.ITERATIONS) {
                iterationsPanel.setVisible(true);
                durationPanel.setVisible(false);
            } else if (loopDurationType == LoopDurationType.INFINITE) {
                iterationsPanel.setVisible(false);
                durationPanel.setVisible(false);
            } else if (loopDurationType == LoopDurationType.TIME) {
                iterationsPanel.setVisible(false);
                durationPanel.setVisible(true);
            }
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        LoopDurationType loopDurationType = (LoopDurationType) loopDurationTypeSelector.getSelectedItem();

        int taskCount = Integer.parseInt(taskCountField.getText());

        if (loopDurationType == LoopDurationType.ITERATIONS) {
            return LoopTask.forIterations(taskCount, Integer.parseInt(iterationCountField.getText()));
        } else if (loopDurationType == LoopDurationType.INFINITE) {
            return LoopTask.forIterations(taskCount, LoopTask.INFINITE_ITERATIONS);
        } else if (durationPanel.getSelectedTimeType() == DurationPanel.TimeType.MINUTES) {
            return LoopTask.forDuration(taskCount, TimeUnit.MINUTES.toMillis(durationPanel.getDuration()));
        } else {
            return LoopTask.untilDateTime(taskCount, durationPanel.getSelectedDateTime());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TaskType.LOOP.name());
        jsonObject.put("taskCount", taskCountField.getText());

        LoopDurationType loopDurationType = (LoopDurationType) loopDurationTypeSelector.getSelectedItem();

        if (loopDurationType == LoopDurationType.ITERATIONS) {
            jsonObject.put("iterationCount", iterationCountField.getText());
        } else if (loopDurationType == loopDurationType.INFINITE) {
            jsonObject.put("loopForever", true);
        } else if (loopDurationType == LoopDurationType.TIME) {
            JSONObject durationObj = durationPanel.toJSON();
            for (Object key: durationObj.keySet()) {
                jsonObject.put(key, durationObj.get(key));
            }
        }
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        taskCountField.setText((String) jsonObject.get("taskCount"));

        if (jsonObject.containsKey("iterationCount")) {
            iterationCountField.setText((String) jsonObject.get("iterationCount"));
            loopDurationTypeSelector.setSelectedItem(LoopDurationType.ITERATIONS);
        } else if (jsonObject.containsKey("loopForever")) {
            loopDurationTypeSelector.setSelectedItem(LoopDurationType.INFINITE);
        } else {
            loopDurationTypeSelector.setSelectedItem(LoopDurationType.TIME);
            durationPanel.fromJSON(jsonObject);
        }
    }

    enum LoopDurationType {
        ITERATIONS("Iterations"),
        INFINITE("Infinite"),
        TIME("Time");

        String name;

        LoopDurationType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

