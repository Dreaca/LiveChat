package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class DashBoardController {

    public HBox hbox;
    public VBox chatContainer;
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
                Object message = "";
                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    if (message.equals("image")) {

                    } else {
                        updateTextArea((String)message);
                    };
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
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            addTextBubble(null, "Me: " + message, true);
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
        Platform.runLater(() -> addTextBubble(null, message, false));
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

    public void addImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp")
        );
        Window window = ((Stage) ((Button) event.getSource()).getScene().getWindow());
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile != null) {

        }
    }
    private void addTextBubble(Node content, String message, boolean isSentByMe) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        String backgroundColor = isSentByMe ? "#b2dfdb" : "#c5cae9";
        String textColor = isSentByMe ? "black" : "black";
        BackgroundFill backgroundFill = new BackgroundFill(Color.web(backgroundColor), new CornerRadii(5), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        messageLabel.setBackground(background);
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setTextFill(Color.web(textColor));
        messageLabel.setMaxWidth(650);

        HBox.setMargin(messageLabel, new Insets(20));
        HBox textBubble;
        if (content != null) {
            textBubble = new HBox(content, messageLabel);
        } else {
            textBubble = new HBox(messageLabel);
        }
        textBubble.setAlignment(isSentByMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        chatContainer.getChildren().add(textBubble);
    }

}
