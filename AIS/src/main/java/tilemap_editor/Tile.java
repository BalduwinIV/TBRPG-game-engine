package tilemap_editor;

import engine.utils.ImageStorage;

public class Tile {
    private final String name;
    private final int width;
    private final int height;
    private final ImageStorage sprites;

    public Tile(String name, int width, int height, ImageStorage sprites) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.sprites = sprites;
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

    public ImageStorage getSprites() {
        return sprites;
    }
}
