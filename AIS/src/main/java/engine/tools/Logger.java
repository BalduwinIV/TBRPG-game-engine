package engine.tools;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Logger implements Runnable {
    private Thread loggerThread;
    private String logFileName;
    private File logFile;
    private boolean isLogging;
    private List<String> loggerBuffer;

    public Logger () {
        this((new Time()).getTimeInNanoSec() + "AISLogger.log");
    }
    public Logger (String logFileName) {
        this.logFileName = logFileName;
        try {
            logFile = new File(logFileName);
            if (logFile.createNewFile()) {
                System.err.println("Logger file \"" + logFileName + "\" was created.");
            } else {
                System.err.println("File with name \"" + logFileName + "\" is already exist.");
                long currentTimeInNanoSec = (new Time()).getTimeInNanoSec();
                System.err.println("Trying to use new name: \"" + currentTimeInNanoSec + "AISLogger.log" + "\"");

                logFile = new File(currentTimeInNanoSec + "AISLogger.log");
                if (logFile.createNewFile()) {
                    System.err.println("Logger file \"" + currentTimeInNanoSec + "AISLogger.log" + "\" was created.");
                    this.logFileName = currentTimeInNanoSec + "AISLogger.log";
                } else {
                    System.err.println("File with name \"" + currentTimeInNanoSec + "AISLogger.log" + "\" is already exist.");
                    System.err.println("No logger file was created.");
                    this.logFileName = "";
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating logger file \"" + logFileName + "\"!");
            e.printStackTrace();
        } finally {
            loggerBuffer = new ArrayList<String>(100);
        }
    }

    public String getLogFileName() {
        return this.logFileName;
    }

    public List<String> getLoggerBuffer() {
        return this.loggerBuffer;
    }

    public void startLogging () {
        loggerThread = new Thread(this);
        loggerThread.start();
        isLogging = true;
    }

    public void info(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        loggerBuffer.add("[INFO]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message);
    }
    public void warning(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        loggerBuffer.add("[WARNING]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message);
    }
    public void error(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        loggerBuffer.add("[ERROR]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message);
    }
    @Override
    public void run() {
        while (Objects.nonNull(loggerThread)) {

        }
    }
}
