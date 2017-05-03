package com.company.controller;

import com.company.bean.Book;
import com.company.bean.Request;
import com.company.bean.Response;
import com.company.controller.ParseRequestResponse.ParseRequestJSON;
import com.company.controller.ParseRequestResponse.ParseRequestXML;
import com.company.dao.HibernateUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 11.04.2017.
 */
public class ClientSession implements Runnable {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private Request request = new Request();
    private Response response = new Response();

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            String responseBook = readRequestFromInputStream();
            writeResponse(responseBook);

        } catch (Throwable throwable) {
            String errorResponse = "<html><body><h1>500 Internal Server Error</h1></body></html>\n";
            System.out.println("Client session stop!!!!!!");
            PrintWriter pr = new PrintWriter(os, true);
            response.setStatusCode(503);
            response.setContentLength(errorResponse.length());
            String resultError = response.getPatternResponseError() + errorResponse;
            pr.println(resultError);
            pr.flush();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeResponse(String responseBook) throws Throwable {
        PrintWriter pr = new PrintWriter(os, true);
        if (!responseBook.equals("invalid url")) {
            switch (request.getMethod()) {
                case "GET":
                    String messageGet = responseBook;
                    response.setContentLength(messageGet.length());
                    response.setStatusCode(200);
                    String resultGet = response.getPatternResponseOk() + messageGet;
                    pr.println(resultGet);
                    pr.flush();
                    break;
                case "POST":
                    String messagePost = "<html><body><h1>Create success!!!</h1></body></html>\n";
                    response.setStatusCode(201);
                    String resultPost = response.getPatternResponseOk() + messagePost;
                    pr.println(resultPost);
                    pr.flush();
                    break;
                case "PUT":
                    String messagePut = "<html><body><h1>Update success!!!</h1></body></html>\n";
                    response.setStatusCode(200);
                    String resultPut = response.getPatternResponseOk() + messagePut;
                    pr.println(resultPut);
                    pr.flush();
                    break;
                case "DELETE":
                    String messageDelete = "<html><body><h1>Delete success!!!</h1></body></html>\n";
                    response.setStatusCode(200);
                    response.setContentLength(messageDelete.length());
                    String resultDelete = response.getPatternResponseOk() + messageDelete;
                    pr.println(resultDelete);
                    pr.flush();
                    break;
                default:
                    String messageDefault = "<html><body><h1>Unknown method!!!</h1></body></html>\n";
                    response.setStatusCode(405);
                    response.setContentLength(messageDefault.length());
                    String resultDefault = response.getPatternResponseError() + messageDefault;
                    pr.println(resultDefault);
                    pr.flush();
                    break;
            }
        } else {
            String messageDefault = "<html><body><h1>Invalid url!!!</h1></body></html>\n";
            response.setContentLength(messageDefault.length());
            response.setStatusCode(404);
            String resultDefault = response.getPatternResponseError() + messageDefault;
            pr.println(resultDefault);
            pr.flush();
        }
    }

    private String readRequestFromInputStream() throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(is));
        List<String> requestHeaderList = new ArrayList();
        String requestHeaderLine;
        int contentLength;
        String contentType;
        while ((requestHeaderLine = requestReader.readLine()) != null) {
            if (requestHeaderLine.isEmpty()) {
                break;
            } else {
                requestHeaderList.add(requestHeaderLine);
                if (requestHeaderLine.contains("Content-Length")) {
                    contentLength = getContentLength(requestHeaderLine);
                    response.setContentLength(contentLength);
                }
                if (requestHeaderLine.contains("Content-Type")) {
                    contentType = getContentFormat(requestHeaderLine);
                    response.setContentType(contentType);
                }

                System.out.println(requestHeaderLine);
            }
        }
        getNameMethod(requestHeaderList.get(0));
        if (request.getUrl().equals("rest")) {
            SwitchMethod switchMethod = new SwitchMethod();
            switchMethod.switchMethod(request, response, requestReader);
            return switchMethod.getOutputStreamBook();
        }
        return "invalid url";
    }

    private String getContentFormat(String requestHeaderLine) {
        return requestHeaderLine.substring(requestHeaderLine.indexOf(":") + 1).trim();
    }

    private int getContentLength(String contentLengthString) {
        return Integer.parseInt(contentLengthString.substring(contentLengthString.indexOf(":") + 1).trim());
    }

    private void getNameMethod(String nameMethod) {
        String name = nameMethod.substring(0, nameMethod.lastIndexOf("H")).trim();
        int id = 0;
        if (name.contains("GET") | name.contains("DELETE")) {
            int count = 0;
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == '/') {
                    count++;
                }
            }
            if (count == 2) {
                try {
                    id = Integer.parseInt(name.substring(name.lastIndexOf("/") + 1));
                } catch (Exception e) {
                    System.out.println("id = " + 0);
                }
            }
        }
        StringBuilder url = new StringBuilder(name);
        url = new StringBuilder(url.substring(url.indexOf("/") + 1, url.lastIndexOf("")));
        if (url.length() > 4) {
            url = new StringBuilder(url.substring(0, url.indexOf("/")));
        }
        request.setUrl(url.toString());
        request.setId(id);
        request.setMethod(name.substring(0, name.indexOf("/")).trim());
    }

}
