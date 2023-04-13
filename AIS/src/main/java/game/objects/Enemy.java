package game.objects;

/**
 * Enemy classes interface.
 */
public interface Enemy {
    /**
     * Sets new target object to enemy.
     * @param   targetObject    Target object.
     */
    void setTargetObject(BaseObject targetObject);

    /**
     * Sets new target objects position.
     * @param   y   Target Y coordinate.
     * @param   x   Target X coordinate.
     */
    void setTargetPosition(double y, double x);
}
