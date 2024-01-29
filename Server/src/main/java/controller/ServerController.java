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
import java.net.SocketException;
import java.util.Map;
import java.util.Objects;
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
                try {
                    message = clientInputStream.readUTF();
                } catch (SocketException e) {
                    if (e.getMessage().equals("Connection reset")) {
                        break;
                    } else {
                        throw e;
                    }
                }
                String[] msg = message.split("&");
                String type = msg[0];
                String sender = msg[1];
                String contain = msg[2];

                if (type.equals("exit")) {
                    // Close the connection for this client
                    closeConnection(clientSocket);
                    break;
                } else if (type.equals("img")) {
                    receiveImage(clientSocket, sender, contain);
                } else {
                    txtArea.appendText("\n" + sender + ":" + contain);
                    broadcastMessage(clientSocket, message);
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
    private void receiveImage(Socket clientSocket,String sender,String absolutePath) {
        if (Objects.equals(sender, "")){
            sender = "someone";
        }
            String message = ("img&" + sender + "&" + absolutePath);
            broadcastMessage(clientSocket, message);
            System.out.println(message);
    }

}
