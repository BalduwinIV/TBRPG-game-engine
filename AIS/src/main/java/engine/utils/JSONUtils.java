package engine.utils;

import engine.tools.characterManager.Character;
import engine.tools.itemsManager.HealManager.Heal;
import engine.tools.itemsManager.Inventory;
import engine.tools.itemsManager.Item;
import engine.tools.itemsManager.WeaponManager.Weapon;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Utils for working with JSON files.
 */
public class JSONUtils {
    private JSONUtils() {}

    /**
     *  Renames JSON object.
     * @param jsonArray JSON array that contains object.
     * @param logger Logger to print messages.
     * @param objectType Object type for logger messages.
     * @param previousName Objects name.
     * @param newName New objects name.
     * @return True if object has been renamed.
     */
    public static boolean renameObject(JSONArray jsonArray, Logger logger, String objectType, String previousName, String newName) {
        for (int objectI = 0; objectI < jsonArray.length(); objectI++) {
            if (jsonArray.getJSONObject(objectI).getString("name").equals(newName)) {
                logger.error(objectType + " with name \"" + newName + "\" already exists.");
                return false;
            }
        }
        for (int tileI = 0; tileI < jsonArray.length(); tileI++) {
            if (jsonArray.getJSONObject(tileI).getString("name").equals(previousName)) {
                jsonArray.getJSONObject(tileI).put("name", newName);
                logger.info(objectType + " \"" + previousName + "\" has been renamed to \"" + newName + "\".");
                return true;
            }
        }

        logger.error(objectType + " with name \"" + previousName + "\" does not exist.");
        return false;
    }

    /**
     *  Removes JSON objects from JSON array.
     * @param jsonArray JSON array that contains object.
     * @param logger Logger to print messages.
     * @param objectType Objects type for logger messages.
     * @param name Objects name.
     * @return True if object has been removed.
     */
    public static boolean removeObject(JSONArray jsonArray, Logger logger, String objectType, String name) {
        for (int objectI = 0; objectI < jsonArray.length(); objectI++) {
            if (name.equals(jsonArray.getJSONObject(objectI).getString("name"))) {
                jsonArray.remove(objectI);
                logger.info(objectType + " with name \"" + name + "\" has been successfully removed.");
                return true;
            }
        }
        logger.warning(objectType + " with name \"" + name + "\" cannot be removed: it does not exist.");
        return false;
    }

