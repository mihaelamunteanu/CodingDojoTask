package com.assignment.spring.weather;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;

import com.assignment.spring.weather.exception.CityNotFoundException;
import com.assignment.spring.weather.exception.WeatherInternalException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    WeatherService weatherService;
    
    static final String CORRECT_PATH = "/api/v1/weather/";
    
    static final String CITY_INTERNAL_ERROR = "New York"; 
    static final String CITY_OK = "Bucharest";
    static final String CITY_NOT_OK = "Parisjhadkas";
    
    static final String CITY_NOT_FOUND_MESSAGE = "city not found";
    static final String UNEXPECTED_EXCEPTION = "unexpected exception";
    
    static final WeatherEntity WEATHER_ENTITY_RECORD1 = new WeatherEntity(1, CITY_OK, "RO", 212.4);
    static final WeatherEntity WEATHER_ENTITY_RECORD2 = new WeatherEntity(2, CITY_NOT_OK, "FR", 200.4);
    
    @Test
    public void getWeather_success() throws Exception {
        
        Mockito.when(weatherService.getWeather("Bucharest")).thenReturn(WEATHER_ENTITY_RECORD1);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/weather/")
                .param("city", "Bucharest")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        		.andExpect(jsonPath("$.city").value("Bucharest"));
    }
    
    @Test
    public void getWeatherFailureCityNotFound() throws Exception {
        
        Mockito.when(weatherService.getWeather(CITY_NOT_OK)).thenThrow(
        		new CityNotFoundException(HttpStatus.NOT_FOUND, CITY_NOT_FOUND_MESSAGE, 
        				new HttpServerErrorException( HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name())));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get(CORRECT_PATH)
                .param("city", CITY_NOT_OK)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CITY_NOT_FOUND_MESSAGE));
    }
    
    /**
     * Although request url is ok - a timeout could occur, or the url is not existent anymore etc.
     * 
     * @throws Exception
     */
    @Test
    public void getWeatherFailureInternalError() throws Exception {
        
        Mockito.when(weatherService.getWeather(CITY_INTERNAL_ERROR)).thenThrow(
        		new WeatherInternalException(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_EXCEPTION, 
        				new HttpServerErrorException( HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name())));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get(CORRECT_PATH)
                .param("city", CITY_INTERNAL_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(UNEXPECTED_EXCEPTION));
    }

}
