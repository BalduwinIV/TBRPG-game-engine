package engine.tools.characterManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Characters button controller.
 */
public class CharacterPanelButtonController implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final Character character;
    public CharacterPanelButtonController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, Character character) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.character = character;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        guiComponents.getParametersPanel().displayCharacter(character);
        if (modelControlComponents.getProgramState().equals(ProgramState.ENGINE)) {
            boolean characterHasBeenAdded = false;
            for (int y = 0; y < modelControlComponents.getGameLevels().getCurrentLevelSize()[1] && !characterHasBeenAdded; y++) {
                for (int x = 0; x < modelControlComponents.getGameLevels().getCurrentLevelSize()[0]; x++) {
                    if (modelControlComponents.getGameLevels().addCharacterToLevel(character, x, y)) {
                        characterHasBeenAdded = true;
                        modelControlComponents.getEngineLogger().info(this, "Character \"" + character.getName() + "\" has been added to [" + x + ", " + y + "].");
                        break;
                    }
                }
            }
            if (!characterHasBeenAdded) {
                modelControlComponents.getEngineLogger().warning(this, "Could not add character \"" + character.getName() + "\" to the game level.");
            }
            guiComponents.getGamePanel().updateSpritesOnCamera();
        }
    }
}
