package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EmojiController {

    private DashBoardController dashBoardController;

    @FXML
    private void selectEmoji(ActionEvent event) {
        if (event.getSource() instanceof JFXButton) {
            JFXButton selectedButton = (JFXButton) event.getSource();
            String selectedEmoji = selectedButton.getText();
            System.out.println("Selected Emoji: " + selectedEmoji);

            if (dashBoardController != null) {
                dashBoardController.setEmoji(selectedEmoji);
            }
        }
    }
    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }
}
