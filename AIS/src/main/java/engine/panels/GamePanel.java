package engine.panels;

import engine.tools.Camera;
import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.characterManager.Character;
import engine.tools.GameState;
import engine.tools.itemsManager.HealManager.Heal;
import engine.tools.itemsManager.InventoryItem;
import engine.tools.itemsManager.WeaponManager.Weapon;
import engine.utils.*;
import engine.utils.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 *  Game panel class.
 */
public class GamePanel extends JPanel implements Runnable {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private Logger logger;
    private final double[] screenRatio;
    private final Camera camera;
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private final MouseMotionHandler mouseMotionHandler;
    private GameState gameState;
    private Thread gameThread;
    private Thread editingThread;

    public GamePanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        this(guiComponents ,modelControlComponents ,800, 450);
    }

    public GamePanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents, int panelWidth, int panelHeight) {
        this(guiComponents, modelControlComponents, panelWidth, panelHeight, DoubleStream.of(panelWidth, panelHeight).toArray());
    }

    public GamePanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents, int panelWidth, int panelHeight, double[] panelRatio) {
        super();
        this.guiComponents = guiComponents;
        guiComponents.setGamePanel(this);
        this.modelControlComponents = modelControlComponents;
        setLogger(modelControlComponents.getGameLogger());

        screenRatio = panelRatio;

        int[] panelSize = new int[2];
        panelSize[0] = panelWidth;
        panelSize[1] = panelHeight;

        camera = new Camera(new Rectangle(0, 0, 1920, 1080), screenRatio);
        camera.setPanelSize(panelSize[0], panelSize[1]);

        setFocusable(true);

        keyHandler = modelControlComponents.getKeyHandler();
        addKeyListener(keyHandler);

        mouseMotionHandler = new MouseMotionHandler();
        addMouseMotionListener(mouseMotionHandler);

        mouseHandler = new MouseHandler(this, mouseMotionHandler);
        addMouseListener(mouseHandler);

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() > 0) {
                camera.zoom(0.8);
            } else {
                camera.zoom(1.25);
            }
        });
    }

    /**
     * Change panel size with correct panel ratio.
     * @param   panelWidth  New game panel width.
     * @param   panelHeight New game panel height.
     */
    public void changePanelSize(int panelWidth, int panelHeight) {
        if (panelWidth <= 0 || panelHeight <= 0) {
            logger.error(this, "Bad window size, panelWidth: " + panelWidth + ", panelHeight: " + panelHeight + ".");
            return;
        }
        int newWidth = panelWidth;
        int newHeight = panelHeight;
        if (newWidth / this.screenRatio[0] < newHeight / this.screenRatio[1]) {
            newHeight = (int)(panelWidth / this.screenRatio[0] * this.screenRatio[1]);
        } else {
            newWidth = (int)(panelHeight / this.screenRatio[1] * this.screenRatio[0]);
        }

        camera.setPanelSize(newWidth, newHeight);
        setSize(newWidth, newHeight);
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Starts game. (Games method)
     */
    public void startGame() {
        stopGame();
        camera.connectGameLevels(modelControlComponents.getGameLevels());
        camera.loadLevel();
        grabFocus();
        logger.info(this, "Starting game...");
        gameThread = new Thread(this);
        changeGameState(GameState.STATE_GAMEPLAY);
        gameThread.start();
    }

    /**
     * Stops game. (Games method)
     */
    public void stopGame() {
        if (Objects.nonNull(gameThread)) {
            logger.info(this, "Stopping game.");
            gameThread = null;
            changeGameState(GameState.STATE_STOP);
        }
    }

    /**
     *  Starts editing (Engine method).
     */
    public void startEditing() {
        stopEditing();
        camera.connectGameLevels(modelControlComponents.getGameLevels());
        camera.loadLevel();
        logger.info(this, "Starting editor...");
        grabFocus();
        editingThread = new Thread(this);
        changeGameState(GameState.STATE_DEVELOPMENT);
        editingThread.start();
    }

    /**
     *  Synchronizes Game Levels level state with camera.
     */
    public void updateSpritesOnCamera() {
        camera.updateSprites();
    }

    /**
     *  Stops editing. (Engine method)
     */
    public void stopEditing() {
        if (Objects.nonNull(editingThread)) {
            logger.info(this, "Stopping editing.");
            camera.unloadLevel();
            editingThread = null;
            changeGameState(GameState.STATE_STOP);
        }
    }

    /**
     *  Sets current game state.
     * @param newState  Defines new game state.
     */
    public void changeGameState(GameState newState) {
        gameState = newState;
    }

    /**
     *  Game loop update method.
     */
    public void gameUpdate() {
        if (mouseHandler.isOnScreen()) {
            camera.setCursorIsOnScreen(true);
            camera.setCursorPosition(mouseMotionHandler.getCursorPosition());

            if (mouseMotionHandler.isBeingDragged() && mouseHandler.getPressedButton() == 2) {
                int[] movement = mouseMotionHandler.getCursorPositionDifference();
                camera.move(movement[0], movement[1]);
            }

            if (mouseHandler.isClicked()) {
                if (camera.selectSprite(mouseHandler.getClickedLocation())) {
                    if (camera.spriteIsSelected()) {
                        if (mouseHandler.getClickedButton() == 1) {
                            if (modelControlComponents.getGameLevels().getSelectedCharacter().getCharacterType().equals("ally") && modelControlComponents.getGameLevels().selectedCharacterCanMove(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) {
                                doMove();
                            } else {
                                changeSpriteSelection();
                            }
                            camera.updateSprites();
                        }
                    } else if (!camera.spriteIsSelected()) {
                        if (Objects.nonNull(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]))) {
                            camera.setSpriteIsSelected(true);
                            modelControlComponents.getGameLevels().deselectCharacter();
                            modelControlComponents.getGameLevels().selectCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
                            guiComponents.getParametersPanel().displayCharacter(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]));
                            camera.updateSprites();
                        } else {
                            guiComponents.getParametersPanel().clearDisplay();
                        }
                    }
                }
                mouseHandler.setIsClicked(false);
            }
        } else {
            camera.setCursorIsOnScreen(false);
        }

        if (keyHandler.isPressed()) {
            if (keyHandler.getPressedKey() == KeyEvent.VK_ESCAPE) {
                if (modelControlComponents.getGameLevels().characterIsSelected()) {
                    modelControlComponents.getGameLevels().getSelectedCharacter().setUsingItem(false);
                    modelControlComponents.getGameLevels().deselectCharacter();
                }
                camera.deselectSprite();
                guiComponents.getParametersPanel().clearDisplay();
                camera.updateSprites();
            }
        }
    }

    /**
     *  Prepares character and game for using item.
     * @param inventoryItem Inventory item that is planned to be used.
     */
    public void prepareItemToUse(InventoryItem inventoryItem) {
        Character selectedCharacter = modelControlComponents.getGameLevels().getSelectedCharacter();
        if (!modelControlComponents.getGameLevels().getSelectedCharacter().hasUsedItem()) {
            selectedCharacter.setUsingItem(true);
            selectedCharacter.setItemToUse(inventoryItem);
            if (inventoryItem.getItem().getItemType().equals("weapon")) {
                prepareWeapon((Weapon) inventoryItem.getItem());
            } else if (inventoryItem.getItem().getItemType().equals("heal")) {
                prepareHeal((Heal) inventoryItem.getItem());
            }
        } else {
            logger.warning(this, "Character \"" + selectedCharacter.getName() + "\" has already used item.");
        }
    }

    /**
     *  Draws area that is in weapons range.
     * @param weapon Characters weapon.
     */
    public void prepareWeapon(Weapon weapon) {
        modelControlComponents.getGameLevels().clearMovementArea();
//        modelControlComponents.getGameLevels().fillMovementArea(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1], weapon.getRange());
        modelControlComponents.getGameLevels().fillMovementArea(modelControlComponents.getGameLevels().getSelectedCharacterPosition()[0], modelControlComponents.getGameLevels().getSelectedCharacterPosition()[1], weapon.getRange());
        camera.updateSprites();
    }

    /**
     *  Draws area that is in heal range.
     * @param heal Characters heal.
     */
    public void prepareHeal(Heal heal) {
        modelControlComponents.getGameLevels().clearMovementArea();
//        modelControlComponents.getGameLevels().fillMovementArea(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1], heal.getHealRange());
        modelControlComponents.getGameLevels().fillMovementArea(modelControlComponents.getGameLevels().getSelectedCharacterPosition()[0], modelControlComponents.getGameLevels().getSelectedCharacterPosition()[1], heal.getHealRange());
        camera.updateSprites();
    }

    /**
     *  Uses item on character with given coordinates. (If possible)
     * @param targetX Targets x coordinate.
     * @param targetY Targets y coordinate.
     */
    public void useItem(int targetX, int targetY) {
        Character selectedCharacter = modelControlComponents.getGameLevels().getSelectedCharacter();
        InventoryItem itemToUse = selectedCharacter.getItemToUse();
        if (itemToUse.getItem().getItemType().equals("weapon")) {
            if (modelControlComponents.getGameLevels().getSelectedCharacterPosition()[0] == targetX && modelControlComponents.getGameLevels().getSelectedCharacterPosition()[1] == targetY) {
                logger.info(this, "You cannot attack yourself.");
                return;
            }
            Weapon weaponToUse = (Weapon) itemToUse.getItem();
            Character enemy = modelControlComponents.getGameLevels().getCurrentLevelCharacter(targetX, targetY);
            if (Objects.nonNull(enemy)) {
                logger.info(this, "Using weapon " + weaponToUse.getName() + " on character " + enemy.getName() + ".");

                boolean doDoubleDamage = (enemy.getSpeed() - selectedCharacter.getSpeed() >= 5);
                int attackAmount = 1;
                if (doDoubleDamage) {
                    attackAmount = 2;
                }
                for (int attackNumber = 1; attackNumber <= attackAmount; attackNumber++) {
                    boolean toAttack = new Random().nextInt(100) < (selectedCharacter.getHitRate() + selectedCharacter.getSkill() + weaponToUse.getHitRate() - enemy.getAvoidRate());
                    if (toAttack) {
                        int damage;
                        int damageModifier = 1;
                        boolean doCriticalDamage = new Random().nextInt(100) < (selectedCharacter.getCriticalHitRate() + selectedCharacter.getSkill() + weaponToUse.getCriticalHitRate() + selectedCharacter.getLuck());
                        if (doCriticalDamage) {
                            logger.info(this, "Critical damage! (x3)");
                            damageModifier = 3;
                        }
                        if (weaponToUse.getWeaponClass().equals("magic")) {
                            damage = damageModifier * (selectedCharacter.getBaseDMG() + selectedCharacter.getMagic() - enemy.getResistance());
                        } else {
                            damage = damageModifier * (selectedCharacter.getBaseDMG() + selectedCharacter.getStrength() - enemy.getDefence());
                        }
                        if (damage < 0) {
                            damage = 0;
                        }
                        logger.info(this, "Damage: " + damage);
                        enemy.changeHP(-damage);
                        int exp = damage*5;
                        if (exp < 30) {
                            exp = 30;
                        } else if (exp > 90) {
                            exp = 90;
                        }
                        selectedCharacter.addExp(exp);
                        if (!enemy.isAlive()) {
                            logger.info(this, "Character \"" + enemy.getName() + "\" has been defeated.");
                            enemy.setHP(0);
                            modelControlComponents.getGameLevels().removeCharacter(targetX, targetY);
                            if (modelControlComponents.getGameStats().isAllyPhase()) {
                                modelControlComponents.getGameStats().decreaseEnemyCount();
                                if (modelControlComponents.getGameStats().getEnemyCount() <= 0) {
                                    camera.updateSprites();
                                    guiComponents.getParametersPanel().clearDisplay();
                                    revalidate();
                                    repaint();
                                    logger.info(this, "Allies won!!!");
                                    JOptionPane.showMessageDialog(this, "Congrats. You have cleared the level!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                                    modelControlComponents.getSaveDataManager().addClearedLevelToCurrentSaveData(modelControlComponents.getGameLevels().getCurrentLevelName());
                                    guiComponents.getMainWindow().dispatchEvent(new WindowEvent(guiComponents.getMainWindow(), WindowEvent.WINDOW_CLOSING));
                                }
                            } else {
                                modelControlComponents.getGameStats().decreaseAllyCount();
                                if (modelControlComponents.getGameStats().getAllyCount() <= 0) {
                                    camera.updateSprites();
                                    guiComponents.getParametersPanel().clearDisplay();
                                    revalidate();
                                    repaint();
                                    logger.info(this, "Enemies won!!!");
                                    JOptionPane.showMessageDialog(this, "You lose.", "Lose message", JOptionPane.INFORMATION_MESSAGE);
                                    guiComponents.getMainWindow().dispatchEvent(new WindowEvent(guiComponents.getMainWindow(), WindowEvent.WINDOW_CLOSING));
                                }
                            }
                            break;
                        }
                    } else {
                        logger.info(this, "Attack failed.");
                    }
                }
                logger.info(this, "Character \"" + enemy.getName() + "\" HP: " + enemy.getHP());

                modelControlComponents.getGameLevels().getSelectedCharacter().setHasMoved(true);
                modelControlComponents.getGameLevels().getSelectedCharacter().setUsingItem(false);
                modelControlComponents.getGameLevels().getSelectedCharacter().setHasUsedItem(true);
                if (modelControlComponents.getGameStats().isAllyPhase()) {
                    modelControlComponents.getGameStats().doMove();
                    changeSpriteSelection();
                    if (modelControlComponents.getGameStats().getMovesCount() <= 0) {
                        logger.info(this, "Starting enemy phase.");
                        modelControlComponents.getGameStats().setIsAllyPhase(false);
                        startEnemyPhase();
                    }
                }
            } else {
                logger.warning(this, "Cannot use weapon: selected place is empty.");
                changeSpriteSelection();
            }
        } else if (itemToUse.getItem().getItemType().equals("heal")) {
            Heal heal = (Heal) itemToUse.getItem();
            Character characterToUseOn = modelControlComponents.getGameLevels().getCurrentLevelCharacter(targetX, targetY);
            if (Objects.nonNull(characterToUseOn)) {
                logger.info("Using healing item \"" + heal.getName() + "\".");
                characterToUseOn.changeHP(heal.getHealAmount());
                logger.info("Character \"" + characterToUseOn.getName() + "\" got healed by " + heal.getHealAmount() + " HP.");

                int exp = 15;
                selectedCharacter.addExp(exp);

                modelControlComponents.getGameLevels().getSelectedCharacter().setHasMoved(true);
                modelControlComponents.getGameLevels().getSelectedCharacter().setUsingItem(false);
                modelControlComponents.getGameLevels().getSelectedCharacter().setHasUsedItem(true);
                if (modelControlComponents.getGameStats().isAllyPhase()) {
                    modelControlComponents.getGameStats().doMove();
                    changeSpriteSelection();
                    if (modelControlComponents.getGameStats().getMovesCount() <= 0) {
                        logger.info(this, "Starting enemy phase.");
                        modelControlComponents.getGameStats().setIsAllyPhase(false);
                        startEnemyPhase();
                    }
                }
            } else {
                logger.warning(this, "Cannot use heal: selected place is empty.");
                changeSpriteSelection();
            }
        }
    }

    /**
     *  Skips to enemies move.
     */
    public void skipMove() {
        modelControlComponents.getGameLevels().getSelectedCharacter().setHasMoved(true);
        modelControlComponents.getGameLevels().getSelectedCharacter().setUsingItem(false);
        modelControlComponents.getGameLevels().getSelectedCharacter().setHasUsedItem(true);
        camera.setSpriteIsSelected(false);
        modelControlComponents.getGameLevels().deselectCharacter();
        modelControlComponents.getGameLevels().clearMovementArea();
        camera.updateSprites();
        logger.info(this, "Starting enemy phase.");
        modelControlComponents.getGameStats().setIsAllyPhase(false);
        startEnemyPhase();
    }

    /**
     *  Moves selected character to selected tile. (If possible)
     */
    public void move() {
        if (!modelControlComponents.getGameLevels().getSelectedCharacter().hasMoved()) {
            if (modelControlComponents.getGameLevels().moveCharacter(camera.getPreviousSelectedSpritePosition()[0], camera.getPreviousSelectedSpritePosition()[1], camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) {
                modelControlComponents.getGameLevels().deselectCharacter();
                modelControlComponents.getGameLevels().selectCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
                Character selectedCharacter = modelControlComponents.getGameLevels().getSelectedCharacter();
                findWeaponInInventoryToUse(selectedCharacter);
            } else {
                changeSpriteSelection();
            }
        } else {
            logger.info(this, "Character \"" + modelControlComponents.getGameLevels().getSelectedCharacter().getName() + "\" has already moved.");
            changeSpriteSelection();
        }
    }

    private void findWeaponInInventoryToUse(Character selectedCharacter) {
        selectedCharacter.setHasMoved(true);
        for (int itemI = 0; itemI < selectedCharacter.getInventory().getInventorySize(); itemI++) {
            if (selectedCharacter.getInventory().getInventoryItem(itemI).getItem().getItemType().equals("weapon")) {
                selectedCharacter.setUsingItem(true);
                prepareItemToUse(selectedCharacter.getInventory().getInventoryItem(itemI));
                break;
            }
        }
        if (!selectedCharacter.usingItem()) {
            logger.warning(this, "No weapon in inventory has been detected.");
        } else {
            logger.info(this, "Weapon \"" + selectedCharacter.getItemToUse().getItem().getName() + "\" has been prepared to use.");
        }
    }

    /**
     *  Changes sprite selection ot none or other character.
     */
    private void changeSpriteSelection() {
        camera.setSpriteIsSelected(false);
        modelControlComponents.getGameLevels().deselectCharacter();
        guiComponents.getParametersPanel().clearDisplay();
        if (Objects.nonNull(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]))) {
            camera.setSpriteIsSelected(true);
            modelControlComponents.getGameLevels().selectCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
            guiComponents.getParametersPanel().displayCharacter(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]));
        }
    }

    /**
     *  Defines if character is planning to move or use item.
     */
    public void doMove() {
        if (modelControlComponents.getGameLevels().getSelectedCharacter().usingItem()) {
            useItem(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
        } else {
            move();
        }
    }

    /**
     *  Plays enemies phase.
     *  Enemy attack everything they can move to.
     */
    public void startEnemyPhase() {
        Character[][] charactersOnScreen = modelControlComponents.getGameLevels().getCurrentLevelCharacters();
        for (int y = 0; y < modelControlComponents.getGameLevels().getCurrentLevelSize()[1]; y++) {
            for (int x = 0; x < modelControlComponents.getGameLevels().getCurrentLevelSize()[0]; x++) {
                if (Objects.nonNull(charactersOnScreen[y][x]) && charactersOnScreen[y][x].getCharacterType().equals("enemy") && !charactersOnScreen[y][x].hasMoved()) {
                    modelControlComponents.getGameLevels().selectCharacter(x, y);
                    boolean[][] movementArea = modelControlComponents.getGameLevels().getSelectedCharacterMovementArea();
                    for (int movementAreaY = 0; movementAreaY < modelControlComponents.getGameLevels().getCurrentLevelSize()[1]; movementAreaY++) {
                        for (int movementAreaX = 0; movementAreaX < modelControlComponents.getGameLevels().getCurrentLevelSize()[0]; movementAreaX++) {
                            if (movementArea[movementAreaY][movementAreaX] && Objects.nonNull(charactersOnScreen[movementAreaY][movementAreaX]) && charactersOnScreen[movementAreaY][movementAreaX].getCharacterType().equals("ally")) {
                                if (modelControlComponents.getGameLevels().moveCharacter(x, y, movementAreaX-1, movementAreaY)) {
                                    modelControlComponents.getGameLevels().deselectCharacter();
                                    modelControlComponents.getGameLevels().selectCharacter(movementAreaX-1, movementAreaY);
                                } else if (modelControlComponents.getGameLevels().moveCharacter(x, y, movementAreaX, movementAreaY-1)) {
                                    modelControlComponents.getGameLevels().deselectCharacter();
                                    modelControlComponents.getGameLevels().selectCharacter(movementAreaX, movementAreaY-1);
                                } else if (modelControlComponents.getGameLevels().moveCharacter(x, y, movementAreaX+1, movementAreaY)) {
                                    modelControlComponents.getGameLevels().deselectCharacter();
                                    modelControlComponents.getGameLevels().selectCharacter(movementAreaX+1, movementAreaY);
                                } else if (modelControlComponents.getGameLevels().moveCharacter(x, y, movementAreaX, movementAreaY+1)) {
                                    modelControlComponents.getGameLevels().deselectCharacter();
                                    modelControlComponents.getGameLevels().selectCharacter(movementAreaX, movementAreaY+1);
                                } else {
                                    continue;
                                }
                                Character enemy = modelControlComponents.getGameLevels().getSelectedCharacter();
                                findWeaponInInventoryToUse(enemy);
                                if (enemy.usingItem()) {
                                    useItem(movementAreaX, movementAreaY);
                                }
                                modelControlComponents.getGameLevels().deselectCharacter();
                            }
                        }
                    }
                }
            }
        }

        charactersOnScreen = modelControlComponents.getGameLevels().getCurrentLevelCharacters();
        for (int y = 0; y < modelControlComponents.getGameLevels().getCurrentLevelSize()[1]; y++) {
            for (int x = 0; x < modelControlComponents.getGameLevels().getCurrentLevelSize()[0]; x++) {
                if (Objects.nonNull(charactersOnScreen[y][x])) {
                    charactersOnScreen[y][x].setHasMoved(false);
                    charactersOnScreen[y][x].setUsingItem(false);
                    charactersOnScreen[y][x].setHasUsedItem(false);
                }
            }
        }

        modelControlComponents.getGameStats().nextRound();
        modelControlComponents.getGameStats().setIsAllyPhase(true);
        logger.info(this, "Starting new round #" + modelControlComponents.getGameStats().getRoundNumber());
        repaint();
    }

    /**
     *  Engines loop update method.
     */
    public void editorUpdate() {
        if (mouseHandler.isOnScreen()) {
            camera.setCursorIsOnScreen(true);
            camera.setCursorPosition(mouseMotionHandler.getCursorPosition());

            if (mouseMotionHandler.isBeingDragged() && mouseHandler.getPressedButton() == 2) {
                int[] movement = mouseMotionHandler.getCursorPositionDifference();
                camera.move(movement[0], movement[1]);
            }

            if (mouseHandler.isClicked()) {
                if (camera.selectSprite(mouseHandler.getClickedLocation())) {
                    if (guiComponents.getToolBar().collisionModeIsEnabled()) {
                        modelControlComponents.getGameLevels().addCollision(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
                        camera.updateSprites();
                    } else if (guiComponents.getToolBar().allyPlaceholderModeIsEnabled()) {
                        modelControlComponents.getGameLevels().addAllyCharacterPlaceholderToLevel(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]);
                        camera.updateSprites();
                    } else {
                        if (camera.spriteIsSelected()) {
                            if (mouseHandler.getClickedButton() == 3) {
                                if (modelControlComponents.getGameLevels().moveCharacter(camera.getPreviousSelectedSpritePosition()[0], camera.getPreviousSelectedSpritePosition()[1], camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) {
                                    camera.setSpriteIsSelected(false);
                                    camera.updateSprites();
                                }
                            } else if (modelControlComponents.getGameLevels().moveTile(camera.getPreviousSelectedSpritePosition(), camera.getSelectedSpritePosition())) {
                                camera.setSpriteIsSelected(false);
                                camera.updateSprites();
                            }
                        } else if (!camera.spriteIsSelected()) {
                            camera.setSpriteIsSelected(true);
                            if (Objects.nonNull(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) && !(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]).getName().equals(""))) {
                                guiComponents.getParametersPanel().displayCharacter(modelControlComponents.getGameLevels().getCurrentLevelCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1]));
                            } else {
                                guiComponents.getParametersPanel().clearDisplay();
                            }
                        }
                    }
                }
                mouseHandler.setIsClicked(false);
            }
        } else {
            camera.setCursorIsOnScreen(false);
        }


        if (keyHandler.isPressed()) {
            if (keyHandler.getPressedKey() == KeyEvent.VK_ESCAPE) {
                camera.deselectSprite();
            } else if (keyHandler.getPressedKey() == KeyEvent.VK_DELETE) {
                if (camera.spriteIsSelected()) {
                    if (mouseHandler.getClickedButton() == 3) {
                        if (modelControlComponents.getGameLevels().removeCharacter(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) {
                            camera.setSpriteIsSelected(false);
                            camera.updateSprites();
                            guiComponents.getParametersPanel().clearDisplay();
                        }
                    } else {
                        if (modelControlComponents.getGameLevels().removeTileFromLevel(camera.getSelectedSpritePosition()[0], camera.getSelectedSpritePosition()[1])) {
                            camera.setSpriteIsSelected(false);
                            camera.updateSprites();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        camera.draw(graphics);
    }

    @Override
    public void run() {
        if (Objects.nonNull(gameThread) && gameState == GameState.STATE_GAMEPLAY) {
            while (Objects.nonNull(gameThread) && gameState == GameState.STATE_GAMEPLAY) {
                gameUpdate();
                repaint();

                try {
                    Thread.sleep(1, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (Objects.nonNull(editingThread) && gameState == GameState.STATE_DEVELOPMENT) {
            while (Objects.nonNull(editingThread) && gameState == GameState.STATE_DEVELOPMENT) {
                editorUpdate();
                repaint();

                try {
                    Thread.sleep(1, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}