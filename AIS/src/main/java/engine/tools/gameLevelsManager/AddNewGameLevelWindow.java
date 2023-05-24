package engine.tools.gameLevelsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  New game level window. View + Controller.
 */
public class AddNewGameLevelWindow extends JDialog implements ActionListener {
    JFrame parent;
    GameLevels gameLevels;
    GameLevelChooser gameLevelChooser;
    JTextField nameField;
    JSpinner spritesWidthSpinner;
    JSpinner spritesHeightSpinner;
    JSpinner levelsWidthSpinner;
    JSpinner levelsHeightSpinner;
    public AddNewGameLevelWindow(JFrame parent, GameLevels gameLevels, GameLevelChooser gameLevelChooser) {
        super(parent, "Add new level", true);
        this.parent = parent;
        this.gameLevels = gameLevels;
        this.gameLevelChooser = gameLevelChooser;

        setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel("Add new level");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);
        add(confirmButton, BorderLayout.SOUTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 1, 5, 5));

        nameField = new JTextField("Levels name");
        nameField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        contentPanel.add(nameField);

        JPanel spritesSizePanel = new JPanel();
        spritesSizePanel.setLayout(new GridLayout(1, 2, 5, 0));
        contentPanel.add(spritesSizePanel);

        JPanel spritesWidthPanel = new JPanel();
        GroupLayout spritesWidthPanelLayout = new GroupLayout(spritesWidthPanel);

        JLabel spritesWidthLabel = new JLabel("Sprites width: ");
        spritesWidthLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        spritesWidthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        spritesWidthSpinner = new JSpinner(new SpinnerNumberModel(16, 1, 256, 1));
        spritesWidthPanelLayout.setHorizontalGroup(spritesWidthPanelLayout.createSequentialGroup().addComponent(spritesWidthLabel).addGap(5).addComponent(spritesWidthSpinner));
        spritesWidthPanelLayout.setVerticalGroup(spritesWidthPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(spritesWidthLabel).addComponent(spritesWidthSpinner));
        spritesWidthPanel.setLayout(spritesWidthPanelLayout);

        JPanel spritesHeightPanel = new JPanel();
        GroupLayout spritesHeightPanelLayout = new GroupLayout(spritesHeightPanel);

        JLabel spritesHeightLabel = new JLabel("Sprites height: ");
        spritesHeightLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        spritesHeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        spritesHeightSpinner = new JSpinner(new SpinnerNumberModel(16, 1, 256, 1));
        spritesHeightPanelLayout.setHorizontalGroup(spritesHeightPanelLayout.createSequentialGroup().addComponent(spritesHeightLabel).addGap(5).addComponent(spritesHeightSpinner));
        spritesHeightPanelLayout.setVerticalGroup(spritesHeightPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(spritesHeightLabel).addComponent(spritesHeightSpinner));
        spritesHeightPanel.setLayout(spritesHeightPanelLayout);

        spritesSizePanel.add(spritesWidthPanel);
        spritesSizePanel.add(spritesHeightPanel);

        JPanel levelsSizePanel = new JPanel();
        levelsSizePanel.setLayout(new GridLayout(1, 2, 5, 0));
        contentPanel.add(levelsSizePanel);

        JPanel levelsWidthPanel = new JPanel();
        GroupLayout levelsWidthPanelLayout = new GroupLayout(levelsWidthPanel);

        JLabel levelsWidthLabel = new JLabel("Levels width:  ");
        levelsWidthLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        levelsWidthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        levelsWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        levelsWidthPanelLayout.setHorizontalGroup(levelsWidthPanelLayout.createSequentialGroup().addComponent(levelsWidthLabel).addGap(5).addComponent(levelsWidthSpinner));
        levelsWidthPanelLayout.setVerticalGroup(levelsWidthPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(levelsWidthLabel).addComponent(levelsWidthSpinner));
        levelsWidthPanel.setLayout(levelsWidthPanelLayout);

        JPanel levelsHeightPanel = new JPanel();
        GroupLayout levelsHeightPanelLayout = new GroupLayout(levelsHeightPanel);

        JLabel levelsHeightLabel = new JLabel("Levels height:  ");
        levelsHeightLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        levelsHeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        levelsHeightSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        levelsHeightPanelLayout.setHorizontalGroup(levelsHeightPanelLayout.createSequentialGroup().addComponent(levelsHeightLabel).addGap(5).addComponent(levelsHeightSpinner));
        levelsHeightPanelLayout.setVerticalGroup(levelsHeightPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(levelsHeightLabel).addComponent(levelsHeightSpinner));
        levelsHeightPanel.setLayout(levelsHeightPanelLayout);

        levelsSizePanel.add(levelsWidthPanel);
        levelsSizePanel.add(levelsHeightPanel);

        add(contentPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!nameField.getText().equals("")) {
            if (gameLevels.addLevel(nameField.getText(), (int)spritesWidthSpinner.getValue(), (int)spritesHeightSpinner.getValue(), (int)levelsWidthSpinner.getValue(), (int)levelsHeightSpinner.getValue())) {
                gameLevels.saveJSONFile();
                gameLevelChooser.insertItemAt(nameField.getText(), gameLevelChooser.getItemCount()-1);
                gameLevelChooser.setSelectedItem(gameLevelChooser.getItemAt(gameLevelChooser.getItemCount()-2));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot create level with name \"" + nameField.getText() + "\".", "New level error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cannot create level with empty name.", "New level error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
