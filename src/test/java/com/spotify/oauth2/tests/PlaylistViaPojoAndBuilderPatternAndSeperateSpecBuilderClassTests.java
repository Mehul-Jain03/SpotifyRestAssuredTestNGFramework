package com.spotify.oauth2.tests;

import org.testng.annotations.Test;
import com.spotify.oauth2.api.SpecBuilder;
import com.spotify.oauth2.pojo.Errors;
import com.spotify.oauth2.pojo.Playlist;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PlaylistViaPojoAndBuilderPatternAndSeperateSpecBuilderClassTests {
	
	@Test
	public void shouldBeAbleToCreatePlaylist(){
		Playlist requestPlaylist = Playlist.builder().name("Creating Playlist Via Pojo Builder").description("Desc. Via Bulider")._public(false).build();		
		Playlist responsePlaylist = given(SpecBuilder.getRequestSpec()).
		body(requestPlaylist).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		spec(SpecBuilder.getResponseSpec()).
		extract().
		response().
		as(Playlist.class);
		assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
		assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
		assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
		
	}
	
	@Test
	public void updateAPlaylist() {
		Playlist requestPlaylist = Playlist.builder().name("Updating Playlist Via Pojo").description("Desc.Update Via Pojo")._public(false).build();
		
		given(SpecBuilder.getRequestSpec()).
		body(requestPlaylist).
		when().
		put("/playlists/2s7W6T7VNiUXgGITldoian").
		then().
		assertThat().statusCode(200);
		
	}
	
	@Test
	public void shouldNotBeAbleToCreatePlaylist(){
		Playlist requestPlaylist = Playlist.builder().name("").description("Desc.Update Via Pojo")._public(false).build();
		
		Errors errorResponse =	given(SpecBuilder.getRequestSpec()).
		body(requestPlaylist).
		when().
		post("/users/h6iftu560uhm7y85jis3kd9nn/playlists").
		then().
		spec(SpecBuilder.getResponseSpec()).
		extract().
		response().
		as(Errors.class);
		
		assertThat(errorResponse.getError().getStatus(),equalTo(400));
		assertThat(errorResponse.getError().getMessage(),equalTo("Missing required field: name"));
	
	}

}