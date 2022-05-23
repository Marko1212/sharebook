package com.udemy.demo.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.udemy.demo.user.UserInfo;
import com.udemy.demo.user.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		List<UserInfo> users =userRepository.findByEmail(login);
		
		if (users.isEmpty()) {
			throw new UsernameNotFoundException(login);
		}
		return new UserPrincipal(users.get(0));
	}

	public static class UserPrincipal implements UserDetails {
		private UserInfo user;
		
		public UserPrincipal(UserInfo user) {
			this.user = user;
		}

		public UserInfo getUser() {
			return user;
		}

		public void setUser(UserInfo user) {
			this.user = user;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			final List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			
			return authorities;
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return this.user.getPassword();
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return this.user.getEmail();
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
			return true;
		}

	}
	
	
}
