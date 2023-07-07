package com.spotify;

import com.spotify.APIs.SpotifyApi;
import com.spotify.data.ErorrsRoute;
import com.spotify.models.ErrorsModels;
import com.spotify.models.SpotifyModels;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.APIs.SpotifyApi.refreshAccessToken;
import static com.spotify.variables.SpotifyVariables.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SpotifyTest {


    @Test
    public void positiveGetArtistInfo(){
        refreshAccessToken(myScopes);

        Response respo= SpotifyApi.positiveGetArtist(accessToken);

        String name=respo.jsonPath().getString("name");

        assertThat(name,equalTo("Eminem"));
        assertThat(respo.statusCode(), equalTo(200));

    }

    @Test
    public void negativeGetArtistInfo(){ //with wrong artist ID
        refreshAccessToken(myScopes);
        Response respo=SpotifyApi.negativeGetArtist(accessToken);

        ErrorsModels returnedError=respo.body().as(ErrorsModels.class);

        assertThat(returnedError.getError().getMessage(),equalTo(ErorrsRoute.INVALID_ID));
        assertThat(returnedError.getError().getStatus(), equalTo(400));

    }

    @Test
    public void positiveGetAlbum(){
        refreshAccessToken(myScopes);

        Response respo =SpotifyApi.positiveGetAlbum(accessToken);

        String albumName=respo.jsonPath().getString("name");

        assertThat(albumName,equalTo("Recovery"));
        assertThat(respo.statusCode(),equalTo(200));
    }

    @Test
    public void negativeGetAlbum(){ //without Album ID
        refreshAccessToken(myScopes);

        Response respo =SpotifyApi.negativeGetAlbum(accessToken);

        ErrorsModels returnedError=respo.body().as(ErrorsModels.class);

        assertThat(returnedError.getError().getStatus(),equalTo(400));
        assertThat((returnedError.getError().getMessage()),equalTo(ErorrsRoute.INVALID_ID));
    }

    @Test
    public void positiveSaveAlbumsToCurrentUser(){
        refreshAccessToken(myScopes);
        SpotifyModels albumsIDs=new SpotifyModels(IDs);

        Response respo = SpotifyApi.positiveSaveAlbumToCurrentUser(accessToken, albumsIDs);

        assertThat(respo.statusCode(),equalTo(200));

    }

    @Test
    public void negativeSaveAlbumsToCurrentUser(){ //with wrong IDs
        refreshAccessToken(myScopes);
        SpotifyModels negativeAlbumsIDs=new SpotifyModels(negativeIDs);
        Response respo = SpotifyApi.negativeSaveAlbumsToCurrentUser(accessToken, negativeAlbumsIDs);

        ErrorsModels returnedError=respo.body().as(ErrorsModels.class);
        assertThat(returnedError.getError().getStatus(),equalTo(400));
        assertThat(returnedError.getError().getMessage(),equalTo(ErorrsRoute.BAD_REQUEST));

    }

    @Test
    public void positiveCheckUserSavedAlbums(){
        refreshAccessToken(myScopes);

        SpotifyModels albumsIDs=new SpotifyModels(IDs);
        SpotifyApi.positiveSaveAlbumToCurrentUser(accessToken, albumsIDs);

        Response respo =SpotifyApi.positiveCheckUserSavedAlbums(accessToken, albumMMLP2ID,albumRecoveryID);

        Boolean returnedResult= respo.jsonPath().getBoolean("[0]");
        assertThat(returnedResult,equalTo(true));
        assertThat(respo.statusCode(),equalTo(200));

    }

    @Test ()
    public void negativeCheckUserSavedAlbums(){ //when the albums are not saved in the library
        refreshAccessToken(myScopes);

        SpotifyApi.deleteUserSavedAlbums(accessToken, IDs);
        Response respo =SpotifyApi.negativeCheckUserSavedAlbums(accessToken,albumMMLP2ID,albumRecoveryID);

        Boolean returnedResult= respo.jsonPath().getBoolean("[0]");
        assertThat(returnedResult,equalTo(false));
        assertThat(respo.statusCode(),equalTo(200));

    }

    @Test
    public void deleteUserSavedAlbums(){
        refreshAccessToken(myScopes);

        Response respo = SpotifyApi.deleteUserSavedAlbums(accessToken, IDs);

                assertThat(respo.statusCode(),equalTo(200));
    }

    @Test
    public void searchInSpotify(){
        refreshAccessToken(myScopes);

        Response respo =SpotifyApi.searchSpotify(accessToken);
        String artistName=respo.jsonPath().getString("tracks.items[0].artists[0].name");

        assertThat(respo.statusCode(),equalTo(200));
        assertThat(artistName,equalTo("Alan Walker"));

    }
}
