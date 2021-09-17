package com.assignment.spring.utils;

public enum WeatherMessages {
	ENTRY_CREATED_SUCCESSFULLY("Entry created successfully");
	
	private final String message;
	private WeatherMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
