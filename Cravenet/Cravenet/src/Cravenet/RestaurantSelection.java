package Cravenet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RestaurantSelection extends Application {
    private Connection connection;
    private GridPane restaurantGrid;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Restaurant Selection");
        primaryStage.setWidth(360);
        primaryStage.setHeight(640);

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #E0FFFF;");

        Image backButtonImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\back1.png");
        Button backButton = new Button();
        ImageView backImageView = new ImageView(backButtonImage);
        backImageView.setFitHeight(30);
        backImageView.setFitWidth(30);
        backButton.setGraphic(backImageView);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnAction(e -> goBack(primaryStage));

        HBox buttonLayout = new HBox(backButton);
        buttonLayout.setAlignment(Pos.TOP_LEFT);
        buttonLayout.setPadding(new Insets(10, 0, 0, 0));

        mainLayout.getChildren().add(buttonLayout);

        Image logoImage = new Image("file:/C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/Logo.png");
        ImageView logoLabel = new ImageView(logoImage);
        logoLabel.setFitHeight(60);
        logoLabel.setFitWidth(60);

        VBox logoBox = new VBox(10);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getChildren().addAll(logoLabel, new Label("CRAVENET") {{
            setFont(new Font("Verdana", 28));
            setTextFill(Color.web("#1E90FF"));
        }});
        mainLayout.getChildren().add(logoBox);

        restaurantGrid = new GridPane();
        restaurantGrid.setHgap(15);
        restaurantGrid.setVgap(15);
        restaurantGrid.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(restaurantGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(420);
        mainLayout.getChildren().add(scrollPane);

        loadRestaurants();

        Scene scene = new Scene(mainLayout, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRestaurants() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantes1_data", "root", "518815");
            Statement stmt = connection.createStatement();
            String sql = "SELECT r.id AS restaurant_id, r.name AS restaurant_name FROM restaurants r";
            ResultSet rs = stmt.executeQuery(sql);

            int index = 0; 
            while (rs.next()) {
                int restaurantId = rs.getInt("restaurant_id");
                String restaurantName = rs.getString("restaurant_name");

                String sanitizedRestaurantName = restaurantName.replaceAll("[^a-zA-Z0-9]", "_");
                String imagePath = "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\" + sanitizedRestaurantName + ".jpg"; 

                HBox restaurantItem = new HBox(10);
                restaurantItem.setAlignment(Pos.CENTER_LEFT);
                restaurantItem.setPadding(new Insets(10));
                restaurantItem.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 2px; -fx-padding: 10;");

                ImageView imageView = new ImageView();
                Image img = new Image(imagePath);

                if (img.isError()) {
                    System.err.println("Image not found: " + imagePath);
                    Label errorLabel = new Label("Image not available");
                    restaurantItem.getChildren().add(errorLabel);
                } else {
                    imageView.setImage(img);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    restaurantItem.getChildren().add(imageView);
                }

                Label restaurantLabel = new Label(restaurantName);
                restaurantLabel.setFont(new Font("Arial", 18));
                restaurantLabel.setTextFill(Color.web("#1E90FF"));
                restaurantLabel.setWrapText(true);
                restaurantLabel.setMaxWidth(200);
                restaurantLabel.setStyle("-fx-padding: 5;");

                restaurantLabel.setOnMouseClicked(e -> openRestaurantDishes(restaurantId, restaurantName, primaryStage));

                restaurantItem.getChildren().add(restaurantLabel);
                restaurantGrid.add(restaurantItem, 0, index++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBack(Stage primaryStage) {
        SearchButton previousPage = new SearchButton();
        primaryStage.close();
        previousPage.start(primaryStage);
    }

    private void openRestaurantDishes(int restaurantId, String restaurantName, Stage primaryStage) {
        RestaurantDishes restaurantDishes = new RestaurantDishes(restaurantId, restaurantName, connection);
        restaurantDishes.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
