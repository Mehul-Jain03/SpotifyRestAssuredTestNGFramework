package com.spotify.oauth2.api.applicationAPI;

public enum StatusCodes {
	
	code_200(200),
	code_201(201),
	code_400(400),
	code_401(401);
	
	public int code;
	
	StatusCodes(int code) {
		this.code = code;
	}

}