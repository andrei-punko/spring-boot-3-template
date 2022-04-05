package by.andd3dfx.templateapp.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.templateapp.IntegrationTestInitializer;
import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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

        mockMvc.perform(post("/api/v1/articles")
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

        final String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Article id shouldn't be present"));
    }

    @Test
    public void createArticleWithoutTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        final String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title should be populated"));
    }

    @Test
    public void createArticleWithEmptyTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        final String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void createArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(createStringWithLength(101));
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void createArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary(createStringWithLength(260));
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Summary length shouldn't be greater than 255"));
    }

    @Test
    public void createArticleWithoutText() throws Exception {
        ArticleDto articleDto = ArticleDto.builder()
                .title("Some title")
                .summary("Some summary value")
                .author("Some author")
                .build();

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text should be populated"));
    }

    @Test
    public void createArticleWithEmptyText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("");
        articleDto.setAuthor("Some author");

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text length should be 1 at least"));
    }

    @Test
    public void createArticleWithoutAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Author should be populated"));
    }

    @Test
    public void createArticleWithDateCreatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");
        articleDto.setDateCreated(LocalDateTime.now());

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateCreated shouldn't be populated"));
    }

    @Test
    public void createArticleWithDateUpdatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("Some author");
        articleDto.setDateUpdated(LocalDateTime.now());

        String message = mockMvc.perform(post("/api/v1/articles")
            .contentType(APPLICATION_JSON)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateUpdated shouldn't be populated"));
    }

    @Test
    public void deleteArticle() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/1")
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAbsentArticle() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/9999")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void readArticle() throws Exception {
        mockMvc.perform(get("/api/v1/articles/1")
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void readAbsentArticle() throws Exception {
        mockMvc.perform(get("/api/v1/articles/345")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void readArticles() throws Exception {
        mockMvc.perform(get("/api/v1/articles")
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
        mockMvc.perform(get("/api/v1/articles")
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
    public void updateArticleTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("Some tittle value")
                .build();

        mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .summary("Some summary value")
                .build();

        mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("Some text value");

        mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleMultipleFields() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");
        articleUpdateDto.setText("Some text value");

        String message = mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Only one field should be modified at once"));
    }

    @Test
    public void updateAbsentArticle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("q")
                .build();

        mockMvc.perform(patch("/api/v1/articles/123")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title("")
                .build();

        String message = mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .title(createStringWithLength(101))
                .build();

        String message = mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .summary(createStringWithLength(260))
                .build();

        String message = mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Summary length shouldn't be greater than 255"));
    }

    @Test
    public void updateArticleWithEmptyText() throws Exception {
        ArticleUpdateDto articleUpdateDto = ArticleUpdateDto.builder()
                .text("")
                .build();

        String message = mockMvc.perform(patch("/api/v1/articles/2")
            .contentType(APPLICATION_JSON)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text length should be 1 at least"));
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
