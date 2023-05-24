package engine.tools.itemsManager.HealManager;

import engine.tools.itemsManager.Item;
import engine.utils.ImageStorage;

public class Heal extends Item {
    private int healAmount;
    private int healRange;

    public Heal(String name, ImageStorage sprite) {
        setItemType("heal");
        setName(name);
        setSprite(sprite);
        setDescription("No description");
        setMaxAmountInInventory(10);
        healAmount = 0;
        healRange = 1;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public int getHealRange() {
        return healRange;
    }

    public void setHealRange(int healRange) {
        this.healRange = healRange;
    }
}
