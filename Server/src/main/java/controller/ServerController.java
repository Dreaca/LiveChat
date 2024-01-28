package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    public TextField txtMessage;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Map<Socket, DataOutputStream> clientStreams = new ConcurrentHashMap<>();

    @FXML
    private TextArea txtArea;

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3000);
                txtArea.appendText("Socket Accepted");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    DataInputStream clientInputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                    clientStreams.put(clientSocket, clientOutputStream);

                    threadPool.execute(() -> handleClient(clientSocket, clientInputStream));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket, DataInputStream clientInputStream) {
        try {
            String message;
            while (true) {
                message = clientInputStream.readUTF();
                if (message.equals("exit")) {
                    break;
                } else if (message.equals("image")) {

                } else {
                    txtArea.appendText("\nClient: " + message);

                    // Broadcast the message to all clients except the sender
                    broadcastMessage(clientSocket, "Client: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(clientSocket);
        }
    }

    private void broadcastMessage(Socket senderSocket, String message) {
        try {
            for (Socket clientSocket : clientStreams.keySet()) {
                // Skip the sender, don't send the message back to the client who sent it
                if (clientSocket != senderSocket) {
                    DataOutputStream clientOutputStream = clientStreams.get(clientSocket);
                    clientOutputStream.writeUTF(message);
                    clientOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(Socket clientSocket) {
        try {
            clientStreams.remove(clientSocket);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void sendOnAction(ActionEvent event) {
        try {
            String message = txtMessage.getText();
            txtArea.appendText("\n" + message);

            for (DataOutputStream clientOutputStream : clientStreams.values()) {
                clientOutputStream.writeUTF("Server : "+message);
                clientOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public  void sendTextOnAction(ActionEvent event){
        sendOnAction(event);
    }
}
