package com.assignment.spring.weather;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;
    
    @Autowired
    public WeatherController(WeatherService weatherService) {
    	this.weatherService = weatherService;
    }

	@GetMapping
    public WeatherEntity getWeather(@RequestParam(required=true) @NotBlank String city) {
		return weatherService.getWeather(city);
    }
}
