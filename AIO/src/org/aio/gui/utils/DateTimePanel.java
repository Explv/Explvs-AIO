package org.aio.gui.utils;

import org.aio.gui.fields.NumberField;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimePanel extends JPanel {

    private JTextField yearField;
    private JTextField monthField;
    private JTextField dayField;
    private JTextField hourField;
    private JTextField minuteField;

    public DateTimePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(new JLabel("Date (year/month/day):"));

        yearField = new NumberField();
        yearField.setColumns(4);
        yearField.setHorizontalAlignment(JTextField.CENTER);
        add(yearField);

        monthField = new NumberField();
        monthField.setColumns(2);
        monthField.setHorizontalAlignment(JTextField.CENTER);
        add(monthField);

        dayField = new NumberField();
        dayField.setColumns(2);
        dayField.setHorizontalAlignment(JTextField.CENTER);
        add(dayField);

        add(new JLabel("Time (24h): "));

        hourField = new NumberField();
        hourField.setColumns(2);
        hourField.setHorizontalAlignment(JTextField.CENTER);
        add(hourField);

        minuteField = new NumberField();
        minuteField.setColumns(2);
        minuteField.setHorizontalAlignment(JTextField.CENTER);
        add(minuteField);

        setDateTime(LocalDateTime.now());
    }

    public void setDateTime(final LocalDateTime dateTime) {
        yearField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("YYYY")));
        monthField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("MM")));
        dayField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("dd")));
        hourField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("HH")));
        minuteField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("mm")));
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(
                Integer.parseInt(yearField.getText()),
                Integer.parseInt(monthField.getText()),
                Integer.parseInt(dayField.getText()),
                Integer.parseInt(hourField.getText()),
                Integer.parseInt(minuteField.getText())
        );
    }
}
