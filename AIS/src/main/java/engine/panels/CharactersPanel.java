package engine.panels;

import engine.GUIColors;
import engine.tools.ProgramState;
import engine.tools.characterManager.AddNewCharacterWindow;
import engine.tools.characterManager.Character;
import engine.tools.characterManager.CharacterMouseListener;
import engine.tools.characterManager.CharacterPanelButtonController;
import engine.utils.ButtonHandler;
import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.*;

/**
 *  Panel for viewing characters.
 */
public class CharactersPanel extends JPanel {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    public CharactersPanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        this.guiComponents = guiComponents;
        guiComponents.setCharactersPanel(this);
        this.modelControlComponents = modelControlComponents;
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBackground(new Color(GUIColors.color(GUIColors.Colors.BACKGROUND)));

        updatePanel();
    }

    /**
     *  Updates panel to more up-to-date state.
     */
    public void updatePanel() {
        removeAll();
        JButton newCharacterButton = new JButton();
        newCharacterButton.addMouseListener(new ButtonHandler(newCharacterButton, 80, 80,
                "src/main/resources/img/gui_icons/new_object_button.png",
                "src/main/resources/img/gui_icons/new_object_button_hover.png",
                "src/main/resources/img/gui_icons/new_object_button_pressed.png"));
        newCharacterButton.setToolTipText("Add a new character");
        newCharacterButton.addActionListener(e -> new AddNewCharacterWindow(guiComponents, modelControlComponents));
        add(newCharacterButton);

        if (modelControlComponents.getProgramState().equals(ProgramState.ENGINE)) {
            for (Character character : modelControlComponents.getKnownCharactersManager().getKnownCharacters()) {
                addCharacterToPanel(character);
            }
        } else {
            for (Character character : modelControlComponents.getSaveDataManager().getCharacters()) {
                addCharacterToPanel(character);
            }
        }
        revalidate();
    }

    /**
     *  Adds character button to characters panel.
     * @param character Character to add to panel.
     */
    public void addCharacterToPanel(Character character) {
        JButton characterButton = new JButton();
        characterButton.setIcon(new ImageIcon(character.getSprite().getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        characterButton.setRolloverEnabled(false);
        characterButton.setPreferredSize(new Dimension(80, 80));
        if (character.getCharacterType().equals("ally")) {
            characterButton.setBackground(new Color(GUIColors.color(GUIColors.Colors.CHARACTER_ALLY)));
        } else if (character.getCharacterType().equals("enemy")) {
            characterButton.setBackground(new Color(GUIColors.color(GUIColors.Colors.CHARACTER_ENEMY)));
        }
        characterButton.setToolTipText(character.getName());
        characterButton.addActionListener(new CharacterPanelButtonController(guiComponents, modelControlComponents, character));
        characterButton.addMouseListener(new CharacterMouseListener(guiComponents, modelControlComponents, characterButton, character.getName()));
        add(characterButton, getComponentCount()-1);
        revalidate();
    }

    /**
     *  Removes characters button from panel.
     * @param button Characters button to remove.
     */
    public void removeButtonFromPanel(JButton button) {
        remove(button);
        guiComponents.getParametersPanel().clearDisplay();
        revalidate();
    }
}
