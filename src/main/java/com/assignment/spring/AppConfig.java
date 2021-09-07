package com.assignment.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.utils.LocalProperties;

@Configuration
public class AppConfig {
	
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public LocalProperties loadLocalProperties() {
    	return new LocalProperties();
    }
}
