package com.spotify.APIs;

import com.spotify.base.RequestSpecs;
import com.spotify.models.SpotifyModels;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static com.spotify.variables.SpotifyVariables.*;
import static com.spotify.variables.SpotifyVariables.accessToken;
import static io.restassured.RestAssured.given;

public class SpotifyApi {


    public static void refreshAccessToken(String myScopes) {
        // Token endpoint URL
        String tokenEndpoint = "https://accounts.spotify.com/api/token";

        // Send POST request to refresh the access token with scopes
        Response response = given()
                .param("grant_type", "refresh_token")
                .param("refresh_token", refreshToken)
                .param("scope", myScopes)
                .auth()
                .preemptive()
                .basic(clientId, clientSecret)
                .post(tokenEndpoint);
        accessToken = response.jsonPath().getString("access_token");
    }


    public static Response positiveGetArtist(String accessToken){


        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .header("Authorization", "Bearer " + accessToken)
                .when().get("artists/"+artistID)
                .then().log().all()
                .extract().response();
    }

    public static Response negativeGetArtist(String accessToken){ //wrong artist id

        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .header("Authorization", "Bearer " + accessToken)
                .when().get("artists/7dGJo4pcD2V6oG8kP0tJR")
                .then().log().all()
                .extract().response();
    }
    public static Response positiveGetAlbum(String accessToken){

         return given()
                 .spec(RequestSpecs.getRequestSpecs())
        .auth().oauth2(accessToken)
        .when().get("albums/"+albumRecoveryID)
        .then().log().all().extract().response();
    }

    public static Response negativeGetAlbum(String accessToken){

        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .when().get("albums/")
                .then().log().all().extract().response();
    }

    public static Response positiveSaveAlbumToCurrentUser(String accessToken, SpotifyModels albumsIDs){

        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(albumsIDs)
                .when().put("me/albums")
                .then().log().all().extract().response();

    }

    public static Response negativeSaveAlbumsToCurrentUser(String accessToken, SpotifyModels negativeAlbumsIDs){
        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(negativeAlbumsIDs)
                .when().put("me/albums")
                .then().log().all().extract().response();

    }
    public static Response positiveCheckUserSavedAlbums(String accessToken, String albumMMLP2ID, String albumRecoveryID ){

        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .param("ids",albumMMLP2ID)
                .param("ids",albumRecoveryID)
                .when().get("me/albums/contains")
                .then().log().all().extract().response();

    }

    public static Response negativeCheckUserSavedAlbums(String accessToken, String albumMMLP2ID, String albumRecoveryID){


        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .param("ids",albumMMLP2ID)
                .param("ids",albumRecoveryID)
                .when().get("me/albums/contains")
                .then().log().all().extract().response();

    }
    public static Response deleteUserSavedAlbums(String accessToken, List<String> IDs){

        return given()
                .spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(IDs)
                .when().delete("me/albums")
                .then().log().all().extract().response();


    }

    public static Response searchSpotify(String accessToken){
        return given().spec(RequestSpecs.getRequestSpecs())
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .param("q", "Ignite")
                .param("type", "track")
                .param("artist", "K-391")

                .when().get("search")
                .then().log().all().extract().response();

    }
}
