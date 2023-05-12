package engine.tools;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Camera class that defines what to render on game map.
 */
public class Camera {
    private ImageStorage backgroundImage;
    BufferedImage croppedImage;
    Image scaledImage;
    private Rectangle viewArea;
    private Rectangle lastCallViewArea;
    private int[] panelSize;
    private int[] lastCallPanelSize;
    private final double[] cameraRatio;

    /**
     * Creates camera with given parameters.
     * @param   viewArea    Rectangle object, that defines viewing area.
     * @param   cameraRatio Two numbers that defines screen ratio ([16,9], [4,3], [21,9], etc.)
     */
    public Camera(Rectangle viewArea, double[] cameraRatio) {
        this.viewArea = viewArea;
        this.lastCallViewArea = new Rectangle(viewArea.getPositionX(), viewArea.getPositionY(), viewArea.getWidth(), viewArea.getHeight());
        this.cameraRatio = cameraRatio;
        this.backgroundImage = new ImageStorage("src/main/resources/img/default_background.png");
    }

    public void setPanelSize(int width, int height) {
        this.panelSize = new int[2];
        this.panelSize[0] = width;
        this.panelSize[1] = height;
        this.lastCallPanelSize = new int[2];
        this.lastCallPanelSize[0] = this.panelSize[0];
        this.lastCallPanelSize[1] = this.panelSize[1];
        updateViewArea();
    }

    /**
     * Sets camera viewing area.
     * @param   viewArea    Rectangle object, that defines viewing area.
     */
    public void setViewArea(Rectangle viewArea) {
        this.viewArea = viewArea;
        updateViewArea();
    }

    /**
     * Changes the position of camera.
     * @param   y   Change in Y coordinate.
     * @param   x   Change in X coordinate.
     */
    public void move(double x, double y) {
        viewArea.changePosition(x, y);
        if (viewArea.getPositionX() + viewArea.getWidth() >= backgroundImage.getImage().getWidth()) {
            viewArea.setPosition(backgroundImage.getImage().getWidth() - viewArea.getWidth()-1, viewArea.getPositionY());
        } else if (viewArea.getPositionX() < 0) {
            viewArea.setPosition(0, viewArea.getPositionY());
        }

        if (viewArea.getPositionY() + viewArea.getHeight() >= backgroundImage.getImage().getHeight()) {
            viewArea.setPosition(viewArea.getPositionX(), backgroundImage.getImage().getHeight() - viewArea.getHeight()-1);
        } else if (viewArea.getPositionY() < 0) {
            viewArea.setPosition(viewArea.getPositionX(), 0);
        }
        updateViewArea();
    }

    /**
     * Increasing or decreasing viewing area size.
     * @param   scale   Positive value. Decrease viewing area size if 0 < scale < 1. Increase viewing area size if scale > 1.
     */
    public void zoom(double scale) {
        double[] currentViewAreaSize = viewArea.getSize();
        viewArea.changeSize(currentViewAreaSize[0] * scale, currentViewAreaSize[1] * scale);
        updateViewArea();
    }

    public void setBackgroundImage(String imageFileName) {
        backgroundImage = new ImageStorage(imageFileName);
        updateViewArea();
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
    public double[] getCameraRatio() {
        return cameraRatio;
    }

    private void cropImage(int x, int y, int width, int height) {
        croppedImage = backgroundImage.getImage().getSubimage(x, y, width, height);
    }

    public void updateViewArea() {
        if (viewArea.getSize()[0] > backgroundImage.getImage().getWidth() && viewArea.getSize()[1] > backgroundImage.getImage().getHeight()) {
            cropImage((int)viewArea.getPositionX(), (int)viewArea.getPositionY(), backgroundImage.getImage().getWidth(), backgroundImage.getImage().getHeight());
        } else if (viewArea.getSize()[0] > backgroundImage.getImage().getWidth()) {
            cropImage((int)viewArea.getPositionX(), (int)viewArea.getPositionY(), backgroundImage.getImage().getWidth(), (int)viewArea.getHeight());
        } else if (viewArea.getSize()[1] > backgroundImage.getImage().getHeight()) {
            cropImage((int)viewArea.getPositionX(), (int)viewArea.getPositionY(), (int)viewArea.getWidth(), backgroundImage.getImage().getHeight());
        } else {
            cropImage((int)viewArea.getPositionX(), (int)viewArea.getPositionY(), (int)viewArea.getWidth(), (int)viewArea.getHeight());
        }
        scaledImage = croppedImage.getScaledInstance(panelSize[0], panelSize[1], Image.SCALE_FAST);
    }

    private boolean isThereChangesFromLastCall() {
        boolean returnValue = false;
        if (!viewArea.equals(lastCallViewArea)) {
            lastCallViewArea = viewArea;
            returnValue = true;
        }
        if (panelSize[0] != lastCallPanelSize[0]) {
            lastCallPanelSize[0] = panelSize[0];
            returnValue = true;
        }
        if (panelSize[1] != lastCallPanelSize[1]) {
            lastCallPanelSize[1] = panelSize[1];
            returnValue = true;
        }
        return returnValue;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(scaledImage, 0, 0, null);
    }
}
