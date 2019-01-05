package org.aio.gui.dialogs;

import org.aio.gui.utils.CopyToClipboardButton;
import org.aio.script.VersionChecker;
import org.aio.util.ScriptProperties;

import javax.swing.*;
import java.awt.*;

public class NewVersionDialog {

    public static int showNewVersionDialog(final Component parentComponent) {
        JPanel contents = new JPanel();
        contents.add(Box.createVerticalGlue());

        contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));

        Dimension contentSize = new Dimension(450, 150);

        contents.setMinimumSize(contentSize);
        contents.setPreferredSize(contentSize);

        JLabel titleLabel = new JLabel("A new version is available");
        titleLabel.setFont(titleLabel.getFont().deriveFont(25.0f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contents.add(titleLabel);

        contents.add(Box.createVerticalStrut(20));

        JLabel sdnLabel = new JLabel("If you are an SDN user the script will be updated automatically over the next few days.");
        sdnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contents.add(sdnLabel);

        contents.add(Box.createVerticalStrut(10));

        JLabel downloadLabel = new JLabel("Alternatively, you can download the latest version now from GitHub:");
        downloadLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contents.add(downloadLabel);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JTextField linkField = new JTextField(VersionChecker.GITHUB_RELEASES_URL);
        linkField.setEditable(false);

        linkField.setBorder(null);
        linkField.setFont(linkField.getFont().deriveFont(14.0f));

        linkPanel.add(linkField);

        JButton copyToClipboardButton = CopyToClipboardButton.create(() -> VersionChecker.GITHUB_RELEASES_URL);

        linkPanel.add(copyToClipboardButton);

        linkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contents.add(linkPanel);

        contents.add(Box.createVerticalGlue());

        return JOptionPane.showOptionDialog(
                parentComponent,
                contents,
                "Explv's AIO",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{ "Ignore this update", "Ok" },
                null
        );
    }
}
