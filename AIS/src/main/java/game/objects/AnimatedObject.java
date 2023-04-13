package game.objects;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for all animated objects.
 */
public abstract class AnimatedObject extends BaseObject {
    protected double[] velocity;
    protected double[] acceleration;
    protected double hp;
    protected AtomicBoolean isAlive;

    private void initVelocityAndAcceleration() {
        velocity = new double[2];
        velocity[0] = 0;
        velocity[1] = 0;
        acceleration = new double[2];
        acceleration[0] = 0;
        acceleration[1] = 0;
        isAlive = new AtomicBoolean(true);
    }

    public AnimatedObject() {
        super();
        hp = 1.0;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(double hp) {
        super();
        this.hp = hp;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(Rectangle objectSpace) {
        super(objectSpace);
        this.hp = 1.0;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(Rectangle objectSpace, double hp) {
        super(objectSpace);
        this.hp = hp;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(ImageStorage texture) {
        super(texture);
        this.hp = 1.0;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(ImageStorage texture, double hp) {
        super(texture);
        this.hp = hp;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(Rectangle objectSpace, ImageStorage texture) {
        super(objectSpace, texture);
        this.hp = 1.0;
        initVelocityAndAcceleration();
    }

    public AnimatedObject(Rectangle objectSpace, ImageStorage texture, double hp) {
        super(objectSpace, texture);
        this.hp = hp;
        initVelocityAndAcceleration();
    }

    /**
     * Changes position by (y, x).
     * @param   y   Change in Y coordinate.
     * @param   x   Change in X coordinate.
     */
    public void move(double y, double x) {
        this.objectSpace.changePosition(y, x);
    }

    /**
     * Starts playing animation.
     */
    public void animationPlay() {
        /*
        * Start playing animation.
        * */
    }

    /**
     * Plays next animation frame.
     */
    public void animationContinue() {
        /*
        * Play next animation frame.
        * */
    }

    /**
     * Stops playing animation.
     */
    public void animationStop() {
        /*
        * Stop playing animation.
        * */
    }

    /**
     * Change objects HP by given value.
     * @param   dHP     Change in HP.
     */
    public void changeHP(double dHP) {
        this.hp += dHP;
        if (this.hp <= 0) {
            die();
        }
    }

    /**
     * Sets objects HP.
     * @param   hp  New objects HP value.
     */
    public void setHP(double hp) {
        this.hp = hp;
    }

    /**
     * Returns objects HP.
     * @return  Objects HP.
     */
    public double getHP() {
        return hp;
    }

    /**
     * Sets objects HP to 0 and object dies.
     */
    public void die() {
        this.hp = 0;
        isAlive.set(false);
    }

    /**
     * Kills different object.
     * @param   object  Object to kill.
     */
    public void kill(AnimatedObject object) {
        object.die();
    }

    /**
     * Sets objects position to (x, y).
     * @param   y   New objects Y coordinate.
     * @param   x   New objects X coordinate.
     */
    public void setPosition(double y, double x) {
        objectSpace.setPosition(y, x);
    }

    /**
     * Return true if object is alive, false otherwise.
     * @return true if object is alive, false otherwise.
     */
    public boolean alive() {
        return isAlive.get();
    }
}
