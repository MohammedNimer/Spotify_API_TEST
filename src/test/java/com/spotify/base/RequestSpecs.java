package com.spotify.base;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestSpecs {
    public static RequestSpecification getRequestSpecs(){

        return given().baseUri("https://api.spotify.com/v1/");

    }
}
