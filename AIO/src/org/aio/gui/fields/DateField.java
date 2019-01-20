package org.aio.gui.fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateField extends JTextField {

    private static final DateTimeFormatter dtFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
            .parseStrict()
            .toFormatter();

    public DateField() {
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

    public void setDate(final LocalDate date) {
        setText(date.format(dtFormatter));
    }

    public LocalDate getDate() {
        return LocalDate.parse(getText().trim(), dtFormatter);
    }

    private boolean validateField() {
        if (getText().trim().isEmpty()) {
            setBorder(BorderFactory.createLineBorder(Color.RED));
            return false;
        }

        try {
            LocalDate.parse(
                    getText().trim(),
                    dtFormatter
            );
        } catch (DateTimeException e) {
            setBorder(BorderFactory.createLineBorder(Color.RED));
            return false;
        }

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return true;
    }
}
