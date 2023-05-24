package engine.utils;

import engine.panels.LoggerPanel;
import engine.windows.MainWindow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Logger, that writes logs in separate file.
 */
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

    /**
     * @param   logFileName   Name of file in which logger will write logs.
     */
    public Logger (String logFileName) {
        this.logFileName = logFileName;
        try {
            File logDir = new File("src/main/resources/logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            logFile = new File(Paths.get("src/main/resources/logs", this.logFileName).toString());
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

    /**
     * Connects current logger with LoggerPanel, so it will show loggers file content in separate tab.
     * @param   loggerPanel   LoggerPanel from MainWindow that shows the loggers file content.
     * @see     MainWindow
     */
    public void connectLoggerPanel(LoggerPanel loggerPanel) {
        this.loggerPanel = loggerPanel;
    }

    /**
     * Returns logging files name.
     * @return  Logging files name.
     */
    public String getLogFileName() {
        return this.logFileName;
    }

    /**
     * Returns list of messages that logger has not written to logger file yet.
     * @return  List of messages that logger has not written to logger file yet.
     */
    public List<String> getLoggerBuffer() {
        return this.loggerBuffer;
    }

    /**
     * Starts new logging thread.
     */
    public void startLogging () {
        loggerThread = new Thread(this);
        loggerThread.start();
        isLogging.set(true);
        info(this, "New logger thread has been created.");
    }

    /**
     * Stops current logging thread.
     */
    public void stopLogging() {
        isLogging.set(false);
        copyFromBufferToFile();
    }

    /**
     * Prints information message to logger file.
     * @param   obj     An object that sends the message.
     * @param   message Information message.
     */
    public void info(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String infoMessage = "[INFO]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(infoMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addInfoMessage(this, infoMessage);
        }
    }

    public void info(String message) {
        String dateTime = LocalDateTime.now().toString();
        String infoMessage = "[INFO]\t" + dateTime + ": " + message + "\n";
        loggerBuffer.add(infoMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addInfoMessage(this, infoMessage);
        }
    }

    /**
     * Prints warning message to logger file.
     * @param   obj     An object that sends the message.
     * @param   message Warning message.
     */
    public void warning(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String warningMessage = "[WARNING]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(warningMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addWarningMassage(this, warningMessage);
        }
    }

    public void warning(String message) {
        String dateTime = LocalDateTime.now().toString();
        String warningMessage = "[WARNING]\t" + dateTime + ": " + message + "\n";
        loggerBuffer.add(warningMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addWarningMassage(this, warningMessage);
        }
    }

    /**
     * Prints error message to logger file.
     * @param   obj     An object that sends the message.
     * @param   message Error message.
     */
    public void error(Object obj, String message) {
        String dateTime = LocalDateTime.now().toString();
        String errorMessage = "[ERROR]\t" + dateTime + " <" + obj.getClass().getSimpleName() + ">: " + message + "\n";
        loggerBuffer.add(errorMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addErrorMessage(this, errorMessage);
        }
    }

    public void error(String message) {
        String dateTime = LocalDateTime.now().toString();
        String errorMessage = "[ERROR]\t" + dateTime + ": " + message + "\n";
        loggerBuffer.add(errorMessage);

        if (Objects.nonNull(this.loggerPanel)) {
            this.loggerPanel.addErrorMessage(this, errorMessage);
        }
    }

    /**
     * Copy messages from logger buffer to logger file every second.
     */
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

    /**
     * Opens logger file, after that copy messages from logger buffer to logger file, finally closing logger file.
     */
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
