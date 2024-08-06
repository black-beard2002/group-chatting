/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group.chating;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);

            while (true) {
                System.out.println("Server Started....");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                String message = new String(buffer, 0, bytesRead);
                System.out.println(clientSocket + message);

               
                ChatServer.broadcastMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}