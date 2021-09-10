package com.assignment.spring.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UtilsTests {

	@Test
	public void generateEncriptionForString() {
	PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
	System.out.println("***" + passwordEncoder.encode("root") + "***");
	}
}
