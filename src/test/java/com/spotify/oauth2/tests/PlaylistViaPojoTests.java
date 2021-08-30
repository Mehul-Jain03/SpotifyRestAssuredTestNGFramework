package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Errors;
import com.spotify.oauth2.pojo.Playlist;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PlaylistViaPojoTests extends BaseTest {
	
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
		Playlist requestPlaylist = Playlist.builder().build();
		requestPlaylist.setName("Creating Playlist Via Pojo");
		requestPlaylist.setDescription("Desc. Via Pojo");
		requestPlaylist.set_public(false);
		
		Playlist responsePlaylist = given(requestSpecification).
		body(requestPlaylist).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		extract().
		response().
		as(Playlist.class);
		
		assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
		assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
		assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
		
	}
	
	@Test
	public void updateAPlaylist() {
		Playlist requestPlaylist = Playlist.builder().build();
		requestPlaylist.setName("Updating Playlist Via Pojo");
		requestPlaylist.setDescription("Desc.Update Via Pojo");
		requestPlaylist.set_public(false);
		
		given(requestSpecification).
		body(requestPlaylist).
		when().
		put("/playlists/2s7W6T7VNiUXgGITldoian").
		then().
		assertThat().statusCode(200);
		
	}
	
	@Test
	public void shouldNotBeAbleToCreatePlaylist(){
		Playlist requestPlaylist = Playlist.builder().build();
		requestPlaylist.setName("");
		requestPlaylist.setDescription("Desc.Update Via Pojo");
		requestPlaylist.set_public(false);
		
		Errors errorResponse =	given(requestSpecification).
		body(requestPlaylist).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		extract().
		response().
		as(Errors.class);
		
		assertThat(errorResponse.getError().getStatus(),equalTo(400));
		assertThat(errorResponse.getError().getMessage(),equalTo("Missing required field: name"));
	
	}

}