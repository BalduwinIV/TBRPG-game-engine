package engine.tools.tilemap;

import engine.utils.ImageStorage;

public class Tile {
    private String name;
    private int width;
    private int height;
    private ImageStorage sprite;

    public Tile(String name, int width, int height, ImageStorage sprite) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setWidth(int newWidth) {
        width = newWidth;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public void setSprite(ImageStorage sprite) {
        this.sprite = sprite;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageStorage getSprite() {
        return sprite;
    }
}
