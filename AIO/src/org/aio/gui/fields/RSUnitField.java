package org.aio.gui.fields;


import org.aio.util.RSUnits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RSUnitField  extends PlaceholderTextField {
    public RSUnitField() {
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

        setPlaceholder("100 / 1.2k / etc.");
    }

    public long getValue() {
        return RSUnits.formattedToValue(getText().trim());
    }

    private boolean validateField() {
        if (getText().trim().isEmpty()) {
            setBorder(BorderFactory.createLineBorder(Color.RED));
            return false;
        }

        String text = getText().trim();

        if (!RSUnits.UNIT_PATTERN.matcher(text).matches()) {
            setBorder(BorderFactory.createLineBorder(Color.RED));
            return false;
        }

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return true;
    }
}
