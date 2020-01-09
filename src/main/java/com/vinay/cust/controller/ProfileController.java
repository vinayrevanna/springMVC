package com.vinay.cust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vinay.cust.dto.CustomerDto;
import com.vinay.cust.model.Customer;
import com.vinay.cust.security.model.AuthenticationRequest;
import com.vinay.cust.security.model.AuthenticationResponse;
import com.vinay.cust.service.CustomerService;
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
	@Autowired
	private CustomerService custService;
	
	
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
			//return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userdetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		final String jwt = jwtTokenUtils.generateToken(userdetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@PostMapping("/customer")
	public ResponseEntity<String> addCustomerProfile(@RequestBody CustomerDto customer){
		
		custService.saveCustomer(customer);
		return new ResponseEntity<>("New user successfully saved", HttpStatus.CREATED);
	}
	
	@PutMapping("/customer")
	public ResponseEntity<String> updateCustomerProfile(@RequestBody CustomerDto customer){
		
		custService.updateCustomer(customer);
		return new ResponseEntity<>("User successfully updated", HttpStatus.CREATED);
		
	}
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable String id){
		return new ResponseEntity<>(custService.getCustomer(id),HttpStatus.OK);
		//return new ResponseEntity<>(custService.findByCustomerId(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable String id){
		custService.deleteCustomer(id);
		return new ResponseEntity<String>("Customer Deleted Successfully",HttpStatus.OK);
	}

}
