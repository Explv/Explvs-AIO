package gui.activity_panels;

import activities.activity.Activity;
import gui.interfaces.JSONSerializable;

import javax.swing.*;

public interface ActivityPanel extends JSONSerializable {
    Activity toActivity();

    JPanel getPanel();
}
