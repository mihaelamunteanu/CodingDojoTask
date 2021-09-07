package com.assignment.spring.weather.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeatherApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	@Getter
	private final WeatherExceptionInfo weatherExceptionInfo;

	WeatherApplicationException(HttpStatus status, String message, Throwable ex) {
		super(message, ex);
		weatherExceptionInfo =  new WeatherExceptionInfo(status, message, ex);
	}
	
}
