package by.andd3dfx.templateapp.api;

/**
 * HTTP API path prefixes and resource paths.
 */
public final class ApiPaths {

    public static final String API_V1_PREFIX = "/api/v1";

    public static final String ARTICLES = API_V1_PREFIX + "/articles";

    private ApiPaths() {
    }

    public static String articleById(long id) {
        return ARTICLES + "/" + id;
    }
}
