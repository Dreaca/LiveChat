package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Map<Socket, DataOutputStream> clientStreams = new ConcurrentHashMap<>();

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3000);
                txtArea.appendText("Socket Accepted");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    DataInputStream clientInputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                    // Store the client's output stream in the map
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
                }

                // Handle the received message (e.g., process, broadcast, etc.)
                // For simplicity, let's just echo the message back to the client
                txtArea.appendText("\nClient: " + message);

                // Echo the message back to the client
                sendToClient(clientSocket, "Server: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(clientSocket);
        }
    }

    private void sendToClient(Socket clientSocket, String message) {
        try {
            // Get the output stream for the specified client from the map
            DataOutputStream clientOutputStream = clientStreams.get(clientSocket);
            if (clientOutputStream != null) {
                clientOutputStream.writeUTF(message);
                clientOutputStream.flush();
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

            // Broadcast the message to all connected clients
            for (DataOutputStream clientOutputStream : clientStreams.values()) {
                clientOutputStream.writeUTF( message);
                clientOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void sendTextOnAction(ActionEvent event) {
        // You can implement additional logic here if needed
    }
}
