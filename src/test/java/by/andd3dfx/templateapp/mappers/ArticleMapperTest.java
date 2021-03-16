package by.andd3dfx.templateapp.mappers;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.persistence.entities.Article;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class ArticleMapperTest {

    private ArticleMapper mapper = Mappers.getMapper(ArticleMapper.class);

    @Test
    void toArticleDto() {
        Article article = buildArticle();

        ArticleDto articleDto = mapper.toArticleDto(article);
        checkCompareAssertions(articleDto, article);
    }

    @Test
    void toArticleDtoForNull() {
        assertThat(mapper.toArticleDto(null), nullValue());
    }

    @Test
    void toArticleDtoList() {
        Article article = buildArticle();
        List<Article> articles = Arrays.asList(article);

        List<ArticleDto> articleDtoItems = mapper.toArticleDtoList(articles);
        assertThat("Wrong result list size", articleDtoItems.size(), is(1));
        checkCompareAssertions(articleDtoItems.get(0), article);
    }

    @Test
    void toArticleDtoListForNull() {
        assertThat(mapper.toArticleDtoList(null), nullValue());
    }

    @Test
    void toArticle() {
        ArticleDto articleDto = buildArticleDto();

        Article article = mapper.toArticle(articleDto);
        checkCompareAssertions(articleDto, article);
    }

    @Test
    void toArticleForNull() {
        assertThat(mapper.toArticle(null), nullValue());
    }

    @Test
    void toArticleWithTarget() {
        ArticleUpdateDto source = new ArticleUpdateDto();
        final String NEW_TITLE = "New title";
        source.setTitle(NEW_TITLE);
        Article target = buildArticle();
        final String OLD_TEXT = target.getText();

        mapper.toArticle(source, target);

        assertThat(target.getTitle(), is(NEW_TITLE));
        assertThat(target.getText(), is(OLD_TEXT));
    }

    @Test
    void toArticleWithTargetForNull() {
        Article target = buildArticle();
        final String OLD_TEXT = target.getText();

        mapper.toArticle(null, target);

        assertThat(target.getText(), is(OLD_TEXT));
    }

    private Article buildArticle() {
        Article article = new Article();
        article.setId(123L);
        article.setTitle("Some tittle value");
        article.setSummary("Some summary value");
        article.setText("Some text");
        article.setAuthor("John Deer");
        article.setDateCreated(LocalDateTime.of(1980, 9, 21, 0, 0));
        article.setDateUpdated(LocalDateTime.of(2011, 3, 5, 0, 0));
        return article;
    }

    private ArticleDto buildArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor("John Deer");
        articleDto.setDateCreated(LocalDateTime.of(1980, 9, 21, 0, 0));
        articleDto.setDateUpdated(LocalDateTime.of(2011, 3, 5, 0, 0));
        return articleDto;
    }

    private void checkCompareAssertions(ArticleDto articleDto, Article article) {
        assertThat("Wrong id", article.getId(), is(articleDto.getId()));
        assertThat("Wrong title", article.getTitle(), is(articleDto.getTitle()));
        assertThat("Wrong summary", article.getSummary(), is(articleDto.getSummary()));
        assertThat("Wrong text", article.getText(), is(articleDto.getText()));
        assertThat("Wrong author", article.getAuthor(), is(articleDto.getAuthor()));
        assertThat("Wrong date created", article.getDateCreated(), is(articleDto.getDateCreated()));
        assertThat("Wrong date updated", article.getDateUpdated(), is(articleDto.getDateUpdated()));
    }
}