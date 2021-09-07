package com.assignment.spring.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ConfigurationProperties()

@NoArgsConstructor
@Data

@Component
@PropertySource("classpath:local.properties")
public class LocalProperties {
	
	@Getter @Setter
	@Value("${appId}")
	private String appId;
	
	

//	@Autowired
//	public String appId(Environment environment) {
//		
//		return environment.getProperty("appId");
//	}
	
//	static private PropertiesWithJavaConfig propertiesWithJavaConfig;
	
//	@Autowired
//	private Environment environment;
//	
//	@Value("${appId}")
//    private String weatherApiKey;
	
//	public static String getWeatherApiKey() {
//		if (propertiesWithJavaConfig == null)
//			propertiesWithJavaConfig = new PropertiesWithJavaConfig();
////		return propertiesWithJavaConfig.weatherApiKey;
//		return propertiesWithJavaConfig.environment.getProperty("appId");
//	}
	
}