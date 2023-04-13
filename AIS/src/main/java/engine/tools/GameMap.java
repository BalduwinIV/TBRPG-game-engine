package engine.tools;

import game.objects.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Game map class.
 */
public class GameMap {
    private int[] mapSize;
    private String currentMap;
    private List<BaseObject> objects;

    public GameMap() {
        mapSize = new int[2];
        objects = new ArrayList<>();
    }

    public GameMap(String currentMap) {
        this.currentMap = currentMap;
        objects = new ArrayList<>();
        loadMap(currentMap);
    }

    /**
     * Loads a game map.
     * @param   map     Game map to load.
     */
    public void loadMap(String map) {
        currentMap = map;
        /*
        * Load map.
        * Update mapSize value.
        * Load objects.
        * */
    }

    /**
     * Sets a new map size.
     * @param   width   Maps width.
     * @param   height  Maps height.
     */
    public void setMapSize(int width, int height) {
        mapSize[0] = width;
        mapSize[1] = height;
    }

    /**
     * Returns list of objects on a map.
     * @return  A list of objects in a map.
     */
    public List<BaseObject> getObjects() {
        return objects;
    }

    /**
     * Returns a map size.
     * @return  Map size.
     */
    public int[] getMapSize() {
        return mapSize;
    }

    /**
     * Returns current maps name.
     * @return  Current maps name.
     */
    public String getCurrentMap() {
        return currentMap;
    }
}
