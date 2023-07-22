package com.example.groupChat;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerDemo {
    public static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            System.out.printf("PORT: %d\n", PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
