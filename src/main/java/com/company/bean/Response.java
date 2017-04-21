package com.company.bean;

/**
 * Created by PC on 21.04.2017.
 */
public class Response {
    private int statusCode;
    private String contentType = "application/json";
    private int contentLength;

    public Response() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getPatternResponseOk() {
        return "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "Connection: close\r\n\r\n";
    }

    public String getPatternResponseError() {
        return "HTTP/1.1 " + statusCode + " Method Not Allowed\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "Connection: close\r\n\r\n";
    }

}

