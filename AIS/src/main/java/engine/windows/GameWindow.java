package engine.windows;

import engine.panels.GamePanel;
import engine.panels.LoggerPanel;
import engine.panels.ParametersPanel;
import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;
import engine.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {
    GUIComponents guiComponents;
    ModelControlComponents modelControlComponents;
    Logger logger;

    public GameWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        modelControlComponents.setProgramState(ProgramState.GAME);
        this.logger = modelControlComponents.getGameLogger();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                modelControlComponents.getEngineLogger().stopLogging();
                modelControlComponents.getGameLogger().stopLogging();
                modelControlComponents.getSaveDataEditorLogger().stopLogging();

                Logger startLevelLogger = new Logger("StartWindowLogger.log");
                startLevelLogger.startLogging();
                MainMenu mainMenu = new MainMenu(startLevelLogger);
                mainMenu.start();

                super.windowClosing(e);
            }
        });
        setResizable(true);
        setTitle("Game");
        setSize(new Dimension(1280, 720));
        setPreferredSize(getSize());
        setLocationRelativeTo(null);
    }

    public void start() {
        getContentPane().setBackground(new Color(0x808080));
        getContentPane().setLayout(new BorderLayout(0, 0));

        addKeyListener(modelControlComponents.getKeyHandler());

        guiComponents.setMainWindow(this);

        GamePanel gamePanel = new GamePanel(guiComponents, modelControlComponents);
        gamePanel.setMinimumSize(new Dimension(800, 720));
        ParametersPanel parametersPanel = new ParametersPanel(guiComponents, modelControlComponents);
        parametersPanel.setPreferredSize(new Dimension(300, 720));

        LoggerPanel loggerPanel = new LoggerPanel();
        loggerPanel.setPreferredSize(new Dimension(1280, 300));
        loggerPanel.addLogger(modelControlComponents.getGameLogger());
        guiComponents.setLoggerPanel(loggerPanel);

        add(gamePanel, BorderLayout.CENTER);
        add(parametersPanel, BorderLayout.EAST);
        add(loggerPanel, BorderLayout.SOUTH);
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gamePanel, parametersPanel));

        gamePanel.startGame();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
