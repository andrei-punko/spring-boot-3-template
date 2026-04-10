package by.andd3dfx.templateapp.error;

import by.andd3dfx.templateapp.error.dto.ExceptionResponse;
import by.andd3dfx.templateapp.error.dto.ValidationFieldError;
import by.andd3dfx.templateapp.error.exception.NotFoundException;
import by.andd3dfx.templateapp.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(NotFoundException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * 409.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalExceptions(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<ValidationFieldError> details = mapBindingErrors(ex);
        var body = new ExceptionResponse("Validation failed", HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), details);
        log.warn("Validation error: {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var body = new ExceptionResponse(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.name(), LocalDateTime.now(), null);
        log.warn("Client error: {}", body);
        return ResponseEntity.status(status).body(body);
    }

    /**
     * 500.
     * Throw exception for any other unpredicted reason.
     * Avoid modification of this method to make problem visible in logs.
     * Don't delete this method to avoid descendants declare it and catch 'any error'.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAnyOtherException(Exception ex) throws Exception {
        throw ex;
    }

    private static List<ValidationFieldError> mapBindingErrors(MethodArgumentNotValidException ex) {
        List<ValidationFieldError> list = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            list.add(new ValidationFieldError(fe.getField(), fe.getDefaultMessage()));
        }
        for (ObjectError oe : ex.getBindingResult().getGlobalErrors()) {
            list.add(new ValidationFieldError(oe.getObjectName(), oe.getDefaultMessage()));
        }
        list.sort(Comparator.comparing(ValidationFieldError::getField, Comparator.nullsFirst(String::compareTo))
                .thenComparing(ValidationFieldError::getMessage, Comparator.nullsFirst(String::compareTo)));
        return list;
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception ex, HttpStatus httpStatus) {
        var body = new ExceptionResponse(ex.getMessage(), httpStatus.name(), LocalDateTime.now(), null);
        if (httpStatus == HttpStatus.UNAUTHORIZED || httpStatus == HttpStatus.NOT_FOUND) {
            log.warn("Client error: {}", body);
        } else {
            log.error("Error happens: {}", body, ex);
        }
        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }
}
