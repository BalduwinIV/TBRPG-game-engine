package game.objects;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

/**
 * Base class for a smart enemy.
 */
public class SmartEnemy extends AnimatedObject implements Enemy {
    private double[] targetPosition;
    private BaseObject targetObject;
    private Weapon weapon;
    private Inventory inventory;

    public SmartEnemy() {
        super();
        targetPosition = new double[2];
        inventory = new Inventory();
    }

    public SmartEnemy(double hp) {
        super(hp);
        targetPosition = new double[2];
        inventory = new Inventory();
    }

    public SmartEnemy(Rectangle objectSpace) {
        super(objectSpace);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        inventory = new Inventory();
    }

    public SmartEnemy(ImageStorage texture) {
        super(texture);
        targetPosition = new double[2];
        inventory = new Inventory();
    }

    public SmartEnemy(Rectangle objectSpace, double hp) {
        super(objectSpace, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        inventory = new Inventory();
    }

    public SmartEnemy(ImageStorage texture, double hp) {
        super(texture, hp);
        targetPosition = new double[2];
        inventory = new Inventory();
    }

    public SmartEnemy(Rectangle objectSpace, ImageStorage texture) {
        super(objectSpace, texture);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        inventory = new Inventory();
    }

    public SmartEnemy(Rectangle objectSpace, ImageStorage texture, double hp) {
        super(objectSpace, texture, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        inventory = new Inventory();
    }

    public SmartEnemy(Rectangle objectSpace, ImageStorage texture, double hp, Weapon weapon) {
        super(objectSpace, texture, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        inventory = new Inventory();
        this.weapon = weapon;
    }

    public SmartEnemy(Rectangle objectSpace, ImageStorage texture, double hp, Weapon weapon, Inventory inventory) {
        super(objectSpace, texture, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
        this.inventory = inventory;
        this.weapon = weapon;
    }

    /**
     * Sets a new weapon for an enemy.
     * @param   weapon  New weapon.
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Resets enemy weapon to default.
     */
    public void resetWeapon() {
        this.weapon = null;
    }

    /**
     * Sets a new target object to this enemy.
     * @param targetObject Target object.
     */
    @Override
    public void setTargetObject(BaseObject targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * Sets a new position of a target.
     * @param y Target Y coordinate.
     * @param x Target X coordinate.
     */
    @Override
    public void setTargetPosition(double y, double x) {
        this.targetPosition[0] = x;
        this.targetPosition[1] = y;
    }
}
