package com.example.groupChat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {
    private static final Map<String, List<String>> chatHistory = new HashMap<>();
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private String clientUserName;
    private String clientChatName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientChatName = bufferedReader.readLine();
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            showMessage(String.format("SERVER: %s has joined the chat.", this.clientUserName));
            if(!chatHistory.isEmpty()) {
                showChatHistory(this, this.clientUserName, this.clientChatName);
            }
        } catch (IOException e) {
            removeClientHandler();
            closeClientHandler();
        }
    }

    private void closeClientHandler() {
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

    private void removeClientHandler() {
        clientHandlers.remove(this);
        showMessage(String.format("SERVER: %s has left the chat.", this.clientUserName));
    }

    private void showMessage(String message) {
        clientHandlers.forEach(clientHandler -> {
            if (!clientHandler.clientUserName.equals(clientUserName) && clientHandler.clientChatName.equals(clientChatName)) {
                show(clientHandler, message);
            }
        });
    }

    private void showChatHistory(ClientHandler clientHandler, String clientUserName, String clientChatName) {
        chatHistory.get(clientChatName).forEach(message -> {
            show(clientHandler, message);
        });
    }

    private void show(ClientHandler clientHandler, String message) {
        try {
            clientHandler.bufferedWriter.write(message);
            clientHandler.bufferedWriter.newLine();
            clientHandler.bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if(!chatHistory.containsKey(clientChatName)) {
                    chatHistory.put(clientChatName, new ArrayList<>());
                }
                chatHistory.get(clientChatName).add(messageFromClient);
                showMessage(messageFromClient);
            } catch (IOException e) {
                removeClientHandler();
                closeClientHandler();
                break;
            }
        }
    }
}
