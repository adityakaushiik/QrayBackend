package major.project.qraybackend.Swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.boot.starter.autoconfigure.SpringfoxConfigurationProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("major.project.qraybackend.Controllers"))
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .docExpansion(DocExpansion.NONE)
                .defaultModelExpandDepth(0)
                .defaultModelsExpandDepth(0)
                .displayOperationId(true)
                .validatorUrl("/this/that")
                .deepLinking(true)
                .swaggerUiBaseUrl("/apidoc")
                .displayRequestDuration(true)
                .validatorUrl(null)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .build();
    }
}
