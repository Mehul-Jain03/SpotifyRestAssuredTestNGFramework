package com.spotify.oauth2.api;

import java.time.Instant;
import java.util.HashMap;

import com.spotify.oauth2.api.utils.ConfigLoader;

import io.restassured.response.Response;

public class TokenManager {
	
	private static String access_token;
	private static Instant expiry_time;
	
	public synchronized static String getToken() {
		
		try{
			if(access_token == null || Instant.now().isAfter(expiry_time)) {
				System.out.println("Renewing the token...");
				Response response  = refreshToken();
				access_token = response.path("access_token");
				int expiryDurationInSec = response.path("expires_in");
				expiry_time = Instant.now().plusSeconds(expiryDurationInSec - 300);
			}
			else {
				System.out.println("Token is good to use");
			}
		}
		catch (Exception e) {
				throw new RuntimeException("Abort!!");
		}
		return access_token;
	}
	
	private static Response refreshToken() {
		HashMap<String , String> map = new HashMap<String, String>();
		map.put("grant_type", ConfigLoader.getInstance().getGrantType());
		map.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
		map.put("client_id", ConfigLoader.getInstance().getClientId());
		map.put("client_secret", ConfigLoader.getInstance().getClientSecret());
		Response response = RestResources.postAccount(map);
	if(response.getStatusCode() != 200) {
		throw new RuntimeException("Refresh Token is Expired! Can't Generate New Token");
	}
	return response;
	}
}