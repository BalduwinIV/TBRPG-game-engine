package engine.tools.gameLevelsManager;

import javax.swing.*;

/**
 *  Game level chooser. View.
 */
public class GameLevelChooser extends JComboBox<String> {
    private final GameLevels gameLevels;
    public GameLevelChooser(GameLevels gameLevels) {
        super();
        this.gameLevels = gameLevels;
        fillItems();
    }

    /**
     *  Fills chooser with options.
     */
    public void fillItems() {
        addItem("-- choose the level --");
        for (String levelName : gameLevels.getLevelNamesList()) {
            addItem(levelName);
        }
        addItem("-- add new level --");
    }
}
