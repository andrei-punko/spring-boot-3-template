package by.andd3dfx.templateapp.services;

import by.andd3dfx.templateapp.dto.ArticleDto;
import by.andd3dfx.templateapp.dto.ArticleUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArticleService {

    ArticleDto create(ArticleDto articleDto);

    ArticleDto get(Long id);

    void update(Long id, ArticleUpdateDto articleUpdateDto);

    void delete(Long id);

    List<ArticleDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Page<ArticleDto> getAll(Pageable pageable);
}
