package by.andd3dfx.templateapp.error.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private String message;
    private String code;
    private LocalDateTime timestamp;
}
