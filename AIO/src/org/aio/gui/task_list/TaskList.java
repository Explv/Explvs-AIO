package org.aio.gui.task_list;

import org.aio.gui.interfaces.JSONSerializable;
import org.aio.gui.styled_components.StyledJPanel;
import org.aio.gui.styled_components.StyledJScrollPane;
import org.aio.gui.task_panels.TaskPanel;
import org.aio.gui.task_panels.TaskPanelFactory;
import org.aio.gui.utils.ColourScheme;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.TutorialIslandTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TaskList implements JSONSerializable {

    private JScrollPane scrollPane;
    private JPanel taskList = new StyledJPanel();

    private ArrayList<TaskPanelContent> taskPanels = new ArrayList<>();

    public TaskList() {
        taskList.setLayout(new BoxLayout(taskList, BoxLayout.Y_AXIS));
        taskList.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        taskList.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        scrollPane = new StyledJScrollPane();
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scrollPane.setViewportView(taskList);
        scrollPane.setPreferredSize(new Dimension(700, 500));
    }

    public Container getContainer() {
        return scrollPane;
    }

    public TaskPanel addTask(final TaskType taskType) {
        TaskPanel taskPanel = TaskPanelFactory.createTaskPanel(taskType);

        if (taskPanel == null) {
            throw new IllegalArgumentException(String.format("Task type %s not supported.", taskType.toString()));
        }

        ArrayList<Component> components = new ArrayList<>();
        components.add(taskPanel.getPanel());
        components.add(Box.createRigidArea(new Dimension(5, 10)));
        TaskPanelContent taskPanelContent = new TaskPanelContent(taskPanel, components);
        taskPanels.add(taskPanelContent);

        for (Component component : components) {
            taskList.add(component);
        }

        taskPanel.getPanel().setMaximumSize(
                new Dimension(
                        taskPanel.getPanel().getMaximumSize().width,
                        taskPanel.getPanel().getPreferredSize().height
                )
        );

        /*
         UI Actions
        */
        taskPanel.addRemoveTaskActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                removeTaskPanel(taskPanelContent);
            });
        });

        taskPanel.addMoveUpActionListener(e -> {
            int from = taskPanels.indexOf(taskPanelContent);
            swapTasks(from, from - 1);
        });

        taskPanel.addMoveDownActionListener(e -> {
            int from = taskPanels.indexOf(taskPanelContent);
            swapTasks(from, from + 1);
        });

        ((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, scrollPane)).pack();

        return taskPanel;
    }

    private void swapTasks(int from, int to) {
        SwingUtilities.invokeLater(() -> {
            if (from < 0 || from >= taskPanels.size() || to < 0 || to >= taskPanels.size()) {
                return;
            }

            Collections.swap(taskPanels, from, to);

            taskList.removeAll();
            for (TaskPanelContent redrawTaskPanelContent : taskPanels) {
                for (Component component : redrawTaskPanelContent.components) {
                    taskList.add(component);
                }
            }

            taskList.revalidate();
            taskList.repaint();

            ((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, scrollPane)).pack();
        });
    }

    private void removeTaskPanel(final TaskPanelContent taskPanelContent) {
        taskPanels.remove(taskPanelContent);

        for (Component component : taskPanelContent.components) {
            taskList.remove(component);
        }

        taskList.revalidate();
        taskList.repaint();
        ((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, scrollPane)).pack();
    }

    /**
     * Public getter for the entire ordered task list
     */
    public final ArrayList<Task> getTasksAsList() {
        ArrayList<Task> tasks = new ArrayList<>();

        int taskIndex = 0;

        Task tutorialIslandTask = new TutorialIslandTask();
        tutorialIslandTask.setExecutionOrder(taskIndex);
        tasks.add(tutorialIslandTask);
        taskIndex++;

        for (TaskPanelContent taskPanel : taskPanels) {
            Task task = taskPanel.panel.toTask();
            task.setExecutionOrder(taskIndex);
            tasks.add(task);
            taskIndex++;
        }

        return tasks;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        JSONArray taskJSONArray = new JSONArray();

        for (TaskPanelContent taskPanel : taskPanels) {
            taskJSONArray.add(taskPanel.panel.toJSON());
        }

        jsonObject.put("tasks", taskJSONArray);

        return jsonObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        taskList.removeAll();
        taskPanels.clear();

        JSONArray tasks = (JSONArray) jsonObject.get("tasks");

        for (Object task : tasks) {
            JSONObject taskJSON = (JSONObject) task;
            addTask(TaskType.valueOf((String) taskJSON.get("type"))).fromJSON(taskJSON);
        }

        taskList.validate();
        taskList.repaint();

        JDialog dialogAncestor = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, scrollPane);

        dialogAncestor.validate();
        dialogAncestor.repaint();
        dialogAncestor.pack();
    }

    /**
     * Task panel content for use with tracking rendered/interactive content for each task panel instance
     */
    class TaskPanelContent {
        TaskPanel panel;
        List<Component> components;

        TaskPanelContent(TaskPanel panel, List<Component> components) {
            this.panel = panel;
            this.components = components;
        }
    }
}
