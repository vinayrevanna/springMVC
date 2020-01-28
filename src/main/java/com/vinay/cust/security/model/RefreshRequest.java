package com.vinay.cust.security.model;

public class RefreshRequest {

	private String refJwt;

	public String getRefJwt() {
		return refJwt;
	}

	public void setRefJwt(String refJwt) {
		this.refJwt = refJwt;
	}

	public RefreshRequest(String refJwt) {
		super();
		this.refJwt = refJwt;
	}

	public RefreshRequest() {
		
	}
	
	
}
