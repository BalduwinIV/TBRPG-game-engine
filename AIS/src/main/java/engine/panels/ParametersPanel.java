package engine.panels;

import engine.tools.GUIComponents;
import engine.tools.ProgramState;
import engine.utils.Logger;
import engine.tools.ModelControlComponents;
import engine.tools.characterManager.Character;

import javax.swing.*;
import java.awt.*;

/**
 * Parameters panel that shows a specific objects parameters.
 */
public class ParametersPanel extends JPanel {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final Logger logger;
    private InventoryPanel inventoryPanel;
    private final JLabel titleLabel;
    private final JPanel characterDisplayPanel;
    private JPanel contentPanel;

    public ParametersPanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        this.guiComponents = guiComponents;
        guiComponents.setParametersPanel(this);
        this.modelControlComponents = modelControlComponents;
        this.logger = modelControlComponents.getEngineLogger();

        setLayout(new BorderLayout(5, 5));

        titleLabel = new JLabel("<Characters name>");
        titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        characterDisplayPanel = new JPanel();
        characterDisplayPanel.setLayout(new BorderLayout());

        add(titleLabel, BorderLayout.NORTH);
        add(characterDisplayPanel, BorderLayout.CENTER);
    }

    /**
     *  Display characters info on parameters panel.
     * @param character Character with information.
     */
    public void displayCharacter(Character character) {
        titleLabel.setText(character.getName());

        characterDisplayPanel.removeAll();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane contentScrollPanel = new JScrollPane(contentPanel);
        characterDisplayPanel.add(contentScrollPanel, BorderLayout.CENTER);
        contentScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        contentScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JLabel characterSpriteViewer = new JLabel(new ImageIcon(character.getSprite().getImage().getScaledInstance(characterDisplayPanel.getWidth(), character.getSprite().getImage().getHeight() * (characterDisplayPanel.getWidth() / character.getSprite().getImage().getWidth()), Image.SCALE_SMOOTH)));
        characterSpriteViewer.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(characterSpriteViewer);

        JPanel characterTypePanel = new JPanel();
        characterTypePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JRadioButton allyType = new JRadioButton("ally");
        JRadioButton enemyType = new JRadioButton("enemy");
        if (character.getCharacterType().equals("ally")) {
            allyType.setSelected(true);
        } else if (character.getCharacterType().equals("enemy")) {
            enemyType.setSelected(true);
        }
        ButtonGroup characterTypeGroup = new ButtonGroup();
        characterTypeGroup.add(allyType);
        characterTypeGroup.add(enemyType);
        characterTypePanel.add(allyType);
        characterTypePanel.add(enemyType);
        if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            allyType.setEnabled(false);
            enemyType.setEnabled(false);
        }
        contentPanel.add(characterTypePanel);

        if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            addParameterField("HP: ", character.getHP());
        }

        JSpinner baseHPSpinner = addParameterField("baseHP: ", character.getBaseHP());
        JSpinner baseDMGSpinner = addParameterField("baseDMG: ", character.getBaseDMG());
        JSpinner hitRateSpinner = addParameterField("hitRate: ", character.getHitRate());
        JSpinner criticalHitRateSpinner = addParameterField("criticalHitRate: ", character.getCriticalHitRate());
        JSpinner avoidRateSpinner = addParameterField("avoidRate: ", character.getAvoidRate());

        JSpinner levelSpinner = addParameterField("level: ", character.getLevel());
        JSpinner expSpinner = addParameterField("exp: ", character.getExp());
        JSpinner strengthSpinner = addParameterField("strength: ", character.getStrength());
        JSpinner magicSpinner = addParameterField("magic: ", character.getMagic());
        JSpinner skillSpinner = addParameterField("skill: ", character.getSkill());
        JSpinner speedSpinner = addParameterField("speed: ", character.getSpeed());
        JSpinner luckSpinner = addParameterField("luck: ", character.getLuck());
        JSpinner defenceSpinner = addParameterField("defence: ", character.getDefence());
        JSpinner resistanceSpinner = addParameterField("resistance: ", character.getResistance());

        JSpinner movementSpinner = addParameterField("movement: ", character.getMovement());

        inventoryPanel = new InventoryPanel(modelControlComponents, guiComponents, character);
        characterDisplayPanel.add(inventoryPanel, BorderLayout.SOUTH);

        if (!modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            JButton saveChangesButton = new JButton("Save changes");
            saveChangesButton.addActionListener(e -> {
                if (!allyType.isSelected() && !enemyType.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Character type has not been chosen.", "Character type warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (allyType.isSelected()) {
                    character.setCharacterType("ally");
                } else if (enemyType.isSelected()) {
                    character.setCharacterType("enemy");
                }
                guiComponents.getCharactersPanel().updatePanel();
                character.setBaseStats(
                        (int) levelSpinner.getValue(),
                        (int) expSpinner.getValue(),
                        (int) baseHPSpinner.getValue(),
                        (int) baseDMGSpinner.getValue(),
                        (int) hitRateSpinner.getValue(),
                        (int) criticalHitRateSpinner.getValue(),
                        (int) avoidRateSpinner.getValue(),
                        (int) movementSpinner.getValue()
                );
                character.setLevelDependentStats(
                        (int) strengthSpinner.getValue(),
                        (int) magicSpinner.getValue(),
                        (int) skillSpinner.getValue(),
                        (int) speedSpinner.getValue(),
                        (int) luckSpinner.getValue(),
                        (int) defenceSpinner.getValue(),
                        (int) resistanceSpinner.getValue()
                );

                if (modelControlComponents.getProgramState().equals(ProgramState.SAVE_DATA_EDITOR)) {
                    if (modelControlComponents.getSaveDataManager().updateCharacterFromCurrentSaveData(character)) {
                        modelControlComponents.getSaveDataManager().saveJSONFile();
                        logger.info(this, "Character changes has been successfully saved.");
                    } else {
                        JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "Could not save changes.", "Save changes error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (modelControlComponents.getProgramState().equals(ProgramState.ENGINE)) {
                    if (modelControlComponents.getKnownCharactersManager().updateCharacter(character)) {
                        modelControlComponents.getKnownCharactersManager().saveJSONFile();
                        guiComponents.getGamePanel().updateSpritesOnCamera();
                        logger.info(this, "Character changes has been successfully saved.");
                    } else {
                        JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "Could not save changes.", "Save changes error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            saveChangesButton.setAlignmentX(CENTER_ALIGNMENT);
            contentPanel.add(saveChangesButton);
        } else {
            JPanel endMovePanel = new JPanel();
            endMovePanel.setLayout(new GridLayout(1, 1));
            JButton endMoveButton = new JButton("End Move");
            endMoveButton.addActionListener(e -> guiComponents.getGamePanel().skipMove());
            endMovePanel.add(endMoveButton);
            contentPanel.add(endMovePanel);
        }

        revalidate();
    }

    /**
     *  Parameters field builder.
     * @param labelText Labels text.
     * @param baseValue Default fields value.
     * @return  JSpinner object.
     */
    private JSpinner addParameterField(String labelText, int baseValue) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        JSpinner spinner;
        if (labelText.equals("level: ") || labelText.equals("baseHP: ")) {
            spinner = new JSpinner(new SpinnerNumberModel(baseValue, 1, 100, 1));
        } else {
            spinner = new JSpinner(new SpinnerNumberModel(baseValue, 0, 100, 1));
        }

        if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            spinner.setEnabled(false);
        }

        panel.add(label);
        panel.add(spinner);
        contentPanel.add(panel);
        return spinner;
    }

    /**
     *  Erase information about character from parameters panel.
     */
    public void clearDisplay() {
        titleLabel.setText("<Characters name>");
        characterDisplayPanel.removeAll();
        revalidate();
        repaint();
    }

    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }
}
