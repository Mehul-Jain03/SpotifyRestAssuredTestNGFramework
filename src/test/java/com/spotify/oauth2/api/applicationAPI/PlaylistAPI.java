package com.spotify.oauth2.api.applicationAPI;

import com.spotify.oauth2.api.RestResources;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.api.utils.ConfigLoader;
import com.spotify.oauth2.pojo.Playlist;
import static com.spotify.oauth2.api.Routes.*;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PlaylistAPI {
	
	@Step
	public static Response post(Playlist requestPlaylist ) {
		return RestResources.post(USERS+"/"+ConfigLoader.getInstance().getUserId()+PLAYLISTS,TokenManager.getToken(),requestPlaylist);
	}
	
	public static Response get(String playlistId) {
		return RestResources.get(PLAYLISTS+"/"+playlistId, TokenManager.getToken());
	}
	
	public static Response update(Playlist requestPlaylist,String playlistId) {
		return RestResources.update(PLAYLISTS+"/"+playlistId, TokenManager.getToken(), requestPlaylist);
	}

}