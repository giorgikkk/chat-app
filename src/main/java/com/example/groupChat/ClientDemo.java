package com.example.groupChat;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int port = getPort(scanner);

        try {
            System.out.println("Enter chat name: ");
            String chatName = scanner.nextLine();
            System.out.println("Enter your username: ");
            String user = scanner.nextLine();
            Socket socket = new Socket("localhost", port);
            Client client = new Client(socket, user, chatName);
            client.listenMessage();
            client.sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPort(Scanner scanner) {
        while(true) {
            System.out.println("Enter port number to join the group chat: ");
            int port = Integer.parseInt(scanner.nextLine());

            if(port == ServerDemo.PORT) {
                return port;
            } else {
                System.out.println("Wrong port number. Try again.");
            }
        }
    }
}
