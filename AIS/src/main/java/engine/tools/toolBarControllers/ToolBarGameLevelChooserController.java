package engine.tools.toolBarControllers;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.gameLevelsManager.AddNewGameLevelWindow;
import engine.tools.gameLevelsManager.GameLevelChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *  Toolbars level chooser controller.
 */
public class ToolBarGameLevelChooserController implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final GameLevelChooser levelChooser;
    private final AbstractButton[] abstractButtons;

    public ToolBarGameLevelChooserController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, GameLevelChooser levelChooser, AbstractButton[] abstractButtons) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.levelChooser = levelChooser;
        this.abstractButtons = abstractButtons;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (levelChooser.getItemCount() > 0 && levelChooser.getSelectedItem().equals("-- add new level --")) {
            new AddNewGameLevelWindow(guiComponents.getMainWindow(), modelControlComponents.getGameLevels(), levelChooser);
        }
        if (Objects.nonNull(levelChooser.getSelectedItem())) {
            for (AbstractButton button : abstractButtons) {
                button.setEnabled(!levelChooser.getSelectedItem().toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --"));
            }
            modelControlComponents.getGameLevels().setLevelIsLoaded(!levelChooser.getSelectedItem().toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --"));
            if (modelControlComponents.getGameLevels().levelIsLoaded()) {
                modelControlComponents.getGameLevels().loadLevel(levelChooser.getSelectedItem().toString());
                guiComponents.getGamePanel().startEditing();
            } else {
                guiComponents.getGamePanel().stopEditing();
            }
        }
    }
}
