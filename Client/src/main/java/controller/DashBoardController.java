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
import javafx.scene.control.*;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class DashBoardController {

    public HBox hbox;
    public VBox chatContainer;
    public ScrollPane scrollPane;
    public AnchorPane root;

    @FXML
    private TextField txtMessage;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Stage emojiBoardStage;
    private String name;

    public void initialize() {
        Image image = new Image("img/2797258.jpg");
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true) // Set 'cover' to true
        );

        root.setBackground(new Background(backgroundImage));

        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                String message = "";

                while (true) {
                    try {
                        message = dataInputStream.readUTF();
                    } catch (SocketException e) {
                        if (e.getMessage().equals("Socket closed") || e.getMessage().equals("Connection Reset")) {
                            break;
                        } else {
                            throw e;
                        }
                    }
                    String[] msg = message.split("&");
                    String type = msg[0];
                    String sender = msg[1];
                    String contain = msg[2];

                    if (type.equals("img")) {
                       updateTextArea(sender,"sent an Image");
                        receiveImage(contain);
                    }
                    else if (type.equals("login")){
                        updateTextArea("Server"," : A new user has joined the chat. ");
                    }
                    else {
                        updateTextArea(sender,contain);
                    };
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            dataOutputStream.writeUTF("txt&" + this.name + "&" +message);
            dataOutputStream.flush();
            addTextBubble(null, "Me: " + message, true);
            txtMessage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutOnAction(ActionEvent event) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Logout Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to logout?");
        confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LoginformController.activeUsers.remove(this.name);
                try {
                    dataOutputStream.writeUTF("exit&"+this.name+"&"+"logging out");
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void updateTextArea(String sender,String message) {
        Platform.runLater(() -> addTextBubble(null,(sender+":"+message) , false));
    }

    public void sendOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }

    public void openEmojiBoard(ActionEvent event) {
        emojiBoardStage.showAndWait();
    }

    public void setEmoji(String emoji) {
        String existingText = txtMessage.getText();
        String newText = existingText + emoji;
        txtMessage.setText(newText);
    }

    public void addImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp")
        );
        Window window = ((Stage) ((Button) event.getSource()).getScene().getWindow());
        File selectedFile = fileChooser.showOpenDialog(window);
        String absolutePath = selectedFile.toURI().toString();

        if (selectedFile != null) {
            sendImage(absolutePath);
        }
    }
    private void sendImage(String absolutePath) {
        Image image = new Image(absolutePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        addTextBubble(imageView, this.name , true);

        try {
            dataOutputStream.writeUTF("img&" + this.name + "&" + absolutePath);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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

        chatContainer.layout();
        scrollPane.setVvalue(1);
    }

    private void receiveImage(String path) {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        Platform.runLater(() -> {
            addTextBubble(imageView, "sent image", false);
        });
    }

    public void setName(String name){
        this.name = name;
    }
}
