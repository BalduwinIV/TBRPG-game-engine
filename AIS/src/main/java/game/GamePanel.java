package game;

import engine.tools.Logger;
import engine.tools.Time;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {
    Logger logger;
    final BufferedImage image;

    {
        try {
            image = ImageIO.read(new File("img/tmp.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Screen settings
    final int originalTileSize = 32; // 32x32 tile
    final int tileScale = 3;
    final int tileSize = originalTileSize * tileScale; // 96x96 tile
    int screenWidth;
    int screenHeight;

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

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void startGameThread() {
        this.gameThreadIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timer = 0;
        int drawCount = 0;
        double currentTime;
        Time time = new Time();
        double lastTime = time.getTimeInSec();
        while (Objects.nonNull(gameThread)) {
            currentTime = time.getTimeInSec();

            update(currentTime - lastTime);
            repaint();
            drawCount++;

            if (currentTime - timer >= 1) {
                logger.info(this, "FPS = " + drawCount + ".");
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
        graphics.drawImage(image, 0, 0, screenWidth, screenHeight, null);
        graphics.drawImage(image, (int)playerPosX, (int)playerPosY, tileSize, tileSize, null);
    }
}
