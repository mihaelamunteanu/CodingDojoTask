package com.assignment.spring.weather.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherExceptionInfo {
	private int code;
	private HttpStatus status;
	
	//without a the deserialization does not work - as it can't tell if 07 is for am or pm
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a") 
	private LocalDateTime timestamp = LocalDateTime.now();;
	private String message;
	private String debugMessage;
	
	WeatherExceptionInfo(HttpStatus status, String message, Throwable ex) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}
}
