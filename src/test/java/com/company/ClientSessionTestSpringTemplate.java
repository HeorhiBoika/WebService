package com.company;

import com.company.bean.Book;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by PC on 03.05.2017.
 */
public class ClientSessionTestSpringTemplate {
    @Test
    public void testGetJSON(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/1";

        Book responseEntity = restTemplate.getForObject(url,Book.class);

        Assert.assertEquals("InterstBook",responseEntity.getName());
    }

    @Test(enabled = false)
    public void testDeleteBook(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/1";
        restTemplate.delete(url);
    }

    @Test
    public void testPostBookJSON(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest";
        JSONObject js = new JSONObject();
        js.put("id", 2);
        js.put("name", "Book2");
        js.put("title", "bbbbbb");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(js.toString(),headers);

        ResponseEntity<String> out = restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
        Assert.assertEquals(201,out.getStatusCode());
    }

}
