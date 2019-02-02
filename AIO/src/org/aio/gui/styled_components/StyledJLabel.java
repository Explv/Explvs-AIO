package org.aio.gui.styled_components;

import org.aio.gui.utils.ColourScheme;

import javax.swing.*;
import java.awt.*;

public class StyledJLabel extends JLabel {

    public StyledJLabel(final String text) {
        super(text);
        setForeground(ColourScheme.WHITE);
    }

    public StyledJLabel() {
        setForeground(ColourScheme.WHITE);
    }
}
