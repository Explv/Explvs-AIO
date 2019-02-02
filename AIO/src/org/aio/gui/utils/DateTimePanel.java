package org.aio.gui.utils;

import org.aio.gui.fields.DateField;
import org.aio.gui.fields.NumberField;
import org.aio.gui.styled_components.StyledJLabel;
import org.aio.gui.styled_components.StyledJPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimePanel extends StyledJPanel {

    private DateField dateField;
    private NumberField hourField;
    private NumberField minuteField;

    public DateTimePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(new StyledJLabel("Date (year-month-day):"));

        dateField = new DateField();
        dateField.setDate(LocalDate.now());
        add(dateField);

        add(new StyledJLabel("Time (24h): "));

        hourField = new NumberField();
        hourField.setMaxValue(23);
        hourField.setColumns(2);
        hourField.setHorizontalAlignment(JTextField.CENTER);
        add(hourField);

        minuteField = new NumberField();
        minuteField.setMaxValue(59);
        minuteField.setColumns(2);
        minuteField.setHorizontalAlignment(JTextField.CENTER);
        add(minuteField);

        setDateTime(LocalDateTime.now());
    }

    public void setDateTime(final LocalDateTime dateTime) {
        dateField.setDate(dateTime.toLocalDate());
        hourField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("HH")));
        minuteField.setText("" + dateTime.format(DateTimeFormatter.ofPattern("mm")));
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(
                dateField.getDate(),
                LocalTime.of(Integer.parseInt(hourField.getText()), Integer.parseInt(minuteField.getText()))
        );
    }
}
