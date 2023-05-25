package engine.tools.itemsManager;

import engine.tools.itemsManager.HealManager.Heal;
import engine.tools.itemsManager.WeaponManager.Weapon;
import engine.utils.ImageStorage;
import engine.utils.JSONUtils;
import engine.utils.Logger;
import engine.tools.ModelControlComponents;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 *  Items manager class.
 */
public class ItemManager {
    ModelControlComponents modelControlComponents;
    Logger logger;
    private final String itemsFileName;
    JSONArray itemsJSONArray;
    ArrayList<Item> items;

    public ItemManager(ModelControlComponents modelControlComponents) {
        this(modelControlComponents ,"src/main/resources/knownItems.json");
    }

    public ItemManager(ModelControlComponents modelControlComponents, String itemsFileName) {
        this.modelControlComponents = modelControlComponents;
        this.logger = modelControlComponents.getEngineLogger();
        this.itemsFileName = itemsFileName;
        items = new ArrayList<>();
        loadItemsFromFile(itemsFileName);
    }

    /**
     *  Load known items from JSON file.
     * @param itemsFileName JSON file name.
     */
    public void loadItemsFromFile(String itemsFileName) {
        File itemsJSONFile = new File(itemsFileName);
        if (!itemsJSONFile.exists()) {
            logger.warning(this, "File \"" + itemsFileName + "\" does not exist. Creating a new one.");
            JSONUtils.createBasicJSONFile(itemsFileName, logger);
            itemsJSONArray = new JSONArray();
        } else {
            try {
                BufferedReader itemsFileReader = new BufferedReader(new FileReader(itemsJSONFile));
                String jsonLine = itemsFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    itemsJSONArray = new JSONArray(jsonLine);
                    for (int itemI = 0; itemI < itemsJSONArray.length(); itemI++) {
                        JSONObject itemObject = itemsJSONArray.getJSONObject(itemI);
                        if (itemObject.getString("itemType").equals("weapon")) {
                            Weapon weapon = new Weapon(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
                            weapon.setWeaponClass(itemObject.getString("weaponClass"));
                            weapon.setDescription(itemObject.getString("description"));
                            weapon.setStats(itemObject.getInt("might"), itemObject.getInt("hitRate"), itemObject.getInt("criticalHitRate"), itemObject.getInt("range"));
                            weapon.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
                            items.add(weapon);
                        } else if (itemObject.getString("itemType").equals("heal")) {
                            Heal heal = new Heal(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
                            heal.setDescription(itemObject.getString("description"));
                            heal.setHealAmount(itemObject.getInt("healAmount"));
                            heal.setHealRange(itemObject.getInt("healRange"));
                            heal.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
                            items.add(heal);
                        }
                    }
                } else {
                    itemsJSONArray = new JSONArray();
                }
                itemsFileReader.close();
                JSONUtils.saveJSONFileCopy(itemsFileName, itemsJSONArray, logger);
            } catch (IOException e) {
                logger.error(this, "An error occurred while reading file \"" + itemsFileName + "\".");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Adds item to known items.
     * @param item Item to add.
     * @return True if item has been added. False otherwise.
     */
    public boolean addItem(Item item) {
        for (Item knownItem : items) {
            if (knownItem.getName().equals(item.getName())) {
                logger.error(this, "Item with name \"" + item.getName() + "\" already exist.");
                return false;
            }
        }
        items.add(item);
        logger.error(this, "Item \"" + item.getName() + "\" has been successfully added.");
        return true;
    }

    public ArrayList<Item> getAllItems() {
        return items;
    }

    public int getItemsAmount() {
        return items.size();
    }

    /**
     *  Checks if item does already exist among known items.
     * @param name Items name.
     * @return True if item does already exist.
     */
    public boolean itemDoesExist(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     *  Returns item of given index or null.
     * @param index Items index.
     * @return Item or null.
     */
    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) {
            logger.error(this, "Item index out of range: " + index + ".");
            return null;
        }

        return items.get(index);
    }

    /**
     *  Returns item of given name or null.
     * @param name Items name.
     * @return Item or null.
     */
    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }

        logger.info(this, "No item with name \"" + name + "\" has been found.");
        return null;
    }

    public ArrayList<String> getAllItemTypes() {
        ArrayList<String> itemTypesArray = new ArrayList<>();
        for (Item item : items) {
            if (!itemTypesArray.contains(item.getItemType())) {
                itemTypesArray.add(item.getItemType());
            }
        }
        return itemTypesArray;
    }

    /**
     *  Renames items.
     * @param previousName Items name.
     * @param newName New items name.
     * @return True if item has been renamed. False otherwise.
     */
    public boolean renameItem(String previousName, String newName) {
        if (itemDoesExist(newName)) {
            logger.error(this, "Item with name \"" + newName + "\" does already exist.");
            return false;
        }

        for (Item item : items) {
            if (item.getName().equals(previousName)) {
                item.setName(newName);
                logger.info(this, "Item \"" + previousName + "\" has been renamed to \"" + newName + "\".");
                return true;
            }
        }

        logger.error(this, "No item with name \"" + previousName + "\" has been found.");
        return false;
    }

    /**
     *  Removes item.
     * @param name Items name.
     * @return True if item has been removed.
     */
    public boolean removeItem(String name) {
        for (int itemI = 0; itemI < items.size(); itemI++) {
            if (items.get(itemI).getName().equals(name)) {
                items.remove(itemI);
                logger.info(this, "Item \"" + name + "\" has been successfully removed.");
                return true;
            }
        }

        logger.error(this, "No item with name \"" + name + "\" has been found.");
        return false;
    }

    /**
     *  Saves all changes to JSON file.
     * @return True if changes has been saved.
     */
    public boolean saveJSONFile() {
        itemsJSONArray = new JSONArray();
        for (Item item: items) {
            JSONObject itemObject = new JSONObject();
            if (item.getItemType().equals("weapon")) {
                Weapon weapon = (Weapon) item;
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
            } else if (item.getItemType().equals("heal")) {
                Heal heal = (Heal) item;
                itemObject.put("itemType", "heal");
                itemObject.put("name", heal.getName());
                itemObject.put("sprite", heal.getSprite().getImageName(0));
                itemObject.put("description", heal.getDescription());
                itemObject.put("healAmount", heal.getHealAmount());
                itemObject.put("healRange", heal.getHealRange());
                itemObject.put("maxAmount", heal.getMaxAmountInInventory());
            }
            itemsJSONArray.put(itemObject);
        }
        return JSONUtils.saveJSONFile(itemsFileName, itemsJSONArray, logger);
    }
}
