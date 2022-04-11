package by.andd3dfx.templateapp.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;

/**
 * Class for testing purposes to catch logs.
 */
public class MemoryAppender extends ListAppender<ILoggingEvent> {

    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(this.list);
    }
}