    /**
     *  Save JSON array to JSON file.
     * @param JSONFilePath JSON file name.
     * @param jsonArray JSON array.
     * @param logger Logger to print messages.
     * @return True if JSON file has been saved. False otherwise.
     */
    public static boolean saveJSONFile(String JSONFilePath, JSONArray jsonArray, Logger logger) {
        File JSONFile = new File(JSONFilePath);
        if (!JSONFile.exists()) {
            logger.warning("File \"" + JSONFile + "\" does not exist. Creating a new one.");
        }
        try {
            FileWriter JSONFileWriter = new FileWriter(JSONFile);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.get(i).toString());
            }
            JSONFileWriter.write(Arrays.toString(list.toArray(new String[list.size()])));
            JSONFileWriter.close();
            logger.info("JSON file \"" + JSONFilePath + "\" has been successfully saved.");
            return true;
        } catch (IOException e) {
            logger.error("An error occurred while saving JSON file \"" + JSONFilePath + "\".");
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  Created JSON files copy.
     * @param JSONFilePath JSON file name.
     * @param jsonArray JSON array.
     * @param logger Logger message.
     * @return True if JSON files copy has been saved. False otherwise.
     */
    public static boolean saveJSONFileCopy(String JSONFilePath, JSONArray jsonArray, Logger logger) {
        int dotIndex = JSONFilePath.lastIndexOf('.');
        String JSONFilePathCopy = JSONFilePath.substring(0, dotIndex) + "_copy.json";
        logger.info("Creating a copy of \"" + JSONFilePath + "\" file.");
        return saveJSONFile(JSONFilePathCopy, jsonArray, logger);
    }

    /**
     *  Creates empty JSON file.
     * @param fileName JSON file name.
     * @param logger Logger to print messages.
     */
    public static void createBasicJSONFile(String fileName, Logger logger) {
        File JSONFile = new File(fileName);
        if (JSONFile.exists()) {
            logger.warning("File \"" + fileName + "\" already exists.");
        } else {
            JSONArray array = new JSONArray();
            try {
                FileWriter knownCharactersFileWriter = new FileWriter(JSONFile);
                knownCharactersFileWriter.write(array.toString());
                knownCharactersFileWriter.close();
                logger.info("File \"" + fileName + "\" has been successfully created.");
            } catch (IOException e) {
                logger.error("An error occurred while writing file \"" + fileName + "\".");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Parses characters JSONObject and returns freshly created character.
     * @param characterObject Characters JSONObject.
     * @return Characters object.
     */
    public static Character parseCharacterObject(JSONObject characterObject) {
        Character characterToAdd = new Character(characterObject.getString("name"), new ImageStorage(characterObject.getString("sprite")));
        characterToAdd.setCharacterType(characterObject.getString("characterType"));
        characterToAdd.setBaseStats(characterObject.getInt("level"), characterObject.getInt("exp"),
                characterObject.getInt("baseHP"), characterObject.getInt("baseDMG"),
                characterObject.getInt("hitRate"), characterObject.getInt("criticalHitRate"),
                characterObject.getInt("avoidRate"), characterObject.getInt("movement"));
        characterToAdd.setLevelDependentStats(characterObject.getInt("strength"),
                characterObject.getInt("magic"), characterObject.getInt("skill"),
                characterObject.getInt("speed"), characterObject.getInt("luck"),
                characterObject.getInt("defence"), characterObject.getInt("resistance"));

        Inventory charactersInventory = new Inventory();
        JSONArray inventoryArray = characterObject.getJSONArray("inventory");
        for (int itemI = 0; itemI < inventoryArray.length(); itemI++) {
            charactersInventory.addItem(parseItemObject(inventoryArray.getJSONObject(itemI).getJSONObject("item")), inventoryArray.getJSONObject(itemI).getInt("amount"));
        }
        characterToAdd.setInventory(charactersInventory);
        return characterToAdd;
    }

    /**
     *  Parses items JSONObject and returns freshly created item.
     * @param itemObject Items JSONObject.
     * @return Items object.
     */
    public static Item parseItemObject(JSONObject itemObject) {
        if (itemObject.getString("itemType").equals("weapon")) {
            Weapon weaponToAdd = new Weapon(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
            weaponToAdd.setWeaponClass(itemObject.getString("weaponClass"));
            weaponToAdd.setDescription(itemObject.getString("description"));
            weaponToAdd.setStats(itemObject.getInt("might"), itemObject.getInt("hitRate"), itemObject.getInt("criticalHitRate"), itemObject.getInt("range"));
            weaponToAdd.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
            return weaponToAdd;
        } else if (itemObject.getString("itemType").equals("heal")) {
            Heal healToAdd = new Heal(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
            healToAdd.setDescription(itemObject.getString("description"));
            healToAdd.setHealAmount(itemObject.getInt("healAmount"));
            healToAdd.setHealRange(itemObject.getInt("healRange"));
            healToAdd.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
            return healToAdd;
        }
        return null;
    }

    /**
     *  Converts character object ot JSONObject.
     * @param character Character to convert.
     * @return Characters JSONObject.
     */
    public static JSONObject convertCharacterToJSONObject(Character character) {
        JSONObject characterObject = new JSONObject();

        characterObject.put("characterType", character.getCharacterType());

        characterObject.put("name", character.getName());
        characterObject.put("width", character.getWidth());
        characterObject.put("height", character.getHeight());
        characterObject.put("sprite", character.getSprite().getImageName(0));

        characterObject.put("baseHP", character.getBaseHP());
        characterObject.put("baseDMG", character.getBaseDMG());
        characterObject.put("hitRate", character.getHitRate());
        characterObject.put("criticalHitRate", character.getCriticalHitRate());
        characterObject.put("avoidRate", character.getAvoidRate());

        characterObject.put("level", character.getLevel());
        characterObject.put("exp", character.getExp());
        characterObject.put("strength", character.getStrength());
        characterObject.put("magic", character.getMagic());
        characterObject.put("skill", character.getSkill());
        characterObject.put("speed", character.getSpeed());
        characterObject.put("luck", character.getLuck());
        characterObject.put("defence", character.getDefence());
        characterObject.put("resistance", character.getResistance());

        characterObject.put("movement", character.getMovement());

        JSONArray inventoryArray = new JSONArray();
        Inventory inventory = character.getInventory();
        for (int itemI = 0; itemI < inventory.getInventorySize(); itemI++) {
            JSONObject inventoryItemObject = new JSONObject();

            JSONObject itemObject = new JSONObject();
            if (inventory.getInventoryItem(itemI).getItem().getItemType().equals("weapon")) {
                Weapon weapon = (Weapon) inventory.getInventoryItem(itemI).getItem();
                itemObject.put("itemType", "weapon");
                itemObject.put("name", weapon.getName());
                itemObject.put("sprite", weapon.getSprite().getImageName(0));
                itemObject.put("description", weapon.getDescription());
                itemObject.put("weaponClass", weapon.getWeaponClass());
                itemObject.put("might", weapon.getMight());
                itemObject.put("hitRate", weapon.getHitRate());
                itemObject.put("criticalHitRate", weapon.getCriticalHitRate());
                itemObject.put("range", weapon.getRange());
                itemObject.put("maxAmount", weapon.getMaxAmountInInventory());
            } else if (inventory.getInventoryItem(itemI).getItem().getItemType().equals("heal")) {
                Heal heal = (Heal) inventory.getInventoryItem(itemI).getItem();
                itemObject.put("itemType", "heal");
                itemObject.put("name", heal.getName());
                itemObject.put("sprite", heal.getSprite().getImageName(0));
                itemObject.put("description", heal.getDescription());
                itemObject.put("healAmount", heal.getHealAmount());
                itemObject.put("healRange", heal.getHealRange());
                itemObject.put("maxAmount", heal.getMaxAmountInInventory());
            }

            inventoryItemObject.put("item", itemObject);
            inventoryItemObject.put("amount", inventory.getInventoryItem(itemI).getAmount());

            inventoryArray.put(inventoryItemObject);
        }

        characterObject.put("inventory", inventoryArray);
        return characterObject;
    }
}
