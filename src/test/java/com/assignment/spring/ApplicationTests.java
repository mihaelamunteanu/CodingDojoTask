package com.assignment.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application_embedded_db.properties")
public class ApplicationTests {

	@Test
	@WithMockUser(username="someUser", password="somePassword")
	public void contextLoads() {
	}
	
}
