package engine.tools.itemsManager;

import engine.utils.ImageStorage;

public abstract class Item {
    protected String itemType;
    protected int maxAmountInInventory;
    protected String name;
    protected ImageStorage sprite;
    protected String description;
    protected int amount;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setMaxAmountInInventory(int maxAmountInInventory) {
        this.maxAmountInInventory = maxAmountInInventory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprite(ImageStorage sprite) {
        this.sprite = sprite;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxAmountInInventory() {
        return maxAmountInInventory;
    }

    public String getName() {
        return name;
    }

    public ImageStorage getSprite() {
        return sprite;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
