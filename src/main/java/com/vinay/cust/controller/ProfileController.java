package com.vinay.cust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vinay.cust.model.AuthenticationRequest;
import com.vinay.cust.model.AuthenticationResponse;
import com.vinay.cust.service.MyUserDetailsService;
import com.vinay.cust.utils.JwtUtil;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtils;
	
	@GetMapping("/hello")
	public String hello(){
		return "hello world";
	}
	
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponse> createAuthToken(@RequestBody AuthenticationRequest authReq) throws Exception{
		try{
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		}catch(BadCredentialsException e){
			throw new Exception("Incorrect username or password");
		}
		final UserDetails userdetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		final String jwt = jwtTokenUtils.generateToken(userdetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
