package com.vinay.cust.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vinay.cust.model.Customer;
import com.vinay.cust.repository.CustomerRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private CustomerRepository customerRepository;
	private Customer cust = null;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try{
			cust = customerRepository.findByUsername(username);
		}catch(Exception e){
			throw new UsernameNotFoundException("User not found");
		}
		if(cust != null){
			return new User(cust.getUsername(),cust.getPassword(),new ArrayList<>());
		}else{
			throw new UsernameNotFoundException("User not found");
		}
		
	}

}
