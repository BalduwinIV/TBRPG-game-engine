import engine.utils.Logger;
import engine.utils.Time;

public class TestLogger {
    public static void main(String [] args) {
        Logger logger = new Logger("AISLogger.log");
        logger.startLogging();
        Time time = new Time();
        logger.info(time, "Object of class Time has been created.");
        time.getTimeInNanoSec();
        logger.warning(time, "Got time in nanosec. (i dont know what it is).");
        System.out.println("Buffer: " + logger.getLoggerBuffer());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.error(time, "I slept for 2 seconds.");
        logger.stopLogging();
    }
}
