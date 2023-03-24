package game;

import javax.swing.*;

public class AISStart {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Adapt. Improve. Survive.");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);

        GamePanel gamePanel = new GamePanel(1920, 1080);
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
