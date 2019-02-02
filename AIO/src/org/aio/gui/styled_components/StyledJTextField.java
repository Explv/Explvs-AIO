package org.aio.gui.styled_components;

import org.aio.gui.utils.ColourScheme;

import javax.swing.*;

public class StyledJTextField extends JTextField {

    public StyledJTextField(final String text) {
        super(text);
        setStyle();
    }

    public StyledJTextField() {
        setStyle();
    }

    protected void setStyle() {
        setBackground(ColourScheme.PANEL_BACKGROUND_GREY.darker());
        setForeground(ColourScheme.WHITE);
        setCaretColor(ColourScheme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    }
}
