package com.assignment.spring.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.assignment.spring.CodingDojoApp;
import com.assignment.spring.auth.User;
import com.assignment.spring.auth.UserRepository;
import com.assignment.spring.weather.WeatherDojoResponse;
import com.assignment.spring.weather.WeatherEntity;
import com.assignment.spring.weather.WeatherRepository;
import com.assignment.spring.weather.exception.WeatherExceptionInfo;

/**
 * Full integration (smoke) test with closest context to production app.  
 * With postgresdb - different that the production with create-drop (see properties file). 
 * With spring.jpa.hibernate.ddl-auto = create-drop (table recreated and dropped) for each run. 
 * With real calls to the external service.
 * 
 * No MockMVC used, call endpoint with <code>TestRestTemplate.</code> 
 * 
 * @author Mihaela Munteanu
 * @since 6 sept. 2021
 */
@SpringBootTest(classes = CodingDojoApp.class, 
webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/app_test_with_postgres_db.properties")
public class WeatherIntegrationWithPostgresTest {
	private static final Logger logger = LoggerFactory.getLogger(WeatherIntegrationWithPostgresTest.class);
	
	public static final String cityOk = "Bucharest";
	public static final String cityNotOk = "Pari23244";
	
	private static String USERNAME = "WeatherMan";
	private static String PASSWORD = "root";
	
    @LocalServerPort
    private int port;
 
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private WeatherRepository weatherRepository;

	private long obj; 
 
	@BeforeEach
	public void before() {
	    // because .withBasicAuth() creates a new TestRestTemplate with the same 
	    // configuration as the autowired one.
//	     restTemplate = restTemplate.withBasicAuth(USERNAME, PASSWORD);
	     
			if (userRepository.findAll() == null) {
				final User user = new User();
				user.setUserName(USERNAME);
				user.setPassword(PASSWORD);
				user.setLockedUser(false);
				user.setActive(true);
				userRepository.save(new User());
			};
	}

	@Test
//	@WithMockUser(username = "WeatherMan", password = "root")
    public void testOkCityInURL() 
    {
		long entriesNo = weatherRepository.count();
        ResponseEntity<WeatherDojoResponse> responseEntity = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .getForEntity("http://localhost:" + port + "/api/v1/weather?city="+cityOk, WeatherDojoResponse.class);
        logger.debug("OK call response: " + responseEntity.toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().getWeatherId().equals(Long.valueOf(entriesNo).intValue()+1));        
    }
 
    @Test
    public void testNotOkCityInGetURL() {
        ResponseEntity<WeatherExceptionInfo> responseEntity = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
            .getForEntity("http://localhost:" + port + "/api/v1/weather?city="+cityNotOk, WeatherExceptionInfo.class);
        logger.debug("NOT OK city call response: " + responseEntity.toString());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getBody().getCode());
    }
    
    /**
     * Test with wrong path e.g. /weather, instead /api/v1/weather
     */
    @Test
    public void testNotOkURLPath() {
        ResponseEntity<Object> responseEntity = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
            .getForEntity("http://localhost:" + port + "/weather?city="+cityOk, Object.class);
        logger.debug("NOT OK path call response: " + responseEntity.toString());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    /**
     * Test with emtpy city /api/v1/weather?city=
     */
    @Test
    public void testNotOkPathEmptyCityValue() {
        ResponseEntity<Object> responseEntity = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
            .getForEntity("http://localhost:" + port + "/api/v1/weather?city=", Object.class);
        logger.debug("Empty city value call response: " + responseEntity.toString());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    /**
     * Test with non existent Username
     */
    @Test
    public void testWithBadCredentials() {
        ResponseEntity<Object> responseEntity = this.restTemplate.withBasicAuth("wrongUser", PASSWORD)
            .getForEntity("http://localhost:" + port + "/api/v1/weather?city="+cityOk, Object.class);
        logger.debug("Empty city value call response: " + responseEntity.toString());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
