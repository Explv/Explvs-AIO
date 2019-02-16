package gui.utils;

import gui.IconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.function.Supplier;

public class CopyToClipboardButton {
    public static JButton create(final Supplier<String> textSupplier) {
        return IconButton.createButton("Copy to clipboard", "clipboardIcon.png", "clipboardIconHover.png", e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(textSupplier.get()), null);
        });
    }
}
