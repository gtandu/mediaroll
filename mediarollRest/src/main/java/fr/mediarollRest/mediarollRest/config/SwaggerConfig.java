package fr.mediarollRest.mediarollRest.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("fr.mediarollRest.mediarollRest.web.resource"))             
          .paths(PathSelectors.any())                          
          .build().apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
          "MediaRoll API REST", 
          "Some custom description of API.", 
          "API TOS", 
          "Terms of service", 
          new Contact("Mapella Corentin & Tandu Glodie", "www.mediaroll.xyz", "myeaddress@company.com"), 
          "License of API", "API license URL", Collections.emptyList());
   }
}
