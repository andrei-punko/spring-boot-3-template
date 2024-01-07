package by.andd3dfx.templateapp.services.impl;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.error.exception.ArticleNotFoundException;
import by.andd3dfx.templateapp.mappers.ArticleMapper;
import by.andd3dfx.templateapp.persistence.dao.ArticleRepository;
import by.andd3dfx.templateapp.persistence.entities.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepositoryMock;

    @Mock
    private ArticleMapper articleMapperMock;

    @Mock
    private Clock clockMock;
    private Clock fixedClock;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    public void before() {
        fixedClock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
        // Allow unnecessary stubbing:
        lenient().doReturn(fixedClock.instant()).when(clockMock).instant();
        lenient().doReturn(fixedClock.getZone()).when(clockMock).getZone();
    }

    @Test
    void create() {
        ArticleDto articleDto = ArticleDto.builder().build();
        Article article = Article.builder().build();
        Article updatedArticle = Article.builder().build();
        ArticleDto updatedArticleDto = ArticleDto.builder().build();

        when(articleMapperMock.toArticle(articleDto)).thenReturn(article);
        when(articleRepositoryMock.save(article)).thenReturn(updatedArticle);
        when(articleMapperMock.toArticleDto(updatedArticle)).thenReturn(updatedArticleDto);

        ArticleDto result = articleService.create(articleDto);

        verify(articleMapperMock).toArticle(articleDto);
        verify(articleRepositoryMock).save(article);
        verify(articleMapperMock).toArticleDto(updatedArticle);
        assertThat(result, is(updatedArticleDto));
    }

    @Test
    public void get() {
        final Long ARTICLE_ID = 123L;
        Article article = new Article();
        Optional<Article> optionalArticle = Optional.of(article);
        ArticleDto articleDto = new ArticleDto();
        when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);
        when(articleMapperMock.toArticleDto(article)).thenReturn(articleDto);

        ArticleDto result = articleService.read(ARTICLE_ID);

        verify(articleRepositoryMock).findById(ARTICLE_ID);
        verify(articleMapperMock).toArticleDto(article);
        assertThat(result, is(articleDto));
    }

    @Test
    public void getAbsentArticle() {
        final Long ARTICLE_ID = 123L;
        Optional<Article> optionalArticle = Optional.empty();
        when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);

        try {
            articleService.read(ARTICLE_ID);

            fail("Exception should be thrown");
        } catch (ArticleNotFoundException ex) {
            verify(articleRepositoryMock).findById(ARTICLE_ID);
        }
    }

    @Test
    void update() {
        final Long ARTICLE_ID = 123L;
        Article article = new Article();
        Article savedArticle = new Article();
        Optional<Article> optionalArticle = Optional.of(article);
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        ArticleDto updatedArticleDto = ArticleDto.builder()
                .title("New title")
                .build();

        when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);
        when(articleRepositoryMock.save(article)).thenReturn(savedArticle);
        when(articleMapperMock.toArticleDto(savedArticle)).thenReturn(updatedArticleDto);

        articleService.update(ARTICLE_ID, articleUpdateDto);

        verify(articleRepositoryMock).findById(ARTICLE_ID);
        verify(articleMapperMock).toArticle(articleUpdateDto, article);
        verify(articleRepositoryMock).save(article);
        verify(articleMapperMock).toArticleDto(savedArticle);
    }

    @Test
    void updateAbsentArticle() {
        final Long ARTICLE_ID = 123L;
        Optional<Article> optionalArticle = Optional.empty();
        when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();

        try {
            articleService.update(ARTICLE_ID, articleUpdateDto);

            fail("Exception should be thrown");
        } catch (ArticleNotFoundException ex) {
            verify(articleRepositoryMock).findById(ARTICLE_ID);
        }
    }

    @Test
    void delete() {
        final Long ARTICLE_ID = 123L;
        when(articleRepositoryMock.existsById(ARTICLE_ID)).thenReturn(true);

        articleService.delete(ARTICLE_ID);

        verify(articleRepositoryMock).existsById(ARTICLE_ID);
        verify(articleRepositoryMock).deleteById(ARTICLE_ID);
    }

    @Test
    void deleteAbsentArticle() {
        final Long ARTICLE_ID = 123L;
        when(articleRepositoryMock.existsById(ARTICLE_ID)).thenReturn(false);

        try {
            articleService.delete(ARTICLE_ID);

            fail("Exception should be thrown");
        } catch (ArticleNotFoundException ex) {
            verify(articleRepositoryMock).existsById(ARTICLE_ID);
            assertThat("Wrong message", ex.getMessage(), is("Could not find an article by id=" + ARTICLE_ID));
        }
    }

    @Test
    void getAllPaged() {
        final Integer pageNo = 2;
        final Integer pageSize = 20;
        final String sortBy = "title";
        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        final List<Article> articles = Arrays.asList(new Article());
        final Slice<Article> pagedResult = new PageImpl<>(articles, pageRequest, articles.size());
        final List<ArticleDto> articleDtoList = Arrays.asList(new ArticleDto());

        doReturn(pagedResult).when(articleRepositoryMock).findAll(pageRequest);
        doReturn(articleDtoList.get(0)).when(articleMapperMock).toArticleDto(articles.get(0));

        Slice<ArticleDto> result = articleService.getAll(pageRequest);

        verify(articleRepositoryMock).findAll(pageRequest);
        verify(articleMapperMock).toArticleDto(articles.get(0));
        assertThat(result.getContent().size(), is(1));
        assertThat(result.getContent().get(0), is(articleDtoList.get(0)));
    }
}
