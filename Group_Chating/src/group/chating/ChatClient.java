/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group.chating;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234); // Change the host and port if necessary

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            Scanner scanner = new Scanner(System.in);

            // Prompt the user to enter a name
            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        String message = new String(buffer, 0, bytesRead);
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();

            while (true) {
                String message = scanner.nextLine();
                message = clientName + ": " + message;
                outputStream.write(message.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
