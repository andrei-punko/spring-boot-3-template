package by.andd3dfx.templateapp.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.StandardEnvironment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartupHelperTest {

    private MemoryAppender memoryAppender;

    @BeforeEach
    public void clearLoggingStatements() {
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        Logger logger = (Logger) LoggerFactory.getLogger(StartupHelper.class.getCanonicalName());
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void logApplicationStartup() {
        final StandardEnvironment env = new StandardEnvironment();

        StartupHelper.logApplicationStartup(env);

        List<ILoggingEvent> loggedEvents = memoryAppender.getLoggedEvents();
        assertEquals(loggedEvents.size(), 1);
        assertEquals(loggedEvents.get(0).getMessage(), "\n"
                + "----------------------------------------------------------\n"
                + "\tApplication '{}' is running! Access URLs:\n"
                + "\tLocal: \t\t{}://localhost:{}{}\n"
                + "\tExternal: \t{}://{}:{}{}\n"
                + "\tProfile(s): \t{}\n"
                + "----------------------------------------------------------");
    }
}
