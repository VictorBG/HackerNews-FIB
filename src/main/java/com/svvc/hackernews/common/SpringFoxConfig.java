package com.svvc.hackernews.common;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.svvc.hackernews.model.AuthUser;

import io.swagger.models.auth.In;
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
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SpringFoxConfig
{
    @Bean
    public Docket api()
    {
        final ApiInfo apiInfo = new ApiInfo("Jeikernius REST API", "Hackernews project of group 12E of ASW",
            "1.0.0", "", null, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());

        return new Docket(DocumentationType.SWAGGER_2)
            .securitySchemes(Collections.singletonList(new ApiKey("UserToken", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.svvc.hackernews.controllers.rest"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo)
            .forCodeGeneration(true)
            .ignoredParameterTypes(AuthUser.class)
            .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public UiConfiguration uiConfiguration()
    {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .defaultModelRendering(ModelRendering.EXAMPLE)
            .docExpansion(DocExpansion.NONE)
            .operationsSorter(OperationsSorter.ALPHA)
            .tagsSorter(TagsSorter.ALPHA)
            .build();
    }

    private SecurityContext securityContext()
    {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth()
    {
        return Collections.singletonList(new SecurityReference("UserToken", new AuthorizationScope[0]));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}

