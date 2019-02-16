package gui.task_panels;

import gui.fields.IntegerField;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import gui.utils.DurationPanel;
import org.json.simple.JSONObject;
import tasks.LoopTask;
import tasks.Task;
import tasks.TaskType;

import javax.swing.*;
import java.awt.*;

public class LoopTaskPanel extends TaskPanel {

    private JTextField taskCountField;
    private JComboBox<LoopDurationType> loopDurationTypeSelector;
    private JTextField iterationCountField;
    private DurationPanel durationPanel;

    LoopTaskPanel() {
        super(TaskType.LOOP);

        JPanel contentPanel = new StyledJPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        JPanel tasksPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Add counter of previous tasks
        tasksPanel.add(new StyledJLabel("Num Previous Tasks:"));
        taskCountField = new IntegerField();
        taskCountField.setColumns(4);
        tasksPanel.add(taskCountField);

        contentPanel.add(tasksPanel);

        JPanel loopTaskDurationPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.add(loopTaskDurationPanel);

        loopTaskDurationPanel.add(new StyledJLabel("Duration:"));

        loopDurationTypeSelector = new StyledJComboBox<>(LoopDurationType.values());
        loopTaskDurationPanel.add(loopDurationTypeSelector);

        JPanel iterationsPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        iterationsPanel.add(new StyledJLabel("Num Iterations:"));
        iterationCountField = new IntegerField();
        iterationCountField.setColumns(4);
        iterationsPanel.add(iterationCountField);
        loopTaskDurationPanel.add(iterationsPanel);

        durationPanel = new DurationPanel();
        durationPanel.setVisible(false);
        loopTaskDurationPanel.add(durationPanel);

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

        setContentPanel(contentPanel);
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
            return LoopTask.forDuration(taskCount, durationPanel.getDurationMS());
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
            for (Object key : durationObj.keySet()) {
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

