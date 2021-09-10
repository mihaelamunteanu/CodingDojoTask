package com.assignment.spring.integration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MyMicroserviceConfigurationForTest {

    @Bean
    public TestRestTemplate testRestTemplate(TestRestTemplate testRestTemplate, SecurityProperties securityProperties) {
        return testRestTemplate.withBasicAuth(securityProperties.getUser().getName(), securityProperties.getUser().getPassword());
    }

}