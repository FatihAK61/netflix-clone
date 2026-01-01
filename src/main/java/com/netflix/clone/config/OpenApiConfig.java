package com.netflix.clone.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "USER_TOKEN";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Netflix Clone App")
                        .version("0.0.1")
                        .description("This is the API documentation for Netflix Clone Application")
                        .contact(new Contact()
                                .name("Fatih AK")
                                .email("fatihak61@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            String httpMethod = getHttpMethod(handlerMethod);
            int order = getMethodOrder(httpMethod);
            operation.addExtension("x-order", order);
            return operation;
        };
    }

    private String getHttpMethod(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethod().getAnnotation(org.springframework.web.bind.annotation.GetMapping.class) != null) {
            return "GET";
        } else if (handlerMethod.getMethod().getAnnotation(org.springframework.web.bind.annotation.PostMapping.class) != null) {
            return "POST";
        } else if (handlerMethod.getMethod().getAnnotation(org.springframework.web.bind.annotation.PutMapping.class) != null) {
            return "PUT";
        } else if (handlerMethod.getMethod().getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class) != null) {
            return "DELETE";
        }
        return "UNKNOWN";
    }

    private int getMethodOrder(String httpMethod) {
        return switch (httpMethod) {
            case "GET" -> 0;
            case "POST" -> 1;
            case "PUT" -> 2;
            case "DELETE" -> 3;
            default -> 4;
        };
    }

}
