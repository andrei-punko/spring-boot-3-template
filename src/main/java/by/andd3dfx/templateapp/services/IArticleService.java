package by.andd3dfx.templateapp.services;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface IArticleService {

    ArticleDto create(ArticleDto articleDto);

    ArticleDto get(Long id);

    void update(Long id, ArticleUpdateDto articleUpdateDto);

    void delete(Long id);

    Slice<ArticleDto> getAll(Pageable pageable);
}
