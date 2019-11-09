package gui;

import util.file_managers.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class IconButton {
    public static JButton createButton(final String toolTip, final String icon, final String rolloverIcon, ActionListener callback) {
        JButton button = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                if (this.model.isRollover()) {
                    int drawX = (this.getWidth() / 2) - (this.getRolloverIcon().getIconWidth() / 2);
                    int drawY = (this.getHeight() / 2) - (this.getRolloverIcon().getIconHeight() / 2);
                    this.getRolloverIcon().paintIcon(this, g, drawX, drawY);
                } else {
                    int drawX = (this.getWidth() / 2) - (this.getIcon().getIconWidth() / 2);
                    int drawY = (this.getHeight() / 2) - (this.getIcon().getIconHeight() / 2);
                    this.getIcon().paintIcon(this, g, drawX, drawY);
                }
            }
        };
        button.setToolTipText(toolTip);

        BufferedImage iconImage = ImageManager.loadImage(icon);
        if (iconImage != null) {
            button.setIcon(new ImageIcon(iconImage));
        }

        BufferedImage rolloverIconImage = ImageManager.loadImage(rolloverIcon);
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
