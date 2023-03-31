package engine;

import engine.panels.LoggerPanel;
import engine.tools.Logger;
import engine.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends JFrame {
    LoggerPanel loggerPanel;
    GamePanel gameWindow;
    JPanel objectPanel;
    JPanel parametersPanel;
    Logger mainWindowLogger;
    JFrame window;
    public MainWindow() {
        this.window = new JFrame();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setResizable(true);
        this.window.setTitle("AIS Engine");
        this.window.setSize(1280, 720);
        this.window.setPreferredSize(this.window.getSize());
    }

    public void setLogger(Logger logger) {
        this.mainWindowLogger = logger;
    }

    public void start() {
        JPanel toolBar = new JPanel();
        GridLayout toolBarGrid = new GridLayout(1, 3);
        toolBar.setLayout(toolBarGrid);
        toolBar.add(new Button("Menu"));
        Button compileButton = new Button("Compile");
        compileButton.addActionListener(e -> {
            if (gameWindow.getGameThreadStatus().get()) {
                gameWindow.stopGameThread();
                mainWindowLogger.info(compileButton, "Game loop has been stopped.");
            } else {
                gameWindow.startGameThread();
                mainWindowLogger.info(compileButton, "Game loop has been started.");
            }
        });
        toolBar.add(compileButton);
        toolBar.add(new Button("Options"));

        window.setLayout(new BorderLayout(5, 5));

        window.add(toolBar, BorderLayout.NORTH);

        objectPanel = new JPanel();
        objectPanel.setBackground(Color.YELLOW);
        objectPanel.setPreferredSize(new Dimension(1850, 300));
        window.add(objectPanel, BorderLayout.SOUTH);

        gameWindow = new GamePanel(1280,720);
        Logger gameLogger = new Logger("GameLogger.log");
        gameWindow.setLogger(gameLogger);
        gameLogger.info(gameWindow, "New GameLogger instance.");
        window.add(gameWindow, BorderLayout.CENTER);

        parametersPanel = new JPanel();
        parametersPanel.setBackground(Color.ORANGE);
        parametersPanel.setPreferredSize(new Dimension(320, 720));
        window.add(parametersPanel, BorderLayout.EAST);

        loggerPanel = new LoggerPanel();
        loggerPanel.addLogger(mainWindowLogger);
        loggerPanel.addLogger(gameLogger);
        loggerPanel.setPreferredSize(new Dimension(250, 720));
        window.add(loggerPanel, BorderLayout.WEST);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Dimension screenSize = gameWindow.getSize();
        mainWindowLogger.info(gameWindow, "gameWindow screenWidth = " + screenSize.width + ", gameWindow screenHeight = " + screenSize.height);
        gameWindow.changeScreenSize(screenSize.width, screenSize.height);
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = gameWindow.getSize();
                gameWindow.changeScreenSize(screenSize.width, screenSize.height);
                super.componentResized(e);
            }
        });
    }
}