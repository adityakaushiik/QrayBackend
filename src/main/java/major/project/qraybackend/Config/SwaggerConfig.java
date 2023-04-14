package major.project.qraybackend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/user/.*"))
                .build();
    }

//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "My API",
//                "API description.",
//                "API version",
//                "Terms of service",
//                new Contact("My Name", "www.example.com", "myemail@example.com"),
//                "License of API", "API license URL", Collections.emptyList());
//    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addRedirectViewController("/myproject", "/");
//    }
}
