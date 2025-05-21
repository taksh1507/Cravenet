package Cravenet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends Application {

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/restaurantes1_data"; // Update with your DB name
    private static final String USER = "root"; // Update with your DB username
    private static final String PASSWORD = "518815"; // Update with your DB password

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Order History");

        // Create main layout
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #E0FFFF;");

        // Title Label
        Label titleLabel = new Label(" Order History");
        titleLabel.setFont(Font.font("Verdana", 28));
        titleLabel.setTextFill(Color.web("#1E90FF"));
        layout.getChildren().add(titleLabel);

        // ListView for displaying orders
        ListView<String> orderListView = new ListView<>();
        List<String> orderHistory = getOrderHistory(); // Fetch order history from the database
        orderListView.getItems().addAll(orderHistory);
        orderListView.setPrefWidth(300);
        orderListView.setPrefHeight(400);
        layout.getChildren().add(orderListView);

        // Go Back button
        Button goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        goBackButton.setOnAction(e -> {
            // Create fade-out transition for the current scene
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
            fadeOut.setNode(layout);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            fadeOut.setOnFinished(event -> {
                // Return to the previous scene (e.g., SearchButton)
                SearchButton searchButton = new SearchButton();
                searchButton.start(primaryStage);
            });

            fadeOut.play(); // Start the fade-out transition
        });
        layout.getChildren().add(goBackButton);

        // Create and set the scene
        Scene scene = new Scene(layout, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> getOrderHistory() {
        List<String> orderHistory = new ArrayList<>();
        String query = "SELECT dish_name, restaurant_name, platform, order_time FROM orders ORDER BY order_time DESC"; // Update with your actual order table and column names

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Construct the order string
                String dishName = resultSet.getString("dish_name");
                String restaurantName = resultSet.getString("restaurant_name");
                String platform = resultSet.getString("platform");
                String orderTime = resultSet.getString("order_time");
                
                // Combine order details into a single string
                String order = String.format("%s from %s via %s on %s", dishName, restaurantName, platform, orderTime);
                orderHistory.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
            orderHistory.add("Error fetching order history.");
        }
        return orderHistory;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
