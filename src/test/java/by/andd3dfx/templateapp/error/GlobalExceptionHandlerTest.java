package by.andd3dfx.templateapp.error;

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
    }

    @Test
    void handleResourceNotFoundException() {
        var result = handler.handleResourceNotFoundException(new NotFoundException("name", 123L) {
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleIllegalArgumentException() {
        var result = handler.handleIllegalExceptions(new IllegalArgumentException());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleIllegalStateException() {
        var result = handler.handleIllegalExceptions(new IllegalStateException());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleAnyOtherException() {
        assertThrows(SpecificException.class, () -> handler.handleAnyOtherException(new SpecificException()));
    }

    private class SpecificException extends Exception {
    }
}
