package com.assignment.spring.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.utils.Constants;
import com.assignment.spring.utils.LocalProperties;
import com.assignment.spring.weather.api.Main;
import com.assignment.spring.weather.api.Sys;
import com.assignment.spring.weather.api.WeatherExceptionResponse;
import com.assignment.spring.weather.api.WeatherResponse;
import com.assignment.spring.weather.exception.CityNotFoundException;
import com.assignment.spring.weather.exception.WeatherInternalException;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {
    @InjectMocks
    WeatherService weatherService;

    @Mock
    WeatherRepository weatherRepository;
    
    @Mock
    RestTemplate restTemplate;
    
    @Autowired
    LocalProperties realLocalProperties;
    
    @Mock
    LocalProperties localProperties;
    
    
    static final String CORRECT_PATH = "/api/v1/weather/";

    static final String CITY_INTERNAL_ERROR = "New York"; //the exception will mimic a timeout or any unchecked exception
    static final String CITY_NOT_OK = "Parisjhadkas";
    static final String CITY_OK = "Bucharest";
    static final String COUNTRY = "RO";
    static final Double TEMPERATURE = 212.8;
    
    static final String CITY_NOT_FOUND_MESSAGE = "city not found";
    static final String NOT_FOUND_RESPONSE = "{\"cod\":\"404\",\"message\":\"city not found\"}";

    static final String UNEXPECTED_EXCEPTION = "unexpected exception";
    static final WeatherEntity WEATHER_ENTITY_RECORD_WITHOUT_ID = new WeatherEntity(null, CITY_OK, COUNTRY, TEMPERATURE);
    static final WeatherEntity WEATHER_ENTITY_RECORD_RETURNED_WITH_ID = new WeatherEntity(1, CITY_OK, COUNTRY, TEMPERATURE);
    
	private static final String USERNAME = "WeatherMan";
	private static final String PASSWORD = "root";
    
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
    
    @Test
    @WithMockUser(username=USERNAME, password=PASSWORD)
    public void getWeather_success() throws Exception {
    	WeatherResponse weatherResponse = new WeatherResponse();
    	Main main = new Main(); main.setTemp(TEMPERATURE);
    	Sys sys = new Sys(); sys.setCountry(COUNTRY);
    	weatherResponse.setMain(main);
    	weatherResponse.setSys(sys);
    	weatherResponse.setName(CITY_OK);
    	
    	Mockito.when(localProperties.getAppId()).thenReturn(Constants.APP_ID);
    	Mockito.when(restTemplate.getForEntity(Constants.WEATHER_API_URL, WeatherResponse.class, CITY_OK, Constants.APP_ID))
    	.thenReturn(new ResponseEntity<WeatherResponse>(weatherResponse, HttpStatus.OK));
        Mockito.when(weatherRepository.save(WEATHER_ENTITY_RECORD_WITHOUT_ID)).thenReturn(WEATHER_ENTITY_RECORD_RETURNED_WITH_ID);
        
        //test service method getWeather
        WeatherEntity weatherEntity = weatherService.getWeather(CITY_OK);
        assertNotNull(weatherEntity);
        assertEquals(CITY_OK, weatherEntity.getCity());
        assertEquals(COUNTRY, weatherEntity.getCountry());
    }
    
    @Test
    @WithMockUser(username=USERNAME, password=PASSWORD)
    public void getWeather_httpStatusException() throws Exception {
    	
    	WeatherExceptionResponse weatherExceptionResponse = new WeatherExceptionResponse();
    	weatherExceptionResponse.setCod(String.valueOf(HttpStatus.NOT_FOUND.value()));
    	weatherExceptionResponse.setMessage(CITY_NOT_FOUND_MESSAGE);
    	
    	HttpServerErrorException httpErrorException = new HttpServerErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), 
    			NOT_FOUND_RESPONSE.getBytes() , null);
    	
    	Mockito.when(localProperties.getAppId()).thenReturn(Constants.APP_ID);    	
    	Mockito.when(restTemplate.getForEntity(Constants.WEATHER_API_URL, WeatherResponse.class, CITY_NOT_OK, Constants.APP_ID))
    	.thenThrow(httpErrorException);

    	CityNotFoundException thrownException = null;
    	try {
    		weatherService.getWeather(CITY_NOT_OK);
    	} catch (CityNotFoundException cityNotFoundException) {
    		thrownException = cityNotFoundException;
    	}
    	
    	assertNotNull(thrownException);
    	assertEquals(HttpStatus.NOT_FOUND, thrownException.getWeatherExceptionInfo().getStatus());
    	assertEquals(CITY_NOT_FOUND_MESSAGE, thrownException.getWeatherExceptionInfo().getMessage());
    	assertEquals(HttpStatus.NOT_FOUND.value(), thrownException.getWeatherExceptionInfo().getCode());
    }
    
    @Test
    @WithMockUser(username=USERNAME, password=PASSWORD)
    public void getWeather_internalException() throws Exception {
    	HttpServerErrorException httpErrorException = new HttpServerErrorException(
    			HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name());
    	
    	Mockito.when(localProperties.getAppId()).thenReturn(Constants.APP_ID);
    	Mockito.when(restTemplate.getForEntity(Constants.WEATHER_API_URL, WeatherResponse.class, CITY_INTERNAL_ERROR, Constants.APP_ID))
    	.thenThrow(httpErrorException);

    	WeatherInternalException thrownException = null;
    	try {
    		weatherService.getWeather(CITY_INTERNAL_ERROR);
    	} catch (WeatherInternalException weatherInternalException) {
    		thrownException = weatherInternalException;
    	}
    	
    	assertNotNull(thrownException);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrownException.getWeatherExceptionInfo().getStatus());
    	assertEquals(UNEXPECTED_EXCEPTION, thrownException.getWeatherExceptionInfo().getMessage());
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), thrownException.getWeatherExceptionInfo().getCode());
    }

}
