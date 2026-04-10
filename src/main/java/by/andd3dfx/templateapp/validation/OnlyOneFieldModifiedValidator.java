package by.andd3dfx.templateapp.validation;

import by.andd3dfx.templateapp.dto.ArticleUpdateDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlyOneFieldModifiedValidator implements ConstraintValidator<OnlyOneFieldModified, ArticleUpdateDto> {

    @Override
    public boolean isValid(ArticleUpdateDto articleUpdateDto, ConstraintValidatorContext context) {
        int count = 0;
        for (Field field : ArticleUpdateDto.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(articleUpdateDto);
                if (o != null) {
                    count++;
                }
            } catch (IllegalAccessException e) {
                log.warn("Cannot read field {} for validation", field.getName(), e);
                return false;
            }
        }
        return count == 1;
    }
}
