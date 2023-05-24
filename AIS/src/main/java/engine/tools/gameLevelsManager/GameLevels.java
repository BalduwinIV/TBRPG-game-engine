package engine.tools.gameLevelsManager;

import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;
import engine.tools.characterManager.Character;
import engine.tools.itemsManager.HealManager.Heal;
import engine.tools.itemsManager.Inventory;
import engine.tools.itemsManager.WeaponManager.Weapon;
import engine.utils.ImageStorage;
import engine.utils.JSONUtils;
import engine.utils.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.*;
import engine.tools.tilemap.Tile;
import engine.tools.tilemap.TileMap;

/**
 * Game levels class.
 */
public class GameLevels {
    String[] reservedNames = {"-- choose the level --", "-- add new level --"};
    private final ModelControlComponents modelControlComponents;
    private final Logger logger;
    private final TileMap tileMap;
    private final String gameLevelsFileName;
    private JSONArray gameLevels;
    private final AtomicBoolean levelIsLoaded;
    private String currentLevelName;
    private int[] currentLevelSize;
    private int[] currentLevelSpriteSize;
    private Tile[][] currentLevelTiles;
    private AtomicBoolean characterIsSelected;
    private int[] selectedCharacterPosition;
    private boolean[][] selectedCharacterMovementArea;
    private boolean[][] currentLevelCollisions;
    private Character[][] currentLevelCharacters;
    private int enemyTotalCount;
    private int allyTotalCount;

    public GameLevels(ModelControlComponents modelControlComponents) {
        this(modelControlComponents, "src/main/resources/levels.json");
    }

    public GameLevels(ModelControlComponents modelControlComponents, String gameLevelsFileName) {
        this.modelControlComponents = modelControlComponents;
        this.logger = modelControlComponents.getEngineLogger();
        this.tileMap = modelControlComponents.getTileMap();
        this.gameLevelsFileName = gameLevelsFileName;
        this.levelIsLoaded = new AtomicBoolean(false);
        loadLevelsFromFile(gameLevelsFileName);
    }

