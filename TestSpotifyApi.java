package com.soptifyapi;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class TestSpotifyApi {
    public String token = "";
    String userId;
    String[] trackIds = {"11dFghVXANMlKmJXsNCbNl", "6rqhFgbbKwnb9MLmUQDhG6"};
    public String[] Playlist_Id = {"3cEYpjA9oz9GiPac4AsH4n", "2q7EXTCRgkOIy3tOPB5z2X"};


    @BeforeTest
    public void setup() {
        token = "Bearer BQCaIqyS9qsL0YNDtfqjloyVeyrRwqmVy-17LkAWgJEIuJDsGuWZo8smZzq6qN3UKfM-16MQ9vDHNOLjHFEoWXJsPpnhMzFJsAxCc9Jf5d5bK6p294BTrj3hqzlNkPAED9Db3zsKhVLvgk56byWQhijPuycRs7sF11VQUbAxbthjb9mqaFlOa-NYislbOjDGK8fZ6NiWytFDWnX263steJNeOD14BOcMUGEiO_DGfhzicq49tTI9ImQqHH5MGf92CO4y4RcyNVMyvbx1gb0wP7IvPYzv0jQZz-_M-ZL7";
    }

    @Test
    public void testGET_UserProfile() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization",token)
                .get("https://api.spotify.com/v1/me");

        userId = response.then().extract().path("id");
        System.out.println("User ID:"+userId);
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_UserProfile_With_UserID() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization",token)
                .get("https://api.spotify.com/v1/users/"+userId);

        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void Get_UserPlaylist() {
        Response response = given()
                .param("limit", 5)
                .param("offset", 10)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/me/playlists");
        response.prettyPrint();
        response.then().statusCode(200);

    }

    @Test
    public void Get_UserPlaylist_PlaylistId() {
        Response response = given()
                .param("market", "IN")
                .param("fields", "items(added_by.id,track(name,href,album(name,href)))")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/playlists/" + Playlist_Id[0]);
        response.prettyPrint();
        response.then().statusCode(200);

    }

    @Test
    public void Get_UserPlaylist_Item() {
        Response response = given()
                .param("market", "IN")
                .param("limit", "10")
                .param("offset", "5")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/playlists/" + Playlist_Id[0] + "/tracks");
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void Get_UserPlaylist_By_UserId() {
        Response response = given()
                .param("limit", "10")
                .param("offset", "5")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/users/" + userId + "/playlists");
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void Get_UserPlayList_ImageCover() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/playlists/" + Playlist_Id[0] + "/images");
        response.prettyPrint();
        response.then().statusCode(200);

    }

    @Test
    public void TestPost_Add_Item_Playlist() {

        String[] uris = {"spotify:track:4iV5W9uYEdYUVa79Axb7Rh", "spotify:track:1301WleyT98MSxVHPZCA6M"};
        Map<String, Object> map = new HashMap<>();
        map.put("uris", uris);

        JSONObject body = new JSONObject(map);

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(body)
                .post("https://api.spotify.com/v1/playlists/" + Playlist_Id[1] + "/tracks");

        response.prettyPrint();
        response.then().statusCode(201);


    }

    @Test
    public void TestPost_Create_Playlist() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", "Barish");
        map.put("description", "Latest Song");
        map.put("public", false);

        //create JSON object
        JSONObject body = new JSONObject(map);

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(body)
                .post("https://api.spotify.com/v1/users/" + userId + "/playlists");

        response.prettyPrint();
        response.then().statusCode(201);


    }

    @Test
    public void TestPut_Update_Playlist_Details() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Bandeya");
        map.put("description", "Latest Song");
        map.put("public", false);

        //create JSON object
        JSONObject body = new JSONObject(map);

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(body)
                .put("https://api.spotify.com/v1/playlists/" + Playlist_Id[1]);

        response.prettyPrint();
        response.then().statusCode(200);

    }

    @Test
    public void TestPut_Replace_Playlist_Item() {

        Map<String, Object> map = new HashMap<>();
        map.put("range_start", 1);
        map.put("insert_before",3);
        map.put("range_length", 2);

        //create JSON object
        JSONObject body = new JSONObject(map);

        Response response = given()
                .param("uris", "spotify:track:4iV5W9uYEdYUVa79Axb7Rh,spotify:track:1301WleyT98MSxVHPZCA6M")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(body)
                .put("https://api.spotify.com/v1/playlists/" + Playlist_Id[1]+"/tracks");

        response.prettyPrint();
        response.then().statusCode(201);


    }

    @Test
    public void testGET_AudioAnalysis_ForATrack() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/audio-analysis/"+ trackIds[0]);
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_AudioFeatures_ForSeveral_Tracks() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/audio-features?ids="+Arrays.toString(trackIds));
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_SeveralTracks() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/tracks?ids="+ trackIds[0]+"&market=IN");

        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_AudioFeatures_ForA_Tracks() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/audio-features/"+trackIds[0]);

        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_Track() {
        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/tracks/"+trackIds[0]+"?market=IN");

        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testGET_Search_Item() {
        Response response = given().queryParam("q", "Uppena")
                .queryParam("type", "album")
                .queryParam("market", "IN")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .get("https://api.spotify.com/v1/search");

        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void testDELETE_RemoveItemsFromAPlaylist() {
        //declared map object and initialized with hashmap
        Map<String,Object> mapUri = new HashMap<>();
        mapUri.put("uri", "spotify:track:5nVFeACm96rucybpDAjfK5");

        //declared and initialized array object
        ArrayList<Object> tracks = new ArrayList<>();
        tracks.add(mapUri);

        Map<String,Object> map = new HashMap<>();
        map.put("tracks", tracks);

        //created json object
        JSONObject body = new JSONObject(map);

        Response response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .body(body.toJSONString())
                .delete("https://api.spotify.com/v1/playlists/" + Playlist_Id[1] +"/tracks");

        response.prettyPrint();
        response.then().statusCode(200);
    }



}
