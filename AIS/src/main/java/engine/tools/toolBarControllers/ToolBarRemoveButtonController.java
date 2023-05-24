package engine.tools.toolBarControllers;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.gameLevelsManager.GameLevelChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *  Toolbars remove button controller.
 */
public class ToolBarRemoveButtonController implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final GameLevelChooser levelChooser;

    public ToolBarRemoveButtonController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, GameLevelChooser levelChooser) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.levelChooser = levelChooser;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Objects.requireNonNull(levelChooser.getSelectedItem()).toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --")) {
            int optionPaneOption = JOptionPane.showConfirmDialog(guiComponents.getMainWindow(), "Are you sure that you want to remove \"" + levelChooser.getSelectedItem().toString() + "\" level?");
            if (optionPaneOption == JOptionPane.YES_OPTION) {
                if (modelControlComponents.getGameLevels().removeLevel(levelChooser.getSelectedItem().toString())) {
                    modelControlComponents.getGameLevels().saveJSONFile();
                    levelChooser.removeItemAt(levelChooser.getSelectedIndex());
                    levelChooser.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "An error occurred while removing \"" + levelChooser.getSelectedItem().toString() + "\" level");
                }
            }
        }
    }
}
