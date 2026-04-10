package by.andd3dfx.templateapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Pagination query parameters")
public class PaginationParams {

    public static final int MAX_PAGE_SIZE = 100;

    @Min(0)
    @Schema(description = "Page index (0-based)", defaultValue = "0", example = "0")
    private int page = 0;

    @Min(1)
    @Max(MAX_PAGE_SIZE)
    @Schema(description = "Page size", defaultValue = "50", maximum = "100", example = "50")
    private int size = 50;
}
