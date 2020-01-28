package com.vinay.cust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.vinay.cust.model.Customer;
import com.vinay.cust.repository.CustomerRepository;

@Service
public class AuthService  implements AuthenticationManager  {
	
	@Autowired
	private CustomerRepository customerRepository;

	private Customer cust = new Customer();
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try{
			cust = customerRepository.findByUsername((String) authentication.getPrincipal());
		}catch(Exception e){
			 authentication.setAuthenticated(false);
			 throw new BadCredentialsException("Username Not found");
		}
		if(cust != null && cust.getPassword().equals(authentication.getCredentials().toString())){
			
		}else{
			authentication.setAuthenticated(false);
			throw new BadCredentialsException("Username/passworkd Incorrect");
		}
		
		return authentication;
	}

}
