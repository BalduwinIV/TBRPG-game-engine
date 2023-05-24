package engine.tools.toolBarControllers;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.gameLevelsManager.GameLevelChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *  Toolbars rename button controller.
 */
public class ToolBarRenameButtonController implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final GameLevelChooser levelChooser;

    public ToolBarRenameButtonController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, GameLevelChooser levelChooser) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.levelChooser = levelChooser;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Objects.requireNonNull(levelChooser.getSelectedItem()).toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --")) {
            Object optionPaneOption = JOptionPane.showInputDialog(guiComponents.getMainWindow(), "Enter levels new name:", "New levels name window", JOptionPane.QUESTION_MESSAGE, null, null, levelChooser.getSelectedItem().toString());
            if (Objects.nonNull(optionPaneOption)) {
                String newLevelsName = (String) optionPaneOption;
                if (modelControlComponents.getGameLevels().renameLevel(levelChooser.getSelectedItem().toString(), newLevelsName)) {
                    modelControlComponents.getGameLevels().saveJSONFile();
                    int selectedItemIndex = levelChooser.getSelectedIndex();
                    levelChooser.removeAllItems();
                    levelChooser.fillItems();
                    levelChooser.setSelectedIndex(selectedItemIndex);
                } else {
                    JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "New name is not valid.", "Rename error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
