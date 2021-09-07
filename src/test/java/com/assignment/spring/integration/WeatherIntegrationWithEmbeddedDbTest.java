package com.assignment.spring.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.assignment.spring.weather.WeatherRepository;

/**
 * Integration tests with embedded h2 database and MockMvc for endpoints.
 * With real calls to the external service. 
 * 
 * @author Mihaela Munteanu
 * @since 6 sept. 2021
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_embedded_db.properties")
public class WeatherIntegrationWithEmbeddedDbTest 
{
	private static final Logger logger = LoggerFactory.getLogger(WeatherIntegrationWithEmbeddedDbTest.class);

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private MockMvc mockMvc;

//	//@Before
//	public void setup() {
//		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	}

	@Test
	public void testWrongPathWithoutVersion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/weather?city=Paris"))
		.andExpect(MockMvcResultMatchers.status().isNotFound());

	}
	
	@Test
	public void testWrongCityEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/weather?city="))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

    @Test
    public void nonExistentCityIntegration() throws Exception {
    	final long entriesNoBefore = weatherRepository.count();
		mockMvc.perform(get("/api/v1/weather")
				.contentType("application/json")
				.param("city", "Bucharest123"))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.code").value("404"))
		.andExpect(jsonPath("$.message").value("city not found"));
		
		assertTrue(weatherRepository.count() == entriesNoBefore);
    }

	@Test
	void positiveTestIntegration() throws Exception {

		MvcResult result = mockMvc.perform(get("/api/v1/weather")
				.contentType("application/json")
				.param("city", "Bucharest"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value("1"))
		.andExpect(jsonPath("$.city").value("Bucharest"))
		.andExpect(jsonPath("$.country").value("RO"))
		.andExpect(jsonPath("$.temperature").exists())
		.andReturn();
		
		logger.info("Positive integration test -" + result.getResponse().toString());

		//table was dropped and recreated with each run so we expect only one item
		assertTrue(weatherRepository.count() == 1);
	}
}
