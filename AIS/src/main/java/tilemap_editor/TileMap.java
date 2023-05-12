package tilemap_editor;

import engine.utils.ImageStorage;
import engine.utils.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TileMap {
    private Logger logger;
    private JSONArray tileMap;
    private final String JSONFilePath;

    public TileMap() {
        this.tileMap = new JSONArray();
        this.JSONFilePath = "src/main/resources/tileMap.json";
        loadTileMap(this.JSONFilePath);
    }

    public TileMap(String tileMapFilePath) {
        this.tileMap = new JSONArray();
        this.JSONFilePath = tileMapFilePath;
        loadTileMap(tileMapFilePath);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void loadTileMap(String tileMapFilePath) {
        File tileMapJSONFile = new File(tileMapFilePath);
        if (!tileMapJSONFile.exists()) {
            System.err.println("File with name \"" + tileMapFilePath + "\" cannot be opened.");
        } else {
            try {
                BufferedReader tileMapFileReader = new BufferedReader(new FileReader(tileMapJSONFile));
                String jsonLine = tileMapFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    tileMap = new JSONArray(jsonLine);
                } else {
                    tileMap = new JSONArray();
                }
                tileMapFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTileMapFile(String tileMapFilePath) {
        File tileMapJSONFile = new File(tileMapFilePath);
        if (tileMapJSONFile.exists()) {
            System.err.println("File \"" + tileMapFilePath + "\" already exists.");
        } else {
            tileMap = new JSONArray();
            try {
                FileWriter tileMapFileWriter = new FileWriter(tileMapJSONFile);
                tileMapFileWriter.write(tileMap.toString());
                tileMapFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addTile(String name, ImageStorage sprites) {
        for (int tileI = 0; tileI < tileMap.length(); tileI++) {
            if (name.equals(tileMap.getJSONObject(tileI).get("name"))) {
                logger.error(this, "Tile with name \"" + name + "\" already exists.");
                return false;
            }
        }

        JSONObject tileObject = new JSONObject();
        tileObject.put("name", name);
        tileObject.put("width", sprites.getImage().getWidth());
        tileObject.put("height", sprites.getImage().getHeight());
        JSONArray tileSprites = new JSONArray();
        for (int tileI = 0; tileI < sprites.getStorageSize(); tileI++) {
            tileSprites.put(sprites.getImageName(tileI));
        }
        tileObject.put("sprites", tileSprites);
        tileMap.put(tileObject);
        return true;
    }

    public void saveJSONFile() {
        File tileMapJSONFile = new File(this.JSONFilePath);
        if (!tileMapJSONFile.exists()) {
            logger.error(this, "File \"" + tileMapJSONFile + "\" does not exist.");
        } else {
            try {
                FileWriter tileMapFileWriter = new FileWriter(tileMapJSONFile);
                ArrayList<String> tileMapList = new ArrayList<>();
                for (int tileI = 0; tileI < tileMap.length(); tileI++) {
                    tileMapList.add(tileMap.get(tileI).toString());
                }
                tileMapFileWriter.write(Arrays.toString(tileMapList.toArray(new String[tileMapList.size()])));
                tileMapFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
