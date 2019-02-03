package org.aio.gui.styled_components;

import org.aio.gui.utils.ColourScheme;
import org.aio.util.file_managers.FontManager;

import javax.swing.*;
import java.awt.*;

public class StyledJLabel extends JLabel {

    public StyledJLabel(final String text) {
        super(text);
        setForeground(ColourScheme.WHITE);
        setFont(FontManager.ROBOTO_REGULAR);
    }

    public StyledJLabel() {
        setForeground(ColourScheme.WHITE);
    }
}