    /**
     *  Loads levels information from file.
     * @param gameLevelsFileName JSON file name.
     */
    public void loadLevelsFromFile(String gameLevelsFileName) {
        File levelsJSONFile = new File(gameLevelsFileName);
        if (!levelsJSONFile.exists()) {
            logger.warning(this, "File \"" + gameLevelsFileName + "\" does not exist. Creating a new one.");
            createLevelsFile(gameLevelsFileName);
        } else {
            try {
                BufferedReader levelsFileReader = new BufferedReader(new FileReader(levelsJSONFile));
                String jsonLine = levelsFileReader.readLine();
                if (Objects.nonNull(jsonLine)) {
                    gameLevels = new JSONArray(jsonLine);
                } else {
                    gameLevels = new JSONArray();
                }
                levelsFileReader.close();
                JSONUtils.saveJSONFileCopy(gameLevelsFileName, gameLevels, logger);
            } catch (IOException e) {
                logger.error(this, "An error occurred while loading levels from \"" + gameLevelsFileName + "\" file.");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Created empty levels JSON file.
     * @param gameLevelsFileName Filename.
     */
    public void createLevelsFile(String gameLevelsFileName) {
        File levelsJSONFile = new File(gameLevelsFileName);
        if (levelsJSONFile.exists()) {
            logger.warning(this, "File \"" + gameLevelsFileName + "\" already exists.");
        } else {
            gameLevels = new JSONArray();
            try {
                FileWriter levelFileWriter = new FileWriter(levelsJSONFile);
                levelFileWriter.write(gameLevels.toString());
                levelFileWriter.close();
                logger.info(this, "Game levels file \"" + gameLevelsFileName + "\" has been successfully created.");
            } catch (IOException e) {
                logger.error(this, "An error occurred while creating \"" + gameLevelsFileName + "\" file.");
                e.printStackTrace();
            }
        }
    }

    /**
     *  Loads information about given level to local variables.
     * @param name Levels name.
     * @return True if level has been loaded. False otherwise.
     */
    public boolean loadLevel(String name) {
        for (int levelI = 0; levelI < gameLevels.length(); levelI++) {
            if (gameLevels.getJSONObject(levelI).getString("name").equals(name)) {
                JSONObject currentLevel = gameLevels.getJSONObject(levelI);
                currentLevelName = name;
                currentLevelSize = new int[2];
                currentLevelSize[0] = currentLevel.getInt("levelWidth");
                currentLevelSize[1] = currentLevel.getInt("levelHeight");
                currentLevelSpriteSize = new int[2];
                currentLevelSpriteSize[0] = currentLevel.getInt("spriteWidth");
                currentLevelSpriteSize[1] = currentLevel.getInt("spriteHeight");
                currentLevelTiles = new Tile[currentLevelSize[1]][currentLevelSize[0]];
                JSONArray currentLevelSprites = currentLevel.getJSONArray("sprites");
                for (int y = 0; y < currentLevelSize[1]; y++) {
                    JSONArray currentLevelSpritesRow = currentLevelSprites.getJSONArray(y);
                    for (int x = 0; x < currentLevelSize[0]; x++) {
                        currentLevelTiles[y][x] = tileMap.getTileByName(currentLevelSpritesRow.getString(x));
                        if (!Objects.nonNull(currentLevelTiles[y][x])) {
                            currentLevelTiles[y][x] = new Tile("", currentLevelSpriteSize[0], currentLevelSpriteSize[1], new ImageStorage("src/main/resources/img/tileMaps/null_tile.png"));
                        }
                    }
                }

                characterIsSelected = new AtomicBoolean(false);
                selectedCharacterPosition = new int[2];
                selectedCharacterMovementArea = new boolean[currentLevelSize[1]][currentLevelSize[0]];
                currentLevelCharacters = new Character[currentLevelSize[1]][currentLevelSize[0]];
                JSONObject currentLevelCharactersObject = currentLevel.getJSONObject("characters");
                JSONArray enemyCharacters = currentLevelCharactersObject.getJSONArray("enemyCharacters");
                enemyTotalCount = enemyCharacters.length();
                for (int enemyI = 0; enemyI < enemyTotalCount; enemyI++) {
                    JSONObject enemyObject = enemyCharacters.getJSONObject(enemyI).getJSONObject("character");
                    Character enemy = new Character(enemyObject.getString("name"), new ImageStorage(enemyObject.getString("sprite")));
                    enemy.setCharacterType(enemyObject.getString("characterType"));
                    enemy.setBaseStats(enemyObject.getInt("level"), enemyObject.getInt("exp"),
                            enemyObject.getInt("baseHP"), enemyObject.getInt("baseDMG"),
                            enemyObject.getInt("hitRate"), enemyObject.getInt("criticalHitRate"),
                            enemyObject.getInt("avoidRate"), enemyObject.getInt("movement"));
                    enemy.setLevelDependentStats(enemyObject.getInt("strength"),
                            enemyObject.getInt("magic"), enemyObject.getInt("skill"),
                            enemyObject.getInt("speed"), enemyObject.getInt("luck"),
                            enemyObject.getInt("defence"), enemyObject.getInt("resistance"));

                    Inventory charactersInventory = new Inventory();
                    JSONArray inventoryArray = enemyObject.getJSONArray("inventory");
                    for (int itemI = 0; itemI < inventoryArray.length(); itemI++) {
                        JSONObject itemObject = inventoryArray.getJSONObject(itemI).getJSONObject("item");
                        if (itemObject.getString("itemType").equals("weapon")) {
                            Weapon weaponToAdd = new Weapon(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
                            weaponToAdd.setWeaponClass(itemObject.getString("weaponClass"));
                            weaponToAdd.setDescription(itemObject.getString("description"));
                            weaponToAdd.setStats(itemObject.getInt("might"), itemObject.getInt("hitRate"), itemObject.getInt("criticalHitRate"), itemObject.getInt("range"));
                            weaponToAdd.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
                            charactersInventory.addItem(weaponToAdd, inventoryArray.getJSONObject(itemI).getInt("amount"));
                        } else if (itemObject.getString("itemType").equals("heal")) {
                            Heal healToAdd = new Heal(itemObject.getString("name"), new ImageStorage(itemObject.getString("sprite")));
                            healToAdd.setDescription(itemObject.getString("description"));
                            healToAdd.setHealAmount(itemObject.getInt("healAmount"));
                            healToAdd.setMaxAmountInInventory(itemObject.getInt("maxAmount"));
                            charactersInventory.addItem(healToAdd, inventoryArray.getJSONObject(itemI).getInt("amount"));
                        }
                    }
                    enemy.setInventory(charactersInventory);

                    currentLevelCharacters[enemyCharacters.getJSONObject(enemyI).getInt("y")][enemyCharacters.getJSONObject(enemyI).getInt("x")] = enemy;
                }

                if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
                    ArrayList<Character> characters = modelControlComponents.getSaveDataManager().getCharacters();
                    JSONArray allyCharactersPositions = currentLevelCharactersObject.getJSONArray("allyCharactersPositions");
                    allyTotalCount = 0;
                    for (int allyI = 0; allyI < allyCharactersPositions.length(); allyI++) {
                        if (allyI < characters.size()) {
                            allyTotalCount++;
                            currentLevelCharacters[allyCharactersPositions.getJSONObject(allyI).getInt("y")][allyCharactersPositions.getJSONObject(allyI).getInt("x")] = characters.get(allyI);
                        } else {
                            break;
                        }
                    }
                } else {
                    Character allyCharacter = new Character("", new ImageStorage("src/main/resources/img/tileMaps/ally_tile_filter.png"));
                    allyCharacter.setCharacterType("ally");
                    JSONArray allyCharactersPositions = currentLevelCharactersObject.getJSONArray("allyCharactersPositions");
                    allyTotalCount = 0;
                    for (int allyI = 0; allyI < allyCharactersPositions.length(); allyI++) {
                        allyTotalCount++;
                        currentLevelCharacters[allyCharactersPositions.getJSONObject(allyI).getInt("y")][allyCharactersPositions.getJSONObject(allyI).getInt("x")] = allyCharacter;
                    }
                }

                currentLevelCollisions = new boolean[currentLevelSize[1]][currentLevelSize[0]];
                JSONArray collisionsArray = currentLevel.getJSONArray("collisions");
                for (int collisionI = 0; collisionI < collisionsArray.length(); collisionI++) {
                    JSONObject collisionObject = collisionsArray.getJSONObject(collisionI);
                    currentLevelCollisions[collisionObject.getInt("y")][collisionObject.getInt("x")] = true;
                }

                levelIsLoaded.set(true);

                return true;
            }
        }
        return false;
    }

    public int getAllyTotalCount() {
        return allyTotalCount;
    }

    public int getEnemyTotalCount() {
        return enemyTotalCount;
    }

    /**
     *  Adds tile to the loaded level.
     * @param newTile Tile to add.
     * @return True if tile has been added. False otherwise.
     */
    public boolean addTileToLevel(Tile newTile) {
        if (!levelIsLoaded()) {
            logger.warning(this, "Level is not loaded.");
            return false;
        }
        for (int y = 0; y < currentLevelSize[1]; y++) {
            for (int x = 0; x < currentLevelSize[0]; x++) {
                if (currentLevelTiles[y][x].getName().equals("")) {
                    currentLevelTiles[y][x] = newTile;
                    logger.info(this, "New tile \"" + newTile.getName() + "\" has been added to the level.");
                    return true;
                }
            }
        }

        logger.error(this, "There is no space on the level to add a new tile.");
        return false;
    }

    /**
     *  Removes tile from the current level.
     * @param xPos Tiles x position.
     * @param yPos Tiles y position.
     * @return True if tile has been removed. False otherwise.
     */
    public boolean removeTileFromLevel(int xPos, int yPos) {
        if (xPos < 0 || xPos >= currentLevelSize[0] || yPos < 0 || yPos >= currentLevelSize[1]) {
            logger.error(this, "Tile index out of range: [" + xPos + ", " + yPos + "].");
            return false;
        }
        logger.info(this, "Tile \"" + currentLevelTiles[yPos][xPos].getName() + "\" has been removed from level.");
        currentLevelTiles[yPos][xPos] = new Tile("", currentLevelSpriteSize[0], currentLevelSpriteSize[1], new ImageStorage("src/main/resources/img/tileMaps/null_tile.png"));
        return true;
    }

    /**
     *  Moves tile from one place to another.
     * @param from Tiles previous position.
     * @param to Tiles new position.
     * @return True if tile has been moved. False otherwise.
     */
    public boolean moveTile(int[] from, int[] to) {
        if ((from[0] < 0 || to[0] < 0) || (from[0] >= currentLevelSize[0] || to[0] >= currentLevelSize[0]) || (from[1] < 0 || to[1] < 0) || (from[1] >= currentLevelSize[1] || to[1] >= currentLevelSize[1])) {
            logger.error(this, "Cannot move tile to given place: [" + to[0] + ", " + to[1] + "].");
            return false;
        }
        if (!currentLevelTiles[to[1]][to[0]].getName().equals("")) {
            logger.warning(this, "Tile collision detected.");
            return false;
        }
        currentLevelTiles[to[1]][to[0]] = currentLevelTiles[from[1]][from[0]];
        currentLevelTiles[from[1]][from[0]] = new Tile("", currentLevelSpriteSize[0], currentLevelSpriteSize[1], new ImageStorage("src/main/resources/img/tileMaps/null_tile.png"));
        logger.info(this, "Tile \"" + getCurrentLevelTile(to[0], to[1]).getName() + "\" has been successfully moved from [" + from[0] + ", " + from[1] + "] to [" + to[0] + ", " + to[1] + "].");
        return true;
    }

    /**
     *  Adds an ally character placeholder to the current level.
     * @param x Place x coordinates.
     * @param y Place y coordinates.
     * @return True if placeholder has been added. False otherwise.
     */
    public boolean addAllyCharacterPlaceholderToLevel(int x, int y) {
        if (!levelIsLoaded()) {
            logger.warning(this, "Level is not loaded.");
            return false;
        }
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Character position index out of range: [" + x + ", " + y + "].");
            return false;
        }
        if (currentLevelCollisions[y][x]) {
            logger.warning(this, "It is not possible to put character on collision.");
            return false;
        }
        Character allyCharacterPlaceholder = new Character("", new ImageStorage("src/main/resources/img/tileMaps/ally_tile_filter.png"));
        allyCharacterPlaceholder.setCharacterType("ally");
        currentLevelCharacters[y][x] = allyCharacterPlaceholder;
        logger.info(this, "Character \"" + allyCharacterPlaceholder.getName() + "\" has been successfully added to the level on position [" + x + ", " + y + "].");
        return true;
    }

    /**
     *  Adds character to current level.
     * @param character Characters to add.
     * @param xPos X position to add.
     * @param yPos Y position to add.
     * @return True if character has been added. False otherwise.
     */
    public boolean addCharacterToLevel(Character character, int xPos, int yPos) {
        if (!levelIsLoaded()) {
            logger.warning(this, "Level is not loaded.");
            return false;
        }
        if (xPos < 0 || xPos >= currentLevelSize[0] || yPos < 0 || yPos >= currentLevelSize[1]) {
            logger.error(this, "Character position index out of range: [" + xPos + ", " + yPos + "].");
            return false;
        }
        if (currentLevelCollisions[yPos][xPos]) {
            logger.warning(this, "It is not possible to put character on collision.");
            return false;
        }
        currentLevelCharacters[yPos][xPos] = character;
        logger.info(this, "Character \"" + character.getName() + "\" has been successfully added to the level on position [" + xPos + ", " + yPos + "].");
        return true;
    }

    /**
     *  Select character for further actions.
     * @param x Characters x position.
     * @param y Characters y position.
     * @return True if character has been selected. False otherwise.
     */
    public boolean selectCharacter(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Position is out of range: [" + x + ", " + y + "].");
            return false;
        }

        if (!Objects.nonNull(currentLevelCharacters[y][x])) {
            return false;
        }

        characterIsSelected.set(true);
        selectedCharacterPosition[0] = x;
        selectedCharacterPosition[1] = y;
        if (!currentLevelCharacters[y][x].hasMoved()) {
            fillSelectedCharacterMovementArea(x, y);
        }
        return true;
    }

    /**
     *  Checks if selected character can move to given position.
     * @param x Target position x.
     * @param y Target position y.
     * @return True if selected character can move to given position. False otherwise.
     */
    public boolean selectedCharacterCanMove(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Coordinates out of range: [" + x + ", " + y + "].");
            return false;
        }
        return selectedCharacterMovementArea[y][x];
    }

