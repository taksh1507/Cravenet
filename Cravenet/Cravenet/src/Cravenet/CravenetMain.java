package Cravenet;

import javafx.application.Application;
import javafx.stage.Stage;

public class CravenetMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Launch the Login page
        Login login = new Login();
        login.start(primaryStage); // Start the login window
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
