package com.vinay.cust.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vinay.cust.service.MyUserDetailsService;
import com.vinay.cust.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtils;
	private String jwt = null;
	private String username = null;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer ")){
			jwt  = authHeader.substring(7);
				
			try{
				username  = jwtTokenUtils.extractUsername(jwt);
			}catch(SignatureException e){
				System.out.println("UNATHORIZED");
				
			}catch(ExpiredJwtException e){
				System.out.println("Call refresh token");
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ){
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			try{
			if(jwtTokenUtils.validateToken(jwt,userDetails)){
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			}catch(ExpiredJwtException e){
				//System.out.println("Call refresh token");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
