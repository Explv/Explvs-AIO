package org.aio.gui.utils;

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

        yearField = new JTextField();
        yearField.setColumns(4);
        yearField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) yearField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        add(yearField);

        monthField = new JTextField();
        monthField.setColumns(2);
        monthField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) monthField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        add(monthField);

        dayField = new JTextField();
        dayField.setColumns(2);
        dayField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) dayField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        add(dayField);

        add(new JLabel("Time (24h): "));

        hourField = new JTextField();
        hourField.setColumns(2);
        hourField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) hourField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        add(hourField);

        minuteField = new JTextField();
        minuteField.setColumns(2);
        minuteField.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) minuteField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
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
