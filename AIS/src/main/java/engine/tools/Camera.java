package engine.tools;

import engine.tools.characterManager.Character;
import engine.tools.gameLevelsManager.GameLevels;
import engine.utils.ImageStorage;
import engine.utils.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Camera class that defines what to render on game panel.
 */
public class Camera {
    private Image[][] loadedSpritesList;
    private Image[][] spritesColorFilterList;
    private Image[][] loadedCharactersList;
    private final ImageStorage cursorImages;
    private final ImageStorage filterImages;
    private final ImageStorage defaultScreenBackground;
    private final AtomicBoolean cursorIsOnScreen;
    private int[] cursorPosition;
    private final AtomicBoolean spriteIsSelected;
    private int[] selectedSpritePosition;
    private int[] previousSelectedSpritePosition;
    private GameLevels gameLevels;
    private final Rectangle viewArea;
    private int[] panelSize;
    private final double[] cameraRatio;
    private double cameraScale;
    private final int[] currentSpriteSize;
    private final AtomicBoolean levelIsLoaded;

    /**
     * Creates camera with given parameters.
     * @param   viewArea    Rectangle object, that defines viewing area.
     * @param   cameraRatio Two numbers that defines screen ratio ([16,9], [4,3], [21,9], etc.)
     */
    public Camera(Rectangle viewArea, double[] cameraRatio) {
        this.viewArea = viewArea;
        this.cameraRatio = cameraRatio;
        this.cursorIsOnScreen = new AtomicBoolean(false);
        this.cursorPosition = new int[2];
        ArrayList<String> cursorImagesNames = new ArrayList<>(2);
        cursorImagesNames.add("src/main/resources/img/tileMaps/hovered_tile.png");
        cursorImagesNames.add("src/main/resources/img/tileMaps/chosen_tile.png");
        cursorImagesNames.add("src/main/resources/img/tileMaps/collision_tile.png");
        this.cursorImages = new ImageStorage(cursorImagesNames);
        this.defaultScreenBackground = new ImageStorage("src/main/resources/img/default_background.png");
        this.filterImages = new ImageStorage();
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/ally_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/moved_ally_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/ally_area_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/enemy_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/moved_enemy_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/enemy_area_tile_filter.png");
        this.filterImages.addImageByName("src/main/resources/img/tileMaps/heal_tile_filter.png");
        this.cameraScale = 1;
        currentSpriteSize = new int[2];
        spriteIsSelected = new AtomicBoolean(false);
        selectedSpritePosition = new int[2];
        previousSelectedSpritePosition = new int[2];
        levelIsLoaded = new AtomicBoolean(false);
    }

    public void connectGameLevels(GameLevels gameLevels) {
        this.gameLevels = gameLevels;
    }

    /**
     *  Load current level on camera.
     */
    public void loadLevel() {
        loadedSpritesList = new Image[gameLevels.getCurrentLevelSize()[1]][gameLevels.getCurrentLevelSize()[0]];
        spritesColorFilterList = new Image[gameLevels.getCurrentLevelSize()[1]][gameLevels.getCurrentLevelSize()[0]];
        loadedCharactersList = new Image[gameLevels.getCurrentLevelSize()[1]][gameLevels.getCurrentLevelSpriteSize()[0]];
        currentSpriteSize[0] = (int)(gameLevels.getCurrentLevelSpriteSize()[0] * cameraScale);
        currentSpriteSize[1] = (int)(gameLevels.getCurrentLevelSpriteSize()[1] * cameraScale);
        levelIsLoaded.set(true);
        updateSprites();
    }

    /**
     *  Unload the level.
     */
    public void unloadLevel() {
        levelIsLoaded.set(false);
    }

    public void setPanelSize(int width, int height) {
        this.panelSize = new int[2];
        this.panelSize[0] = width;
        this.panelSize[1] = height;
        viewArea.setSize(width, height);
        updateSprites();
    }

