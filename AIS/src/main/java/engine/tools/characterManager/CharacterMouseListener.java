package engine.tools.characterManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 *  Characters button popup menu class.
 */
public class CharacterMouseListener implements MouseListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final JButton characterButton;
    private String characterName;

    public CharacterMouseListener(GUIComponents guiComponents, ModelControlComponents modelControlComponents, JButton characterButton, String characterName) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.characterButton = characterButton;
        this.characterName = characterName;
    }

    private void showCharacterPopupMenu(int xPos, int yPos) {
        JPopupMenu characterPopupMenu = new JPopupMenu("Manage character");
        JMenuItem renameOption = new JMenuItem("Rename");
        JMenuItem removeOption = new JMenuItem("Remove");
        characterPopupMenu.add(renameOption);
        characterPopupMenu.add(removeOption);

        renameOption.addActionListener(e -> {
            Object optionPaneOption = JOptionPane.showInputDialog(guiComponents.getMainWindow(), "Enter characters new name:", "New characters name window", JOptionPane.QUESTION_MESSAGE, null, null, characterName);
            if (Objects.nonNull(optionPaneOption)) {
                String newName = optionPaneOption.toString();
                if (modelControlComponents.getProgramState().equals(ProgramState.SAVE_DATA_EDITOR)) {
                    if (modelControlComponents.getSaveDataManager().renameCharacterFromCurrentSaveData(characterName, newName)) {
                        modelControlComponents.getSaveDataManager().saveJSONFile();
                        characterButton.setToolTipText(newName);
                        characterName = newName;
                        guiComponents.getParametersPanel().displayCharacter(modelControlComponents.getSaveDataManager().getCharacter(newName));
                    } else {
                        JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "New name is not valid.", "Rename error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (modelControlComponents.getProgramState().equals(ProgramState.ENGINE)) {
                    if (modelControlComponents.getKnownCharactersManager().renameCharacter(characterName, newName)) {
                        modelControlComponents.getKnownCharactersManager().saveJSONFile();
                        characterButton.setToolTipText(newName);
                        characterName = newName;
                        guiComponents.getParametersPanel().displayCharacter(modelControlComponents.getKnownCharactersManager().getCharacter(newName));
                    } else {
                        JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "New name is not valid.", "Rename error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        removeOption.addActionListener(e -> {
            if (modelControlComponents.getProgramState().equals(ProgramState.SAVE_DATA_EDITOR)) {
                if (modelControlComponents.getSaveDataManager().removeCharacterFromCurrentSaveData(characterName)) {
                    modelControlComponents.getSaveDataManager().saveJSONFile();
                    guiComponents.getCharactersPanel().removeButtonFromPanel(characterButton);
                }
            } else if (modelControlComponents.getProgramState().equals(ProgramState.ENGINE)) {
                if (modelControlComponents.getKnownCharactersManager().removeCharacter(characterName)) {
                    modelControlComponents.getKnownCharactersManager().saveJSONFile();
                    guiComponents.getCharactersPanel().removeButtonFromPanel(characterButton);
                }
            }
        });

        characterPopupMenu.show(guiComponents.getCharactersPanel(), xPos, yPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showCharacterPopupMenu(characterButton.getLocation().x + characterButton.getWidth(), characterButton.getLocation().y + characterButton.getHeight());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
