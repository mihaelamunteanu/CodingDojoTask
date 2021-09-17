package com.assignment.spring.weather;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.spring.weather.exception.CityNotFoundException;
import com.assignment.spring.weather.exception.WeatherExceptionInfo;
import com.assignment.spring.weather.exception.WeatherInternalException;

@RestController
@RequestMapping("api/v1/weather")
@Validated
public class WeatherController {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    
    private final WeatherService weatherService;
    
    @Autowired
    public WeatherController(WeatherService weatherService) {
    	this.weatherService = weatherService;
    }

	@GetMapping
    public WeatherDojoResponse getWeather(@RequestParam(required=true) @NotBlank String city) throws CityNotFoundException, WeatherInternalException {
		return weatherService.getWeather(city);
    }
	
	@ExceptionHandler(value = CityNotFoundException.class)
    public ResponseEntity<WeatherExceptionInfo> handleCityNotFoundException(CityNotFoundException cityNotFoundException) {
		logger.debug(cityNotFoundException.getMessage());
        return new ResponseEntity<WeatherExceptionInfo>(cityNotFoundException.getWeatherExceptionInfo(), 
        		cityNotFoundException.getWeatherExceptionInfo().getStatus());
	}
	
	@ExceptionHandler(value = WeatherInternalException.class)
    public ResponseEntity<WeatherExceptionInfo> handleWeatherInternalException(WeatherInternalException weatherInternalException) {
		logger.debug(weatherInternalException.getMessage());
        return new ResponseEntity<WeatherExceptionInfo>(weatherInternalException.getWeatherExceptionInfo(), 
        		weatherInternalException.getWeatherExceptionInfo().getStatus());
	}	
	
	@ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<WeatherExceptionInfo> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
		logger.debug(constraintViolationException.getMessage());
        return new ResponseEntity<WeatherExceptionInfo>(HttpStatus.BAD_REQUEST);
	}	
}
