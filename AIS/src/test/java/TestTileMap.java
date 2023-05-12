import engine.utils.ImageStorage;
import engine.utils.Logger;
import tilemap_editor.TileMap;

public class TestTileMap {
    public static void main(String[] args) {
        Logger logger = new Logger("GameLogger.log");
        TileMap tileMap = new TileMap();
        tileMap.setLogger(logger);
        ImageStorage chessImageStorage = new ImageStorage();
        chessImageStorage.addImageByName("src/main/resources/img/tileMaps/greenVillage/chest_closed.png");
        chessImageStorage.addImageByName("src/main/resources/img/tileMaps/greenVillage/chest_opened.png");
        tileMap.addTile("chest", chessImageStorage);
        tileMap.saveJSONFile();
        logger.stopLogging();
    }
}
