package by.andd3dfx.templateapp.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard API error response")
public class ExceptionResponse {

    @Schema(description = "Human-readable summary", example = "Validation failed")
    private String message;

    @Schema(
            description = "Machine-oriented code aligned with HTTP semantics (e.g. BAD_REQUEST, NOT_FOUND, CONFLICT, METHOD_NOT_ALLOWED, UNAUTHORIZED)",
            example = "BAD_REQUEST")
    private String code;

    @Schema(description = "Error time (server clock)", example = "2026-04-10T12:00:00")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "Present for validation errors (HTTP 400)")
    private List<ValidationFieldError> errors;
}