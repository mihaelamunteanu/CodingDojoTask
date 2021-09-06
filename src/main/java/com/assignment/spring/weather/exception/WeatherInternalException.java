package com.assignment.spring.weather.exception;

import org.springframework.http.HttpStatus;

public class WeatherInternalException extends WeatherApplicationException {
	private static final long serialVersionUID = 1L;
	
    public WeatherInternalException(HttpStatus httpStatus, Throwable throwable) {
        super(httpStatus, throwable);
    }
    
    public WeatherInternalException(HttpStatus httpStatus, String message, Throwable throwable) {
        super(httpStatus, message, throwable);
    }
}
