package engine.tools;

import engine.utils.Rectangle;

/**
 * Camera class that defines what to render on game map.
 */
public class Camera {
    private Rectangle viewArea;
    private final int[] cameraRatio;

    /**
     * Creates camera with given parameters.
     * @param   viewArea    Rectangle object, that defines viewing area.
     * @param   cameraRatio Two numbers that defines screen ratio (16:9, 4:3, 21:9, etc.)
     */
    public Camera(Rectangle viewArea, int[] cameraRatio) {
        this.viewArea = viewArea;
        this.cameraRatio = cameraRatio;
    }

    /**
     * Sets camera viewing area.
     * @param   viewArea    Rectangle object, that defines viewing area.
     */
    public void setViewArea(Rectangle viewArea) {
        this.viewArea = viewArea;
    }

    /**
     * Changes the position of camera.
     * @param   y   Change in Y coordinate.
     * @param   x   Change in X coordinate.
     */
    public void move(double y, double x) {
        viewArea.changePosition(y, x);
    }

    /**
     * Increasing or decreasing viewing area size.
     * @param   scale   Positive value. Decrease viewing area size if 0 < scale < 1. Increase viewing area size if scale > 1.
     */
    public void zoom(double scale) {
        double[] currentViewAreaSize = viewArea.getSize();
        viewArea.changeSize(currentViewAreaSize[0] * scale, currentViewAreaSize[1] * scale);
    }

    /**
     * Checks if some object is visible on camera.
     * @param   object  Rectangle that is going to be checked.
     * @return  true if object is visible on camera, false otherwise.
     */
    public boolean isInsideCameraView(Rectangle object) {
        return viewArea.checkCollision(object);
    }

    /**
     * Returns coordinates of top left viewing areas corner.
     * @return  Camera top left corners coordinates.
     */
    public double[] getAbsolutePosition() {
        return viewArea.getPosition();
    }

    /**
     * Returns coordinates of viewing areas center.
     * @return  Cameras center coordinates.
     */
    public double[] getCenterPosition() {
        return viewArea.getCenter();
    }

    /**
     * Return cameras screen size.
     * @return  Width and height of viewing area.
     */
    public double[] getSize() {
        return viewArea.getSize();
    }

    /**
     * Return rectangle that defines cameras viewing area.
     * @return  Rectangle that defines viewing area.
     */
    public Rectangle getViewArea() {
        return viewArea;
    }

    /**
     * Return Current camera ratio.
     * @return  Current camera ratio.
     */
    public int[] getCameraRatio() {
        return cameraRatio;
    }
}
