package engine;

import engine.utils.Logger;
import engine.windows.MainMenu;

public class Start {
    public static void main(String[] args) {
        Logger startLevelLogger = new Logger("StartWindowLogger.log");
        startLevelLogger.startLogging();

        MainMenu mainMenu = new MainMenu(startLevelLogger);
        mainMenu.start();
    }
}
