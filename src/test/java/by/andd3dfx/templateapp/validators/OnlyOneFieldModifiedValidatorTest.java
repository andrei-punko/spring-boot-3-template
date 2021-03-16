package by.andd3dfx.templateapp.validators;

import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class OnlyOneFieldModifiedValidatorTest {

    @Mock
    private ConstraintValidatorContext contextMock;

    @InjectMocks
    private OnlyOneFieldModifiedValidator onlyOneFieldModifiedValidator; // constructor injection

    @Test
    void isValidWhenNoFieldsChanged() {
        ArticleUpdateDto article = new ArticleUpdateDto();

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }

    @Test
    void isValidWhenOneFieldChanged_ForTitle() {
        ArticleUpdateDto article = new ArticleUpdateDto();
        article.setTitle("Some Title");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForSummary() {
        ArticleUpdateDto article = new ArticleUpdateDto();
        article.setSummary("Some Summary");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForText() {
        ArticleUpdateDto article = new ArticleUpdateDto();
        article.setText("Some Text");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenTwoFieldsChanged() {
        ArticleUpdateDto article = new ArticleUpdateDto();
        article.setTitle("Some Title");
        article.setSummary("Some Summary");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }

    @Test
    void isValidWhenThreeFieldsChanged() {
        ArticleUpdateDto article = new ArticleUpdateDto();
        article.setTitle("Some Title");
        article.setSummary("Some Summary");
        article.setText("Some Text");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }
}
