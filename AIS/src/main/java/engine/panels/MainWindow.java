package engine.panels;

import engine.tools.GameLevels;
import engine.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Engines window class.
 */
public class MainWindow extends JFrame {
    private GamePanel gameWindow;
    private Logger mainWindowLogger;
    private final JFrame window;
    public MainWindow() {
        this.window = new JFrame();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setResizable(true);
        this.window.setTitle("AIS Engine");
        this.window.setSize(1280, 720);
        this.window.setPreferredSize(this.window.getSize());
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.mainWindowLogger = logger;
    }

    /**
     * Adding all necessary panels to window and shows it to user.
     */
    public void start() {
        window.setLayout(new BorderLayout(5, 5));
        window.setBackground(new Color(0xcccccc));

        GameLevels gameLevels = new GameLevels("src/main/resources/levels.json");

        ObjectsPanel objectsPanel = new ObjectsPanel();
        objectsPanel.setPreferredSize(new Dimension(1850, 300));
        window.add(objectsPanel, BorderLayout.SOUTH);

        gameWindow = new GamePanel(1280,720);
        Logger gameLogger = new Logger("GameLogger.log");
        gameWindow.setLogger(gameLogger);
        gameLogger.info(gameWindow, "New GameLogger instance.");
        gameWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        window.add(gameWindow, BorderLayout.CENTER);

        ToolBar toolBar = new ToolBar(gameLevels, gameWindow);
        toolBar.setBackground(new Color(0xb3b3b3));
        window.add(toolBar, BorderLayout.NORTH);

        ParametersPanel parametersPanel = new ParametersPanel();
        parametersPanel.setBackground(Color.ORANGE);
        parametersPanel.setPreferredSize(new Dimension(320, 720));
        window.add(parametersPanel, BorderLayout.EAST);

        LoggerPanel loggerPanel = new LoggerPanel();
        loggerPanel.addLogger(mainWindowLogger);
        loggerPanel.addLogger(gameLogger);
        loggerPanel.setPreferredSize(new Dimension(250, 720));
        window.add(loggerPanel, BorderLayout.WEST);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Dimension screenSize = gameWindow.getSize();
        mainWindowLogger.info(gameWindow, "gameWindow screenWidth = " + screenSize.width + ", gameWindow screenHeight = " + screenSize.height);
        gameWindow.changePanelSize(screenSize.width, screenSize.height);
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = gameWindow.getSize();
                gameWindow.changePanelSize(screenSize.width, screenSize.height);
                super.componentResized(e);
            }
        });
    }
}