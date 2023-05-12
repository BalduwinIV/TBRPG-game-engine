import engine.tools.GameLevels;
import engine.utils.ImageStorage;
import engine.utils.Logger;

public class TestGameLevel {
    public static void main(String[] args) {
        GameLevels new_level = new GameLevels();
        Logger gameLogger = new Logger("GameLogger.log");
        new_level.setLogger(gameLogger);
        new_level.loadLevels("src/main/resources/levels.json");
        new_level.addLevel("testLevel1", new ImageStorage("src/main/resources/img/levelBackgrounds/testLevel.png"));
        new_level.addLevel("testLevel2", new ImageStorage("src/main/resources/img/levelBackgrounds/testLevel.png"));
        new_level.saveJSONFile();
        gameLogger.stopLogging();
    }
}