    /**
     *  Fills area on which character can move.
     * @param x Characters position x.
     * @param y Characters position y.
     */
    public void fillSelectedCharacterMovementArea(int x, int y) {
        fillMovementAreaUp(x, y, currentLevelCharacters[y][x].getMovement());
        fillMovementAreaDown(x, y, currentLevelCharacters[y][x].getMovement());
    }

    /**
     *  Fills area on which action is possible to do.
     * @param x Source position x.
     * @param y Source position y.
     * @param range Action range.
     */
    public void fillMovementArea(int x, int y, int range) {
        fillMovementAreaUp(x, y, range);
        fillMovementAreaDown(x, y, range);
    }

    /**
     *  Recursively fill area upper then source.
     * @param x Tiles position x.
     * @param y Tiles position y.
     * @param movesLeft How many tiles left.
     */
    private void fillMovementAreaUp(int x, int y, int movesLeft) {
        if (movesLeft >= 0 && (x >= 0 && x < currentLevelSize[0] && y >= 0 && y < currentLevelSize[1])) {
            selectedCharacterMovementArea[y][x] = true;
            if (x >= 1 && !currentLevelCollisions[y][x-1]) fillMovementAreaUp(x-1, y, movesLeft-1);
            if (y >= 1 && !currentLevelCollisions[y-1][x]) fillMovementAreaUp(x, y-1, movesLeft-1);
            if (x < currentLevelSize[0]-1 && !currentLevelCollisions[y][x+1]) fillMovementAreaUp(x+1, y, movesLeft-1);
        }
    }

