package engine.panels;

import engine.tools.Camera;
import engine.tools.GameLevels;
import engine.tools.GameState;
import engine.utils.*;
import engine.utils.Rectangle;
import game.objects.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Engines game panel class.
 */
public class GamePanel extends JPanel implements Runnable {
    private Logger logger;
    private final int[] panelSize;
    private final double[] screenRatio;
    private final Camera camera;
    private Player player;
    private GameLevels gameLevels;
    private KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    public GameState gameState;
    public Thread gameThread;

    public GamePanel() {
        screenRatio = new double[2];
        screenRatio[0] = 16;
        screenRatio[1] = 9;

        panelSize = new int[2];
        panelSize[0] = 800;
        panelSize[1] = 450;

        camera = new Camera(new Rectangle(0, 0, 1920, 1080), screenRatio);
        camera.setPanelSize(panelSize[0], panelSize[1]);

        mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
    }

    public GamePanel(int panelWidth, int panelHeight) {
        screenRatio = new double[2];
        screenRatio[0] = panelWidth;
        screenRatio[1] = panelHeight;

        panelSize = new int[2];
        panelSize[0] = panelWidth;
        panelSize[1] = panelHeight;

        camera = new Camera(new Rectangle(0, 0, 1920, 1080), screenRatio);
        camera.setPanelSize(panelSize[0], panelSize[1]);

        mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
    }

    public GamePanel(int panelWidth, int panelHeight, double[] panelRatio) {
        screenRatio = panelRatio;

        panelSize = new int[2];
        panelSize[0] = panelWidth;
        panelSize[1] = panelHeight;

        camera = new Camera(new Rectangle(0, 0, 1920, 1080), screenRatio);
        camera.setPanelSize(panelSize[0], panelSize[1]);

        mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
    }

    /**
     * Change panel size with correct panel ratio.
     * @param   panelWidth  New game panel width.
     * @param   panelHeight New game panel height.
     */
    public void changePanelSize(int panelWidth, int panelHeight) {
        if (panelWidth <= 0 || panelHeight <= 0) {
            logger.error(this, "Bad window size: {panelWidth = " + panelWidth + ", panelHeight = " + panelHeight + "}.");
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
        this.setSize(newWidth, newHeight);
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Starts game.
     */
    public void startGame() {
        stopGame();
        camera.setViewArea(new Rectangle(0, 0, 1920, 1080));
        logger.info(this, "Starting game...");
        gameThread = new Thread(this);
        gameState = GameState.STATE_GAMEPLAY;
        gameThread.start();
    }

    /**
     * Pauses game.
     */
    public void pauseGame() {
        logger.info(this, "Game is on pause.");
    }

    /**
     * Stops game.
     */
    public void stopGame() {
        if (Objects.nonNull(gameThread)) {
            logger.info(this, "Stopping game.");
            gameThread = null;
            gameState = GameState.STATE_STOP;
        }
    }

    public void changeGameState(GameState newState) {
        gameState = newState;
    }

    /**
     * Updates all objects parameters.
     * Reacts to keyboard and mouse events.
     * @param   timeFromLastCall    The time that has passed from the last call.
     */
    public void update(double timeFromLastCall) {
        int[] cursorPosition = mouseHandler.getCursorPosition();
        int[] lastCursorPosition = mouseHandler.getLastCursorPosition();
        if (mouseHandler.isPressed()) {
            camera.move(lastCursorPosition[0] - cursorPosition[0], lastCursorPosition[1] - cursorPosition[1]);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        camera.draw(graphics);
    }

    @Override
    public void run() {
        camera.setBackgroundImage("src/main/resources/img/levelBackgrounds/testLevel.png");

        Time time = new Time();
        double currentTime;
        double lastTime = time.getTimeInSec();
        double lastRenderTime = lastTime;

        while (Objects.nonNull(gameThread) && gameState == GameState.STATE_GAMEPLAY) {
            currentTime = time.getTimeInSec();

            update(currentTime - lastTime);
            if (currentTime - lastRenderTime > 1e-4) {
                repaint();
                lastRenderTime = currentTime;
            }

            lastTime = currentTime;
            try {
                Thread.sleep(1, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}