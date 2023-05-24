package engine.tools;

import engine.tools.characterManager.Character;
import engine.utils.JSONUtils;
import engine.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaveDataManager {
    private final ModelControlComponents modelControlComponents;
    private final Logger logger;
    private final String saveDataFileName;
    private int chosenSaveDataIndex;
    private final AtomicBoolean saveDataIsRemoved;
    private final AtomicBoolean saveDataIsLoaded;
    private JSONArray saveData;
    private ArrayList<Character> characters;
    private ArrayList<String> clearedLevels;

    public SaveDataManager(ModelControlComponents modelControlComponents) {
        this(modelControlComponents, "src/main/resources/saveData.json");
    }

    public SaveDataManager(ModelControlComponents modelControlComponents, String saveDataFileName) {
        this.modelControlComponents = modelControlComponents;
        this.logger = modelControlComponents.getGameLogger();
        this.saveDataFileName = saveDataFileName;
        characters = new ArrayList<>();
        clearedLevels = new ArrayList<>();
        chosenSaveDataIndex = 0;
        saveDataIsRemoved = new AtomicBoolean(false);
        saveDataIsLoaded = new AtomicBoolean(false);
        loadSaveDataFromFile(saveDataFileName);
    }

    public void loadSaveDataFromFile(String saveDataFileName) {
        File saveDataJSONFile = new File(saveDataFileName);
        if (!saveDataJSONFile.exists()) {
            logger.warning(this, "File \"" + saveDataFileName + "\" does not exist. Creating a new one.");
            JSONUtils.createBasicJSONFile(saveDataFileName, logger);
            saveData = new JSONArray();
            JSONObject templateSaveData = new JSONObject();
            JSONArray charactersArray = new JSONArray();
            JSONArray clearedLevelsArray = new JSONArray();
            templateSaveData.put("characters", charactersArray);
            templateSaveData.put("clearedLevels", clearedLevelsArray);
            saveData.put(templateSaveData);
        } else {
            try {
                BufferedReader saveDataFileReader = new BufferedReader(new FileReader(saveDataJSONFile));
                String jsonLine = saveDataFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    saveData = new JSONArray(jsonLine);
                } else {
                    saveData = new JSONArray();
                    JSONObject templateSaveData = new JSONObject();
                    JSONArray charactersArray = new JSONArray();
                    JSONArray clearedLevelsArray = new JSONArray();
                    templateSaveData.put("characters", charactersArray);
                    templateSaveData.put("clearedLevels", clearedLevelsArray);
                    saveData.put(templateSaveData);
                }
                saveDataFileReader.close();
                JSONUtils.saveJSONFileCopy(saveDataFileName, saveData, logger);
            } catch (IOException e) {
                logger.error(this, "An error occurred while reading file \"" + saveDataFileName + "\".");
                e.printStackTrace();
            }
        }
    }

    public boolean loadSaveData(int saveDataIndex) {
        if (saveDataIndex < 0 || saveDataIndex >= saveData.length()) {
            logger.error(this, "Save data index out of range: " + saveDataIndex + ".");
            return false;
        }

        saveDataIsLoaded.set(true);
        chosenSaveDataIndex = saveDataIndex;
        JSONObject currentSaveData = saveData.getJSONObject(saveDataIndex);
        /* load characters */
        JSONArray saveDataCharacters = currentSaveData.getJSONArray("characters");
        characters = new ArrayList<>(saveDataCharacters.length());
        for (int characterI = 0; characterI < saveDataCharacters.length(); characterI++) {
            characters.add(JSONUtils.parseCharacterObject(saveDataCharacters.getJSONObject(characterI)));
        }

        JSONArray clearedLevelsArray = currentSaveData.getJSONArray("clearedLevels");
        clearedLevels = new ArrayList<>(clearedLevelsArray.length());
        for (int levelI = 0; levelI < clearedLevelsArray.length(); levelI++) {
            clearedLevels.add(clearedLevelsArray.getString(levelI));
        }

        logger.info(this, "Save data #" + chosenSaveDataIndex + " has been successfully loaded.");
        return true;
    }

    public void addSaveData() {
        JSONObject newSaveData = new JSONObject(saveData.getJSONObject(0).toString());

        saveData.put(newSaveData);
    }

    public boolean isCharacterInCurrentSaveData(String name) {
        for (Character character : characters) {
            if (character.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean addCharacterToCurrentSaveData(Character character) {
        for (Character characterFromSaveData : characters) {
            if (characterFromSaveData.getName().equals(character.getName())) {
                logger.error(this, "A new character has same name as other character in (current) save data #" + chosenSaveDataIndex + ".");
                return false;
            }
        }
        characters.add(character);
        logger.info(this, "\nCharacter \"" + character.getName() + "\" has been added to (current) save data #" + chosenSaveDataIndex + ".\n" + character);
        return true;
    }

    public boolean changeCharacterFromCurrentSaveData(Character changedCharacter) {
        for (int characterI = 0; characterI < characters.size(); characterI++) {
            if (characters.get(characterI).getName().equals(changedCharacter.getName())) {
                characters.remove(characterI);
                characters.add(characterI, changedCharacter);

                logger.info(this, "\nCharacter \"" + changedCharacter.getName() + "\" has been changed in (current) save data #" + chosenSaveDataIndex + ".\n" + changedCharacter);
                return true;
            }
        }
        logger.error(this, "Character \"" + changedCharacter.getName() + "\" has not been found in (current) save data #" + chosenSaveDataIndex + ".");
        return false;
    }

    public boolean renameCharacterFromCurrentSaveData(String previousName, String newName) {
        if (isCharacterInCurrentSaveData(newName)) {
            logger.error(this, "Character with name \"" + newName + "\" already exists.");
            return false;
        }
        for (Character character : characters) {
            if (character.getName().equals(previousName)) {
                character.setName(newName);
                logger.info(this, "Character \"" + previousName + "\" has been renamed to \"" + newName + "\".");

                return true;
            }
        }
        logger.warning(this, "Character \"" + previousName + "\" does not exist.");
        return false;
    }

    public boolean removeCharacterFromCurrentSaveData(Character character) {
        for (int characterI = 0; characterI < characters.size(); characterI++) {
            if (characters.get(characterI).getName().equals(character.getName())) {
                characters.remove(characterI);
                logger.info(this, "Character \"" + character.getName() + "\" has been removed from (current) save data #" + chosenSaveDataIndex + ".");
                return true;
            }
        }
        logger.warning(this, "Character \"" + character.getName() + "\" has not been found in (current) save data #" + chosenSaveDataIndex + ".");
        return false;
    }

    public boolean removeCharacterFromCurrentSaveData(String name) {
        for (int characterI = 0; characterI < characters.size(); characterI++) {
            if (characters.get(characterI).getName().equals(name)) {
                characters.remove(characterI);
                logger.info(this, "Character \"" + name + "\" has been removed from (current) save data #" + chosenSaveDataIndex + ".");
                return true;
            }
        }
        logger.warning(this, "Character \"" + name + "\" has not been found in (current) save data #" + chosenSaveDataIndex + ".");
        return false;
    }

    public boolean addClearedLevelToCurrentSaveData(String levelName) {
        for (String gameLevelsName : modelControlComponents.getGameLevels().getLevelNamesList()) {
            if (gameLevelsName.equals(levelName)) {
                if (clearedLevels.contains(levelName)) {
                    logger.info(this, "Level \"" + levelName + "\" has been already added to cleared levels list in save data #" + chosenSaveDataIndex + " before.");
                    return false;
                } else {
                    clearedLevels.add(levelName);
                    modelControlComponents.getSaveDataManager().saveJSONFile();
                    logger.info(this, "Level \"" + levelName + "\" has been added to cleared levels list in save data #" + chosenSaveDataIndex + ".");
                    return true;
                }
            }
        }

        logger.info(this, "Level \"" + levelName + "\" has not been found in level names list.");
        return false;
    }

    public boolean levelIsCleared(String levelName) {
        for (String gameLevelsName : clearedLevels) {
            if (gameLevelsName.equals(levelName)) {
                return true;
            }
        }

        logger.info(this, "Level \"" + levelName + "\" has not been found in cleared level names list.");
        return false;
    }

    public boolean removeClearedLevelFromCurrentSaveData(String levelName) {
        for (int levelI = 0; levelI < clearedLevels.size(); levelI++) {
            if (clearedLevels.get(levelI).equals(levelName)) {
                clearedLevels.remove(levelI);
                logger.info(this, "Level \"" + levelName + "\" has been successfully removed from (current) save data #" + chosenSaveDataIndex + ".");
                return true;
            }
        }
        logger.warning(this, "Level \"" + levelName + "\" has not been found in cleared levels list.");
        return false;
    }

    public boolean removeSaveData(int saveDataIndex) {
        if (saveDataIndex < 0 || saveDataIndex >= saveData.length()) {
            logger.error(this, "Save Data index is out of range: " + saveDataIndex);
            return false;
        }
        saveData.remove(saveDataIndex);
        saveDataIsRemoved.set(true);

        logger.info(this, "Save Data #" + saveDataIndex + " has been removed.");
        return true;
    }

    public int getSaveDataCount() {
        return saveData.length();
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public Character getCharacter(String name) {
        for (Character character : characters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }
        logger.info(this, "No character with name \"" + name + "\" has been found.");
        return null;
    }

    public ArrayList<String> getClearedLevels() {
        return clearedLevels;
    }

    public boolean saveJSONFile() {
        if (Objects.nonNull(saveData) && !saveDataIsRemoved.get() && saveDataIsLoaded.get()) {
            JSONArray charactersArray = new JSONArray();
            for (Character localCharacter : characters) {
                charactersArray.put(JSONUtils.convertCharacterToJSONObject(localCharacter));
            }

            JSONArray clearedLevelsArray = new JSONArray();
            for (String levelName : clearedLevels) {
                clearedLevelsArray.put(levelName);
            }

            saveData.getJSONObject(chosenSaveDataIndex).put("characters", charactersArray);
            saveData.getJSONObject(chosenSaveDataIndex).put("clearedLevels", clearedLevelsArray);
        }
        if (saveDataIsRemoved.get()) {
            saveDataIsRemoved.set(false);
        }
        return JSONUtils.saveJSONFile(saveDataFileName, saveData, logger);
    }
}
