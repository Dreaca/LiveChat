package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EmojiController {

    private DashBoardController dashBoardController; // Reference to your main controller

    @FXML
    private void selectEmoji(ActionEvent event) {
        if (event.getSource() instanceof JFXButton) {
            JFXButton selectedButton = (JFXButton) event.getSource();
            String selectedEmoji = selectedButton.getText();
            System.out.println("Selected Emoji: " + selectedEmoji);

            // Call the method in DashBoardController to handle the selected emoji
            if (dashBoardController != null) {
                dashBoardController.setEmoji(selectedEmoji);
            }
        }
    }

    // Setter method to inject the reference to the main controller
    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }
}
