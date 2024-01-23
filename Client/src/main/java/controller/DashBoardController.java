package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DashBoardController {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                String message = "";
                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    updateTextArea("Server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            String message = txtMessage.getText();
            updateTextArea("Me: " + message);
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logoutOnAction(ActionEvent event) {
        // Add logout functionality
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTextArea(String message) {
        Platform.runLater(() -> txtArea.appendText("\n" + message));
    }

    public void sendOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }
}
