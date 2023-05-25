package engine.windows;

import engine.GUIColors;
import engine.panels.*;
import engine.tools.ProgramState;
import engine.tools.GUIComponents;
import engine.utils.Logger;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Engines window class.
 */
public class MainWindow extends JFrame {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;

    public MainWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super();
        this.guiComponents = guiComponents;
        guiComponents.setMainWindow(this);
        this.modelControlComponents = modelControlComponents;
        modelControlComponents.setProgramState(ProgramState.ENGINE);
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
        setTitle("AIS Engine");
        setPreferredSize(new Dimension(1280, 720));
    }

    /**
     * Adding all necessary panels to window and shows it to user.
     */
    public void start() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(GUIColors.color(GUIColors.Colors.BACKGROUND)));

        addKeyListener(modelControlComponents.getKeyHandler());

        GamePanel gamePanel = new GamePanel(guiComponents, modelControlComponents ,1280,720);
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(gamePanel, BorderLayout.CENTER);

        ObjectsPanel objectsPanel = new ObjectsPanel(guiComponents, modelControlComponents);
        objectsPanel.setPreferredSize(new Dimension(1850, 120));
        add(objectsPanel, BorderLayout.SOUTH);

        ToolBar toolBar = new ToolBar(guiComponents, modelControlComponents);
        add(toolBar, BorderLayout.NORTH);

        ParametersPanel parametersPanel = new ParametersPanel(guiComponents, modelControlComponents);
        parametersPanel.setPreferredSize(new Dimension(320, 720));
        add(parametersPanel, BorderLayout.EAST);

        LoggerPanel loggerPanel = new LoggerPanel();
        loggerPanel.addLogger(modelControlComponents.getEngineLogger());
        loggerPanel.addLogger(modelControlComponents.getGameLogger());
        loggerPanel.addLogger(modelControlComponents.getSaveDataEditorLogger());
        loggerPanel.setPreferredSize(new Dimension(250, 720));
        add(loggerPanel, BorderLayout.WEST);
        guiComponents.setLoggerPanel(loggerPanel);

        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, loggerPanel, gamePanel));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        Dimension screenSize = gamePanel.getSize();
        gamePanel.changePanelSize(screenSize.width, screenSize.height);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension screenSize = gamePanel.getSize();
                gamePanel.changePanelSize(screenSize.width, screenSize.height);
                super.componentResized(e);
            }
        });
    }
}