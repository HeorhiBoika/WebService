package com.company.controller;

import com.company.bean.Book;
import com.company.bean.Request;
import com.company.bean.Response;
import com.company.controller.ParseRequestResponse.ParseRequestJSON;
import com.company.controller.ParseRequestResponse.ParseRequestXML;
import com.company.dao.HibernateUtil;
import com.company.service.RestWebService;
import com.company.service.factory.ServiceFactory;

import java.io.*;
import java.util.List;

/**
 * Created by PC on 21.04.2017.
 */
public class SwitchMethod {
    private StringBuffer outputStreamBook = new StringBuffer();

    public SwitchMethod() {
    }

    public void switchMethod(Request request, Response response, BufferedReader requestReader) throws IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RestWebService restWebService = serviceFactory.getBookService();
        ParseRequestJSON parseRequestJSON = new ParseRequestJSON();
        ParseRequestXML parseRequestXML = new ParseRequestXML();
        switch (request.getMethod()) {
            case "GET":
                if (request.getId() != 0) {
                    Book book = restWebService.getBookById(request.getId());
                    if (response.getContentType().contains("json")) {
                        outputStreamBook.append(parseRequestJSON.parseToJSONObject(book));
                        System.out.println(parseRequestJSON.parseToJSONObject(book));
                    } else {
                        File file = new File("fileXML.xml");
                        outputStreamBook.append(parseRequestXML.parseToXMLObject(book, file));
                        System.out.println(parseRequestXML.parseToXMLObject(book, file));
                    }

                } else {
                    List<Book> list = restWebService.listBooks();
                    if (response.getContentType().contains("json")) {
                        for (Book book : list) {
                            outputStreamBook.append(parseRequestJSON.parseToJSONObject(book)).append("\n");
                            System.out.println(parseRequestJSON.parseToJSONObject(book));
                        }
                    } else {
                        File file = new File("fileXML.xml");
                        for (Book book : list) {
                            outputStreamBook.append(parseRequestXML.parseToXMLObject(book, file)).append("\n");
                            System.out.println(parseRequestXML.parseToXMLObject(book, file));
                        }
                    }
                }
                break;
            case "POST":
                String bodyPost = getBody(response.getContentLength(), requestReader);
                restWebService.addBook(deserializeBody(bodyPost, response.getContentType()));
                break;
            case "PUT":
                String bodyPut = getBody(response.getContentLength(), requestReader);
                restWebService.updateBook(deserializeBody(bodyPut, response.getContentType()));
                break;
            case "DELETE":
                if (request.getId() != 0) {
                    restWebService.removeBookById(request.getId());
                } else {
                    restWebService.removeBooks();
                }
                break;
            default:
                System.out.println("Wrong method!!!");
                break;
        }
    }

    private String getBody(int contentLength, BufferedReader requestReader) throws IOException {
        StringWriter body = new StringWriter();
        char[] buffer = new char[1024];
        int charToWrite;
        while ((charToWrite = requestReader.read(buffer)) != -1) {
            body.write(buffer, 0, charToWrite);
            if (charToWrite == contentLength) {
                break;
            }
        }
        System.out.println("BODY FROM INPUT STREAM" + "\n" + body.toString());
        return body.toString();
    }

    private Book deserializeBody(String body, String format) {
        Book book;
        if (format.contains("json")) {
            book = new ParseRequestJSON().parseJSONObject(body);
        } else {
            File file = new File("fileXML.xml");
            PrintWriter pr = null;
            try {
                pr = new PrintWriter(file);
                pr.write(body);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                pr.close();
            }
            book = new ParseRequestXML().parseXMLObject(file);
        }
        return book;
    }

    public String getOutputStreamBook() {
        return outputStreamBook.toString();
    }
}
