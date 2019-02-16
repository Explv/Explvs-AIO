package gui.styled_components;

import gui.utils.ColourScheme;
import util.file_managers.FontManager;

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
        setFont(FontManager.ROBOTO_REGULAR);
        setBackground(ColourScheme.PANEL_BACKGROUND_GREY.darker());
        setForeground(ColourScheme.WHITE);
        setCaretColor(ColourScheme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    }
}
