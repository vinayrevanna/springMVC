package com.vinay.cust.security.model;

import java.util.Date;

public class AuthenticationResponse {

	private String jwt;
	private String refJwt;
	private String Status;
	private Date expTime;
	
	public AuthenticationResponse(String jwt, String refJwt, String status, Date expTime) {
		super();
		this.jwt = jwt;
		this.refJwt = refJwt;
		Status = status;
		this.expTime = expTime;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public String getRefJwt() {
		return refJwt;
	}
	public void setRefJwt(String refJwt) {
		this.refJwt = refJwt;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Date getExpTime() {
		return expTime;
	}
	public void setExpTime(Date expTime) {
		this.expTime = expTime;
	}
	
	

	
}
