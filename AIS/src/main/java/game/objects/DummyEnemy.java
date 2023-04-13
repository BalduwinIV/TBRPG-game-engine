package game.objects;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

/**
 * Dummy enemy class that is passive to player.
 */
public class DummyEnemy extends AnimatedObject implements Enemy {
    private BaseObject targetObject;
    private double[] targetPosition;

    public DummyEnemy() {
        super();
        targetPosition = new double[2];
    }

    public DummyEnemy(double hp) {
        super(hp);
        targetPosition = new double[2];
    }

    public DummyEnemy(Rectangle objectSpace) {
        super(objectSpace);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
    }

    public DummyEnemy(ImageStorage texture) {
        super(texture);
        targetPosition = new double[2];
    }

    public DummyEnemy(Rectangle objectSpace, double hp) {
        super(objectSpace, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
    }

    public DummyEnemy(ImageStorage texture, double hp) {
        super(texture, hp);
        targetPosition = new double[2];
    }

    public DummyEnemy(Rectangle objectSpace, ImageStorage texture) {
        super(objectSpace, texture);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
    }

    public DummyEnemy(Rectangle objectSpace, ImageStorage texture, double hp) {
        super(objectSpace, texture, hp);
        targetPosition = new double[2];
        targetPosition = objectSpace.getPosition();
    }

    /**
     * Sets a new target object to this dummy enemy.
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
