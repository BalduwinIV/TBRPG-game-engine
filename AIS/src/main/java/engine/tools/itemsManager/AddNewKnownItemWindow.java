package engine.tools.itemsManager;

import engine.tools.GUIComponents;
import engine.tools.itemsManager.HealManager.ChangeHealCharacteristics;
import engine.tools.itemsManager.HealManager.Heal;
import engine.tools.itemsManager.WeaponManager.ChangeWeaponCharacteristics;
import engine.tools.itemsManager.WeaponManager.Weapon;
import engine.utils.ImageStorage;
import engine.tools.ModelControlComponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddNewKnownItemWindow extends JDialog implements ActionListener {
    private final JTextField nameField;
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final AtomicBoolean imageHasBeenChosen;
    private File chosenImage;
    private final JRadioButton weaponType;
    private final JRadioButton healType;

    public AddNewKnownItemWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super(guiComponents.getMainWindow(), "Add a new known item", true);
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;

        JLabel label = new JLabel("Add a new known item");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 5, 5));

        JPanel fieldsPanel = new JPanel();
        JPanel imagePanel = new JPanel();
        contentPanel.add(fieldsPanel);
        contentPanel.add(imagePanel);

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        imagePanel.setLayout(new BorderLayout());

        JLabel imagePreview = new JLabel();
        imagePanel.add(imagePreview, BorderLayout.CENTER);

        nameField = new JTextField("Items name");
        nameField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JFileChooser imageFileChooser = new JFileChooser(System.getProperty("user.dir"));
        imageFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif"));
        imageHasBeenChosen = new AtomicBoolean(false);

        JPanel itemTypePanel = new JPanel();
        itemTypePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        weaponType = new JRadioButton("weapon");
        healType = new JRadioButton("heal");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(weaponType);
        typeGroup.add(healType);
        itemTypePanel.add(weaponType);
        itemTypePanel.add(healType);

        JButton imageFileChooserButton = new JButton("Choose items image");
        imageFileChooserButton.addActionListener(e -> {
            int option = imageFileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                chosenImage = imageFileChooser.getSelectedFile();
                if (chosenImage.isFile()) {
                    try {
                        if (ImageIO.read(chosenImage) == null) {
                            imageHasBeenChosen.set(false);
                            imagePreview.setIcon(null);
                            imagePreview.setText("Not an image");
                        } else {
                            imageHasBeenChosen.set(true);
                            nameField.setText(chosenImage.getName());
                            ImageIcon tileImage = new ImageIcon(chosenImage.getPath());
                            Image scaledImage;
                            if (tileImage.getIconHeight() * (imagePreview.getWidth() / tileImage.getIconWidth()) > imagePreview.getHeight()) {
                                scaledImage = tileImage.getImage().getScaledInstance((int)(tileImage.getIconWidth() * (imagePreview.getHeight() / (float)tileImage.getIconHeight())), imagePreview.getHeight(), Image.SCALE_SMOOTH);
                            } else {
                                scaledImage = tileImage.getImage().getScaledInstance(imagePreview.getWidth(), (int)(tileImage.getIconHeight() * (imagePreview.getWidth() / (float)tileImage.getIconWidth())), Image.SCALE_SMOOTH);
                            }
                            imagePreview.setIcon(new ImageIcon(scaledImage));
                            imagePreview.setText(null);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });
        imageFileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        fieldsPanel.add(nameField);
        fieldsPanel.add(itemTypePanel);
        fieldsPanel.add(imageFileChooserButton);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        setLayout(new BorderLayout(5, 5));
        add(label, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(350, 200));
        pack();
        setLocationRelativeTo(guiComponents.getMainWindow());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!nameField.getText().equals("") && imageHasBeenChosen.get()) {
            if (!modelControlComponents.getItemManager().itemDoesExist(nameField.getText())) {
                if (weaponType.isSelected()) {
                    Weapon weapon = new Weapon(nameField.getText(), new ImageStorage(chosenImage.getPath()));
                    modelControlComponents.getItemManager().addItem(weapon);
                    guiComponents.getParametersPanel().getInventoryPanel().addItemToKnownItems(weapon);
                    dispose();
                    new ChangeWeaponCharacteristics(guiComponents, modelControlComponents, weapon);
                } else if (healType.isSelected()) {
                    Heal heal = new Heal(nameField.getText(), new ImageStorage(chosenImage.getPath()));
                    modelControlComponents.getItemManager().addItem(heal);
                    guiComponents.getParametersPanel().getInventoryPanel().addItemToKnownItems(heal);
                    dispose();
                    new ChangeHealCharacteristics(guiComponents, modelControlComponents, heal);
                } else {
                    JOptionPane.showMessageDialog(this, "Item type has not been chosen.", "Item type warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Item with name \"" + nameField.getText() + "\" already exists.", "Item already exists", JOptionPane.ERROR_MESSAGE);
            }
        } else if (nameField.getText().equals("") && !imageHasBeenChosen.get()) {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "Not enough information for adding a new item.", "No data error", JOptionPane.ERROR_MESSAGE);
        } else if (nameField.getText().equals("")) {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "No name has been given.", "No name error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "No image has been chosen", "No image error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
