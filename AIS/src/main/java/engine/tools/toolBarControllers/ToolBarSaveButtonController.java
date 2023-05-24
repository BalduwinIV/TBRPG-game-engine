package engine.tools.toolBarControllers;

import engine.tools.ModelControlComponents;
import engine.tools.gameLevelsManager.GameLevelChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *  Toolbars save button controller.
 */
public class ToolBarSaveButtonController implements ActionListener {
    private final ModelControlComponents modelControlComponents;
    private final GameLevelChooser levelChooser;

    public ToolBarSaveButtonController(ModelControlComponents modelControlComponents, GameLevelChooser levelChooser) {
        this.modelControlComponents = modelControlComponents;
        this.levelChooser = levelChooser;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Objects.requireNonNull(levelChooser.getSelectedItem()).toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --")) {
            modelControlComponents.getGameLevels().saveJSONFile();
        }
    }
}
