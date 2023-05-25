package engine.tools;

import engine.tools.characterManager.KnownCharactersManager;
import engine.tools.itemsManager.ItemManager;
import engine.tools.gameLevelsManager.GameLevels;
import engine.tools.tilemap.TileMap;
import engine.utils.KeyHandler;
import engine.utils.Logger;

import java.util.Objects;

/**
 *  Program models singleton.
 */
public class ModelControlComponents {
    private ProgramState programState;
    private static Logger engineLogger;
    private static Logger gameLogger;
    private static Logger saveDataEditorLogger;
    private static TileMap tileMap;
    private static GameLevels gameLevels;
    private static SaveDataManager saveDataManager;
    private static KnownCharactersManager knownCharactersManager;
    private static ItemManager itemManager;
    private static KeyHandler keyHandler;
    private GameStats gameStats;

    public ModelControlComponents() {}

    public ProgramState getProgramState() {
        return programState;
    }

    public void setProgramState(ProgramState programState) {
        this.programState = programState;
    }

    public Logger getEngineLogger() {
        if (Objects.nonNull(engineLogger)) {
            return engineLogger;
        }
        engineLogger = new Logger("Engine.log");
        return engineLogger;
    }

    public Logger getGameLogger() {
        if (Objects.nonNull(gameLogger)) {
            return gameLogger;
        }
        gameLogger = new Logger("Game.log");
        return gameLogger;
    }

    public Logger getSaveDataEditorLogger() {
        if (Objects.nonNull(saveDataEditorLogger)) {
            return saveDataEditorLogger;
        }
        saveDataEditorLogger = new Logger("SaveDataEditor.log");
        return saveDataEditorLogger;
    }

    public TileMap getTileMap() {
        if (Objects.nonNull(tileMap)) {
            return tileMap;
        }
        tileMap = new TileMap(getEngineLogger());
        return tileMap;
    }

    public GameLevels getGameLevels() {
        if (Objects.nonNull(gameLevels)) {
            return gameLevels;
        }
        gameLevels = new GameLevels(this);
        return gameLevels;
    }

    public SaveDataManager getSaveDataManager() {
        if (Objects.nonNull(saveDataManager)) {
            return saveDataManager;
        }
        saveDataManager = new SaveDataManager(this);
        return saveDataManager;
    }

    public KnownCharactersManager getKnownCharactersManager() {
        if (Objects.nonNull(knownCharactersManager)) {
            return knownCharactersManager;
        }
        knownCharactersManager = new KnownCharactersManager(this);
        return knownCharactersManager;
    }

    public ItemManager getItemManager() {
        if (Objects.nonNull(itemManager)) {
            return itemManager;
        }
        itemManager = new ItemManager(this);
        return itemManager;
    }

    public KeyHandler getKeyHandler() {
        if (Objects.nonNull(keyHandler)) {
            return keyHandler;
        }
        keyHandler = new KeyHandler();
        return keyHandler;
    }

    public GameStats getGameStats() {
        if (Objects.nonNull(gameStats)) {
            return gameStats;
        }
        gameStats = new GameStats(getGameLevels().getAllyTotalCount(), getGameLevels().getEnemyTotalCount());
        return gameStats;
    }
}
