package Cravenet;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
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

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Registration extends Application {
    private TextField emailField;
    private TextField otpField;
    private TextField userIdField;
    private TextField passwordField;
    private Label lblMessage;
    private String generatedOTP;
    private VBox otpContainer;
    private Button btnGenerateOTP;
    private Button btnVerify;

    // MySQL database connection
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cravenet_database";
    private static final String DB_USER = "root";  
    private static final String DB_PASSWORD = "518815"; 

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CRAVENET - Registration");

        // Create the main layout
        VBox contentPane = new VBox(15);
        contentPane.setPadding(new Insets(20));
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setStyle("-fx-background-color: #E0FFFF;"); 

        // Logo
        Image logoImage = new Image("file:/C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/Logo.png");
        ImageView logoLabel = new ImageView(logoImage);
        logoLabel.setFitHeight(150);
        logoLabel.setFitWidth(200);
        contentPane.getChildren().add(logoLabel);

        // Title Label
        Label titleLabel = new Label("CRAVENET");
        titleLabel.setFont(new Font("Verdana", 28));
        titleLabel.setTextFill(Color.web("#1E90FF"));
        contentPane.getChildren().add(titleLabel);

        // Registration Panel
        VBox registrationPanel = new VBox(10);
        registrationPanel.setPadding(new Insets(20));
        registrationPanel.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #4682B4; " +
                "-fx-border-width: 1; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10;");
        registrationPanel.setAlignment(Pos.CENTER);
        contentPane.getChildren().add(registrationPanel);

        // User ID Field
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setFont(new Font("Verdana", 16));
        userIdField = new TextField();
        userIdField.setFont(new Font("Arial", 14));
        userIdField.setMaxWidth(250);

        // Password Field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Verdana", 16));
        passwordField = new PasswordField(); 
        passwordField.setFont(new Font("Arial", 14));
        passwordField.setMaxWidth(250);

        // Email Field
        Label emailLabel = new Label("Email:");
        emailLabel.setFont(new Font("Verdana", 16));
        emailField = new TextField();
        emailField.setFont(new Font("Arial", 14));
        emailField.setMaxWidth(250);

        // Generate OTP Button
        btnGenerateOTP = new Button("Generate OTP");
        btnGenerateOTP.setFont(new Font("Verdana", 14));
        btnGenerateOTP.setStyle("-fx-background-color: #1E90FF; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5;");
        btnGenerateOTP.setPrefWidth(150);
        btnGenerateOTP.setOnAction(e -> handleOTPGeneration());

        // OTP Container (initially hidden)
        otpContainer = new VBox(10);
        otpContainer.setAlignment(Pos.CENTER);
        otpContainer.setVisible(false);

        // OTP Field
        Label otpLabel = new Label("Enter OTP:");
        otpLabel.setFont(new Font("Verdana", 16));
        otpField = new TextField();
        otpField.setFont(new Font("Arial", 14));
        otpField.setMaxWidth(250);

        // Verify Button
        btnVerify = new Button("Verify OTP");
        btnVerify.setFont(new Font("Verdana", 14));
        btnVerify.setStyle("-fx-background-color: #00CED1; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5;");
        btnVerify.setPrefWidth(150);
        btnVerify.setOnAction(e -> verifyOTP());

        // Add components to OTP container
        otpContainer.getChildren().addAll(otpLabel, otpField, btnVerify);

        // Message Label
        lblMessage = new Label("");
        lblMessage.setTextFill(Color.RED);
        lblMessage.setFont(new Font("Verdana", 12));
        lblMessage.setWrapText(true);
        lblMessage.setMaxWidth(250);

        // Add all components to registration panel
        registrationPanel.getChildren().addAll(
                userIdLabel, userIdField,
                passwordLabel, passwordField,
                emailLabel, emailField,
                btnGenerateOTP,
                otpContainer,
                lblMessage
        );

        // Create scene and set it on the stage
        Scene scene = new Scene(contentPane, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleOTPGeneration() {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText();
        String email = emailField.getText().trim();

        // Input validation
        if (userId.isEmpty() || password.isEmpty() || email.isEmpty()) {
            lblMessage.setText("Please fill all fields.");
            return;
        }

        // Email validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            lblMessage.setText("Please enter a valid email address.");
            return;
        }

        if (registerUser(email, userId, password)) {
            generatedOTP = generateOTP();
            sendEmail(email, generatedOTP);

            // Show OTP container with animation
            otpContainer.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), otpContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            lblMessage.setTextFill(Color.GREEN);
            lblMessage.setText("OTP sent to your email.");
            btnGenerateOTP.setDisable(true);
        } else {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText("Registration failed. User ID or Email might already exist.");
        }
    }

    private void verifyOTP() {
        String enteredOTP = otpField.getText().trim();
        
        if (enteredOTP.isEmpty()) {
            lblMessage.setText("Please enter the OTP.");
            return;
        }

        if (enteredOTP.equals(generatedOTP)) {
            lblMessage.setTextFill(Color.GREEN);
            lblMessage.setText("OTP verified! Redirecting to login page...");

           
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.0), otpContainer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        Stage stage = (Stage) lblMessage.getScene().getWindow();
                        Login login = new Login();
                        stage.close();
                        login.start(new Stage());
                    });
                }
            }, 2000);
        } else {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText("Invalid OTP. Please try again.");
        }
    }

    private boolean registerUser(String email, String userId, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (email, user_id, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, userId);
            pstmt.setString(3, password);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendEmail(String recipientEmail, String otp) {
        final String username = "your email id "; 
        final String password = "your 2fa password"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("CRAVENET - Your OTP Code");
            message.setText("Dear User,\n\nYour OTP code is: " + otp + 
                          "\n\nThis OTP is valid for 5 minutes. Please do not share it with anyone.\n\n" +
                          "Best regards,\nCRAVENET Team");

            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println("Email sending failed: " + e.getMessage());
            lblMessage.setText("Failed to send OTP. Please try again.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}