package com.assignment.spring.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
		@Query("SELECT u FROM User u WHERE u.userName = ?1")  //JPQL
	    Optional<User> findByUserName(String userName);
}
