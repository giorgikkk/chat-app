package com.example.groupChat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;
    private String chatName;

    public Client(Socket socket, String userName, String chatName) {
        try {
            this.socket = socket;
            this.userName = userName;
            this.chatName = chatName;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeClient();
        }
    }

     private void closeClient() {
        try {
            if (socket != null) {
                socket.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(chatName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(String.format("%s: %s", userName, messageToSend));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeClient();
        }
    }

    public void listenMessage() {
        new Thread(() -> {
            String messageFromOthers;

            while (socket.isConnected()) {
                try {
                    messageFromOthers = bufferedReader.readLine();
                    System.out.println(messageFromOthers);
                } catch (IOException e) {
                    closeClient();
                }
            }
        }).start();
    }
}
