package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spotify.oauth2.api.TokenManager;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;


public class PlaylistDirectTests {
	
	RequestSpecification requestSpecification;
	ResponseSpecification responseSpecification;
	String access_token = TokenManager.getToken();
	
	
	@BeforeClass
	public void settingUp() {
		
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
				setBaseUri("https://api.spotify.com").
				setBasePath("/v1").
				setContentType(ContentType.JSON).
				addHeader("Authorization", "Bearer "+access_token).
				log(LogDetail.ALL);
		requestSpecification = requestSpecBuilder.build();
		
		ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
				expectContentType(ContentType.JSON).
				log(LogDetail.ALL);
		responseSpecification = responseSpecBuilder.build();
	}
	
	@Test
	public void shouldBeAbleToCreatePlaylist(){
		String payload = "{\n"
				+ "  \"name\": \"Mehul Automation Playlist\",\n"
				+ "  \"description\": \"New playlist description\",\n"
				+ "  \"public\": false\n"
				+ "}";
		
		given(requestSpecification).
		body(payload).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		spec(responseSpecification).
		assertThat().statusCode(201).body("name", is(equalTo("Mehul Automation Playlist")),
				"description" , is(equalTo("New playlist description")),
				"public",is(equalTo(false)));
	}
	
	@Test
	public void getAPlaylists() {
		
		given(requestSpecification).
		when().
		get("/playlists/5aFRcvxTgyE4o00lFN4Gdi").
		then().
		spec(responseSpecification).
		assertThat().statusCode(200).
		body("name",is(equalTo("Updated Playlist Name")),
				"description", is(equalTo("Updated playlist description")),
				"public",is(equalTo(false)));
		
	}
	
	@Test
	public void updateAPlaylist() {
		String payload = "{\n"
				+ "  \"name\": \"Mehul Automation Playlist Updated\",\n"
				+ "  \"description\": \"New playlist description updated\",\n"
				+ "  \"public\": false\n"
				+ "}";
		
		given(requestSpecification).
		body(payload).
		when().
		put("/playlists/2s7W6T7VNiUXgGITldoian").
		then().
		assertThat().statusCode(200);
		
	}
	
	@Test
	public void shouldNotBeAbleToCreatePlaylist(){
		String payload = "{\n"
				+ "  \"name\": \"\",\n"
				+ "  \"description\": \"New playlist description\",\n"
				+ "  \"public\": false\n"
				+ "}";
		
		given(requestSpecification).
		body(payload).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		spec(responseSpecification).
		assertThat().statusCode(400).body("error.status", is(equalTo(400)),
				"error.message" , is(equalTo("Missing required field: name")));
	}

}