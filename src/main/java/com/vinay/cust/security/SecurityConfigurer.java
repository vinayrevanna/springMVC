package com.vinay.cust.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vinay.cust.service.MyUserDetailsService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyUserDetailsService myuserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtReqFilter;
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
	// TODO Auto-generated method stub
	auth.userDetailsService(myuserDetailsService);
}

@Override
protected void configure(HttpSecurity http) throws Exception {
	http.csrf().disable().authorizeRequests().antMatchers("/authenticate","/refreshToken","/signUp").permitAll()
	.anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	http.cors();
	http.addFilterBefore(jwtReqFilter, UsernamePasswordAuthenticationFilter.class);
}

@Override
@Bean
public AuthenticationManager authenticationManagerBean() throws Exception {
	// TODO Auto-generated method stub
	return super.authenticationManagerBean();
}

@Bean
public PasswordEncoder passwordEncoder(){
	return NoOpPasswordEncoder.getInstance();
}
	
}
