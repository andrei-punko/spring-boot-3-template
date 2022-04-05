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
    private OnlyOneFieldModifiedValidator onlyOneFieldModifiedValidator;

    @Test
    void isValidWhenNoFieldsChanged() {
        var articleUpdateDto = new ArticleUpdateDto();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(false));
    }

    @Test
    void isValidWhenOneFieldChanged_ForTitle() {
        var articleUpdateDto = ArticleUpdateDto.builder()
                .title("Some Title")
                .build();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForSummary() {
        var articleUpdateDto = ArticleUpdateDto.builder()
                .summary("Some Summary")
                .build();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForText() {
        var articleUpdateDto = ArticleUpdateDto.builder()
                .text("Some Text")
                .build();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(true));
    }

    @Test
    void isValidWhenTwoFieldsChanged() {
        var articleUpdateDto = ArticleUpdateDto.builder()
                .title("Some Title")
                .summary("Some Summary")
                .build();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(false));
    }

    @Test
    void isValidWhenThreeFieldsChanged() {
        var articleUpdateDto = ArticleUpdateDto.builder()
                .title("Some Title")
                .summary("Some Summary")
                .text("Some Text")
                .build();

        assertThat(onlyOneFieldModifiedValidator.isValid(articleUpdateDto, contextMock), is(false));
    }
}
