package major.project.qraybackend.Swagger;

import com.google.cloud.Timestamp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(("major.project.qraybackend.Controllers")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .ignoredParameterTypes(Timestamp.class)
                .securitySchemes(List.of(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer Token", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return List.of(new SecurityReference("Bearer Token", authorizationScopes));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .docExpansion(DocExpansion.NONE)
                .defaultModelExpandDepth(0)
                .defaultModelsExpandDepth(0)
                .displayOperationId(true)
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


//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket api() {
//        // Add Authorization header
//        List<Parameter> parameters = new ArrayList<>();
//        parameters.add(new ParameterBuilder()
//                .name("Authorization")
//                .description("Bearer Token")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(true)
//                .build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .build()
//                .globalOperationParameters(parameters)
//                .securitySchemes(Collections.singletonList(apiKey()));
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey("Bearer Token", "Authorization", "header");
//    }
//    @Bean
//    public Docket api() {
////        List<Parameter> globalParameters = new ArrayList<>();
////        globalParameters.add(new ParameterBuilder()
////                .name("Authorization")
////                .description("Access Token")
////                .modelRef(new springfox.documentation.schema.ModelRef("string"))
////                .parameterType("header")
////                .required(false)
////                .build());
////
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .build()
////                .globalRequestParameters(globalParameters);
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("major.project.qraybackend.Controllers"))
//                .paths(PathSelectors.regex("/api/.*"))
//                .build()
//                .apiInfo(apiInfo());
//    }

//    @Bean
//    UiConfiguration uiConfig() {
//        return UiConfigurationBuilder.builder()
//                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
//                .docExpansion(DocExpansion.NONE)
//                .defaultModelExpandDepth(0)
//                .defaultModelsExpandDepth(0)
//                .displayOperationId(true)
//                .validatorUrl("/this/that")
//                .deepLinking(true)
//                .swaggerUiBaseUrl("/apidoc")
//                .displayRequestDuration(true)
//                .validatorUrl(null)
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .build();
//    }