    /**
     * Changes the position of camera.
     * @param   y   Change in Y coordinate.
     * @param   x   Change in X coordinate.
     */
    public void move(double x, double y) {
        viewArea.changePosition(x * (viewArea.getWidth() / panelSize[0]), y * (viewArea.getHeight() / panelSize[1]));
        if (viewArea.getPositionX() + viewArea.getWidth() >= gameLevels.getCurrentLevelSize()[0] * gameLevels.getCurrentLevelSpriteSize()[0]) {
            viewArea.setPosition(gameLevels.getCurrentLevelSize()[0] * gameLevels.getCurrentLevelSpriteSize()[0] - viewArea.getWidth()-1, viewArea.getPositionY());
        }
        if (viewArea.getPositionX() < 0) {
            viewArea.setPosition(0, viewArea.getPositionY());
        }

        if (viewArea.getPositionY() + viewArea.getHeight() >= gameLevels.getCurrentLevelSize()[1] * gameLevels.getCurrentLevelSpriteSize()[1]) {
            viewArea.setPosition(viewArea.getPositionX(), gameLevels.getCurrentLevelSize()[1] * gameLevels.getCurrentLevelSpriteSize()[1] - viewArea.getHeight()-1);
        }
        if (viewArea.getPositionY() < 0) {
            viewArea.setPosition(viewArea.getPositionX(), 0);
        }
    }

    /**
     * Increasing or decreasing viewing area size.
     * @param   scale   Positive value. Decrease viewing area size if 0 < scale < 1. Increase viewing area size if scale > 1.
     */
    public void zoom(double scale) {
        cameraScale *= scale;
        viewArea.setSize(panelSize[0] / cameraScale, panelSize[1] / cameraScale);
        updateSprites();
    }

    public void setCursorIsOnScreen(boolean state) {
        cursorIsOnScreen.set(state);
    }

    public void setCursorPosition(int[] position) {
        cursorPosition = position;
    }

