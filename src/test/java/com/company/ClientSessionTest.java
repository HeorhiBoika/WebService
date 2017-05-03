package com.company;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by PC on 15.04.2017.
 */
public class ClientSessionTest {
    @BeforeClass
    public void before() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void testGet() {
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().get("rest").then().assertThat().statusCode(200).contentType(ContentType.JSON);
    }

    @Test(enabled = true)
    public void testPost() {
        JSONObject js = new JSONObject();
        js.put("id", 1);
        js.put("name", "InterstBook");
        js.put("title", "kkkkkkk");
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().body(js.toString()).post("rest").then().assertThat().statusCode(201);
    }

    @Test
    public void testGetNewBook() {
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().get("rest/1").then().assertThat().statusCode(200).body("name", equalTo("InterstBook"));

    }

    @Test(enabled = true)
    public void testDelete() {

        RestAssured.delete("rest").then().assertThat().statusCode(200);
    }

    @Test
    public void testGetDeleteBook() {
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().get("rest/3").then().assertThat().statusCode(503);

    }

    @Test
    public void testGetNotFoundPage() {
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().get("res").then().assertThat().statusCode(404);

    }


    @Test(enabled = false)
    public void testUpdate() {
        String xml = "<book>\n <id>1</id>\n <name>lololo</name> \n <title>ololol</title> \n </book>";
        RestAssured.given().header("Accept", "application/xml").and()
                .header("Content-Type", "application/xml").and()
                .when().body(xml).put("rest/1").then().assertThat().statusCode(200);
    }
}
