package engine.tools.itemsManager.WeaponManager;

import engine.tools.itemsManager.Item;
import engine.utils.ImageStorage;

public class Weapon extends Item {
    private String weaponClass;
    private int might;  /* Adds points to strength/magic */
    private int hitRate;
    private int criticalHitRate;
    private int range;  /* Weapon attack range */
    public Weapon(String name, ImageStorage sprite) {
        setItemType("weapon");
        setName(name);
        setSprite(sprite);
        setWeaponClass("none");
        setDescription("No description.");
        setMaxAmountInInventory(1);
    }

    public void setWeaponClass(String weaponClass) {
        this.weaponClass = weaponClass;
    }

    public void setStats(int might, int hitRate, int criticalHitRate, int range) {
        this.might = Math.max(might, 0);
        this.hitRate = Math.max(hitRate, 0);
        this.criticalHitRate = Math.max(criticalHitRate, 0);
        this.range = Math.max(range, 0);
    }

    public void setMaxAmountInInventory(int newAmount) {
        maxAmountInInventory = newAmount;
    }

    public String getWeaponClass() {
        return weaponClass;
    }

    public int getMight() {
        return might;
    }

    public int getHitRate() {
        return hitRate;
    }

    public int getCriticalHitRate() {
        return criticalHitRate;
    }

    public int getRange() {
        return range;
    }
}
