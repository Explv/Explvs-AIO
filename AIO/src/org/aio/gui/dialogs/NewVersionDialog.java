package org.aio.gui.dialogs;

import org.aio.gui.utils.CopyToClipboardButton;
import org.aio.script.VersionChecker;

import javax.swing.*;
import java.awt.*;

public class NewVersionDialog {

    public static void showNewVersionDialog(final Component parentComponent) {
        JPanel contents = new JPanel(new BorderLayout());

        Dimension contentSize = new Dimension(400, 100);

        contents.setMinimumSize(contentSize);
        contents.setPreferredSize(contentSize);

        JLabel titleLabel = new JLabel("A new version is available");
        titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JTextField linkField = new JTextField(VersionChecker.GITHUB_RELEASES_URL);
        linkField.setEditable(false);

        linkField.setBorder(null);
        linkField.setFont(linkField.getFont().deriveFont(14.0f));

        linkPanel.add(linkField);

        JButton copyToClipboardButton = CopyToClipboardButton.create(() -> VersionChecker.GITHUB_RELEASES_URL);

        linkPanel.add(copyToClipboardButton);

        contents.add(titleLabel, BorderLayout.NORTH);
        contents.add(linkPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parentComponent, contents, "Explv's AIO", JOptionPane.PLAIN_MESSAGE, null);
    }
}
