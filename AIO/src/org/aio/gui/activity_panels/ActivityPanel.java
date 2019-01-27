package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.gui.interfaces.JSONSerializable;

import javax.swing.*;

public interface ActivityPanel extends JSONSerializable {
    Activity toActivity();
    JPanel getPanel();
}
