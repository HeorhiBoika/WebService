package com.company;

import com.company.controller.ClientSession;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        // write your code here
        ServerSocket server = new ServerSocket(8080);
        ExecutorService service = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = server.accept();
            System.out.println("Client accept");
            service.submit(new ClientSession(socket));
        }

    }
}
