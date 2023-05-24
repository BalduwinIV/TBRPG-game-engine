package engine.tools.characterManager;

import engine.tools.ModelControlComponents;
import engine.utils.JSONUtils;
import engine.utils.Logger;
import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 *  Class for managing known characters in Engines Objects panel.
 */
public class KnownCharactersManager {
    private final Logger logger;
    private final String knownCharactersFileName;
    private JSONArray knownCharactersArray;
    private final ArrayList<Character> knownCharacters;

    public KnownCharactersManager(ModelControlComponents modelControlComponents) {
        this(modelControlComponents, "src/main/resources/knownCharacters.json");
    }

    public KnownCharactersManager(ModelControlComponents modelControlComponents, String knownCharactersFileName) {
        this.logger = modelControlComponents.getEngineLogger();
        this.knownCharactersFileName = knownCharactersFileName;
        knownCharacters = new ArrayList<>();
        loadKnownCharactersFromFile(knownCharactersFileName);
    }

    /**
     *  Loads all known characters from file to knownCharacters array.
     * @param knownCharactersFileName   JSON filename.
     */
    public void loadKnownCharactersFromFile(String knownCharactersFileName) {
        File knownCharactersJSONFile = new File(knownCharactersFileName);
        if (!knownCharactersJSONFile.exists()) {
            logger.warning(this, "File \"" + knownCharactersFileName + "\" does not exist. Creating a new one.");
            JSONUtils.createBasicJSONFile(knownCharactersFileName, logger);
            knownCharactersArray = new JSONArray();
        } else {
            try {
                BufferedReader knownCharactersFileReader = new BufferedReader(new FileReader(knownCharactersJSONFile));
                String jsonLine = knownCharactersFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    knownCharactersArray = new JSONArray(jsonLine);
                    for (int characterI = 0; characterI < knownCharactersArray.length(); characterI++) {
                        knownCharacters.add(JSONUtils.parseCharacterObject(knownCharactersArray.getJSONObject(characterI)));
                    }
                } else {
                    knownCharactersArray = new JSONArray();
                }
                knownCharactersFileReader.close();
                JSONUtils.saveJSONFileCopy(knownCharactersFileName, knownCharactersArray, logger);
            } catch (IOException e) {
                logger.error(this, "An error occurred while reading file \"" + knownCharactersFileName + "\".");
                e.printStackTrace();
            }
        }
    }


    /**
     *  Checks if character already exist among known characters.
     * @param name Characters name.
     * @return  True if exists. False otherwise.
     */
    public boolean characterDoesExist(String name) {
        for (Character knownCharacter : knownCharacters) {
            if (knownCharacter.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Adds character to known characters.
     * @param character Character to add.
     * @return True if characters has been added. False otherwise.
     */
    public boolean addCharacter(Character character) {
        for (Character knownCharacter : knownCharacters) {
            if (knownCharacter.getName().equals(character.getName())) {
                logger.error(this, "Character with name \"" + character.getName() + "\" already exists.");
                return false;
            }
        }

        knownCharacters.add(character);
        printCharactersInfoToLogger(character);
        return true;
    }

    /**
     *  Prints characters info.
     * @param character Character.
     */
    private void printCharactersInfoToLogger(Character character) {
        logger.info(this, "\nCharacter \"" + character.getName() + "\" has been added.\n" + character);
    }

    /**
     *  Updates characteristics of character with name of changedCharacter.
     * @param changedCharacter  Character with new characteristics.
     * @return True if character characteristics has been updated. False otherwise.
     */
    public boolean updateCharacter(Character changedCharacter) {
        for (int characterI = 0; characterI < knownCharacters.size(); characterI++) {
            if (knownCharacters.get(characterI).getName().equals(changedCharacter.getName())) {
                knownCharacters.remove(characterI);
                knownCharacters.add(characterI, changedCharacter);

                printCharactersInfoToLogger(changedCharacter);
                return true;
            }
        }
        logger.error(this, "Character \"" + changedCharacter.getName() + "\" has not been found.");
        return false;
    }

    /**
     *  Renames character with "previousName" to "newName".
     * @param previousName Previous characters name.
     * @param newName New characters name.
     * @return True if character has been renamed. False otherwise.
     */
    public boolean renameCharacter(String previousName, String newName) {
        for (Character knownCharacter : knownCharacters) {
            if (knownCharacter.getName().equals(newName)) {
                logger.error(this, "Character with name \"" + newName + "\" already exists.");
                return false;
            }
        }

        for (Character knownCharacter : knownCharacters) {
            if (knownCharacter.getName().equals(previousName)) {
                knownCharacter.setName(newName);
                logger.info(this, "Character \"" + previousName + "\" has been renamed to \"" + newName + "\".");
                return true;
            }
        }

        logger.error(this, "Character with name \"" + previousName + "\" has not been found.");
        return false;
    }

    /**
     *  Removes character from known characters.
     * @param name Name of character to remove.
     * @return True if character has been removed. False otherwise True if character has been removed. False otherwise.
     */
    public boolean removeCharacter(String name) {
        for (int characterI = 0; characterI < knownCharacters.size(); characterI++) {
            if (knownCharacters.get(characterI).getName().equals(name)) {
                knownCharacters.remove(characterI);
                logger.info(this, "Character \"" + name + "\" has been successfully removed.");
                return true;
            }
        }

        logger.error(this, "No character with name \"" + name + "\" has been found.");
        return false;
    }

    /**
     *  Returns list of all known characters.
     * @return ArrayList of all known characters.
     */
    public ArrayList<Character> getKnownCharacters() {
        return knownCharacters;
    }

    /**
     *  Returns character of given index in known characters array.
     * @param index Characters index.
     * @return Character or null.
     */
    public Character getCharacter(int index) {
        if (index < 0 || index >= knownCharacters.size()) {
            logger.error(this, "Character index out of range: " + index + ".");
            return null;
        }
        return knownCharacters.get(index);
    }

    /**
     *  Returns character with given name.
     * @param name Characters name.
     * @return Character if found. Null otherwise.
     */
    public Character getCharacter(String name) {
        for (Character character : knownCharacters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }

        logger.warning(this, "Character with name \"" + name + "\" has not been found.");
        return null;
    }

    /**
     *  Saves up-to-date information about known characters to JSON file.
     * @return True if changes have been saved. False otherwise.
     */
    public boolean saveJSONFile() {
        if (Objects.nonNull(knownCharactersArray)) {
            knownCharactersArray = new JSONArray();
            for (Character localCharacter : knownCharacters) {
                knownCharactersArray.put(JSONUtils.convertCharacterToJSONObject(localCharacter));
            }
        }
        return JSONUtils.saveJSONFile(knownCharactersFileName, knownCharactersArray, logger);
    }
}