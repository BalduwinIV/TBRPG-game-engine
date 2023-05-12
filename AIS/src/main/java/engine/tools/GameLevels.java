package engine.tools;

import engine.utils.ImageStorage;
import engine.utils.Logger;
import engine.utils.Rectangle;
import game.objects.BaseObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.json.*;

/**
 * Game map class.
 */
public class GameLevels {
    private Logger logger;
    private final String gameLevelsFileName;
    private JSONArray gameLevels;
    private String currentLevel;
    private int[] currentLevelSize;
    private ImageStorage currentLevelBackground;
    private final List<Rectangle> currentLevelCollisions;
    private final List<BaseObject> currentLevelCharacters;

    public GameLevels() {
        gameLevelsFileName = "src/main/resources/levels.json";
        currentLevelSize = new int[2];
        currentLevelCollisions = new ArrayList<>();
        currentLevelCharacters = new ArrayList<>();
    }

    public GameLevels(String gameLevelsFileName) {
        this.gameLevelsFileName = gameLevelsFileName;
        currentLevelCollisions = new ArrayList<>();
        currentLevelCharacters = new ArrayList<>();
        loadLevels(gameLevelsFileName);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void loadLevels(String gameLevelsFileName) {
        File levelsJSONFile = new File(gameLevelsFileName);
        if (!levelsJSONFile.exists()) {
            logger.warning(this, "File does not exist. Creating a new one.");
            createLevelsFile(gameLevelsFileName);
        } else {
            try {
                BufferedReader levelsFileReader = new BufferedReader(new FileReader(levelsJSONFile));
                String jsonLine = levelsFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    gameLevels = new JSONArray(jsonLine);
                } else {
                    gameLevels = new JSONArray();
                }
                levelsFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createLevelsFile(String gameLevelsFileName) {
        File levelsJSONFile = new File(gameLevelsFileName);
        if (levelsJSONFile.exists()) {
            System.err.println("File already exists.");
        } else {
            gameLevels = new JSONArray();
            try {
                FileWriter levelFileWriter = new FileWriter(levelsJSONFile);
                levelFileWriter.write(gameLevels.toString());
                levelFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addLevel(String name, ImageStorage backgroundImage) {
        for (int levelIndex = 0; levelIndex < gameLevels.length(); levelIndex++) {
            JSONObject level = gameLevels.getJSONObject(levelIndex);
            if (name.equals(level.get("name"))) {
                logger.error(this, "Level with name \"" + name + "\" already exists.");
                return false;
            }
        }
        JSONObject newLevel = new JSONObject();
        newLevel.put("name", name);
        newLevel.put("background", backgroundImage.getImageName(0));
        newLevel.put("width", backgroundImage.getImage(0).getWidth());
        newLevel.put("height", backgroundImage.getImage(0).getHeight());
        JSONArray characters = new JSONArray();
        newLevel.put("characters", characters);
        JSONArray collisions = new JSONArray();
        newLevel.put("collisions", collisions);
        gameLevels.put(newLevel);

        logger.info(this, "New level \"" + name + "\" with background image \"" + backgroundImage.getImageName(0) + "\" has been added.");
        return true;
    }

    public void saveJSONFile() {
        File levelsJSONFile = new File(gameLevelsFileName);
        if (!levelsJSONFile.exists()) {
            logger.error(this, "File does not exist.");
        } else {
            try {
                FileWriter levelFileWriter = new FileWriter(levelsJSONFile);
                ArrayList<String> gameLevelsList = new ArrayList<>();
                for (int i = 0; i < gameLevels.length(); i++) {
                    gameLevelsList.add(gameLevels.get(i).toString());
                }
                levelFileWriter.write(Arrays.toString(gameLevelsList.toArray(new String[gameLevelsList.size()])));
                levelFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets a new map size.
     * @param   width   Maps width.
     * @param   height  Maps height.
     */
    public void setLevelSize(int width, int height) {
        currentLevelSize[0] = width;
        currentLevelSize[1] = height;
    }

    public String[] getLevelsList() {
        String[] levelsList = new String[gameLevels.length()];
        for (int levelI = 0; levelI < gameLevels.length(); levelI++) {
            levelsList[levelI] = (String) gameLevels.getJSONObject(levelI).get("name");
        }

        return levelsList;
    }

    public ImageStorage getLevelBackground() {
        return currentLevelBackground;
    }

    /**
     * Returns list of objects on a map.
     * @return  A list of objects in a map.
     */
    public List<BaseObject> getCurrentLevelCharacters() {
        return currentLevelCharacters;
    }

    /**
     * Returns a map size.
     * @return  Map size.
     */
    public int[] getLevelSize() {
        return currentLevelSize;
    }

    /**
     * Returns current maps name.
     * @return  Current maps name.
     */
    public String getCurrentLevel() {
        return currentLevel;
    }
}
