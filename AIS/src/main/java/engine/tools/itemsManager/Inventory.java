package engine.tools.itemsManager;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<InventoryItem> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(Item item, int count) {
        items.add(new InventoryItem(item, count));
    }

    public void remove(InventoryItem inventoryItem) {
        items.remove(inventoryItem);
    }

    public InventoryItem getInventoryItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public InventoryItem getInventoryItem(String name) {
        for (InventoryItem item : items) {
            if (item.getItem().getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public int getInventorySize() {
        return items.size();
    }
}
