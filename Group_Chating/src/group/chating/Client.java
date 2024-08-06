/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package group.chating;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client extends JFrame {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private JTextArea chatTextArea;
    private JTextField messageTextField;
    private JTextField usernameTextField;

    public Client() {
        createUI();
        connectToServer();
    }

    private void createUI() {
        setTitle("Whatsapp Group");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JLabel usernameLabel = new JLabel("Username:");
        usernameTextField = new JTextField();
        usernameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameTextField.setEnabled(false);
                messageTextField.requestFocus();
            }
        });

        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(usernameTextField, BorderLayout.CENTER);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        messageTextField = new JTextField();
        messageTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(messageTextField, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        String message = new String(buffer, 0, bytesRead);
                        chatTextArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String username = usernameTextField.getText();
        String message = messageTextField.getText();

        if (!username.isEmpty() && !message.isEmpty()) {
            try {
                String fullMessage = username + ": " + message;
                outputStream.write(fullMessage.getBytes());
                outputStream.flush();
                messageTextField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }
}


     
  /*  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
*/