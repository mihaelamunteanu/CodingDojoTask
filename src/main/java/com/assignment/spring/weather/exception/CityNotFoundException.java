package com.assignment.spring.weather.exception;

import org.springframework.http.HttpStatus;

public class CityNotFoundException extends WeatherApplicationException {
	
	private static final long serialVersionUID = 1L;
	
    public CityNotFoundException(HttpStatus httpStatus, String message, Throwable throwable) {
        super(httpStatus, message, throwable);
    }
}
