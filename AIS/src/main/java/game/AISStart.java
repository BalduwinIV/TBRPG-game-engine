package game;

import engine.panels.GamePanel;
import engine.utils.Logger;

import javax.swing.*;
import java.awt.*;

public class AISStart {
    public static void main(String[] args) {
        Logger logger = new Logger("GamePanelLogger.log");
        logger.startLogging();
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(1920, 1080));
        window.setTitle("Adapt. Improve. Survive.");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        logger.info(window, "Window options: {defaultCloseOperation = JFrame.EXIT_ON_CLOSE, " +
                "resizable = true, title = \"Adapt. Improve. Survive.\", extendedState = JFrame.MAXIMIZED_BOTH, " +
                "undecorated = true}.");

        GamePanel gamePanel = new GamePanel(1920, 1080);
        gamePanel.setLogger(logger);
        logger.info(gamePanel, "GamePanel initialized with parameters 1920 and 1080.");
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        logger.info(gamePanel, "Starting gameThread.");
        gamePanel.startGame();
    }
}
