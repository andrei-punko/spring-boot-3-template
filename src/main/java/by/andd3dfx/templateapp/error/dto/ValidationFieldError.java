package by.andd3dfx.templateapp.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFieldError {

    private String field;
    private String message;
}
