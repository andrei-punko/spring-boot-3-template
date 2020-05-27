package by.andd3dfx.templateapp.exceptions;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException(Long id) {
        super("an article", id);
    }
}
