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
import javafx.stage.Window;

import java.io.IOException;

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

        if(loginBo.checkValidity(username,password)){
            //dosomething
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