    /**
     *  Recursively fill area lower than source.
     * @param x Tiles position x.
     * @param y Tiles position y.
     * @param movesLeft How many tiles left.
     */
    private void fillMovementAreaDown(int x, int y, int movesLeft) {
        if (movesLeft >= 0 && (x >= 0 && x < currentLevelSize[0] && y >= 0 && y < currentLevelSize[1])) {
            selectedCharacterMovementArea[y][x] = true;
            if (x >= 1 && !currentLevelCollisions[y][x-1]) fillMovementAreaDown(x-1, y, movesLeft-1);
            if (y < currentLevelSize[1]-1 && !currentLevelCollisions[y+1][x]) fillMovementAreaDown(x, y+1, movesLeft-1);
            if (x < currentLevelSize[0]-1 && !currentLevelCollisions[y][x+1]) fillMovementAreaDown(x+1, y, movesLeft-1);
        }
    }

    /**
     *  Checks if some character is selected.
     * @return True if there is selected character. False otherwise.
     */
    public boolean characterIsSelected() {
        return characterIsSelected.get();
    }

    /**
     *  Returns selected character position.
     * @return Selected characters position.
     */
    public int[] getSelectedCharacterPosition() {
        return selectedCharacterPosition;
    }

    /**
     *  Returns selected character.
     * @return Selected character.
     */
    public Character getSelectedCharacter() {
        return currentLevelCharacters[selectedCharacterPosition[1]][selectedCharacterPosition[0]];
    }

