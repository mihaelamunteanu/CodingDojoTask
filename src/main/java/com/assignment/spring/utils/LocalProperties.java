package com.assignment.spring.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Component
@PropertySource("classpath:local.properties")
public class LocalProperties {
	
	@Value("${appId}")
	private String appId;
	
	@Value("${api.openweather}")
	private String weatherApi;
	
}