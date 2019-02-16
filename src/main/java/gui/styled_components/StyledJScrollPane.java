package gui.styled_components;

import gui.utils.ColourScheme;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class StyledJScrollPane extends JScrollPane {

    public StyledJScrollPane() {
        getVerticalScrollBar().setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
        getVerticalScrollBar().setForeground(ColourScheme.WHITE);
        getVerticalScrollBar().setUI(new CustomScrollBarUI());

        getHorizontalScrollBar().setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
        getHorizontalScrollBar().setForeground(ColourScheme.WHITE);
        getHorizontalScrollBar().setUI(new CustomScrollBarUI());
    }

    static class CustomScrollBarUI extends BasicScrollBarUI {
        public static ComponentUI createUI(JComponent c) {
            return new CustomScrollBarUI();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = super.createDecreaseButton(orientation);
            styleButton(button);
            return button;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = super.createIncreaseButton(orientation);
            styleButton(button);
            return button;
        }

        private void styleButton(final JButton jButton) {
            jButton.setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
            jButton.setOpaque(false);
            jButton.setContentAreaFilled(false);
            jButton.setBorderPainted(false);
            jButton.setBorder(BorderFactory.createEmptyBorder());
            jButton.setFocusPainted(false);
        }

        @Override
        protected void configureScrollBarColors() {
            thumbHighlightColor = ColourScheme.PANEL_BACKGROUND_GREY;
            thumbLightShadowColor = ColourScheme.PANEL_BACKGROUND_GREY;
            thumbDarkShadowColor = ColourScheme.PANEL_BACKGROUND_GREY;
            thumbColor = ColourScheme.PANEL_BACKGROUND_GREY.darker();

            trackColor = ColourScheme.PANEL_BACKGROUND_GREY;
            trackHighlightColor = ColourScheme.PANEL_BACKGROUND_GREY;
        }
    }
}
