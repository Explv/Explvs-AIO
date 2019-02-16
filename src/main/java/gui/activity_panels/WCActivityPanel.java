package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.woodcutting.Tree;
import activities.skills.woodcutting.WoodcuttingActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;
import util.Location;
import util.ResourceMode;

import javax.swing.*;
import java.awt.*;

public class WCActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Tree> treeSelector;
    private JComboBox<Location> locationSelector;
    private JComboBox<ResourceMode> resourceModeSelector;

    public WCActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        final JLabel label1 = new StyledJLabel("Tree:");
        mainPanel.add(label1);

        treeSelector = new StyledJComboBox<>();
        mainPanel.add(treeSelector);

        final JLabel label2 = new StyledJLabel("Location:");
        mainPanel.add(label2);

        locationSelector = new StyledJComboBox<>();
        mainPanel.add(locationSelector);

        final JLabel label3 = new StyledJLabel("Collection Mode:");
        mainPanel.add(label3);

        resourceModeSelector = new StyledJComboBox<>();
        mainPanel.add(resourceModeSelector);

        treeSelector.setModel(new DefaultComboBoxModel<>(Tree.values()));
        locationSelector.setModel(new DefaultComboBoxModel<>(Tree.values()[0].locations));
        resourceModeSelector.setModel(new DefaultComboBoxModel<>(ResourceMode.values()));
        treeSelector.addActionListener(e -> {
            Tree selectedTree = (Tree) treeSelector.getSelectedItem();
            locationSelector.setModel(new DefaultComboBoxModel<>(selectedTree.locations));
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new WoodcuttingActivity((Tree) treeSelector.getSelectedItem(), (Location) locationSelector.getSelectedItem(), (ResourceMode) resourceModeSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tree", ((Tree) treeSelector.getSelectedItem()).name());
        jsonObject.put("resource_mode", ((ResourceMode) resourceModeSelector.getSelectedItem()).name());
        jsonObject.put("location_index", locationSelector.getSelectedIndex());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        treeSelector.setSelectedItem(Tree.valueOf((String) jsonObject.get("tree")));
        resourceModeSelector.setSelectedItem(ResourceMode.valueOf((String) jsonObject.get("resource_mode")));
        locationSelector.setSelectedIndex(new Long((long) jsonObject.get("location_index")).intValue());
    }
}
