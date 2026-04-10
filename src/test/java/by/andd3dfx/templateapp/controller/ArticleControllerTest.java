package by.andd3dfx.templateapp.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.assertj.core.api.Assertions.assertThat;

import by.andd3dfx.templateapp.api.ApiPaths;
import by.andd3dfx.templateapp.IntegrationTestInitializer;
import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(initializers = IntegrationTestInitializer.class)
@SpringBootTest
@WebAppConfiguration
class ArticleControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    public void createArticle() throws Exception {
        ArticleDto articleDto = ArticleDto.builder()
                .title("Some tittle value")
                .summary("Some summary value")
                .text("Some text")
                .author("Some author")
                .build();

        mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
            .andExpect(jsonPath("$.summary", is(articleDto.getSummary())))
            .andExpect(jsonPath("$.text", is(articleDto.getText())))
            .andExpect(jsonPath("$.author", is(articleDto.getAuthor())))
            .andExpect(jsonPath("$.dateCreated", notNullValue()))
            .andExpect(jsonPath("$.dateUpdated", notNullValue()));
    }

    @Test
    public void createArticleWithIdPopulated() throws Exception {
        ArticleDto articleDto = ArticleDto.builder()
                .id(123L)
                .title("Some tittle value")
                .summary("Some summary value")
                .text("Some text")
                .author("Some author")
                .build();

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Article id shouldn't be present");
    }

    @Test
    public void createArticleWithoutTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Title should be populated");
    }

    @Test
    public void createArticleWithEmptyTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Title length must be between 1 and 100");
    }

    @Test
    public void createArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(createStringWithLength(101));
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Title length must be between 1 and 100");
    }

    @Test
    public void createArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary(createStringWithLength(260));
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Summary length shouldn't be greater than 255");
    }

    @Test
    public void createArticleWithoutText() throws Exception {
        ArticleDto articleDto = ArticleDto.builder()
                .title("Some title")
                .summary("Some summary value")
                .author("Some author")
                .build();

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Text should be populated");
    }

    @Test
    public void createArticleWithEmptyText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("");
        articleDto.setAuthor("Some author");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Text length should be 1 at least");
    }

    @Test
    public void createArticleWithoutAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "Author should be populated");
    }

    @Test
    public void createArticleWithDateCreatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");
        articleDto.setDateCreated(LocalDateTime.now());

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "DateCreated shouldn't be populated");
    }

    @Test
    public void createArticleWithDateUpdatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");
        articleDto.setDateUpdated(LocalDateTime.now());

        expectValidationBadRequest(mockMvc.perform(post(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        ), "DateUpdated shouldn't be populated");
    }

    @Test
    public void deleteArticle() throws Exception {
        mockMvc.perform(delete(ApiPaths.articleById(1))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAbsentArticle() throws Exception {
        expectNotFound(mockMvc.perform(delete(ApiPaths.articleById(9999))
            .contentType(APPLICATION_JSON)), 9999L);
    }

    @Test
    public void readArticle() throws Exception {
        mockMvc.perform(get(ApiPaths.articleById(1))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void readAbsentArticle() throws Exception {
        expectNotFound(mockMvc.perform(get(ApiPaths.articleById(345))
            .contentType(APPLICATION_JSON)), 345L);
    }

    @Test
    public void readArticles() throws Exception {
        mockMvc.perform(get(ApiPaths.ARTICLES)
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(10)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(50)))
            .andExpect(jsonPath("$.totalPages", is(1)))
            .andExpect(jsonPath("$.totalElements", is(10)));
    }

    @Test
    public void readArticlesWithPageSizeLimit() throws Exception {
        mockMvc.perform(get(ApiPaths.ARTICLES)
            .param("size", "5")
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPages", is(2)))
            .andExpect(jsonPath("$.totalElements", is(9)));
    }

    @Test
    public void readArticlesRejectsNegativePage() throws Exception {
        expectValidationBadRequest(mockMvc.perform(get(ApiPaths.ARTICLES)
            .param("page", "-1")
            .contentType(APPLICATION_JSON)), "greater than or equal to 0");
    }

    @Test
    public void readArticlesRejectsZeroPageSize() throws Exception {
        expectValidationBadRequest(mockMvc.perform(get(ApiPaths.ARTICLES)
            .param("size", "0")
            .contentType(APPLICATION_JSON)), "greater than or equal to 1");
    }

    @Test
    public void readArticlesRejectsPageSizeAboveMax() throws Exception {
        expectValidationBadRequest(mockMvc.perform(get(ApiPaths.ARTICLES)
            .param("size", "101")
            .contentType(APPLICATION_JSON)), "less than or equal to 100");
    }

    @Test
    public void updateArticleTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("Some tittle value")
                .build();

        mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.title", is(articleUpdateDto.getTitle())));
    }

    @Test
    public void updateArticleSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .summary("Some summary value")
                .build();

        mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.summary", is(articleUpdateDto.getSummary())));
    }

    @Test
    public void updateArticleText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("Some text value");

        mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.text", is(articleUpdateDto.getText())));
    }

    @Test
    public void updateArticleMultipleFields() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");
        articleUpdateDto.setText("Some text value");

        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        ), "Only one field should be modified at once");
    }

    @Test
    public void updateAbsentArticle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("q")
                .build();

        expectNotFound(mockMvc.perform(patch(ApiPaths.articleById(123))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))), 123L);
    }

    @Test
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("")
                .build();

        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        ), "Title length must be between 1 and 100");
    }

    @Test
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title(createStringWithLength(101))
                .build();

        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        ), "Title length must be between 1 and 100");
    }

    @Test
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .summary(createStringWithLength(260))
                .build();

        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        ), "Summary length shouldn't be greater than 255");
    }

    @Test
    public void updateArticleWithEmptyText() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .text("")
                .build();

        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        ), "Text length should be 1 at least");
    }

    @Test
    public void unsupportedHttpMethodReturnsProblemBody() throws Exception {
        mockMvc.perform(put(ApiPaths.articleById(1)))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.code", is("METHOD_NOT_ALLOWED")))
            .andExpect(jsonPath("$.message", notNullValue()))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    public void readArticlesFarFuturePageReturnsEmptySlice() throws Exception {
        mockMvc.perform(get(ApiPaths.ARTICLES)
                .param("page", "99")
                .param("size", "10")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)))
            .andExpect(jsonPath("$.empty", is(true)));
    }

    @Test
    public void readArticlesSortedByIdAscendingIsMonotonic() throws Exception {
        String json = mockMvc.perform(get(ApiPaths.ARTICLES)
                .param("sort", "id,asc")
                .param("size", "50")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode root = objectMapper.readTree(json);
        JsonNode content = root.get("content");
        assertThat(content.isArray()).isTrue();
        assertThat(content.size()).isGreaterThanOrEqualTo(2);
        long prev = content.get(0).get("id").asLong();
        for (int i = 1; i < content.size(); i++) {
            long cur = content.get(i).get("id").asLong();
            assertThat(cur).isGreaterThanOrEqualTo(prev);
            prev = cur;
        }
    }

    @Test
    public void readArticlesSortedByIdDescendingIsMonotonic() throws Exception {
        String json = mockMvc.perform(get(ApiPaths.ARTICLES)
                .param("sort", "id,desc")
                .param("size", "50")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode root = objectMapper.readTree(json);
        JsonNode content = root.get("content");
        assertThat(content.isArray()).isTrue();
        assertThat(content.size()).isGreaterThanOrEqualTo(2);
        long prev = content.get(0).get("id").asLong();
        for (int i = 1; i < content.size(); i++) {
            long cur = content.get(i).get("id").asLong();
            assertThat(cur).isLessThanOrEqualTo(prev);
            prev = cur;
        }
    }

    @Test
    public void createArticleWithMalformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiPaths.ARTICLES)
                .contentType(APPLICATION_JSON)
                .content("{ \"title\": "))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithMalformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(patch(ApiPaths.articleById(2))
                .contentType(APPLICATION_JSON)
                .content("not-json"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithEmptyBodyObject() throws Exception {
        expectValidationBadRequest(mockMvc.perform(patch(ApiPaths.articleById(2))
                .contentType(APPLICATION_JSON)
                .content("{}")), "Only one field should be modified at once");
    }

    @Test
    public void actuatorHealthIsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    public void openApiDocsAvailable() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.openapi", notNullValue()));
    }

    @Test
    public void checkSwaggerUiLinkAvailability() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isFound());
    }

    private void expectValidationBadRequest(ResultActions actions, String messageSubstring) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")))
            .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
            .andExpect(jsonPath("$.timestamp", notNullValue()))
            .andExpect(jsonPath("$.errors[*].message", hasItem(containsString(messageSubstring))));
    }

    private void expectNotFound(ResultActions actions, long articleId) throws Exception {
        actions
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code", is("NOT_FOUND")))
            .andExpect(jsonPath("$.message", is("Could not find an article by id=" + articleId)))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }

    private String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++) {
            builder.append("a");
        }
        return builder.toString();
    }
}
