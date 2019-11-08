package com.cg.voting.dto;
/**
 * Author: Swanand Pande
 * Description: Handling the user details for Spring Security
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails{

	private String username;
	private String password;
	private Boolean active = true;
	private List<GrantedAuthority> authorities;
	
	public MyUserDetails(User user) {
		this.username = user.getEmailId();
		this.password = user.getPassword();
		this.authorities = new ArrayList<GrantedAuthority>();
		if(user.getIsAdmin()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return active;
	}

}