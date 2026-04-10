package by.andd3dfx.templateapp.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private String message;
    private String code;
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationFieldError> errors;
}
