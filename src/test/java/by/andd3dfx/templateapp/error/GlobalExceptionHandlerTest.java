package by.andd3dfx.templateapp.error;

import by.andd3dfx.templateapp.error.dto.ExceptionResponse;
import by.andd3dfx.templateapp.error.exception.NotFoundException;
import by.andd3dfx.templateapp.error.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleUnauthorizedException() {
        var result = handler.handleUnauthorizedException(new UnauthorizedException("msg"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        ExceptionResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("msg");
        assertThat(body.getCode()).isEqualTo("UNAUTHORIZED");
        assertThat(body.getTimestamp()).isNotNull();
        assertThat(body.getErrors()).isNull();
    }

    @Test
    void handleResourceNotFoundException() {
        var result = handler.handleResourceNotFoundException(new NotFoundException("name", 123L) {
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ExceptionResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Could not find name by id=123");
        assertThat(body.getCode()).isEqualTo("NOT_FOUND");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void handleIllegalArgumentException() {
        var result = handler.handleIllegalExceptions(new IllegalArgumentException("bad arg"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        ExceptionResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("bad arg");
        assertThat(body.getCode()).isEqualTo("CONFLICT");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void handleIllegalStateException() {
        var result = handler.handleIllegalExceptions(new IllegalStateException("bad state"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        ExceptionResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("bad state");
        assertThat(body.getCode()).isEqualTo("CONFLICT");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void handleAnyOtherException() {
        assertThrows(SpecificException.class, () -> handler.handleAnyOtherException(new SpecificException()));
    }

    private class SpecificException extends Exception {
    }
}
