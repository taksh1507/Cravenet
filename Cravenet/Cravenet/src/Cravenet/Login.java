package Cravenet;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends Application {

    private TextField textField;
    private PasswordField passwordField;
    private TextField visiblePasswordField;
    private Label errorLabel;
    private boolean isPasswordVisible = false;
    private Button togglePasswordVisibilityButton;

    // MySQL database Connection
    private static final String DBURL = "jdbc:mysql://localhost:3306/cravenet_database";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "518815";

  
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CRAVENET - Login");

        // Main content pane
        VBox contentPane = new VBox(15);
        contentPane.setPadding(new Insets(20));
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setStyle("-fx-background-color: #E0FFFF;");

        
        Image logoImage = new Image("file:C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/Logo.jpg");
        ImageView logoLabel = new ImageView(logoImage);
        logoLabel.setFitHeight(160);
        logoLabel.setFitWidth(160);
        contentPane.getChildren().add(logoLabel);

        // Title
        Label titleLabel = new Label("CRAVENET");
        titleLabel.setFont(new Font("Verdana", 28));
        titleLabel.setTextFill(Color.web("#1E90FF"));
        contentPane.getChildren().add(titleLabel);

        // Login panel
        VBox loginPanel = new VBox(10);
        loginPanel.setPadding(new Insets(20));
        loginPanel.setStyle("-fx-background-color: white; -fx-border-color: #4682B4; -fx-border-width: 1; -fx-background-radius: 10; -fx-border-radius: 10;");
        loginPanel.setAlignment(Pos.CENTER);
        contentPane.getChildren().add(loginPanel);

        // User ID field
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setFont(new Font("Verdana", 16));
        loginPanel.getChildren().add(userIdLabel);

        textField = new TextField();
        textField.setFont(new Font("Arial", 14));
        textField.setPadding(new Insets(10));
        loginPanel.getChildren().add(textField);

        // Password field section
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Verdana", 16));
        loginPanel.getChildren().add(passwordLabel);

        // Create a StackPane to hold both password fields
        StackPane passwordFieldStack = new StackPane();

        passwordField = new PasswordField();
        passwordField.setFont(new Font("Arial", 14));
        passwordField.setPadding(new Insets(10));
        passwordField.setPrefWidth(280);

        visiblePasswordField = new TextField();
        visiblePasswordField.setFont(new Font("Arial", 14));
        visiblePasswordField.setPadding(new Insets(10));
        visiblePasswordField.setPrefWidth(280);
        visiblePasswordField.setVisible(false);

        // Add synchronization between fields
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            visiblePasswordField.setText(newVal);
        });

        visiblePasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            passwordField.setText(newVal);
        });

        passwordFieldStack.getChildren().addAll(passwordField, visiblePasswordField);

        // Create password container
        HBox passwordContainer = new HBox(5);
        passwordContainer.setAlignment(Pos.CENTER);

        
        Image eyeOpenImage = new Image("file:C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/icons/eye-open.jpg");
        Image eyeClosedImage = new Image("file:C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/icons/eye-closed.jpg");

        ImageView eyeIcon = new ImageView(eyeOpenImage);
        eyeIcon.setFitHeight(20);
        eyeIcon.setFitWidth(20);

        togglePasswordVisibilityButton = new Button();
        togglePasswordVisibilityButton.setGraphic(eyeIcon);
        togglePasswordVisibilityButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-padding: 0 5 0 5;" +
            "-fx-cursor: hand;"
        );

        // Add the components to the password container
        passwordContainer.getChildren().addAll(passwordFieldStack, togglePasswordVisibilityButton);
        loginPanel.getChildren().add(passwordContainer);

        // Error label
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(new Font("Verdana", 12));
        loginPanel.getChildren().add(errorLabel);

        // Button panel
        HBox buttonPanel = new HBox(20);
        buttonPanel.setAlignment(Pos.CENTER);
        loginPanel.getChildren().add(buttonPanel);

        // Login button
        Button btnLogin = createStyledButton("Login", Color.web("#1E90FF"));
        btnLogin.setOnAction(e -> handleLogin(primaryStage));
        buttonPanel.getChildren().add(btnLogin);

        // Register button
        Button btnRegister = createStyledButton("Register", Color.web("#00CED1"));
        btnRegister.setOnAction(e -> handleRegistration(primaryStage));
        buttonPanel.getChildren().add(btnRegister);

        // Password visibility toggle action
        togglePasswordVisibilityButton.setOnAction(e -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                passwordField.setVisible(false);
                eyeIcon.setImage(eyeClosedImage);
                visiblePasswordField.requestFocus();
                visiblePasswordField.positionCaret(visiblePasswordField.getText().length());
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                visiblePasswordField.setVisible(false);
                eyeIcon.setImage(eyeOpenImage);
                passwordField.requestFocus();
                passwordField.positionCaret(passwordField.getText().length());
            }
        });

        // Create and show scene
        Scene scene = new Scene(contentPane, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, Color bgColor) {
        Button button = new Button(text);
        button.setFont(new Font("Verdana", 14));
        button.setStyle("-fx-background-color: " + toHexString(bgColor) + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + toHexString(bgColor.darker()) + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + toHexString(bgColor) + "; -fx-text-fill: white;"));
        return button;
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }

    private void handleLogin(Stage primaryStage) {
        String username = textField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (authenticateUser(username, password)) {
            LocationSelection locationSelection = new LocationSelection();
            Stage stage = (Stage) textField.getScene().getWindow();

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                stage.close();
                locationSelection.start(primaryStage);
            });
            fadeOut.play();
        } else {
            errorLabel.setText("Invalid username or password!");

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    javafx.application.Platform.runLater(() -> errorLabel.setText(""));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void handleRegistration(Stage primaryStage) {
        Registration registration = new Registration();
        Stage stage = (Stage) textField.getScene().getWindow();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), stage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            stage.close();
            registration.start(primaryStage);
        });
        fadeOut.play();
    }

    private boolean authenticateUser(String username, String password) {

        try (Connection connection = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
            String sql = "SELECT * FROM users WHERE User_ID = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