    /**
     *  Select tile.
     * @param position Cursor position.
     * @return True if tile has been selected. False otherwise.
     */
    public boolean selectSprite(int[] position) {
        int[] tilePosition = new int[2];
        tilePosition[0] = (int)((position[0] + (viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth()))) / currentSpriteSize[0]);
        tilePosition[1] = (int)((position[1] + (viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight()))) / currentSpriteSize[1]);
        if (spriteIsSelected.get()) {
            if ((tilePosition[0] >= 0 && tilePosition[0] < gameLevels.getCurrentLevelSize()[0] && tilePosition[1] >= 0 && tilePosition[1] < gameLevels.getCurrentLevelSize()[1])
                    && (tilePosition[0] != selectedSpritePosition[0] || tilePosition[1] != selectedSpritePosition[1])) {
                previousSelectedSpritePosition = selectedSpritePosition.clone();
                selectedSpritePosition = tilePosition;
                return true;
            }
        } else if ((tilePosition[0] >= 0 && tilePosition[0] < gameLevels.getCurrentLevelSize()[0] && tilePosition[1] >= 0 && tilePosition[1] < gameLevels.getCurrentLevelSize()[1]) &&
                !gameLevels.getCurrentLevelTile((int)((position[0] + (viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth()))) / currentSpriteSize[0]), (int)((position[1] + (viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight()))) / currentSpriteSize[1])).getName().equals("")) {
            previousSelectedSpritePosition = selectedSpritePosition.clone();
            selectedSpritePosition = tilePosition;
            return true;
        }
        return false;
    }

    public void setSpriteIsSelected(boolean state) {
        spriteIsSelected.set(state);
    }

    public boolean spriteIsSelected() {
        return spriteIsSelected.get();
    }

    public int[] getSelectedSpritePosition() {
        return selectedSpritePosition;
    }

    public int[] getPreviousSelectedSpritePosition() {
        return previousSelectedSpritePosition;
    }

    public void deselectSprite() {
        spriteIsSelected.set(false);
    }

    /**
     *  Synchronizes view with game levels class.
     */
    public void updateSprites() {
        if (Objects.nonNull(gameLevels) && Objects.nonNull(gameLevels.getCurrentLevelTiles())) {
            for (int y = 0; y < gameLevels.getCurrentLevelSize()[1]; y++) {
                for (int x = 0; x < gameLevels.getCurrentLevelSize()[0]; x++) {
                    loadedCharactersList[y][x] = null;
                    spritesColorFilterList[y][x] = null;
                }
            }
            currentSpriteSize[0] = (int)(gameLevels.getCurrentLevelSpriteSize()[0] * cameraScale);
            currentSpriteSize[1] = (int)(gameLevels.getCurrentLevelSpriteSize()[1] * cameraScale);
            for (int y = 0; y < gameLevels.getCurrentLevelSize()[1]; y++) {
                for (int x = 0; x < gameLevels.getCurrentLevelSize()[0]; x++) {
                    loadedSpritesList[y][x] = gameLevels.getCurrentLevelTile(x, y).getSprite().getImage().getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                    if (gameLevels.characterIsSelected() && gameLevels.selectedCharacterCanMove(x, y)) {
                        if (gameLevels.getSelectedCharacter().getCharacterType().equals("ally")) {
                            if (gameLevels.getSelectedCharacter().usingItem()) {
                                if (gameLevels.getSelectedCharacter().getItemToUse().getItem().getItemType().equals("weapon")) {
                                    spritesColorFilterList[y][x] = filterImages.getImage(5).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                                } else if (gameLevels.getSelectedCharacter().getItemToUse().getItem().getItemType().equals("heal")) {
                                    spritesColorFilterList[y][x] = filterImages.getImage(6).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_SMOOTH);
                                }
                            } else {
                                spritesColorFilterList[y][x] = filterImages.getImage(2).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                            }
                        } else if (gameLevels.getSelectedCharacter().getCharacterType().equals("enemy")) {
                            spritesColorFilterList[y][x] = filterImages.getImage(5).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                        }
                    }
                    if (Objects.nonNull(gameLevels.getCurrentLevelCharacter(x, y))) {
                        Character character = gameLevels.getCurrentLevelCharacter(x, y);
                        if (character.getCharacterType().equals("ally")) {
                            if (character.hasMoved()) {
                                spritesColorFilterList[y][x] = filterImages.getImage(1).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                            } else {
                                spritesColorFilterList[y][x] = filterImages.getImage(0).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                            }
                        } else if (character.getCharacterType().equals("enemy")) {
                            if (character.hasMoved()) {
                                spritesColorFilterList[y][x] = filterImages.getImage(4).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                            } else {
                                spritesColorFilterList[y][x] = filterImages.getImage(3).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                            }
                        }
                        loadedCharactersList[y][x] = character.getSprite().getImage().getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST);
                    }
                }
            }
        }
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

    /**
     *  Draws on the game panel.
     * @param graphics Game panels graphics.
     */
    public void draw(Graphics graphics) {
        if (Objects.nonNull(gameLevels) && Objects.nonNull(gameLevels.getCurrentLevelName()) && levelIsLoaded.get()) {
            int tileX = 0;
            int tileY = 0;
            if (cursorIsOnScreen.get()) {
                tileX = (int)((cursorPosition[0] + (viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth()))) / currentSpriteSize[0]);
                tileY = (int)((cursorPosition[1] + (viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight()))) / currentSpriteSize[1]);
            }
            for (int y = 0; y < gameLevels.getCurrentLevelSize()[1]; y++) {
                for (int x = 0; x < gameLevels.getCurrentLevelSize()[0]; x++) {
                    graphics.drawImage(loadedSpritesList[y][x], x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                    if (Objects.nonNull(spritesColorFilterList[y][x])) {
                        graphics.drawImage(spritesColorFilterList[y][x], x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                    }
                    if (Objects.nonNull(loadedCharactersList[y][x])) {
                        graphics.drawImage(loadedCharactersList[y][x], x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                    }
                    if (spriteIsSelected.get() && selectedSpritePosition[1] == y && selectedSpritePosition[0] == x) {
                        graphics.drawImage(cursorImages.getImage(1).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST), x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                    }
                    if (cursorIsOnScreen.get() && tileY == y && tileX == x) {
                        if (gameLevels.checkCollision(tileX, tileY)) {
                            graphics.drawImage(cursorImages.getImage(2).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST), x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                        } else {
                            graphics.drawImage(cursorImages.getImage(0).getScaledInstance(currentSpriteSize[0], currentSpriteSize[1], Image.SCALE_FAST), x * currentSpriteSize[0] - (int)(viewArea.getPositionX() * (panelSize[0] / viewArea.getWidth())), y * currentSpriteSize[1] - (int)(viewArea.getPositionY() * (panelSize[1] / viewArea.getHeight())), null);
                        }
                    }
                }
            }
        } else if (!levelIsLoaded.get()) {
            graphics.drawImage(defaultScreenBackground.getImage().getScaledInstance(panelSize[0], panelSize[1], Image.SCALE_FAST), 0, 0, null);
        }
    }
}
