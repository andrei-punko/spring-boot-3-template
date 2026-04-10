package by.andd3dfx.templateapp.config;

import by.andd3dfx.templateapp.api.ApiPaths;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot 3 Template API")
                        .version("1.0.0")
                        .description("""
                                Sample REST API for articles.

                                **Versioning:** URI prefix `%s` (current REST version). Breaking changes should introduce a new prefix (e.g. `/api/v2`).

                                **Errors:** JSON body `ExceptionResponse`: `message`, `code` (e.g. `BAD_REQUEST`, `NOT_FOUND`, `CONFLICT`, `METHOD_NOT_ALLOWED`), `timestamp`, and optional `errors` (field-level validation details).

                                **Security:** Endpoints are open in this template; protect them in production (see project `RECOMMENDATIONS.md`).
                                """.formatted(ApiPaths.API_V1_PREFIX))
                        .contact(new Contact().name("Template project")))
                .addServersItem(new Server().url("/").description("Current host"));
    }
}
