package org.aio.gui.styled_components;

import org.aio.gui.utils.ColourScheme;
import org.aio.util.file_managers.FontManager;

import javax.swing.*;
import java.awt.*;

public class StyledJCheckBox extends JCheckBox {

    public StyledJCheckBox(final String text) {
        super(text);
        setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
        setForeground(ColourScheme.WHITE);

        setFont(FontManager.ROBOTO_REGULAR);
    }

    public StyledJCheckBox() {
        this(null);
    }
}
