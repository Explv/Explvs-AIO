package org.aio.gui.fields;

import org.aio.activities.grand_exchange.item_guide.ItemGuide;
import org.aio.gui.utils.ColourScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ItemField extends AutoCompleteTextField {

    public ItemField() {
        setColumns(20);
        addPosibilities(ItemGuide.getAllGEItems().keySet());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                validateItemNameField();
            }
        });

        setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(final JComponent input) {
                return validateItemNameField();
            }
        });
    }

    public boolean validateItemNameField() {
        String itemName = getText().trim();

        if (itemName.isEmpty() || !ItemGuide.getAllGEItems().containsKey(itemName)) {
            setForeground(Color.RED);
            return false;
        }

        setForeground(ColourScheme.WHITE);
        return true;
    }
}
