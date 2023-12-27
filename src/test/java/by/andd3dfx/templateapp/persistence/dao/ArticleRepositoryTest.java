package by.andd3dfx.templateapp.persistence.dao;

import by.andd3dfx.templateapp.persistence.entities.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;

    private Article entity;
    private Article entity2;
    private Article entity3;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        entity = buildArticle("Ivan", "HD", LocalDateTime.parse("2010-12-03T10:15:30"));
        entity2 = buildArticle("Vasily", "HD", LocalDateTime.parse("2011-12-03T10:15:30"));
        entity3 = buildArticle("Ivan", "4K", LocalDateTime.parse("2012-12-03T10:15:30"));
        repository.saveAll(Arrays.asList(entity, entity2, entity3));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void findAll() {
        var result = repository.findAll(Pageable.ofSize(10));

        assertThat("Wrong records amount", result.getNumberOfElements(), is(3));
        assertTrue(result.getContent().containsAll(List.of(entity, entity2, entity3)));
    }

    @Test
    public void findAll_withPageNSizeNSorting() {
        var result = repository.findAll(PageRequest.of(0, 2, Sort.by("title", "summary")));

        assertThat("Wrong records amount", result.getNumberOfElements(), is(2));
        var articles = result.getContent();
        assertThat(articles.get(0), is(entity3));
        assertThat(articles.get(1), is(entity));
    }

    private static Article buildArticle(String title, String summary, LocalDateTime timestamp) {
        Article article = new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setText("any text");
        article.setTimestamp(timestamp);
        article.setAuthor("Some author");
        return article;
    }
}