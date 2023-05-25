package engine.utils;

/**
 * Class for objects parameters and calculating collision between that objects.
 */
public class Rectangle {
    private final double[] position;
    private final double[] size;

    public Rectangle(double positionX, double positionY, double width, double height) {
        position = new double[2];
        position[0] = positionX;
        position[1] = positionY;
        size = new double[2];
        size[0] = width;
        size[1] = height;
    }

    public void setPosition(double x, double y) {
        position[0] = x;
        position[1] = y;
    }

    public void setSize(double width, double height) {
        size[0] = width;
        size[1] = height;
    }

    public void changePosition(double x, double y) {
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

    public double getPositionX() {
        return position[0];
    }

    public double getPositionY() {
        return position[1];
    }

    public double[] getSize() {
        return size;
    }

    public double getWidth() {
        return size[0];
    }

    public double getHeight() {
        return size[1];
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Rectangle other)) {
            return false;
        }

        return (position[0] == other.getPositionX()) &&
                (position[1] == other.getPositionY()) &&
                (size[0] == other.getWidth()) &&
                (size[1] == other.getHeight());
    }
}
