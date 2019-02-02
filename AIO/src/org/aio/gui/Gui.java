package org.aio.gui;

import org.aio.gui.conf_man.ConfigManager;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;
import org.aio.gui.task_list.TaskList;
import org.aio.gui.utils.ColourScheme;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class Gui {

    public static final Color DARK_GREY = Color.decode("#181818");

    private JDialog gui;
    private TaskList taskList;

    private boolean started;

    public Gui() {
        gui = new JDialog();
        gui.setTitle("Explv's AIO");
        gui.setModal(true);
        gui.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        gui.setBackground(DARK_GREY);

        JPanel mainPanel = new StyledJPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(DARK_GREY);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20), null));

        final JLabel titleLabel = new StyledJLabel();
        titleLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 26));
        titleLabel.setForeground(ColourScheme.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText("<html><span color='#33b5e5'>Explv</span>'s AIO</html>");
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        final JPanel controlsPanel = new StyledJPanel();
        controlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        controlsPanel.setBackground(DARK_GREY);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        final JPanel saveLoadPanel = new StyledJPanel();
        saveLoadPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        saveLoadPanel.setBackground(DARK_GREY);
        controlsPanel.add(saveLoadPanel);
        saveLoadPanel.setBorder(BorderFactory.createTitledBorder(null, "Save / Load", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, ColourScheme.WHITE));

        saveLoadPanel.add(createButtonPanel(
                "Save",
                "Save",
                "saveIcon.png",
                "saveIconHover.png",
                e -> {
                    if (!validate(gui)) {
                        JOptionPane.showMessageDialog(gui, "Fields highlighted in red are invalid", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        saveConfig();
                    }
                }
        ));

        saveLoadPanel.add(createButtonPanel(
                "Load",
                "Load",
                "loadIcon.png",
                "loadIconHover.png",
                e -> loadConfig()
        ));

        controlsPanel.add(createSpacerPanel());

        taskList = new TaskList();

        final JPanel addTaskPanel = new StyledJPanel();
        addTaskPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addTaskPanel.setBackground(DARK_GREY);
        controlsPanel.add(addTaskPanel);
        addTaskPanel.setBorder(BorderFactory.createTitledBorder(null, "Add a Task", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, ColourScheme.WHITE));

        addTaskPanel.add(createButtonPanel(
                "Level",
                "Level Task",
                "levelIcon.png",
                "levelIconHover.png",
                e -> taskList.addTask(TaskType.LEVEL)
        ));

        addTaskPanel.add(createButtonPanel(
                "Resource",
                "Resource Task",
                "resourceIcon.png",
                "resourceIconHover.png",
                e -> taskList.addTask(TaskType.RESOURCE)
        ));

        addTaskPanel.add(createButtonPanel(
                "Timed",
                "Timed Task",
                "timedIcon.png",
                "timedIconHover.png",
                e -> taskList.addTask(TaskType.TIMED)
        ));

        addTaskPanel.add(createButtonPanel(
                "Loop",
                "Loop Previous Tasks",
                "loopIcon.png",
                "loopIconHover.png",
                e -> taskList.addTask(TaskType.LOOP)
        ));

        addTaskPanel.add(createButtonPanel(
                "Quest",
                "Quest Task",
                "questIcon.png",
                "questIconHover.png",
                e -> taskList.addTask(TaskType.QUEST)
        ));

        addTaskPanel.add(createButtonPanel(
                "Grand Exchange",
                "GE Task",
                "geIcon.png",
                "geIconHover.png",
                e -> taskList.addTask(TaskType.GRAND_EXCHANGE)
        ));

        addTaskPanel.add(createButtonPanel(
                "Break",
                "Break Task",
                "breakIcon.png",
                "breakIconHover.png",
                e -> taskList.addTask(TaskType.BREAK)
        ));
        controlsPanel.add(createSpacerPanel());

        final JPanel startPanel = new StyledJPanel();
        startPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        startPanel.setBackground(DARK_GREY);
        controlsPanel.add(startPanel);
        startPanel.setBorder(BorderFactory.createTitledBorder(null, "Start", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, ColourScheme.WHITE));

        startPanel.add(createButtonPanel(
                "Start",
                "Start",
                "startIcon.png",
                "startIconHover.png",
                e -> {
                    if (!validate(gui)) {
                        JOptionPane.showMessageDialog(gui, "Fields highlighted in red are invalid", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        started = true;
                        close();
                    }
                }
        ));

        controlsPanel.add(startPanel);

        mainPanel.add(taskList.getContainer(), BorderLayout.CENTER);

        gui.setMinimumSize(new Dimension(700, 300));
        gui.setMaximumSize(new Dimension(2000, 2000));

        gui.setLocationRelativeTo(null);
        gui.setContentPane(mainPanel);
        gui.pack();
        gui.setResizable(true);
    }

    private boolean validate(final Container container) {
        boolean valid = true;

        Component[] comps = container.getComponents();
        for (Component component : comps) {
            if (!component.isVisible()) {
                continue;
            }

            if (component instanceof JComponent) {
                JComponent jComponent = (JComponent) component;
                if (jComponent.getInputVerifier() != null) {
                    if (!jComponent.getInputVerifier().verify(jComponent)) {
                        valid = false;
                    }
                }
            }
            if (component instanceof Container) {
                if (!validate((Container) component)) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    private JPanel createSpacerPanel() {
        final JPanel panel = new StyledJPanel(new BorderLayout(0, 0));
        panel.setBackground(DARK_GREY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20), null));
        return panel;
    }

    private JPanel createButtonPanel(final String label, final String toolTip, final String icon, final String rolloverIcon, ActionListener callback) {
        JPanel buttonPanel = new StyledJPanel(new BorderLayout(0, 3));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        buttonPanel.setBackground(DARK_GREY);

        final JLabel panelLabel = new StyledJLabel();
        panelLabel.setForeground(ColourScheme.WHITE);
        panelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelLabel.setText(label);
        buttonPanel.add(panelLabel, BorderLayout.SOUTH);

        JButton button = IconButton.createButton(toolTip, icon, rolloverIcon, callback);

        buttonPanel.add(button, BorderLayout.NORTH);

        return buttonPanel;
    }

    public List<Task> getTasksAsList() {
        return taskList.getTasksAsList();
    }

    public boolean isStarted() {
        return started;
    }

    public void open() {
        gui.setVisible(true);
    }

    public boolean isOpen() {
        return gui.isVisible();
    }

    public void close() {
        gui.setVisible(false);
        gui.dispose();
    }

    private void saveConfig() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                ConfigManager configManager = new ConfigManager();
                configManager.saveConfig(taskList.toJSON());
                return null;
            }
        }.execute();
    }

    private void loadConfig() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                ConfigManager configManager = new ConfigManager();

                Optional<JSONObject> jsonObjectOpt = configManager.readConfig();

                jsonObjectOpt.ifPresent(jsonObject -> taskList.fromJSON(jsonObject));

                return null;
            }
        }.execute();
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.open();
    }
}
