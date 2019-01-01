package org.aio.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class IconButton {
    public static JButton createButton(final String toolTip, final String icon, final String rolloverIcon, ActionListener callback) {
        JButton button = new JButton();
        button.setBackground(Color.BLACK);
        button.setToolTipText(toolTip);
        try {
            try (InputStream imageStream = IconButton.class.getResourceAsStream("/resources/" + icon)) {
                BufferedImage iconImage = ImageIO.read(imageStream);
                button.setIcon(new ImageIcon(iconImage));
            }

            try (InputStream rolloverIconStream = IconButton.class.getResourceAsStream("/resources/" + rolloverIcon)) {
                BufferedImage rolloverIconImage = ImageIO.read(rolloverIconStream);
                button.setRolloverIcon(new ImageIcon(rolloverIconImage));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
