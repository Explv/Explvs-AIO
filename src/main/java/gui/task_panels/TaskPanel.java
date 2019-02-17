package gui.task_panels;

import gui.IconButton;
import gui.interfaces.JSONSerializable;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import gui.utils.ColourScheme;
import tasks.Task;
import tasks.TaskType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A TaskPanel used to create tasks of `type`
 */
public abstract class TaskPanel implements JSONSerializable {

    private JPanel mainPanel;
    private JPanel contentPanel;

    private JButton removeTaskButton;
    private JButton moveTaskUpButton;
    private JButton moveTaskDownButton;

    private JMenuItem menuItemRemoveTask;
    private JMenuItem menuItemMoveTaskUp;
    private JMenuItem menuItemMoveTaskDown;

    public TaskPanel(final TaskType taskType) {
        mainPanel = new StyledJPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(ColourScheme.PANEL_BACKGROUND_GREY);

        JPanel northControlsPanel = new StyledJPanel();
        mainPanel.add(northControlsPanel, BorderLayout.NORTH);
        northControlsPanel.setBackground(Color.decode("#404040"));
        northControlsPanel.setLayout(new BoxLayout(northControlsPanel, BoxLayout.X_AXIS));
        northControlsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        Box box = Box.createHorizontalBox();
        northControlsPanel.add(box);
        JLabel titleLabel = new StyledJLabel(taskType.toString());
        titleLabel.setForeground(ColourScheme.BLUE);
        box.add(titleLabel);

        box.add(Box.createHorizontalGlue());

        moveTaskUpButton = IconButton.createButton("Move task up", "images/moveUpIcon.png", "images/moveUpIconHover.png", null);
        box.add(moveTaskUpButton);

        box.add(Box.createHorizontalStrut(5));

        moveTaskDownButton = IconButton.createButton("Move task down", "images/moveDownIcon.png", "images/moveDownIconHover.png", null);
        box.add(moveTaskDownButton);

        box.add(Box.createHorizontalStrut(5));

        removeTaskButton = IconButton.createButton("Remove task", "images/closeIcon.png", "images/closeIconHover.png", null);
        box.add(removeTaskButton);

        JPopupMenu contextMenu = new JPopupMenu();
        menuItemRemoveTask = new JMenuItem("Delete");
        menuItemMoveTaskUp = new JMenuItem("Move up");
        menuItemMoveTaskDown = new JMenuItem("Move down");

        contextMenu.add(menuItemRemoveTask);
        contextMenu.add(new JSeparator());
        contextMenu.add(menuItemMoveTaskUp);
        contextMenu.add(menuItemMoveTaskDown);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    contextMenu.show(mainPanel, e.getX(), e.getY());
                }
            }
        });
    }

    public void addRemoveTaskActionListener(final ActionListener actionListener) {
        removeTaskButton.addActionListener(actionListener);
        menuItemRemoveTask.addActionListener(actionListener);
    }

    public void addMoveUpActionListener(final ActionListener actionListener) {
        menuItemMoveTaskUp.addActionListener(actionListener);
        moveTaskUpButton.addActionListener(actionListener);
    }

    public void addMoveDownActionListener(final ActionListener actionListener) {
        menuItemMoveTaskDown.addActionListener(actionListener);
        moveTaskDownButton.addActionListener(actionListener);
    }

    void setContentPanel(final JPanel contentPanel) {
        if (this.contentPanel != null) {
            mainPanel.remove(contentPanel);
        }
        this.contentPanel = contentPanel;
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    public abstract Task toTask();

    public JPanel getPanel() {
        return mainPanel;
    }
}
