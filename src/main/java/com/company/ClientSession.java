package com.company;

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
    private int id;
    private StringBuffer outputStreamBook = new StringBuffer();
    private String contentType;

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            String methodName = readRequestFromInputStream();
            writeResponse(methodName);

        } catch (Throwable throwable) {
            String errorResponse = "<html><body><h1>500 Internal Server Error</h1></body></html>\n";
            System.out.println("Client session stop!!!!!!");
            PrintWriter pr = new PrintWriter(os, true);
            String responseError = "HTTP/1.1 500 Internal Server Error\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + errorResponse.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String resultError = responseError + errorResponse;
            pr.println(resultError);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeResponse(String methodName) throws Throwable {
        PrintWriter pr = new PrintWriter(os, true);
        switch (methodName) {
            case "GET":
                String messageGet =  outputStreamBook.toString();
                String responseGet = "HTTP/1.1 200 OK\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + messageGet.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                String resultGet = responseGet + messageGet;
                pr.println(resultGet);
                break;
            case "POST":
                String messagePost = "<html><body><h1>Create success!!!</h1></body></html>\n";
                String responsePost = "HTTP/1.1 201 Created\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + messagePost.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                String resultPost = responsePost + messagePost;
                pr.println(resultPost);
                break;
            case "PUT":
                String messagePut = "<html><body><h1>Update success!!!</h1></body></html>\n";
                String responsePut = "HTTP/1.1 200 OK\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + messagePut.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                String resultPut = responsePut + messagePut;
                pr.println(resultPut);
                break;
            case "DELETE":
                String messageDelete = "<html><body><h1>Delete success!!!</h1></body></html>\n";
                String responseDelete = "HTTP/1.1 200 OK\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + messageDelete.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                String resultDelete = responseDelete + messageDelete;
                pr.println(resultDelete);
                break;
            default:
                String messageDefault = "<html><body><h1>Unknown method!!!</h1></body></html>\n";
                String responseDefault = "HTTP/1.1 405 Method Not Allowed\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + messageDefault.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                String resultDefault = responseDefault + messageDefault;
                pr.println(resultDefault);
                break;
        }
    }

    private String readRequestFromInputStream() throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(is));
        List<String> requestHeaderList = new ArrayList();
        String requestHeaderLine;
        int contentLength = 0;
        boolean formatBodyFlag = false;
        while ((requestHeaderLine = requestReader.readLine()) != null) {
            if (requestHeaderLine.isEmpty()) {
                break;
            } else {
                requestHeaderList.add(requestHeaderLine);
                if (requestHeaderLine.contains("Content-Length")) {
                    contentLength = getContentLength(requestHeaderLine);
                }
                if (requestHeaderLine.contains("Content-Type")) {
                    formatBodyFlag = getContentFormat(requestHeaderLine);
                }

                System.out.println(requestHeaderLine);
            }
        }
        switchMethod(getNameMethod(requestHeaderList.get(0)), contentLength, formatBodyFlag, requestReader);
        return getNameMethod(requestHeaderList.get(0));
    }

    private void switchMethod(String methodName, int contentLength, boolean formatBodyFlag, BufferedReader requestReader) throws IOException {
        HibernateUtil hibernateUtil = new HibernateUtil();
        ParseRequestJSON parseRequestJSON = new ParseRequestJSON();
        ParseRequestXML parseRequestXML = new ParseRequestXML();
        switch (methodName) {
            case "GET":
                if (id != 0) {
                    Book book = hibernateUtil.getBookById(id);
                    if (formatBodyFlag) {
                        outputStreamBook.append(parseRequestJSON.parseToJSONObject(book));
                        System.out.println(parseRequestJSON.parseToJSONObject(book));
                    } else {
                        File file = new File("fileXML.xml");
                        outputStreamBook.append(parseRequestXML.parseToXMLObject(book, file));
                        System.out.println(parseRequestXML.parseToXMLObject(book, file));
                    }

                } else {
                    List<Book> list = hibernateUtil.listBooks();
                    if (formatBodyFlag) {
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
                String bodyPost = getBody(contentLength, requestReader);
                hibernateUtil.addBook(deserializeBody(bodyPost, formatBodyFlag));
                break;
            case "PUT":
                String bodyPut = getBody(contentLength, requestReader);
                hibernateUtil.updateBook(deserializeBody(bodyPut, formatBodyFlag));
                break;
            case "DELETE":
                if (id != 0) {
                    hibernateUtil.removeBookById(id);
                } else {
                    hibernateUtil.removeBooks();
                }
                break;
            default:
                System.out.println("Wrong method!!!");
                break;
        }
    }

    private String getNameMethod(String nameMethod) {
        String name = nameMethod.substring(0, nameMethod.lastIndexOf("H")).trim();
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
        return name.substring(0, name.indexOf("/")).trim();
    }

    private boolean getContentFormat(String requestHeaderLine) {
        String format = requestHeaderLine.substring(requestHeaderLine.indexOf(":") + 1).trim();
        contentType = format;
        return format.equals("application/json") ? true : false;
    }

    private int getContentLength(String contentLengthString) {
        return Integer.parseInt(contentLengthString.substring(contentLengthString.indexOf(":") + 1).trim());
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

    private Book deserializeBody(String body, boolean formatFlag) {
        Book book;
        if (formatFlag) {
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
}
