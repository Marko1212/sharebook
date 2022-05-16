package com.udemy.demo.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.demo.configuration.MyUserDetailsService;

@RestController
public class JwtController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private MyUserDetailsService service;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest request,
			HttpServletResponse response) throws Exception 
	{
		authenticate(request.getEmail(), request.getPassword());
		MyUserDetailsService.UserPrincipal principal = (MyUserDetailsService.UserPrincipal) service.loadUserByUsername(request.getEmail());
		
		String token = jwtUtils.generateToken(principal);
		
		Cookie cookie = new Cookie("token", token);
		response.addCookie(cookie);
		
		return ResponseEntity.ok(new JwtResponse(
				principal.getUser().getId(),
				principal.getUser().getFirstName() + " " + principal.getUser().getLastName()));
	}

	private void authenticate(String email, String password) {
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		
	}
	

}
