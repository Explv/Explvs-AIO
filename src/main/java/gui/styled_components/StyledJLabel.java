package gui.styled_components;

import gui.utils.ColourScheme;
import util.file_managers.FontManager;

import javax.swing.*;

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
