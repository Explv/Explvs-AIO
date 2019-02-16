package gui.utils;

import gui.fields.DoubleField;
import gui.interfaces.JSONSerializable;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class DurationPanel extends StyledJPanel implements JSONSerializable {

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JComboBox<TimeType> timeTypeSelector;
    private DateTimePanel dateTimePanel;
    private JTextField durationField;

    public DurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        timeTypeSelector = new StyledJComboBox<>(TimeType.values());
        add(timeTypeSelector);

        JPanel durationPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        durationPanel.add(new StyledJLabel("Duration:"));
        durationField = new DoubleField();
        durationField.setColumns(6);
        durationPanel.add(durationField);
        add(durationPanel);

        dateTimePanel = new DateTimePanel();
        dateTimePanel.setVisible(false);
        add(dateTimePanel);

        timeTypeSelector.addActionListener(e -> {
            TimeType selectedTimeType = (TimeType) timeTypeSelector.getSelectedItem();
            if (selectedTimeType == TimeType.DATE_TIME) {
                durationPanel.setVisible(false);
                dateTimePanel.setVisible(true);
            } else {
                dateTimePanel.setVisible(false);
                durationPanel.setVisible(true);
            }
        });
    }

    public TimeType getSelectedTimeType() {
        return (TimeType) timeTypeSelector.getSelectedItem();
    }

    public long getDurationMS() {
        return (long) getSelectedTimeType().toTimeUnit(
                Double.parseDouble(durationField.getText()),
                TimeUnit.MILLISECONDS
        );
    }

    public LocalDateTime getSelectedDateTime() {
        return dateTimePanel.getDateTime();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        if (timeTypeSelector.getSelectedItem() == TimeType.MINUTES) {
            jsonObject.put("duration", durationField.getText());
        } else {
            jsonObject.put(
                    "datetime",
                    dateTimePanel.getDateTime().format(dtFormatter)
            );
        }

        return jsonObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        if (jsonObject.containsKey("duration")) {
            durationField.setText((String) jsonObject.get("duration"));
            timeTypeSelector.setSelectedItem(TimeType.MINUTES);
        }

        if (jsonObject.containsKey("datetime")) {
            String datetimeStr = (String) jsonObject.get("datetime");
            LocalDateTime datetime = LocalDateTime.parse(
                    datetimeStr,
                    dtFormatter
            );
            dateTimePanel.setDateTime(datetime);
            timeTypeSelector.setSelectedItem(TimeType.DATE_TIME);
        }
    }

    public enum TimeType {
        SECONDS("Seconds", TimeUnit.SECONDS),
        MINUTES("Minutes", TimeUnit.MINUTES),
        HOURS("Hours", TimeUnit.HOURS),
        DAYS("Days", TimeUnit.DAYS),
        DATE_TIME("Date / Time", null);

        private String name;
        private TimeUnit timeUnit;

        TimeType(final String name, final TimeUnit timeUnit) {
            this.name = name;
            this.timeUnit = timeUnit;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        @Override
        public String toString() {
            return name;
        }

        public double toTimeUnit(double amount, TimeUnit to) {
            if (getTimeUnit().ordinal() < to.ordinal()) {
                return amount / getTimeUnit().convert(1, to);
            } else {
                return amount * to.convert(1, getTimeUnit());
            }
        }
    }
}
