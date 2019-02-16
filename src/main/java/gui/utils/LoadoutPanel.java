package gui.utils;

import activities.grand_exchange.item_guide.ItemGuide;
import gui.fields.ItemField;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.file_managers.ImageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoadoutPanel extends JPanel {

    private static final String IMAGE_DIR = "/loadout/";
    private static final BufferedImage SLOT_BACKGROUND_IMAGE = ImageManager.loadImage(IMAGE_DIR + "slot_background.png");

    private Map<EquipmentSlot, String> equipmentMap = new HashMap<>();

    public LoadoutPanel() {
        setLayout(new GridBagLayout());
        setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc;

        final JButton helmetButton = createButton(EquipmentSlot.HAT, "helmet.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(helmetButton, gbc);

        final JPanel spacer1 = new StyledJPanel();
        spacer1.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(spacer1, gbc);

        final JPanel spacer2 = new StyledJPanel();
        spacer2.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(spacer2, gbc);

        final JButton amuletButton = createButton(EquipmentSlot.AMULET, "amulet.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(amuletButton, gbc);

        final JButton capeButton = createButton(EquipmentSlot.CAPE, "cape.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(capeButton, gbc);

        final JButton arrowsButton = createButton(EquipmentSlot.ARROWS, "arrows.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(arrowsButton, gbc);

        final JButton chestButton = createButton(EquipmentSlot.CHEST, "chest.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(chestButton, gbc);

        final JButton shieldButton = createButton(EquipmentSlot.SHIELD, "shield.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(shieldButton, gbc);

        final JButton weaponButton = createButton(EquipmentSlot.WEAPON, "weapon.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(weaponButton, gbc);

        final JButton legsButton = createButton(EquipmentSlot.LEGS, "legs.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(legsButton, gbc);

        final JButton bootsButton = createButton(EquipmentSlot.FEET, "boots.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bootsButton, gbc);

        final JButton ringButton = createButton(EquipmentSlot.RING, "ring.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(ringButton, gbc);

        final JButton glovesButton = createButton(EquipmentSlot.HANDS, "gloves.png");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(glovesButton, gbc);

        final JPanel spacer3 = new StyledJPanel();
        gbc = new GridBagConstraints();
        spacer3.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(spacer3, gbc);

        final JPanel spacer4 = new StyledJPanel();
        spacer4.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(spacer4, gbc);

        final JPanel spacer5 = new StyledJPanel();
        spacer5.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(spacer5, gbc);

        final JPanel spacer6 = new StyledJPanel();
        spacer6.setBackground(ColourScheme.DIALOG_BACKGROUND_GREY);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(spacer6, gbc);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setContentPane(new LoadoutPanel());
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    public Map<EquipmentSlot, String> getSelectedEquipment() {
        return equipmentMap;
    }

    private JButton createButton(final EquipmentSlot equipmentSlot, final String imageName) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);

        button.setIcon(new ImageIcon(ImageManager.loadImage(IMAGE_DIR + imageName)));
        button.addActionListener(new EquipmentButtonActionListener(equipmentSlot));

        return button;
    }

    class EquipmentButtonActionListener implements ActionListener {

        private final EquipmentSlot equipmentSlot;

        public EquipmentButtonActionListener(final EquipmentSlot equipmentSlot) {
            this.equipmentSlot = equipmentSlot;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            Optional<String> selectedItemNameOpt = setItemDialog(equipmentSlot);

            if (!selectedItemNameOpt.isPresent()) {
                return;
            }

            String selectedItemName = selectedItemNameOpt.get();

            JButton sourceButton = (JButton) event.getSource();

            if (selectedItemName.isEmpty()) {
                equipmentMap.remove(equipmentSlot);
                sourceButton.setIcon(new ImageIcon(SLOT_BACKGROUND_IMAGE));
                return;
            }

            equipmentMap.put(equipmentSlot, selectedItemName);

            sourceButton.setToolTipText(selectedItemName);

            Optional<URL> iconURL = getIcon(ItemGuide.getAllGEItems().get(selectedItemName));

            if (iconURL.isPresent()) {
                try {
                    BufferedImage iconImage = ImageIO.read(iconURL.get());
                    BufferedImage combinedImage = combineImages(SLOT_BACKGROUND_IMAGE, iconImage);
                    sourceButton.setIcon(new ImageIcon(combinedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Optional<String> setItemDialog(final EquipmentSlot equipmentSlot) {
            ItemField itemField = new ItemField();

            final JButton okButton = new JButton("Ok");

            okButton.addActionListener(e -> {
                JOptionPane optionPane = (JOptionPane) okButton.getParent().getParent();
                optionPane.setValue(okButton);

                /* close the dialog */
                Window w = SwingUtilities.getWindowAncestor(okButton);
                if (w != null) {
                    w.setVisible(false);
                }
            });

            itemField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(final KeyEvent e) {
                    super.keyReleased(e);
                    okButton.setEnabled(
                            itemField.getText().trim().isEmpty() || itemField.validateItemNameField()
                    );
                }
            });

            int selectedOption = JOptionPane.showOptionDialog(
                    null,
                    itemField,
                    "Choose Item: " + equipmentSlot,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{okButton},
                    okButton
            );

            if (selectedOption == JOptionPane.YES_OPTION) {
                return Optional.of(itemField.getText().trim());
            }
            return Optional.empty();
        }

        private BufferedImage combineImages(final BufferedImage backgroundImage, final BufferedImage foregroundImage) {
            final BufferedImage combinedImage = new BufferedImage(
                    backgroundImage.getWidth(),
                    backgroundImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = combinedImage.createGraphics();
            g.drawImage(backgroundImage, 0, 0, null);

            int widthDiff = SLOT_BACKGROUND_IMAGE.getWidth() - foregroundImage.getWidth();
            int heightDiff = SLOT_BACKGROUND_IMAGE.getHeight() - foregroundImage.getHeight();

            g.drawImage(foregroundImage, widthDiff / 2, heightDiff / 2, null);
            g.dispose();

            return combinedImage;
        }

        private Optional<URL> getIcon(final int itemID) {
            try {
                URL url = new URL("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemID);
                URLConnection con = url.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

                try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = (JSONObject) jsonParser.parse(bufferedReader);

                    JSONObject itemJSON = (JSONObject) json.get("item");
                    String iconURL = (String) itemJSON.get("icon");

                    return Optional.of(new URL(iconURL));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to get icon from RuneScape api");
            }
            return Optional.empty();
        }
    }
}