    /**
     *  Resets selection to selected character.
     */
    public void deselectCharacter() {
        characterIsSelected.set(false);
        clearMovementArea();
    }

    /**
     *  Clears movement area for selected character.
     */
    public void clearMovementArea() {
        for (boolean[] rows : selectedCharacterMovementArea) {
            Arrays.fill(rows, false);
        }
    }

    /**
     *  Returns current movement area.
     * @return Movement area.
     */
    public boolean[][] getSelectedCharacterMovementArea() {
        return selectedCharacterMovementArea;
    }

    /**
     *  Moves character if possible.
     * @param previousX Characters x position.
     * @param previousY Characters y position.
     * @param newX New characters x position.
     * @param newY New characters y position.
     * @return True if character has been moved. False otherwise.
     */
    public boolean moveCharacter(int previousX, int previousY, int newX, int newY) {
        if (previousX < 0 || previousX >= currentLevelSize[0] || previousY < 0 || previousY >= currentLevelSize[1] ||
            newX < 0 || newX >= currentLevelSize[0] || newY < 0 || newY >= currentLevelSize[1]) {
            logger.error(this, "Position is out of range: [" + previousX + ", " + previousY + "] -> [" + newX + ", " + newY + "].");
            return false;
        }

        if (Objects.nonNull(currentLevelCharacters[newY][newX])) {
            return false;
        }

        if (currentLevelCollisions[newY][newX]) {
            logger.warning(this, "Collision detected: [" + newX + ", " + newY + "].");
            return false;
        }

        currentLevelCharacters[newY][newX] = currentLevelCharacters[previousY][previousX];
        currentLevelCharacters[previousY][previousX] = null;
        logger.info(this, "Character has been successfully moved from [" + previousX + ", " + previousY + "] to [" + newX + ", " + newY + "].");
        return true;
    }

    /**
     *  Removes character on given position.
     * @param x Characters x position.
     * @param y Characters y position.
     * @return True if character has been removed. False otherwise.
     */
    public boolean removeCharacter(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Position is out of range: [" + x + ", " + y + "].");
            return false;
        }

