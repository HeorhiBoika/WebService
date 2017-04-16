package com.company;

import org.json.JSONObject;

/**
 * Created by PC on 13.04.2017.
 */
public class ParseRequestJSON {

    public ParseRequestJSON() {
    }

    public Book parseJSONObject(String requestJSONLine) {
        JSONObject js = new JSONObject(requestJSONLine);
        Book book = new Book();
        book.setId((Integer) js.get("id"));
        book.setName((String) js.get("name"));
        book.setTitle((String) js.get("title"));
        return book;
    }

    public String parseToJSONObject(Book book) {
        JSONObject js = new JSONObject();
        js.put("id", book.getId());
        js.put("name", book.getName());
        js.put("title", book.getTitle());
        return js.toString();
    }


}
