package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
@EnableWebMvc
public class MvcConfigurer implements WebMvcConfigurer {



	@Override
	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedHeaders("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .maxAge(-1)   
        .allowCredentials(true);

        
	}
	
}
