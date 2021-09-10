package com.assignment.spring.auth;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

	private static final long serialVersionUID = -334995428346397318L;
	private User user; 
	List<GrantedAuthority> rolesForUser;
	
	public AppUserDetails(User user) {
		this.user = user;
        this.rolesForUser = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return rolesForUser;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return (LocalDate.now().isAfter(user.getStartDate()) && LocalDate.now().isBefore(user.getExpireDate()));
	}

	@Override
	public boolean isAccountNonLocked() {
		return !user.isLockedUser();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}

}
