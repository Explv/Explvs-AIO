package gui.styled_components;

import gui.utils.ColourScheme;
import util.file_managers.FontManager;

import javax.swing.*;

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
