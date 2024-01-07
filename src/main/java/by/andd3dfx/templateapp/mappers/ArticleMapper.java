package by.andd3dfx.templateapp.mappers;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.persistence.entities.Article;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArticleMapper {

    ArticleDto toArticleDto(Article article);

    Article toArticle(ArticleDto articleDto);

    void toArticle(ArticleUpdateDto articleUpdateDto, @MappingTarget Article article);
}
