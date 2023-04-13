package engine;

import engine.panels.MainWindow;
import engine.utils.Logger;

public class EngineStart {
    public static void main(String[] args) {
        Logger logger = new Logger("EngineLogger.log");
        logger.startLogging();
        MainWindow mainWindow = new MainWindow();
        logger.info(mainWindow, "Creating MainWindow.");
        mainWindow.setLogger(logger);
        mainWindow.start();
    }
}
