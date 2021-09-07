package com.assignment.spring.weather;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.utils.Constants;
import com.assignment.spring.utils.LocalProperties;
import com.assignment.spring.weather.api.WeatherExceptionResponse;
import com.assignment.spring.weather.api.WeatherResponse;
import com.assignment.spring.weather.exception.CityNotFoundException;
import com.assignment.spring.weather.exception.WeatherInternalException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for business logic of Weather related requests.
 * 
 * @author Mihaela Munteanu
 * @since 7 sept. 2021
 */
@Service
public class WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
	
	private final WeatherRepository weatherRepository;
    
    private final RestTemplate restTemplate;
    
    private final LocalProperties localProperties;
    
    @Autowired
    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate, LocalProperties localProperties) {
    	this.weatherRepository = weatherRepository;
    	this.restTemplate = restTemplate;
    	this.localProperties = localProperties;
    }
	
	public WeatherEntity getWeather(String city) throws CityNotFoundException, WeatherInternalException {
		final WeatherEntity weatherEntity;
//		String url = Constants.buildURL(Constants.WEATHER_API_URL, city, Constants.APP_ID);
		try {
			
			ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(
					Constants.WEATHER_API_URL, 
					WeatherResponse.class,city, 
					localProperties.getAppId());
//					environment.getProperty("appId"));
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Trying to call repository save for weatherInfo which extends Weather Entity: " + response.getBody());
			}
			logger.info("Trying to call repository save for weatherInfo which extends Weather Entity: " + response.getBody());
			
			weatherEntity = convertWeatherResponseToWeatherEntity(response.getBody());
			return weatherRepository.save(weatherEntity);
			
		} catch (HttpStatusCodeException httpStatusException) {//HttpServerErrorException | HttpClientErrorException
			ObjectMapper mapper = new ObjectMapper();
			try {
				WeatherExceptionResponse weatherExceptionResponse = 
						mapper.readValue(httpStatusException.getResponseBodyAsString(), WeatherExceptionResponse.class);
				if (HttpStatus.BAD_REQUEST.equals(httpStatusException.getStatusCode())) {
					logger.debug("City not provided or wrong format: " + weatherExceptionResponse);
					throw new WeatherInternalException(HttpStatus.BAD_REQUEST, weatherExceptionResponse.getMessage(), httpStatusException); 
				}
				logger.debug("City not found with error: " + weatherExceptionResponse);
				throw new CityNotFoundException(httpStatusException.getStatusCode(), weatherExceptionResponse.getMessage(), httpStatusException);
			} catch (IOException e) {
				logger.error("Calling " + Constants.WEATHER_API_URL + " for " + city + "and hardcoded ID from app returned unexpected exception", e);
				throw new WeatherInternalException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected exception", e);
			}
        } 
	}
	
    private WeatherEntity convertWeatherResponseToWeatherEntity(WeatherResponse weatherResponse) {
    	WeatherEntity weatherEntity = new WeatherEntity();
    	weatherEntity.setCity(weatherResponse.getName());
    	weatherEntity.setCountry(weatherResponse.getSys().getCountry());
    	weatherEntity.setTemperature(weatherResponse.getMain().getTemp());
        return weatherEntity;
}
		
}
