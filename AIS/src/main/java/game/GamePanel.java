package game;

import engine.tools.ImageStorage;
import engine.tools.Logger;
import engine.tools.Time;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePanel extends JPanel implements Runnable {
    Logger logger;
    ImageStorage playerImage;
    ImageStorage numbersImages;

    final int originalTileSize = 32; // 32x32 tile
    final int tileScale = 3;
    final int tileSize = originalTileSize * tileScale; // 96x96 tile
    final int [] screenRatio = {16, 9};
    int screenWidth;
    int screenHeight;
    int FPS = 0;
    int fontScale = 3;
    int fontWidth = 6;
    int fontHeight = 7;

    KeyHandler keyHandler;
    Thread gameThread;
    AtomicBoolean gameThreadIsRunning;
    double playerPosX = 100;
    double playerPosY = 100;
    double playerMaxSpeed = 4 * tileSize;
    double playerAcceleration = tileSize;
    double playerSpeed = 0; // TODO: implement Vector class with addition and multiplication methods.

    public GamePanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        if (this.screenWidth / this.screenRatio[0] < this.screenHeight / this.screenRatio[1]) {
            this.screenHeight = screenWidth / this.screenRatio[0] * this.screenRatio[1];
        } else {
            this.screenWidth = screenHeight / this.screenRatio[1] * this.screenRatio[0];
        }
        this.keyHandler = new KeyHandler();
        this.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.gameThreadIsRunning = new AtomicBoolean(false);

        this.playerImage = new ImageStorage("img/tmp.png");
        ArrayList<String> numbersFileNameNames = new ArrayList<>(10);
        numbersFileNameNames.add("img/pixelFont/Number0.png");
        numbersFileNameNames.add("img/pixelFont/Number1.png");
        numbersFileNameNames.add("img/pixelFont/Number2.png");
        numbersFileNameNames.add("img/pixelFont/Number3.png");
        numbersFileNameNames.add("img/pixelFont/Number4.png");
        numbersFileNameNames.add("img/pixelFont/Number5.png");
        numbersFileNameNames.add("img/pixelFont/Number6.png");
        numbersFileNameNames.add("img/pixelFont/Number7.png");
        numbersFileNameNames.add("img/pixelFont/Number8.png");
        numbersFileNameNames.add("img/pixelFont/Number9.png");
        this.numbersImages = new ImageStorage(numbersFileNameNames);
    }

    private void printError(String errorMessage) {
        if (Objects.nonNull(logger)) {
            logger.error(this, errorMessage);
        } else {
            System.err.println(errorMessage);
        }
    }

    public void changeScreenSize(int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            printError("Bad window size: {screenWidth = " + screenWidth + ", screenHeight = " + screenHeight + "}.");
            return;
        }
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        if (this.screenWidth / this.screenRatio[0] < this.screenHeight / this.screenRatio[1]) {
            this.screenHeight = screenWidth / this.screenRatio[0] * this.screenRatio[1];
        } else {
            this.screenWidth = screenHeight / this.screenRatio[1] * this.screenRatio[0];
        }
        this.setSize(this.screenWidth, this.screenHeight);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void startGameThread() {
        resetGameThread(); // temporary
        gameThread = new Thread(this);
        gameThread.start();
        this.gameThreadIsRunning.set(true);
        this.requestFocus();
    }

    public void resetGameThread() {
        this.playerPosX = 100;
        this.playerPosY = 100;
        this.playerSpeed = 0;
    }

    public void stopGameThread() {
        this.gameThreadIsRunning.set(false);
    }

    public AtomicBoolean getGameThreadStatus() {
        return this.gameThreadIsRunning;
    }

    @Override
    public void run() {
        Time time = new Time();
        int drawCount = 0;  // FPS counter
        double timer = 0;   // 1-second timer
        double currentTime;
        double lastTime = time.getTimeInSec();
        while (Objects.nonNull(gameThread) && this.gameThreadIsRunning.get()) {
            currentTime = time.getTimeInSec();

            update(currentTime - lastTime);
            repaint();
            drawCount++;

            if (currentTime - timer >= 1) {
                this.FPS = drawCount;
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
            playerSpeed -= playerAcceleration * time;
            if (playerSpeed < 0) {
                playerSpeed = 0;
            }
        } else {
            playerSpeed += playerAcceleration * time;
            if (playerSpeed > playerMaxSpeed) {
                playerSpeed = playerMaxSpeed;
            }
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
        graphics.drawImage(playerImage.getImage(), 0, 0, screenWidth, screenHeight, null);
        graphics.drawImage(playerImage.getImage(), (int)playerPosX, (int)playerPosY, tileSize, tileSize, null);
        int currentFPS = this.FPS;
        int xPos = screenWidth - 5 - fontWidth * fontScale;
        int yPos = 5;
        while (currentFPS > 0) {
            graphics.drawImage(numbersImages.getImage(currentFPS % 10), xPos, yPos, fontWidth * fontScale, fontHeight * fontScale, null);
            xPos -= fontWidth * fontScale + 5;
            currentFPS /= 10;
        }
    }
}
