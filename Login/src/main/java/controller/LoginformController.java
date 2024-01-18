package controller;

import bo.BoFactory;
import bo.custom.LoginBo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginformController {

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

}
