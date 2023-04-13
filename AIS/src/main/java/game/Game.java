package game;

import engine.tools.Camera;
import engine.tools.GameMap;
import engine.tools.GameState;
import engine.utils.KeyHandler;
import engine.utils.Logger;
import engine.utils.MouseHandler;
import game.objects.Player;

/**
 * Main game class.
 */
public class Game implements Runnable {
    private Logger logger;
    private Player player;
    private GameMap gameMap;
    private Camera camera;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;
    private GameState gameState;
    private Thread gameThread;
    private int FPS;

    public Game() {
        /*
        * Initialize game from the beginning.
        * */
    }

    public Game(GameMap gameMap) {
        /*
        * Start game on given game map.
        * */
    }

    /**
     * Connects logger to the game.
     * @param   logger  Game logger.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Changes game state.
     * @param   newState    New game state.
     */
    public void changeGameState(GameState newState) {
        gameState = newState;
    }

    /**
     * Loads new games map.
     * @param   map     Maps name.
     */
    public void loadGameMap(String map) {
        this.gameMap.loadMap(map);
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        /*
        * Start the game loop.
        * */
    }

    /**
     * Pauses the game loop.
     */
    public void pause() {
        /*
        * Pause the game loop.
        * */
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        /*
        * Stop the game loop.
        * */
    }

    /**
     * Updates all objects parameters.
     * Reacts to keyboard and mouse events.
     */
    public void update() {
        /*
        * Update objects parameters.
        * React to keyboard and mouse events.
        * */
    }

    /**
     * Render image from camera view area.
     */
    public void render() {
        /*
        * Render image from camera view area.
        * */
    }

    /**
     * Contains main game loop.
     */
    @Override
    public void run() {
        /*
        * Do main game loop.
        * */
    }
}
