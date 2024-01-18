package by.andd3dfx.templateapp.mapper;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import by.andd3dfx.templateapp.persistence.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArticleMapper {

    ArticleDto toArticleDto(Article article);

    Article toArticle(ArticleDto articleDto);

    void toArticle(ArticleUpdateDto articleUpdateDto, @MappingTarget Article article);
}
