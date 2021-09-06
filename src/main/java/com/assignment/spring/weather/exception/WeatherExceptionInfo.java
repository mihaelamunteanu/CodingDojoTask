package com.assignment.spring.weather.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class WeatherExceptionInfo {
	private int code;
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();;
	private String message;
	private String debugMessage;
	
	WeatherExceptionInfo(HttpStatus status, Throwable ex) {
		this.code = status.value();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	WeatherExceptionInfo(HttpStatus status, String message, Throwable ex) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}
}
