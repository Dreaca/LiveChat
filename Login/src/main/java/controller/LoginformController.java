package controller;

import bo.BoFactory;
import bo.custom.LoginBo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginformController {

    public AnchorPane root;
    @FXML
    private PasswordField txtPassWord;

    @FXML
    private TextField txtUsername;

    private LoginBo loginBo = (LoginBo) BoFactory.getInstance().getBo(BoFactory.BO.LOGIN);
    @FXML
    void loginOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassWord.getText();

        try {
//            boolean b = loginBo.checkValidity(username, password);
            if(true){
                Stage cur = (Stage) root.getScene().getWindow();
                cur.close();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(""));
                Object load = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene((Parent) load));
                stage.show();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public void signUpOnAction(ActionEvent actionEvent) throws IOException {
        Stage cur = (Stage) root.getScene().getWindow();
        cur.close();
        Object load = FXMLLoader.load(getClass().getResource("/view/signupForm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Parent) load));
        stage.show();
    }
}
