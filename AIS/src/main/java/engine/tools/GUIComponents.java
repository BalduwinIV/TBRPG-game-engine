package engine.tools;

import engine.panels.*;

import javax.swing.*;

/**
 *  Class that contains windows and panels to give easier access to it.
 */
public class GUIComponents {
    private JFrame mainWindow;
    private ToolBar toolBar;
    private LoggerPanel loggerPanel;
    private GamePanel gamePanel;
    private ObjectsPanel objectsPanel;
    private ParametersPanel parametersPanel;
    private CharactersPanel charactersPanel;
    private InventoryPanel inventoryPanel;

    public GUIComponents () {
    }

    public JFrame getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(JFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public LoggerPanel getLoggerPanel() {
        return loggerPanel;
    }

    public void setLoggerPanel(LoggerPanel loggerPanel) {
        this.loggerPanel = loggerPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public ObjectsPanel getObjectsPanel() {
        return objectsPanel;
    }

    public void setObjectsPanel(ObjectsPanel objectsPanel) {
        this.objectsPanel = objectsPanel;
    }

    public ParametersPanel getParametersPanel() {
        return parametersPanel;
    }

    public void setParametersPanel(ParametersPanel parametersPanel) {
        this.parametersPanel = parametersPanel;
    }

    public CharactersPanel getCharactersPanel() {
        return charactersPanel;
    }

    public void setCharactersPanel(CharactersPanel charactersPanel) {
        this.charactersPanel = charactersPanel;
    }

    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }

    public void setInventoryPanel(InventoryPanel inventoryPanel) {
        this.inventoryPanel = inventoryPanel;
    }
}
