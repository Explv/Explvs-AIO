package gui.fields;

import gui.document_filters.IntegerDocumentFilter;
import gui.styled_components.StyledJTextField;
import gui.utils.ColourScheme;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntegerField extends StyledJTextField {
    private int minValue = -1;
    private int maxValue = -1;

    public IntegerField() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                validateField();
            }
        });

        setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(final JComponent input) {
                return validateField();
            }
        });
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private boolean validateField() {
        if (getText().trim().isEmpty()) {
            setForeground(Color.RED);
            return false;
        }

        int value = Integer.parseInt(getText().trim());

        if (minValue != -1 && value < minValue) {
            setForeground(Color.RED);
            return false;
        }

        if (maxValue != -1 && value > maxValue) {
            setForeground(Color.RED);
            return false;
        }

        setForeground(ColourScheme.WHITE);
        return true;
    }
}
