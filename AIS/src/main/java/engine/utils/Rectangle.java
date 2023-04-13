package engine.utils;

/**
 * Class for objects parameters and calculating collision between that objects.
 */
public class Rectangle {
    private double[] position;
    private double[] size;

    public Rectangle(double positionY, double positionX, double width, double height) {
        position = new double[2];
        position[0] = positionX;
        position[1] = positionY;
        size = new double[2];
        size[0] = width;
        size[1] = height;
    }

    public void setPosition(double y, double x) {
        position[0] = x;
        position[1] = y;
    }

    public void setSize(double width, double height) {
        size[0] = width;
        size[1] = height;
    }

    public void changePosition(double y, double x) {
        position[0] += x;
        position[1] += y;
    }

    public void changeSize(double width, double height) {
        size[0] += width;
        size[1] += height;
    }

    /**
     * @param   other   An object to check collision with.
     * @return  true if there is a collision, false otherwise.
     */
    public boolean checkCollision(Rectangle other) {
        /**
         * Checking collision;
         */
        return false;
    }

    /**
     * Method that calculates objects center coordinates and returns them.
     * @return  Coordinates of objects center.
     */
    public double[] getCenter() {
        double[] center = new double[2];
        center[0] = position[0] + size[0]*0.5;
        center[1] = position[1] + size[1]*0.5;
        return center;
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getSize() {
        return size;
    }
}
