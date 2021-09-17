package com.assignment.spring.weather;

import com.assignment.spring.utils.WeatherMessages;

import lombok.Data;

//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//    "weatherId",
//    "statusMessage"
//})
@Data
public class WeatherDojoResponse {
	
//	@JsonProperty("weatherId")
    private Integer weatherId;
	
//	@JsonProperty("statusMessage")
    private String statusMessage;
	
	public WeatherDojoResponse() {}
    
    public WeatherDojoResponse(Integer id) {
    	this.weatherId = id; 
    	this.statusMessage = WeatherMessages.ENTRY_CREATED_SUCCESSFULLY.getMessage();
    }
}
