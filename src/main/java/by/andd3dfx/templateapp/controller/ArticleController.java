package by.andd3dfx.templateapp.controller;

import by.andd3dfx.templateapp.api.ApiPaths;
import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.dto.PaginationParams;
import by.andd3dfx.templateapp.error.dto.ExceptionResponse;
import by.andd3dfx.templateapp.service.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.ARTICLES)
@Tag(name = "Articles", description = "CRUD and paged listing under " + ApiPaths.API_V1_PREFIX)
public class ArticleController {

    private static final String EX_CREATE = """
            {"title":"Spring Boot tips","summary":"Short intro","text":"Long article body.","author":"jdoe"}""";
    private static final String EX_UPDATE = """
            {"title":"Updated title"}""";

    private final IArticleService articleService;

    @Operation(
            summary = "Create new article",
            requestBody = @RequestBody(
                    required = true,
                    description = "Must not include id or timestamps; title, text, and author are required.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArticleDto.class),
                            examples = @ExampleObject(name = "create", summary = "Valid payload", value = EX_CREATE)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Article created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bean Validation failed",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto createArticle(
            @Parameter(description = "New article's data", hidden = true)
            @Validated
            @org.springframework.web.bind.annotation.RequestBody ArticleDto newArticleDto
    ) {
        return articleService.create(newArticleDto);
    }

    @Operation(
            summary = "Get existing article by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Article found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Article not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ArticleDto readArticle(
            @Parameter(description = "Article id", example = "1")
            @PathVariable("id") Long id
    ) {
        return articleService.read(id);
    }

    @Operation(
            summary = "Update article",
            requestBody = @RequestBody(
                    required = true,
                    description = "Exactly one of title, summary, or text may be set (see @OnlyOneFieldModified).",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArticleUpdateDto.class),
                            examples = @ExampleObject(name = "patchTitle", summary = "Change title only", value = EX_UPDATE)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Article updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bean Validation failed",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Article not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping("/{id}")
    public ArticleDto updateArticle(
            @Parameter(description = "Article id", example = "2")
            @PathVariable("id") Long id,
            @Parameter(hidden = true)
            @Validated
            @org.springframework.web.bind.annotation.RequestBody ArticleUpdateDto articleUpdateDto
    ) {
        return articleService.update(id, articleUpdateDto);
    }

    @Operation(
            summary = "Delete article by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Article deleted"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Article not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(
            @Parameter(description = "Article id", example = "1")
            @PathVariable("id") Long id
    ) {
        articleService.delete(id);
    }

    @Operation(
            summary = "List articles (paged slice)",
            description = """
                    Query: `page` (0-based), `size` (1–100, default 50), `sort` (see parameter below). Default sort is `title` ascending when `sort` is omitted.
                    """,
            parameters = {
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            description = "Spring Data sort: `field,direction` with direction `asc` or `desc` (e.g. `title,asc`, `author,DESC`). Not a JSON body.",
                            example = "title,asc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Slice of articles (Spring Data `Slice` of `ArticleDto`; inferred from return type)"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid pagination parameters",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping
    public Slice<ArticleDto> readArticlesPaged(
            @Valid @ParameterObject PaginationParams pagination,
            @Parameter(hidden = true)
            @SortDefault(sort = "title") Sort sort
    ) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), sort);
        return articleService.getAll(pageable);
    }
}
