package engine.tools.tilemap;

import engine.utils.ImageStorage;
import engine.utils.JSONUtils;
import engine.utils.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 *  Tile manager class.
 */
public class TileMap {
    private final Logger logger;
    private JSONArray tileMap;
    private final ArrayList<Tile> tiles;
    private final String JSONFilePath;

    public TileMap(Logger logger) {
        this(logger, "src/main/resources/tileMap.json");
    }

    public TileMap(Logger logger, String tileMapFilePath) {
        this.logger = logger;
        this.tileMap = new JSONArray();
        this.tiles = new ArrayList<>();
        this.JSONFilePath = tileMapFilePath;
        loadTileMap(tileMapFilePath);
    }

    /**
     *  Load all known tiles from the JSON file.
     * @param tileMapFilePath JSON file name.
     */
    public void loadTileMap(String tileMapFilePath) {
        File tileMapJSONFile = new File(tileMapFilePath);
        if (!tileMapJSONFile.exists()) {
            logger.error(this, "File with name \"" + tileMapFilePath + "\" cannot be opened. Trying to create a new one.");
            createTileMapFile(tileMapFilePath);
        } else {
            try {
                BufferedReader tileMapFileReader = new BufferedReader(new FileReader(tileMapJSONFile));
                String jsonLine = tileMapFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    tileMap = new JSONArray(jsonLine);

                    for (int tileI = 0; tileI < tileMap.length(); tileI++) {
                        ImageStorage sprite = new ImageStorage(tileMap.getJSONObject(tileI).getString("sprite"));
                        tiles.add(new Tile(tileMap.getJSONObject(tileI).getString("name"),
                                tileMap.getJSONObject(tileI).getInt("width"),
                                tileMap.getJSONObject(tileI).getInt("height"),
                                sprite));
                    }
                } else {
                    tileMap = new JSONArray();
                }
                tileMapFileReader.close();
                logger.info(this, "TileMap \"" + tileMapFilePath + "\" has been successfully loaded.");
                JSONUtils.saveJSONFileCopy(tileMapFilePath, tileMap, logger);
            } catch (IOException e) {
                logger.error(this, "An error occurred while reading \"" + tileMapFilePath + "\" file.");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Creates a new empty JSON file.
     * @param tileMapFilePath JSON file name.
     */
    public void createTileMapFile(String tileMapFilePath) {
        File tileMapJSONFile = new File(tileMapFilePath);
        if (tileMapJSONFile.exists()) {
            logger.error(this, "File \"" + tileMapFilePath + "\" already exists.");
        } else {
            tileMap = new JSONArray();
            try {
                FileWriter tileMapFileWriter = new FileWriter(tileMapJSONFile);
                tileMapFileWriter.write(tileMap.toString());
                tileMapFileWriter.close();
                logger.info(this, "TileMap file \"" + tileMapFilePath + "\" has been successfully created.");
            } catch (IOException e) {
                logger.error(this, "An error occurred while creating \"" + tileMapFilePath + "\" file");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Adds tile to the local array.
     * @param name New tiles name.
     * @param sprite New tiles sprite.
     * @return True if tile has been added.
     */
    public boolean addTile(String name, ImageStorage sprite) {
        for (int tileI = 0; tileI < tileMap.length(); tileI++) {
            if (name.equals(tileMap.getJSONObject(tileI).getString("name"))) {
                logger.warning(this, "Tile with name \"" + name + "\" already exists.");
                return false;
            }
        }

        JSONObject tileObject = new JSONObject();
        tileObject.put("name", name);
        tileObject.put("width", sprite.getImage().getWidth());
        tileObject.put("height", sprite.getImage().getHeight());
        tileObject.put("sprite", sprite.getImageName(0));
        tileMap.put(tileObject);

        tiles.add(new Tile(name, sprite.getImage().getWidth(), sprite.getImage().getHeight(), sprite));
        logger.info(this, "A new tile with name \"" + name + "\" has been successfully added.");

        return true;
    }

    /**
     *  Renames tile.
     * @param previousName Tiles name.
     * @param newName New tiles name.
     * @return True if tile has been renamed. False otherwise.
     */
    public boolean renameTile(String previousName, String newName) {
        if (JSONUtils.renameObject(tileMap, logger, "Tile", previousName, newName)) {
            for (Tile tile : tiles) {
                if (tile.getName().equals(previousName)) {
                    tile.setName(newName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  Removes tile.
     * @param name Tiles name.
     * @return True if tile has been removed. False otherwise.
     */
    public boolean removeTile(String name) {
        if (JSONUtils.removeObject(tileMap, logger, "Tile", name)) {
            for (int tileI = 0; tileI < tiles.size(); tileI++) {
                if (tiles.get(tileI).getName().equals(name)) {
                    tiles.remove(tileI);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  Saves all changes to JSON file.
     * @return True if changes has been saved. False otherwise.
     */
    public boolean saveJSONFile() {
        return JSONUtils.saveJSONFile(this.JSONFilePath, tileMap, logger);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     *  Returns tile of given index.
     * @param index Tiles index.
     * @return Tile or null.
     */
    public Tile getTileByIndex(int index) {
        if (index < 0 || index >= tiles.size()) {
            logger.error(this, "Tile index (" + index + ") out of range.\n");
            return null;
        }
        return tiles.get(index);
    }

    /**
     *  Returns tile of given name.
     * @param name Tiles name.
     * @return Tile of null.
     */
    public Tile getTileByName(String name) {
        for (Tile tile : tiles) {
            if (tile.getName().equals(name)) {
                return tile;
            }
        }
        return null;
    }

    public int getTilesCount() {
        return tiles.size();
    }
}