        currentLevelCharacters[y][x] = null;
        logger.info(this, "Character has been successfully removed.");
        return true;
    }

    /**
     *  Adds collision to the current level.
     * @param x Collision x position.
     * @param y Collision y position.
     * @return True if collision has been added.
     */
    public boolean addCollision(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Collision position out of range: [" + x + ", " + y + "].");
            return false;
        }

        currentLevelCollisions[y][x] = !currentLevelCollisions[y][x];
        return true;
    }

    /**
     *  Checks if there is collision on given position.
     * @param x Tiles x position.
     * @param y Tiles y position.
     * @return True if there is collision. False otherwise.
     */
    public boolean checkCollision(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Collision position out of range: [" + x + ", " + y + "].");
            return false;
        }

        return currentLevelCollisions[y][x];
    }

    /**
     *  Removes collision on given position from the current level.
     * @param x Collision x position.
     * @param y Collision y position.
     * @return True if collision has been removed. False otherwise.
     */
    public boolean removeCollision(int x, int y) {
        if (x < 0 || x >= currentLevelSize[0] || y < 0 || y >= currentLevelSize[1]) {
            logger.error(this, "Collision position out of range: [" + x + ", " + y + "].");
            return false;
        }

        currentLevelCollisions[y][x] = false;
        return true;
    }

    /**
     *  Adds a new level.
     * @param name Levels name.
     * @param spriteWidth Tiles sprite width.
     * @param spriteHeight Tiles sprite height.
     * @param levelWidth Levels width in tiles.
     * @param levelHeight Levels height in tiles.
     * @return True if level has been added. False otherwise.
     */
    public boolean addLevel(String name, int spriteWidth, int spriteHeight, int levelWidth, int levelHeight) {
        for (String reservedName : reservedNames) {
            if (name.equals(reservedName)) {
                logger.error(this, "This name is reserved!");
                return false;
            }
        }
        for (int levelIndex = 0; levelIndex < gameLevels.length(); levelIndex++) {
            JSONObject level = gameLevels.getJSONObject(levelIndex);
            if (name.equals(level.get("name"))) {
                logger.error(this, "Level with name \"" + name + "\" already exists.");
                return false;
            }
        }
        JSONObject newLevel = new JSONObject();
        newLevel.put("name", name);

        newLevel.put("spriteWidth", spriteWidth);
        newLevel.put("spriteHeight", spriteHeight);

        newLevel.put("levelWidth", levelWidth);
        newLevel.put("levelHeight", levelHeight);
        JSONArray sprites = new JSONArray();
        for (int y = 0; y < levelHeight; y++) {
            JSONArray spritesRow = new JSONArray();
            for (int x = 0; x < levelWidth; x++) {
                spritesRow.put("");
            }
            sprites.put(spritesRow);
        }
        newLevel.put("sprites", sprites);

        JSONObject characters = new JSONObject();
        JSONArray allyCharacters = new JSONArray();
        JSONArray enemyCharacters = new JSONArray();
        characters.put("allyCharactersPositions", allyCharacters);
        characters.put("enemyCharacters", enemyCharacters);
        newLevel.put("characters", characters);
        JSONArray collisions = new JSONArray();
        newLevel.put("collisions", collisions);
        gameLevels.put(newLevel);

        logger.info(this, "New level \"" + name + "\" with parameters: {spriteWidth=" + spriteWidth + ", spriteHeight=" + spriteHeight + ", levelWidth=" + levelWidth + ", levelHeight=" + levelHeight + "} has been added.");
        return true;
    }

    /**
     *  Renames level.
     * @param previousName Previous levels name.
     * @param newName New levels name.
     * @return True if level has been renamed. False otherwise.
     */
    public boolean renameLevel(String previousName, String newName) {
        for (String reservedName : reservedNames) {
            if (newName.equals(reservedName)) {
                logger.error(this, "This name is reserved!");
                return false;
            }
        }
        return JSONUtils.renameObject(gameLevels, logger, "Level", previousName, newName);
    }

    /**
     *  Removes level.
     * @param name Levels name.
     * @return True if level has been removed. False otherwise.
     */
    public boolean removeLevel(String name) {
        levelIsLoaded.set(false);
        return JSONUtils.removeObject(gameLevels, logger, "Level", name);
    }

    /**
     *  Saves up-to-date version of the level to JSON file.
     * @return True if data has been saved. False otherwise.
     */
    public boolean saveJSONFile() {
        if (levelIsLoaded.get()) {
            for (int levelI = 0; levelI < gameLevels.length(); levelI++) {
                JSONObject level = gameLevels.getJSONObject(levelI);
                if (level.getString("name").equals(currentLevelName)) {
                    level.put("levelWidth", currentLevelSize[0]);
                    level.put("levelHeight", currentLevelSize[1]);
                    JSONArray levelSprites = level.getJSONArray("sprites");
                    for (int y = 0; y < currentLevelSize[1]; y++) {
                        JSONArray levelSpritesRow = levelSprites.getJSONArray(y);
                        for (int x = 0; x < currentLevelSize[0]; x++) {
                            levelSpritesRow.put(x, currentLevelTiles[y][x].getName());
                        }
                    }
                    JSONObject characters = new JSONObject();
                    JSONArray allyCharactersPositions = new JSONArray();
                    JSONArray enemyCharacters = new JSONArray();
                    JSONArray collisionsArray = new JSONArray();
                    for (int y = 0; y < currentLevelSize[1]; y++) {
                        for (int x = 0; x < currentLevelSize[0]; x++) {
                            if (Objects.nonNull(currentLevelCharacters[y][x])) {
                                if (currentLevelCharacters[y][x].getCharacterType().equals("ally"))  {
                                    JSONObject allyCharacterPosition = new JSONObject();
                                    allyCharacterPosition.put("x", x);
                                    allyCharacterPosition.put("y", y);
                                    allyCharactersPositions.put(allyCharacterPosition);
                                } else if (currentLevelCharacters[y][x].getCharacterType().equals("enemy")) {
                                    JSONObject enemyCharacter = new JSONObject();
                                    enemyCharacter.put("x", x);
                                    enemyCharacter.put("y", y);

                                    JSONObject character = new JSONObject();

                                    character.put("characterType", currentLevelCharacters[y][x].getCharacterType());

                                    character.put("name", currentLevelCharacters[y][x].getName());
                                    character.put("width", currentLevelCharacters[y][x].getWidth());
                                    character.put("height", currentLevelCharacters[y][x].getHeight());
                                    character.put("sprite", currentLevelCharacters[y][x].getSprite().getImageName(0));

                                    character.put("baseHP", currentLevelCharacters[y][x].getBaseHP());
                                    character.put("baseDMG", currentLevelCharacters[y][x].getBaseDMG());
                                    character.put("hitRate", currentLevelCharacters[y][x].getHitRate());
                                    character.put("criticalHitRate", currentLevelCharacters[y][x].getCriticalHitRate());
                                    character.put("avoidRate", currentLevelCharacters[y][x].getAvoidRate());

                                    character.put("level", currentLevelCharacters[y][x].getLevel());
                                    character.put("exp", currentLevelCharacters[y][x].getExp());
                                    character.put("strength", currentLevelCharacters[y][x].getStrength());
                                    character.put("magic", currentLevelCharacters[y][x].getMagic());
                                    character.put("skill", currentLevelCharacters[y][x].getSkill());
                                    character.put("speed", currentLevelCharacters[y][x].getSpeed());
                                    character.put("luck", currentLevelCharacters[y][x].getLuck());
                                    character.put("defence", currentLevelCharacters[y][x].getDefence());
                                    character.put("resistance", currentLevelCharacters[y][x].getResistance());

                                    character.put("movement", currentLevelCharacters[y][x].getMovement());

                                    JSONArray inventoryArray = new JSONArray();
                                    Inventory inventory = currentLevelCharacters[y][x].getInventory();
                                    for (int itemI = 0; itemI < inventory.getInventorySize(); itemI++) {
                                        JSONObject inventoryItemObject = new JSONObject();

                                        JSONObject itemObject = new JSONObject();
                                        if (inventory.getInventoryItem(itemI).getItem().getItemType().equals("weapon")) {
                                            Weapon weapon = (Weapon) inventory.getInventoryItem(itemI).getItem();
                                            itemObject.put("itemType", "weapon");
                                            itemObject.put("name", weapon.getName());
                                            itemObject.put("sprite", weapon.getSprite().getImageName(0));
                                            itemObject.put("description", weapon.getDescription());
                                            itemObject.put("weaponClass", weapon.getWeaponClass());
                                            itemObject.put("might", weapon.getMight());
                                            itemObject.put("hitRate", weapon.getHitRate());
                                            itemObject.put("criticalHitRate", weapon.getCriticalHitRate());
                                            itemObject.put("range", weapon.getRange());
                                            itemObject.put("maxAmount", weapon.getMaxAmountInInventory());
                                        } else if (inventory.getInventoryItem(itemI).getItem().getItemType().equals("heal")) {
                                            Heal heal = (Heal) inventory.getInventoryItem(itemI).getItem();
                                            itemObject.put("itemType", "heal");
                                            itemObject.put("name", heal.getName());
                                            itemObject.put("sprite", heal.getSprite().getImageName(0));
                                            itemObject.put("description", heal.getDescription());
                                            itemObject.put("healAmount", heal.getHealAmount());
                                            itemObject.put("maxAmount", heal.getMaxAmountInInventory());
                                        }

                                        inventoryItemObject.put("item", itemObject);
                                        inventoryItemObject.put("amount", inventory.getInventoryItem(itemI).getAmount());

                                        inventoryArray.put(inventoryItemObject);
                                    }

                                    character.put("inventory", inventoryArray);

                                    enemyCharacter.put("character", character);
                                    enemyCharacters.put(enemyCharacter);
                                }
                            }

                            if (currentLevelCollisions[y][x]) {
                                JSONObject collisionObject = new JSONObject();
                                collisionObject.put("x", x);
                                collisionObject.put("y", y);
                                collisionsArray.put(collisionObject);
                            }
                        }
                    }

                    characters.put("allyCharactersPositions", allyCharactersPositions);
                    characters.put("enemyCharacters", enemyCharacters);
                    level.put("characters", characters);
                    level.put("collisions", collisionsArray);
                }
            }
        }
        return JSONUtils.saveJSONFile(gameLevelsFileName, gameLevels, logger);
    }

    /**
     * Sets a new map size.
     * @param   width   Maps width.
     * @param   height  Maps height.
     */
    public void setLevelSize(int width, int height) {
        currentLevelSize[0] = width;
        currentLevelSize[1] = height;
    }

    /**
     *  Returns all levels names.
     * @return Levels names.
     */
    public ArrayList<String> getLevelNamesList() {
        ArrayList<String> levelsList = new ArrayList<>(gameLevels.length());
        for (int levelI = 0; levelI < gameLevels.length(); levelI++) {
            levelsList.add(gameLevels.getJSONObject(levelI).getString("name"));
        }

        return levelsList;
    }

    public void setLevelIsLoaded(boolean state) {
        levelIsLoaded.set(state);
    }

    public boolean levelIsLoaded() {
        return levelIsLoaded.get();
    }

    /**
     *  Returns all current level tiles.
     * @return All current level tiles.
     */
    public Tile[][] getCurrentLevelTiles() {
        return currentLevelTiles;
    }

    /**
     *  Returns current level tile on given position.
     * @param x Tile x position.
     * @param y Tile y position.
     * @return Tile or null.
     */
    public Tile getCurrentLevelTile(int x, int y) {
        if ((x < 0 || x >= currentLevelSize[0]) || (y < 0 || y >= currentLevelSize[1])) {
            logger.error(this, "Tile index out of range: (x, y) = (" + x + ", " + y + ").");
            return null;
        }
        return currentLevelTiles[y][x];
    }

    /**
     *  Returns all current level characters.
     * @return Current level characters.
     */
    public Character[][] getCurrentLevelCharacters() {
        return currentLevelCharacters;
    }

    /**
     *  Returns character on given position.
     * @param x Character x position.
     * @param y Character y position.
     * @return Character or null.
     */
    public Character getCurrentLevelCharacter(int x, int y) {
        if ((x < 0 || x >= currentLevelSize[0]) || (y < 0 || y >= currentLevelSize[1])) {
            logger.error(this, "Character index out of range: [" + x + ", " + y + "].");
            return null;
        }
        return currentLevelCharacters[y][x];
    }

    /**
     *  Returns total levels amount.
     * @return Total levels amount.
     */
    public int getLevelsAmount() {
        return gameLevels.length();
    }

    /**
     * Returns current level size.
     * @return  Current levels size.
     */
    public int[] getCurrentLevelSize() {
        return currentLevelSize;
    }

    /**
     *  Returns current levels sprite size.
     * @return Current levels sprite size.
     */
    public int[] getCurrentLevelSpriteSize() {
        return currentLevelSpriteSize;
    }

    /**
     * Returns current levels name.
     * @return  Current levels name.
     */
    public String getCurrentLevelName() {
        return currentLevelName;
    }
}
