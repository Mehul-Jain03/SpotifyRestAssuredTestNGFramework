package com.spotify.oauth2.tests;

import org.testng.annotations.Test;
import com.spotify.oauth2.api.applicationAPI.PlaylistAPI;
import com.spotify.oauth2.api.applicationAPI.StatusCodes;
import com.spotify.oauth2.api.utils.DataLoader;
import com.spotify.oauth2.pojo.Errors;
import com.spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.spotify.oauth2.api.utils.FakerUtils.*;


@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")
public class PlaylistViaGenericMethodsTests extends BaseTest {
	
	@Story("Create a playlist")
	@Description("This Method is creating a new Playlist")
	@Test(description = "Creating a new playlist")
	public void shouldBeAbleToCreatePlaylist(){
		Playlist requestPlaylist = playlistBuilder(generateName(),generateDesc(),false);
		Response response = PlaylistAPI.post(requestPlaylist);
		assertStatusCode(response.getStatusCode(),StatusCodes.code_201.code);
		assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
	}
	
	@Test(description = "Getting Playlist details")
	public void getAPlaylists() {
	Response response =	PlaylistAPI.get(DataLoader.getInstance().getGetPlaylistId());
	assertStatusCode(response.getStatusCode(),StatusCodes.code_200.code);
	Playlist responsePlaylist = response.as(Playlist.class);
	assertThat(responsePlaylist.getName(), equalTo("Updated Playlist Name"));
	assertThat(responsePlaylist.getDescription(), equalTo("Updated playlist description"));
	assertThat(responsePlaylist.get_public(), equalTo(false));
	}
	
	@Test(description = "Updating Playlist details")
	public void updateAPlaylist() {
		Playlist requestPlaylist = playlistBuilder(generateName(),generateDesc(),false);
		Response response = PlaylistAPI.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
		assertStatusCode(response.getStatusCode(),StatusCodes.code_200.code);
	}
	
	@Story("Create a playlist")
	@Test(description = "Missing Name field in creating playlist")
	public void shouldNotBeAbleToCreatePlaylist(){
	Playlist requestPlaylist = playlistBuilder("",generateDesc(),false);
		Response response = PlaylistAPI.post(requestPlaylist);
		assertStatusCode(response.getStatusCode(),StatusCodes.code_400.code);
		Errors errorResponse = response.as(Errors.class);
		assertThat(errorResponse.getError().getStatus(),equalTo(StatusCodes.code_400.code));
		assertThat(errorResponse.getError().getMessage(),equalTo("Missing required field: name"));
	}
	
	@Step
	public Playlist playlistBuilder(String name,String description,boolean _public) {
		return Playlist.builder().name(name).description(description)._public(_public).build();
	}
	
	@Step
	public void assertPlaylistEqual(Playlist responsePlaylist,Playlist requestPlaylist) {
		assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
		assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
		assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
	}
	
	@Step
	public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
		assertThat(actualStatusCode, equalTo(expectedStatusCode));
	}

}