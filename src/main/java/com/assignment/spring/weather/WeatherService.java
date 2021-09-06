package com.assignment.spring.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.utils.Constants;


@Service
public class WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	private final WeatherRepository weatherRepository;

	private final RestTemplate restTemplate;

	@Autowired
	public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
		this.weatherRepository = weatherRepository;
		this.restTemplate = restTemplate;
	}

	public WeatherEntity getWeather(String city) {
		final WeatherEntity weatherEntity;
		//		String url = Constants.buildURL(Constants.WEATHER_API_URL, city, Constants.APP_ID);

		ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(Constants.WEATHER_API_URL, WeatherResponse.class,city, Constants.APP_ID);

		if (logger.isDebugEnabled())
		{
			logger.debug("Trying to call repository save for weatherInfo which extends Weather Entity: " + response.getBody());
		}
		logger.info("Trying to call repository save for weatherInfo which extends Weather Entity: " + response.getBody());

		weatherEntity = convertWeatherResponseToWeatherEntity(response.getBody());
		return weatherRepository.save(weatherEntity);


	}

	private WeatherEntity convertWeatherResponseToWeatherEntity(WeatherResponse weatherResponse) {
		WeatherEntity weatherEntity = new WeatherEntity();
		weatherEntity.setCity(weatherResponse.getName());
		weatherEntity.setCountry(weatherResponse.getSys().getCountry());
		weatherEntity.setTemperature(weatherResponse.getMain().getTemp());
		return weatherEntity;
	}

}
