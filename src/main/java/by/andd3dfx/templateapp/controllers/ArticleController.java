package by.andd3dfx.templateapp.controllers;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.services.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final IArticleService articleService;

    @Operation(summary = "Create new article", responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class)),
                    responseCode = "201", description = "Article successfully created"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto createArticle(
            @Parameter(description = "New article's data")
            @Validated
            @RequestBody ArticleDto newArticleDto
    ) {
        return articleService.create(newArticleDto);
    }

    @Operation(summary = "Get existing article by id", responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class)),
                    responseCode = "200", description = "Article successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Article not found"
            ),
    })
    @GetMapping("/{id}")
    public ArticleDto readArticle(
            @Parameter(description = "Article's id")
            @NotNull
            @PathVariable("id") Long id
    ) {
        return articleService.read(id);
    }

    @Operation(summary = "Update article", responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class)),
                    responseCode = "200", description = "Article successfully updated"
            ),
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class)),
                    responseCode = "404", description = "Article not found"
            ),
    })
    @PatchMapping("/{id}")
    public ArticleDto updateArticle(
            @Parameter(description = "Article's id")
            @NotNull
            @PathVariable("id") Long id,
            @Parameter(description = "Updated fields of article")
            @Validated
            @RequestBody ArticleUpdateDto articleUpdateDto
    ) {
        return articleService.update(id, articleUpdateDto);
    }

    @Operation(summary = "Delete article by id", responses = {
            @ApiResponse(
                    responseCode = "204", description = "Article successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Article not found"
            ),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(
            @Parameter(description = "Article's id")
            @NotNull
            @PathVariable("id") Long id
    ) {
        articleService.delete(id);
    }

    @Operation(summary = "Read articles paged", responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Slice.class)),
                    responseCode = "200", description = "Articles successfully retrieved"
            ),
    })
    @GetMapping
    public Slice<ArticleDto> readArticlesPaged(
            @Parameter(description = "Page request parameters")
            @PageableDefault(size = 50)
            @SortDefault(sort = "title")
            Pageable pageable
    ) {
        return articleService.getAll(pageable);
    }
}
