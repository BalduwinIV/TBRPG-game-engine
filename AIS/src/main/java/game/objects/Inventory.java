package game.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class.
 */
public class Inventory {
    private List<Item> itemsList;

    public Inventory() {
        itemsList = new ArrayList<>();
    }

    public Inventory(ArrayList<Item> itemsList) {
        this.itemsList = itemsList;
    }

    /**
     * Gets object of given index or null if index is out of inventory size range.
     * @param   index   Objects index.
     * @return  Object of given index or null if index is out of inventory size range.
     */
    public Item getItem(int index) {
        if (index >= 0 && index < itemsList.size()) {
            return itemsList.get(index);
        }
        return null;
    }

    /**
     * Gets object of given name or null if such a name does not occur.
     * @param   name    Objects name.
     * @return  Object of given name or null if such a name does not occur.
     */
    public Item getItemByName(String name) {
        for (Item item : itemsList) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Removes object of given index if index is on inventory size range.
     * @param   index   Index of an object to remove.
     */
    public void removeItem(int index) {
        if (index >= 0 && index < itemsList.size()) {
            itemsList.remove(index);
        }
    }

    /**
     * Removes object of given name if such a name occur in an inventory.
     * @param   name    Name of an object to remove.
     */
    public void removeItemByName(String name) {
        itemsList.removeIf(item -> item.getName().equals(name));
    }

    /**
     * Clears inventory from all items.
     */
    public void clear() {
        itemsList.clear();
    }
}
