package com.vinay.cust.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vinay.cust.dto.CustomerDto;
import com.vinay.cust.model.Customer;
import com.vinay.cust.security.model.AuthenticationRequest;
import com.vinay.cust.security.model.AuthenticationResponse;
import com.vinay.cust.security.model.RefreshRequest;
import com.vinay.cust.security.model.SignupResponse;
import com.vinay.cust.service.AuthService;
import com.vinay.cust.service.CustomerService;
import com.vinay.cust.service.MyUserDetailsService;
import com.vinay.cust.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
@CrossOrigin("http://localhost:4200")
public class ProfileController {

	@Autowired
	private AuthService authManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtils;
	@Autowired
	private CustomerService custService;

	@GetMapping("/hello")
	public ResponseEntity<SignupResponse> hello() {
		final String status = "Success";
		return new ResponseEntity<SignupResponse>(new SignupResponse("Hello User!"), HttpStatus.OK);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponse> createAuthToken(@RequestBody AuthenticationRequest authReq)
			throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		} catch (BadCredentialsException e) {

			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", "", "failure", new Date()),
					HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userdetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		final String jwt = jwtTokenUtils.generateAccessToken(userdetails);
		final String refJwt = jwtTokenUtils.generateRefToken(userdetails);
		custService.updateRefToken(refJwt, authReq.getUsername());
		final Date expDate = jwtTokenUtils.extractExpiration(jwt);
		final String status = "Success";
		return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt, refJwt, status, expDate),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public ResponseEntity<SignupResponse> createCustomer(@RequestBody CustomerDto customer) {
		try {
			custService.saveCustomer(customer);
			return new ResponseEntity<SignupResponse>(new SignupResponse("New user successfully saved"), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			// e.printStackTrace();
			return new ResponseEntity<SignupResponse>(new SignupResponse("Username Already Exists!"),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<SignupResponse>(new SignupResponse("New user Creation failed"),
					HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponse> validateRefreshToken(@RequestBody RefreshRequest refReq) {
		String username = "";
		try {
			username = jwtTokenUtils.extractUsername(refReq.getRefJwt());
		} catch (SignatureException e) {
			System.out.println("UNATHORIZED refres");
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", "", "failure", new Date()),
					HttpStatus.UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", "", "failure", new Date()),
					HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userdetails = userDetailsService.loadUserByUsername(username);
		if (custService.getRefToken(username).equals(refReq.getRefJwt())) {
			if (jwtTokenUtils.validateToken(refReq.getRefJwt(), userdetails)) {
				final String jwt = jwtTokenUtils.generateAccessToken(userdetails);
				final String refJwt = jwtTokenUtils.generateRefToken(userdetails);
				custService.updateRefToken(refJwt, username);
				final Date expDate = jwtTokenUtils.extractExpiration(jwt);
				final String status = "Success";
				return new ResponseEntity<AuthenticationResponse>(
						new AuthenticationResponse(jwt, refJwt, status, expDate), HttpStatus.OK);
			} else {
				return new ResponseEntity<AuthenticationResponse>(
						new AuthenticationResponse("", "", "failure", new Date()), HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse("", "", "failure", new Date()),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public List<Customer> getAllCustomers() {
		return custService.getAllCustomers();
	}

}
