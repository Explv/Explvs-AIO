package gui;

import gui.conf_man.ConfigManager;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import gui.task_list.TaskList;
import gui.utils.ColourScheme;
import org.json.simple.JSONObject;
import script.AIO;
import tasks.Task;
import tasks.TaskType;
import util.file_managers.FontManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class Gui {
    
    private JDialog gui;
    private TaskList taskList;

    private boolean started;

    public Gui() {
        gui = new JDialog();
        gui.setTitle("Explv's AIO " + AIO.VERSION);
        gui.setModal(true);
        gui.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        gui.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);

        JPanel mainPanel = new StyledJPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));

        final JLabel titleLabel = new StyledJLabel();
        titleLabel.setFont(FontManager.ROBOTO_REGULAR.deriveFont(Font.BOLD, 26));
        titleLabel.setForeground(ColourScheme.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText(
                String.format("<html><span color='#33b5e5'>Explv</span>'s AIO <span style='font-size:16'>%s</span></html>",
                        AIO.VERSION
                )
        );
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        final JPanel controlsPanel = new StyledJPanel();
        controlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        controlsPanel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        final JPanel saveLoadPanel = new StyledJPanel();
        saveLoadPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        saveLoadPanel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        controlsPanel.add(saveLoadPanel);
        saveLoadPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Save / Load",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Roboto-Regular", Font.PLAIN, 12),
                        ColourScheme.WHITE
                )
        );

        saveLoadPanel.add(createButtonPanel(
                "Save",
                "Save",
                "images/saveIcon.png",
                "images/saveIconHover.png",
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
                "images/loadIcon.png",
                "images/loadIconHover.png",
                e -> loadConfig()
        ));

        controlsPanel.add(createSpacerPanel());

        taskList = new TaskList();

        final JPanel addTaskPanel = new StyledJPanel();
        addTaskPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addTaskPanel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        controlsPanel.add(addTaskPanel);
        addTaskPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Add a Task",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        FontManager.ROBOTO_REGULAR,
                        ColourScheme.WHITE
                )
        );

        addTaskPanel.add(createButtonPanel(
                "Level",
                "Level Task",
                "images/levelIcon.png",
                "images/levelIconHover.png",
                e -> taskList.addTask(TaskType.LEVEL)
        ));

        addTaskPanel.add(createButtonPanel(
                "Resource",
                "Resource Task",
                "images/resourceIcon.png",
                "images/resourceIconHover.png",
                e -> taskList.addTask(TaskType.RESOURCE)
        ));

        addTaskPanel.add(createButtonPanel(
                "Timed",
                "Timed Task",
                "images/timedIcon.png",
                "images/timedIconHover.png",
                e -> taskList.addTask(TaskType.TIMED)
        ));

        addTaskPanel.add(createButtonPanel(
                "Loop",
                "Loop Previous Tasks",
                "images/loopIcon.png",
                "images/loopIconHover.png",
                e -> taskList.addTask(TaskType.LOOP)
        ));

        addTaskPanel.add(createButtonPanel(
                "Quest",
                "Quest Task",
                "images/questIcon.png",
                "images/questIconHover.png",
                e -> taskList.addTask(TaskType.QUEST)
        ));

        addTaskPanel.add(createButtonPanel(
                "Grand Exchange",
                "GE Task",
                "images/geIcon.png",
                "images/geIconHover.png",
                e -> taskList.addTask(TaskType.GRAND_EXCHANGE)
        ));

        addTaskPanel.add(createButtonPanel(
                "Break",
                "Break Task",
                "images/breakIcon.png",
                "images/breakIconHover.png",
                e -> taskList.addTask(TaskType.BREAK)
        ));
        controlsPanel.add(createSpacerPanel());

        final JPanel startPanel = new StyledJPanel();
        startPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        startPanel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        controlsPanel.add(startPanel);
        startPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Start",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        FontManager.ROBOTO_REGULAR,
                        ColourScheme.WHITE
                )
        );

        startPanel.add(createButtonPanel(
                "Start",
                "Start",
                "images/startIcon.png",
                "images/startIconHover.png",
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

        gui.setMinimumSize(new Dimension(700, 500));

        gui.setLocationRelativeTo(null);
        gui.setContentPane(mainPanel);
        gui.pack();
        gui.setResizable(true);
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.open();
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
        panel.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return panel;
    }

    private JPanel createButtonPanel(final String label, final String toolTip, final String icon, final String rolloverIcon, ActionListener callback) {
        JPanel buttonPanel = new StyledJPanel(new BorderLayout(0, 3));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        buttonPanel.setOpaque(false);

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
}
