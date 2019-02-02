package org.aio.gui.styled_components;

import org.aio.gui.utils.ColourScheme;

import javax.swing.*;

public class StyledJCheckBox extends JCheckBox {

    public StyledJCheckBox(final String text) {
        super(text);
        setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
        setForeground(ColourScheme.WHITE);
    }

    public StyledJCheckBox() {
        this(null);
    }
}
