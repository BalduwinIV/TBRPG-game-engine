package engine.tools.characterManager;

import engine.tools.itemsManager.Inventory;
import engine.tools.itemsManager.InventoryItem;
import engine.utils.ImageStorage;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Allies and enemies character class.
 */
public class Character {
    private String name;
    private final int width;
    private final int height;
    private final ImageStorage sprite;
    private String characterType;

    /* Base stats */
    private int baseHP;
    private int baseDMG;
    private int hitRate;
    private int criticalHitRate;
    private int avoidRate;

    /* Level-dependent stats */
    private int level;      /* Maximal level is 20. */
    private int exp;        /* On 100 experience level is increasing */
    private int strength;   /* Affects physical damage. */
    private int magic;      /* Affects magical damage. */
    private int skill;      /* Affects hit rate and critical hit rate. */
    private int speed;      /* Affects avoid rate. Attacks twice if 5 higher than opponent. */
    private int luck;       /* Lowers risk of enemy critical hit. */
    private int defence;    /* Reduces damage from physical attacks. */
    private int resistance; /* Reduces damage from magical attacks. */

    private int movement;   /* Determines how many spaces character can move. */

    /* Game variables */
    private int HP;
    private final AtomicBoolean hasMoved;
    private final AtomicBoolean hasUsedItem;
    private final AtomicBoolean usingItem;
    private InventoryItem itemToUse;
    private final AtomicBoolean isAlive;

    private Inventory inventory;

    public Character(String name, ImageStorage sprite) {
        this.name = name;
        this.sprite = sprite;
        this.width = sprite.getImage().getWidth();
        this.height = sprite.getImage().getHeight();

        this.hasMoved = new AtomicBoolean(false);
        this.hasUsedItem = new AtomicBoolean(false);
        this.usingItem = new AtomicBoolean(false);
        this.isAlive = new AtomicBoolean(true);
    }

    public String getCharacterType() {
        return characterType;
    }

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setBaseStats(int level, int exp, int baseHP, int baseDMG, int hitRate, int criticalHitRate, int avoidRate, int movement) {
        this.level = Math.min(20, Math.max(1, level));
        this.exp = Math.min(99, Math.max(0, exp));
        this.baseHP = Math.max(baseHP, 1);
        this.HP = baseHP;
        this.baseDMG = Math.max(baseDMG, 0);
        this.hitRate = Math.max(hitRate, 0);
        this.criticalHitRate = Math.max(criticalHitRate, 0);
        this.avoidRate = Math.max(avoidRate, 0);
        this.movement = Math.max(movement, 0);
    }

    public void setLevelDependentStats(int strength, int magic, int skill, int speed, int luck, int defence, int resistance) {
        this.strength = Math.max(strength, 0);
        this.magic = Math.max(magic, 0);
        this.skill = Math.max(skill, 0);
        this.speed = Math.max(speed, 0);
        this.luck = Math.max(luck, 0);
        this.defence = Math.max(defence, 0);
        this.resistance = Math.max(resistance, 0);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addExp(int addedEXPValue) {
        exp += Math.max(addedEXPValue, 0);
        if (exp >= 100) {
            level = Math.min(20, level + 1);
            exp -= 100;
            increaseRandomStats();
        }
    }

    public void increaseRandomStats() {
        ThreadLocalRandom currentRNG = ThreadLocalRandom.current();
        int numberOfPoints = currentRNG.nextInt(1, 8);
        for (int pointI = 0; pointI < numberOfPoints; pointI++) {
            int stat = currentRNG.nextInt(1, 8);
            if (stat == 1) strength++;
            else if (stat == 2) magic++;
            else if (stat == 3) skill++;
            else if (stat == 4) speed++;
            else if (stat == 5) luck++;
            else if (stat == 6) defence++;
            else resistance++;
        }
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageStorage getSprite() {
        return sprite;
    }

    public int getBaseHP() {
        return baseHP;
    }

    public int getBaseDMG() {
        return baseDMG;
    }

    public int getHitRate() {
        return hitRate;
    }

    public int getCriticalHitRate() {
        return criticalHitRate;
    }

    public int getAvoidRate() {
        return avoidRate;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getStrength() {
        return strength;
    }

    public int getMagic() {
        return magic;
    }

    public int getSkill() {
        return skill;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLuck() {
        return luck;
    }

    public int getDefence() {
        return defence;
    }

    public int getResistance() {
        return resistance;
    }

    public int getMovement() {
        return movement;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setHasMoved(boolean state) {
        hasMoved.set(state);
    }

    public boolean hasMoved() {
        return hasMoved.get();
    }

    public void setIsAlive(boolean state) {
        isAlive.set(state);
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    public boolean hasUsedItem() {
        return hasUsedItem.get();
    }

    public InventoryItem getItemToUse() {
        return itemToUse;
    }

    public void setItemToUse(InventoryItem itemToUse) {
        this.itemToUse = itemToUse;
    }

    public void setHasUsedItem(boolean state) {
        hasUsedItem.set(state);
    }

    public boolean usingItem() {
        return usingItem.get();
    }

    public void setUsingItem(boolean state) {
        this.usingItem.set(state);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
        if (this.HP <= 0) {
            setIsAlive(false);
        }
    }

    public void changeHP(int value) {
        this.HP += value;
        if (this.HP <= 0) {
            setIsAlive(false);
        } else if (this.HP > baseHP) {
            this.HP = baseHP;
        }
    }

    @Override
    public String toString() {
        return "Base stats:\n" +
                "   baseHP: " + getBaseHP() + "\n" +
                "   baseDMG: " + getBaseDMG() + "\n" +
                "   hitRate: " + getHitRate() + "\n" +
                "   criticalHitRate: " + getCriticalHitRate() + "\n" +
                "   avoidRate: " + getAvoidRate() + "\n" +
                "Level-dependent stats:\n" +
                "   level: " + getLevel() + "\n" +
                "   exp: " + getExp() + "\n" +
                "   strength: " + getStrength() + "\n" +
                "   magic: " + getMagic() + "\n" +
                "   skill: " + getSkill() + "\n" +
                "   speed: " + getSpeed() + "\n" +
                "   luck: " + getLuck() + "\n" +
                "   defence: " + getDefence() + "\n" +
                "   resistance: " + getResistance() + "\n\n" +
                "   movement: " +  getMovement();
    }
}
