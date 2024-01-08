package by.andd3dfx.templateapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class ArticleDto {

    @Null(message = "Article id shouldn't be present")
    @Schema(description = "The database generated article ID")
    private Long id;

    @NotNull(message = "Title should be populated")
    @Size(min = 1, max = 100, message = "Title length must be between 1 and 100")
    @Schema(description = "Article's title", required = true)
    private String title;

    @Size(max = 255, message = "Summary length shouldn't be greater than 255")
    @Schema(description = "Article's summary")
    private String summary;

    @NotNull(message = "Text should be populated")
    @Size(min = 1, message = "Text length should be 1 at least")
    @Schema(description = "Article's text", required = true)
    private String text;

    @NotNull(message = "Author should be populated")
    @Size(max = 50, message = "Author length shouldn't be greater than 50")
    @Schema(description = "Article's author", required = true)
    private String author;

    @Null(message = "DateCreated shouldn't be populated")
    @Schema(description = "Date & time of article creation")
    private LocalDateTime dateCreated;

    @Null(message = "DateUpdated shouldn't be populated")
    @Schema(description = "Date & time of article update")
    private LocalDateTime dateUpdated;
}
