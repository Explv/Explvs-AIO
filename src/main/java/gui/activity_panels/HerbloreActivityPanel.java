package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.herblore.HerbloreType;
import activities.skills.herblore.herb_cleaning.Herb;
import activities.skills.herblore.herb_cleaning.HerbCleaningActivity;
import activities.skills.herblore.potion_making.Potion;
import activities.skills.herblore.potion_making.PotionMakingActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class HerbloreActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<HerbloreType> typeSelector;
    private JComboBox<Herb> herbSelector;
    private JComboBox<Potion> potionSelector;

    public HerbloreActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Type:"));
        typeSelector = new StyledJComboBox<>(HerbloreType.values());
        mainPanel.add(typeSelector);

        JPanel herbPanel = new StyledJPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(herbPanel);

        herbPanel.add(new StyledJLabel("Herb:"));
        herbSelector = new StyledJComboBox<>(Herb.values());
        herbPanel.add(herbSelector);

        JPanel potionPanel = new StyledJPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        potionPanel.setVisible(false);
        mainPanel.add(potionPanel);

        potionPanel.add(new StyledJLabel("Potion:"));
        potionSelector = new StyledJComboBox<>(Potion.values());
        potionPanel.add(potionSelector);

        typeSelector.addActionListener(e -> {
            HerbloreType herbloreType = (HerbloreType) typeSelector.getSelectedItem();
            if (herbloreType == HerbloreType.HERB_CLEANING) {
                potionPanel.setVisible(false);
                herbPanel.setVisible(true);
            } else {
                herbPanel.setVisible(false);
                potionPanel.setVisible(true);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        if ((typeSelector.getSelectedItem()) == HerbloreType.HERB_CLEANING) {
            return new HerbCleaningActivity((Herb) herbSelector.getSelectedItem());
        }
        return new PotionMakingActivity((Potion) potionSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", ((HerbloreType) typeSelector.getSelectedItem()).name());
        jsonObject.put("herb", ((Herb) herbSelector.getSelectedItem()).name());
        jsonObject.put("potion", ((Potion) potionSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        typeSelector.setSelectedItem(HerbloreType.valueOf((String) jsonObject.get("type")));
        herbSelector.setSelectedItem(Herb.valueOf((String) jsonObject.get("herb")));
        potionSelector.setSelectedItem((Potion.valueOf((String) jsonObject.get("potion"))));
    }
}
