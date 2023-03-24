package game;

import engine.tools.Time;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 32; // 32x32 tile
    final int tileScale = 3;
    final int tileSize = originalTileSize * tileScale; // 96x96 tile
    final int tilesCountHorizontal = 20;
    final int tilesCountVertical = 12;
    int screenWidth;
    int screenHeight;

    int screenFPS = 60;
    int inGameFPS = 100;
    KeyHandler keyHandler;
    Thread gameThread;
    boolean gameThreadIsRunning;
    double playerPosX = 100;
    double playerPosY = 100;
    double playerMaxSpeed = 4 * tileSize;
    double playerAcceleration = tileSize;
    double playerSpeed = 0;

    public GamePanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.keyHandler = new KeyHandler();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.gameThreadIsRunning = false;
    }

    public void startGameThread() {
        this.gameThreadIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double inGameTimer = 0;
        double screenTimer = 0;
        double timer = 0;
        int drawCount = 0;
        int gameCount = 0;
        double currentTime;
        Time time = new Time();
        double lastTime = time.getTimeInSec();
        while (Objects.nonNull(gameThread)) {
            currentTime = time.getTimeInSec();

            update(currentTime - lastTime);
            repaint();
            drawCount++;

            if (currentTime - timer >= 1) {
                System.out.println("FPS = " + drawCount);
                drawCount = 0;
                timer = currentTime;
            }
            lastTime = currentTime;
            try {
                Thread.sleep(0, 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(double time) {
        if (!keyHandler.isUpPressed() && !keyHandler.isDownPressed() && !keyHandler.isLeftPressed() && !keyHandler.isRightPressed()) {
            playerSpeed = 0;
            return;
        }

        playerSpeed += playerAcceleration * time;
        if (playerSpeed > playerMaxSpeed) {
            playerSpeed = playerMaxSpeed;
        }
        if (keyHandler.isUpPressed()) {
            playerPosY -= playerSpeed * time;
        }
        if (keyHandler.isDownPressed()) {
            playerPosY += playerSpeed * time;
        }
        if (keyHandler.isLeftPressed()) {
            playerPosX -= playerSpeed * time;
        }
        if (keyHandler.isRightPressed()) {
            playerPosX += playerSpeed * time;
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D =(Graphics2D)graphics;
        graphics2D.setColor(Color.white);
        graphics2D.fillRect((int)playerPosX, (int)playerPosY, tileSize, tileSize);
        graphics2D.dispose();
    }
}
