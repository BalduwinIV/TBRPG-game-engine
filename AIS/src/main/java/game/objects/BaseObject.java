package game.objects;

import engine.utils.ImageStorage;
import engine.utils.Rectangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * General class for all objects in game.
 */
public class BaseObject {
    protected Rectangle objectSpace;
    protected ImageStorage texture;

    public BaseObject() {
        this.objectSpace = new Rectangle(0, 0, 0, 0);
        this.texture = new ImageStorage();
    }

    public BaseObject(Rectangle objectSpace) {
        this.objectSpace = objectSpace;
        this.texture = new ImageStorage();
    }

    public BaseObject(ImageStorage texture) {
        this.objectSpace = new Rectangle(0, 0, 0, 0);
        this.texture = texture;
    }

    public BaseObject(Rectangle objectSpace, ImageStorage texture) {
        this.objectSpace = objectSpace;
        this.texture = texture;
    }

    /**
     * Sets objects position.
     * @param   y   Objects new Y coordinate.
     * @param   x   Objects new X coordinate.
     */
    public void setPosition(double y, double x) {
        objectSpace.setPosition(y, x);
    }

    /**
     * Sets objects size.
     * @param   width   Objects new width.
     * @param   height  Objects new height.
     */
    public void setSize(double width, double height) {
        objectSpace.setSize(width, height);
    }

    /**
     * Sets image by given name as objects texture.
     * @param   imageName   Textures filename.
     */
    public void loadTextureByFilename(String imageName) {
        texture.addImageByName(imageName);
    }

    /**
     * Sets series of images as objects texture (as animation or skin).
     * @param   imageNames  Textures animation/skins filenames.
     */
    public void loadTexturesByFilename(ArrayList<String> imageNames) {
        texture.addImagesByName(imageNames);
    }

    /**
     * Sets image as objects texture.
     * @param   image       Texture.
     * @param   imageName   Textures name.
     */
    public void loadTextureByObject(BufferedImage image, String imageName) {
        texture.addImageByObject(image, imageName);
    }

    /**
     * Sets series of images as objects texture (as animation of skin).
     * @param   images      Textures.
     * @param   imageNames  Textures names.
     */
    public void loadTexturesByObject(ArrayList<BufferedImage> images, ArrayList<String> imageNames) {
        texture.addImagesByObject(images, imageNames);
    }

    /**
     * Returns objects position and size parameters as Rectangle object.
     * @return  Objects position and size parameters.
     */
    public Rectangle getObjectSpace() {
        return objectSpace;
    }

    /**
     * Returns objects top left corner position.
     * @return  Objects top left corner position.
     */
    public double[] getAbsolutePosition() {
        return objectSpace.getPosition();
    }

    /**
     * Returns objects center position.
     * @return  Objects center position.
     */
    public double[] getCenterPosition() {
        return objectSpace.getCenter();
    }

    /**
     * Returns objects size.
     * @return  Objects size.
     */
    public double[] getSize() {
        return objectSpace.getSize();
    }

    /**
     * Checks if there is a collision between this and other objects.
     * @param   other   Object to check collision with.
     * @return  true if collision is detected, false otherwise.
     */
    public boolean checkCollision(BaseObject other) {
        return objectSpace.checkCollision(other.getObjectSpace());
    }

    /**
     * Returns objects texture.
     * @return  Objects texture.
     */
    public ImageStorage getTexture() {
        return texture;
    }
}
