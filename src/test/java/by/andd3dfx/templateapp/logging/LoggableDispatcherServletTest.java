package by.andd3dfx.templateapp.logging;

import by.andd3dfx.templateapp.util.MemoryAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class LoggableDispatcherServletTest {

    @Test
    void responseBodyIsCopiedToClientUsingUtf8() throws Exception {
        HandlerExecutionChain chain = new HandlerExecutionChain((HttpRequestHandler) (request, response) -> {
            response.setStatus(200);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            byte[] body = ("""
                    {"msg":"кириллица","emoji":"🙂"}
                    """).getBytes(StandardCharsets.UTF_8);
            response.getOutputStream().write(body);
        });

        try (AutoCloseableServlet servlet = createServlet(chain)) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/ping");
            MockHttpServletResponse response = new MockHttpServletResponse();

            servlet.servlet().service(request, response);

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString(StandardCharsets.UTF_8))
                    .contains("кириллица")
                    .contains("🙂");
        }
    }

    @Test
    void logLineUsesUnknownPlaceholderWhenResponseBodyIsEmpty() throws Exception {
        HandlerExecutionChain chain = new HandlerExecutionChain((HttpRequestHandler) (request, response) ->
                response.setStatus(204));

        MemoryAppender memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        Logger logbackLogger = (Logger) LoggerFactory.getLogger(LoggableDispatcherServlet.class);
        logbackLogger.addAppender(memoryAppender);
        memoryAppender.start();

        try (AutoCloseableServlet servlet = createServlet(chain)) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/no-body");
            MockHttpServletResponse response = new MockHttpServletResponse();

            servlet.servlet().service(request, response);

            assertThat(response.getStatus()).isEqualTo(204);
            assertThat(memoryAppender.getLoggedEvents())
                    .anySatisfy(e -> {
                        assertThat(e.getFormattedMessage()).contains("response='[unknown]'");
                    });
        } finally {
            logbackLogger.detachAppender(memoryAppender);
            memoryAppender.stop();
        }
    }

    private static AutoCloseableServlet createServlet(HandlerExecutionChain fixedChain) throws Exception {
        StaticWebApplicationContext wac = new StaticWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        wac.setServletContext(servletContext);
        wac.getDefaultListableBeanFactory().registerSingleton("httpRequestHandlerAdapter", new HttpRequestHandlerAdapter());
        wac.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

        LoggableDispatcherServlet servlet = new LoggableDispatcherServlet() {
            @Override
            protected HandlerExecutionChain getHandler(jakarta.servlet.http.HttpServletRequest request) {
                return fixedChain;
            }
        };
        servlet.setApplicationContext(wac);
        servlet.init(new MockServletConfig(servletContext));
        return new AutoCloseableServlet(servlet);
    }

    private record AutoCloseableServlet(LoggableDispatcherServlet servlet) implements AutoCloseable {
        @Override
        public void close() {
            servlet.destroy();
        }
    }
}
