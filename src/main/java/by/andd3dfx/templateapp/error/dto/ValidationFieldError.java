package by.andd3dfx.templateapp.error.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Single field or object-level validation error")
public class ValidationFieldError {

    @Schema(description = "Property name from the request", example = "title")
    private String field;

    @Schema(description = "Validation message", example = "Title should be populated")
    private String message;
}