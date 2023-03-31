package engine.tools;

import engine.panels.LoggerPanel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Logger implements Runnable {
    LoggerPanel loggerPanel;
    private Thread loggerThread;
    private final AtomicBoolean isLogging = new AtomicBoolean(false);
    private final String logFileName;
    private File logFile;
    private final List<String> loggerBuffer;
    private FileWriter fileWriter;

    public Logger () {
        this((new Time()).getTimeInNanoSec() + "_AISLogger.log");
    }
    public Logger (String logFileName) {
        this.logFileName = logFileName;
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            logFile = new File(Paths.get("logs", this.logFileName).toString());
            if (logFile.createNewFile()) {
                System.err.println("Logger file \"" + logFileName + "\" was created.");
                fileWriter = new FileWriter(logFile, true);
            } else {
                System.err.println("File with name \"" + logFileName + "\" is already exist.");
                System.err.println("Using it as logger file.");
                fileWriter = new FileWriter(logFile, true);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating logger file \"" + logFileName + "\"!");
            e.printStackTrace();
        } finally {
            loggerBuffer = new ArrayList<>(100);
        }
    }

    public void connectLoggerPanel(LoggerPanel loggerPanel) {
        this.loggerPanel = loggerPanel;
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
        isLogging.set(true);
        info(this, "New logger thread has been created.");
    }

    public void stopLogging() {
        isLogging.set(false);
    }

    public void info(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String infoMessage = "[INFO]\t\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(infoMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addInfoMessage(this, infoMessage);
        }
    }

    public void warning(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String warningMessage = "[WARNING]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(warningMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addWarningMassage(this, warningMessage);
        }
    }

    public void error(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String errorMessage = "[ERROR]\t\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(errorMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addErrorMessage(this, errorMessage);
        }
    }
    @Override
    public void run() {
        while (Objects.nonNull(loggerThread) && isLogging.get()) {
            copyFromBufferToFile();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        copyFromBufferToFile();
    }

    private void copyFromBufferToFile() {
        if (loggerBuffer.size() == 0) {
            return;
        }
        try {
            fileWriter = new FileWriter(logFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (loggerBuffer.size() > 0) {
            try {
                fileWriter.write(loggerBuffer.get(0));
            } catch (IOException e) {
                System.err.println("An exception occurred while writing from loggerBuffer to " + logFileName + ".");
                e.printStackTrace();
            }
            loggerBuffer.remove(0);
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
