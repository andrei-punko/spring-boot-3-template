package by.andd3dfx.templateapp.error.exception;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException(Long id) {
        super("an article", id);
    }
}
