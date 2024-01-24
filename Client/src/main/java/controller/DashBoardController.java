package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Stage emojiBoardStage;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                String message = "";
                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    updateTextArea(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();
        emojiBoardStage = new Stage();
        showEmojiBoard();
    }
    private void showEmojiBoard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/emojiBoard.fxml"));
            Object emojiBoard = loader.load();

            EmojiController emojiBoardController = loader.getController();
            emojiBoardController.setDashBoardController(this);

            Scene emojiBoardScene = new Scene((Parent) emojiBoard);
            emojiBoardStage.setScene(emojiBoardScene);
            emojiBoardStage.initModality(Modality.APPLICATION_MODAL);
            emojiBoardStage.setTitle("Emoji Board");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void openEmojiBoard(ActionEvent event) {
        emojiBoardStage.showAndWait();
    }
    public void setEmoji(String emoji) {
        txtMessage.setText(emoji);
    }
}
