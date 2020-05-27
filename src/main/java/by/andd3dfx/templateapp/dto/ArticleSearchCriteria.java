package by.andd3dfx.templateapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchCriteria {

    private String title;
    private String timestamp;
    private String summary;
}
