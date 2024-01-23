import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/server.fxml"));
        Object load = loader.load();
        stage.setScene(new Scene((Parent) load));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
