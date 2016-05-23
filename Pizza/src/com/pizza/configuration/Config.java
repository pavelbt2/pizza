package com.pizza.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan("com.pizza")
@EnableWebMvc
// http://outbottle.com/spring-4-web-mvc-hello-world-using-annotation-configuration-with-netbeans/
public class Config extends WebMvcConfigurerAdapter {
	
	@Bean
	public ContentNegotiatingViewResolver mainViewResolver() {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setOrder(0);
		Map<String, MediaType> mediaTypes = new HashMap<>();
		mediaTypes.put("html", new MediaType("html"));
		ContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy(mediaTypes);			
		ContentNegotiationManager manager = new ContentNegotiationManager(strategy);
		resolver.setContentNegotiationManager(manager);
	
         		
		return resolver;
	}
	
	
    @Bean
    public UrlBasedViewResolver jspViewResolver() {  
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setOrder(1);
        //resolver.setPrefix("/WEB-INF/jsp/");  
        //resolver.setSuffix(".jsp");  
        resolver.setViewClass(JstlView.class);  
        return resolver;  
    }  
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/node/**").addResourceLocations("/node/");
    }
    
    

}
