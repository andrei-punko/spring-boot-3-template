package by.andd3dfx.templateapp.error;

import by.andd3dfx.templateapp.error.exception.NotFoundException;
import by.andd3dfx.templateapp.error.exception.UnauthorizedException;
import by.andd3dfx.templateapp.error.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Add more methods if needed.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 401.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 404.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(NotFoundException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * 409.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity handleIllegalExceptions(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT);
    }

    /**
     * 500.
     * Throw exception for any other unpredicted reason.
     * Avoid modification of this method to make problem visible in logs.
     * Don't delete this method to avoid descendants declare it and catch 'any error'.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleOtherExceptions(Exception ex) throws Exception {
        throw ex;
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception ex, HttpStatus httpStatus) {
        var body = new ExceptionResponse(ex.getMessage(), httpStatus.name(), LocalDateTime.now());
        log.error("Error happens: " + body);
        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }
}
