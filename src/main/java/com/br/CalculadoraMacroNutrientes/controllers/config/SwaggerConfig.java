package com.br.CalculadoraMacroNutrientes.controllers.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc //para nao dar problema com o Spring Fox (swagger)
@EnableSwagger2
@Component
//@Profile("prod")
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
	    return new Docket(DocumentationType.SWAGGER_2)
	        .select()
	        .apis(RequestHandlerSelectors.basePackage("com.br.CalculadoraMacroNutrientes.controllers")) //exibe apenas as controller do projeto, e nao do Spring tambem
	        .paths(PathSelectors.any()) //os caminhos das controllers que serão visiveis .ant("/*")
	        .build()
	        .useDefaultResponseMessages(false);
	        //.globalResponseMessage(RequestMethod.GET, responseMessageForGET());
	}

	private List<ResponseMessage> responseMessageForGET()
	{
	    return new ArrayList<ResponseMessage>() {{
	        add(new ResponseMessageBuilder()
	            .code(500)
	            .message("500 message")
	            .responseModel(new ModelRef("Error"))
	            .build());
	        add(new ResponseMessageBuilder()
	            .code(403)
	            .message("Forbidden!")
	            .build());
	    }
	    };
	}
}
