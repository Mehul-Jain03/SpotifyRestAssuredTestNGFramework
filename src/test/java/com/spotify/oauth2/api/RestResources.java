package com.spotify.oauth2.api;

import static io.restassured.RestAssured.given;
import static com.spotify.oauth2.api.Routes.*;
import java.util.HashMap;
import io.restassured.response.Response;

public class RestResources {
	
	public static Response post(String path, String token,Object requestObj) {
		return given(SpecBuilder.getRequestSpec()).
		body(requestObj).
		auth().oauth2(token).
		when().
		post(path).
		then().
		spec(SpecBuilder.getResponseSpec()).
		extract().
		response();
	}
	
	public static Response get(String path,String token) {
		return given(SpecBuilder.getRequestSpec()).
		auth().oauth2(token).
		when().
		get(path).
		then().
		spec(SpecBuilder.getResponseSpec()).
		extract().
		response();
	}
	
	public static Response update(String path, String token,Object requestObj) {
		return 	given(SpecBuilder.getRequestSpec()).
				auth().oauth2(token).
				body(requestObj).
				when().
				put(path).
				then().
				extract().
				response();
	}
	
	public static Response postAccount(HashMap<String, String> map) {
		return given(SpecBuilder.getAccountRequestSpec()).
		formParams(map).
		when().
		post(API+TOKEN).
		then().
		spec(SpecBuilder.getResponseSpec()).
		extract().
		response();
	}

}