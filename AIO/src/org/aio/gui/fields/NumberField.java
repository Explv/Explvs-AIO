package org.aio.gui.fields;

import org.aio.gui.utils.NumberDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumberField extends JTextField {
    public NumberField() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new NumberDocumentFilter());

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
            setBorder(BorderFactory.createLineBorder(Color.RED));
            return false;
        }

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return true;
    }
}
