package org.aio.gui.fields;

import org.aio.gui.document_filters.DoubleDocumentFilter;
import org.aio.gui.styled_components.StyledJTextField;
import org.aio.gui.utils.ColourScheme;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DoubleField extends StyledJTextField {
    public DoubleField() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new DoubleDocumentFilter());

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

    private boolean validateField() {
        if (getText().trim().isEmpty()) {
            setForeground(Color.RED);
            return false;
        }

        try {
            Double.parseDouble(getText().trim());
        } catch (NumberFormatException e) {
            setForeground(Color.RED);
            return false;
        }

        setForeground(ColourScheme.WHITE);
        return true;
    }
}
