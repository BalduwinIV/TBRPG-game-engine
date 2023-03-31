package game;

import engine.panels.GamePanel;
import engine.tools.Logger;

import javax.swing.*;

public class AISStart {
    public static void main(String[] args) {
        Logger logger = new Logger("GameLogger.log");
        logger.startLogging();
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Adapt. Improve. Survive.");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        logger.info(window, "Window options: {defaultCloseOperation = JFrame.EXIT_ON_CLOSE, " +
                "resizable = false, title = \"Adapt. Improve. Survive.\", extendedState = JFrame.MAXIMIZED_BOTH, " +
                "undecorated = true}.");

        GamePanel gamePanel = new GamePanel(1920, 1080);
        gamePanel.setLogger(logger);
        logger.info(gamePanel, "GamePanel initialized with parameters 1920 and 1080.");
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        logger.info(gamePanel, "Starting gameThread.");
        gamePanel.startGameThread();
    }
}
