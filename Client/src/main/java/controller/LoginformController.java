package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoginformController {

    public AnchorPane root;
    public AnchorPane logPanel;
    @FXML
    private PasswordField txtPassWord;

    @FXML
    private TextField txtUsername;
    public static List<String> activeUsers = new ArrayList<>();
    private Socket socket;
    private DataInputStream loginIn;
    private DataOutputStream loginOut;

    public void initialize(){
        Image image = new Image("img/sl_021821_40890_06.jpg");
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
        logPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);"
        + "-fx-border-radius : 20;" + "-fx-background-radius : 20;");

        new Thread(()->{
            try {
                socket = new Socket("localhost",3000);
                loginIn = new DataInputStream(socket.getInputStream());
                loginOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    @FXML
    void loginOnAction(ActionEvent event) {
        String username = txtUsername.getText();

        try {
            boolean userExists = activeUsers.contains(username);

            if (userExists) {
                new Alert(Alert.AlertType.ERROR, "User chat already exists").show();
                txtUsername.clear();
                txtPassWord.clear();
            } else {
                txtUsername.clear();
                txtPassWord.clear();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/dashBoard.fxml"));
                    Object load = loader.load();
                    DashBoardController controller = loader.getController();
                    controller.setName(username);
                    Stage stage = new Stage();
                    stage.setTitle(username + "'s chat");
                    stage.setResizable(false);
                    stage.setScene(new Scene((Parent) load));
                    stage.show();

                    activeUsers.add(username);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkOnAction(ActionEvent event) {
        loginOnAction(event);
    }
    public boolean getExists(){
        String userName = txtUsername.getText();
        String passWord = txtPassWord.getText();
        String message = "login&"+userName+"&"+passWord;
        try {
            loginOut.writeUTF(message);
            if (loginIn.readUTF().contains("is valid")){
                return true;
            }
            else return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
