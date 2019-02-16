package gui.styled_components;

import gui.utils.ColourScheme;
import util.file_managers.FontManager;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class StyledJComboBox<E> extends JComboBox<E> {

    public StyledJComboBox(ComboBoxModel<E> model) {
        super(model);
        setStyle();
    }

    public StyledJComboBox(E[] items) {
        super(items);
        setStyle();
    }

    public StyledJComboBox() {
        setStyle();
    }

    private void setStyle() {
        setFont(FontManager.ROBOTO_REGULAR);
        setUI(new CustomComboBoxUI());
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        setBackground(ColourScheme.PANEL_BACKGROUND_GREY.darker());
        setForeground(ColourScheme.WHITE);
    }

    static class CustomComboBoxUI extends BasicComboBoxUI {
        public static ComponentUI createUI(JComponent c) {
            return new CustomComboBoxUI();
        }

        protected JButton createArrowButton() {
            JButton arrowButton = super.createArrowButton();
            arrowButton.setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
            arrowButton.setOpaque(false);
            arrowButton.setContentAreaFilled(false);
            arrowButton.setBorderPainted(false);
            arrowButton.setBorder(BorderFactory.createEmptyBorder());
            arrowButton.setFocusPainted(false);
            return arrowButton;
        }
    }
}
