import static org.junit.jupiter.api.Assertions.*;

import engine.utils.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestLogger {
    private Logger testLogger;
    @BeforeAll
    void setUp() {
        testLogger = new Logger("TestLogger.log");
        testLogger.startLogging();
    }

    @AfterAll
    void endUp() {
        testLogger.stopLogging();
    }

    @Test
    void testInfoMessageTypeString() {
        testLogger.info(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();
        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals(infoMessage.substring(0, 6), "[INFO]");
    }

    @Test
    void testInfoMessageText() {
        testLogger.info(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();

        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals(infoMessage.substring(infoMessage.length()-28, infoMessage.length()-1), "<TestLogger>: Test message.");
    }

    @Test
    void testWarningMessageTypeString() {
        testLogger.warning(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();
        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals("[WARNING]", infoMessage.substring(0, 9));
    }

    @Test
    void testWarningMessageText() {
        testLogger.warning(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();

        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals(infoMessage.substring(infoMessage.length()-28, infoMessage.length()-1), "<TestLogger>: Test message.");
    }

    @Test
    void testErrorMessageTypeString() {
        testLogger.error(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();
        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals("[ERROR]", infoMessage.substring(0, 7));
    }

    @Test
    void testErrorMessageText() {
        testLogger.warning(this, "Test message.");
        ArrayList<String> loggerBuffer = (ArrayList<String>) testLogger.getLoggerBuffer();

        String infoMessage = loggerBuffer.get(loggerBuffer.size()-1);

        assertEquals(infoMessage.substring(infoMessage.length()-28, infoMessage.length()-1), "<TestLogger>: Test message.");
    }
}
