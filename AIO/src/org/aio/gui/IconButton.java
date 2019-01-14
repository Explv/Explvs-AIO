package org.aio.gui;

import org.aio.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class IconButton {
    public static JButton createButton(final String toolTip, final String icon, final String rolloverIcon, ActionListener callback) {
        JButton button = new JButton();
        button.setBackground(Color.BLACK);
        button.setToolTipText(toolTip);

        BufferedImage iconImage = ResourceManager.getImage(icon);
        if (iconImage != null) {
            button.setIcon(new ImageIcon(iconImage));
        }

        BufferedImage rolloverIconImage = ResourceManager.getImage(rolloverIcon);
        if (rolloverIconImage != null) {
            button.setRolloverIcon(new ImageIcon(rolloverIconImage));
        }

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);

        button.addActionListener(callback);

        return button;
    }
}
