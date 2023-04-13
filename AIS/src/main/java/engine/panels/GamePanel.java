package engine.panels;

import engine.utils.Logger;
import game.Game;

import javax.swing.*;

/**
 * Engines game panel class.
 */
public class GamePanel extends JPanel {
    private Logger logger;
    private int[] panelSize;
    private double[] screenRatio;
    private Game game;

    public GamePanel() {
        screenRatio = new double[2];
        screenRatio[0] = 16;
        screenRatio[1] = 9;

        panelSize = new int[2];
        panelSize[0] = 800;
        panelSize[1] = 450;

        game = new Game();
    }

    public GamePanel(int panelWidth, int panelHeight) {
        screenRatio = new double[2];
        screenRatio[0] = panelWidth;
        screenRatio[1] = panelHeight;

        panelSize = new int[2];
        panelSize[0] = panelWidth;
        panelSize[1] = panelHeight;

        game = new Game();
    }

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
        game.start();
    }

    /**
     * Pauses game.
     */
    public void pauseGame() {
        game.pause();
    }

    /**
     * Stops game.
     */
    public void stopGame() {
        game.stop();
    }
}