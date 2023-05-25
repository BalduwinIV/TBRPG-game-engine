package engine.tools.tilemap;

import engine.tools.GUIComponents;
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

/**
 *  A new tile adding window.
 */
public class AddNewTileWindow extends JDialog implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final JTextField nameField;
    private File chosenImage;
    private final AtomicBoolean imageHasBeenChosen;
    public AddNewTileWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super(guiComponents.getMainWindow(), "Add new tile", true);
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;

        JLabel label = new JLabel("Add a new tile");
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

        nameField = new JTextField("Tiles name");
        nameField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JFileChooser imageFileChooser = new JFileChooser(System.getProperty("user.dir"));
        imageFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif"));
        imageHasBeenChosen = new AtomicBoolean(false);
        JButton imageFileChooserButton = new JButton("Choose tiles image");
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

        fieldsPanel.add(nameField);
        fieldsPanel.add(imageFileChooserButton);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        setLayout(new BorderLayout(5, 5));
        add(label, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(300, 200));
        pack();
        setLocationRelativeTo(guiComponents.getMainWindow());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!nameField.getText().equals("") && imageHasBeenChosen.get()) {
            if (modelControlComponents.getTileMap().addTile(nameField.getText(), new ImageStorage(chosenImage.getPath()))) {
                modelControlComponents.getTileMap().saveJSONFile();

                guiComponents.getObjectsPanel().addTileToPanel(modelControlComponents.getTileMap().getTileByName(nameField.getText()));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tile with name \"" + nameField.getText() + "\" already exists.", "Tile already exists", JOptionPane.ERROR_MESSAGE);
            }
        } else if (nameField.getText().equals("") && !imageHasBeenChosen.get()) {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "Not enough information for adding a new tile.", "No data error", JOptionPane.ERROR_MESSAGE);
        } else if (nameField.getText().equals("")) {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "No name has been given.", "No name error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "No image has been chosen", "No image error", JOptionPane.ERROR_MESSAGE);
        }
    }
}