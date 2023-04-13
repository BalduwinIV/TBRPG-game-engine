package game.objects;

import java.awt.*;

/**
 * Weapon base class.
 */
public class Weapon extends BaseObject {
    private double damage;
    private double fireRate;
    private int ammunitionCount;

    public Weapon(double damage, double fireRate) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.ammunitionCount = 0;
    }

    /**
     * Changes damage value.
     * @param   newValue    New damage value.
     */
    public void changeDamage(double newValue) {
        damage = newValue;
    }

    /**
     * Changes fire rate value.
     * @param   newValue    New fire rate value.
     */
    public void changeFireRate(double newValue) {
        fireRate = newValue;
    }

    /**
     * Sets ammunition count.
     * @param   value   Total ammunition count.
     */
    public void setAmmunitionCount(int value) {
        this.ammunitionCount = value;
    }

    /**
     * Changes ammunition count.
     * @param   value   Count of used or got ammunition.
     */
    public void changeAmmunitionCount(int value) {
        this.ammunitionCount += value;
    }

    /**
     * Shoots in direction of given point.
     * If bullet hits some object, returns it.
     * Returns null otherwise.
     * @param   y   Target Y coordinate.
     * @param   x   Target X coordinate.
     * @return  An object that bullet hit. null if object does not hit anything.
     */
    public BaseObject shoot(double y, double x) {
        Point shootingTarget = new Point((int)x, (int)y);
        /*
        * Bullet trajectory calculation.
        * Check is something got hit.
        * Return the object that got hit, null otherwise.
        * */
        return null;
    }
}
