package com.company;

import com.jayway.restassured.RestAssured;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by PC on 15.04.2017.
 */
public class ClientSesssionTest {
    @BeforeClass
    public void before() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void testGet() {
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().get("book").then().assertThat().statusCode(200).body("id", equalTo(1));

    }

    @Test(enabled = false)
    public void testPots() {
        JSONObject js = new JSONObject();
        js.put("id", 2);
        js.put("name", "InterstBook");
        js.put("title", "kkkkkkk");
        RestAssured.given().header("Accept", "application/json").and()
                .header("Content-Type", "application/json").and()
                .when().body(js.toString()).post("book").then().assertThat().statusCode(201);
    }

    @Test(enabled = false)
    public void testDelete() {
        RestAssured.delete("book/2").then().assertThat().statusCode(200);
    }

    @Test(enabled = true)
    public void testUpdate() {
        String xml = "<book>\n <id>1</id>\n <name>lololo</name> \n <title>ololol</title> \n </book>";
        RestAssured.given().header("Accept", "application/xml").and()
                .header("Content-Type", "application/xml").and()
                .when().body(xml).put("book").then().assertThat().statusCode(200);
    }
}
