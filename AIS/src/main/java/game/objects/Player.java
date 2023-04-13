package game.objects;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

/**
 * Players class.
 */
public class Player extends AnimatedObject {
    private Weapon weapon;
    private Inventory inventory;

    public Player() {
        super();
        inventory = new Inventory();
    }

    public Player(double hp) {
        super(hp);
        inventory = new Inventory();
    }

    public Player(Rectangle objectSpace) {
        super(objectSpace);
        inventory = new Inventory();
    }

    public Player(ImageStorage texture) {
        super(texture);
        inventory = new Inventory();
    }

    public Player(Rectangle objectSpace, double hp) {
        super(objectSpace, hp);
        inventory = new Inventory();
    }

    public Player(ImageStorage texture, double hp) {
        super(texture, hp);
        inventory = new Inventory();
    }

    public Player(Rectangle objectSpace, ImageStorage texture) {
        super(objectSpace, texture);
        inventory = new Inventory();
    }

    public Player(Rectangle objectSpace, ImageStorage texture, double hp) {
        super(objectSpace, texture, hp);
        inventory = new Inventory();
    }

    public Player(Rectangle objectSpace, ImageStorage texture, double hp, Weapon weapon) {
        super(objectSpace, texture, hp);
        inventory = new Inventory();
        this.weapon = weapon;
    }

    public Player(Rectangle objectSpace, ImageStorage texture, double hp, Weapon weapon, Inventory inventory) {
        super(objectSpace, texture, hp);
        this.inventory = inventory;
        this.weapon = weapon;
    }

    public Player(Rectangle objectSpace, ImageStorage texture, double hp, Inventory inventory) {
        super(objectSpace, texture, hp);
        this.inventory = inventory;
    }

    /**
     * Sets a new weapon to player.
     * @param   weapon  A new weapon.
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Resets player weapon to default.
     */
    public void resetWeapon() {
        weapon = null;
    }
}
