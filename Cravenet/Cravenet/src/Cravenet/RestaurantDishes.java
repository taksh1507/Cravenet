package Cravenet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantDishes extends Application {
    private Connection connection;
    private int restaurantId;
    private String restaurantName;

    public RestaurantDishes(int restaurantId, String restaurantName, Connection connection) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dishes at " + restaurantName);

     
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #E0FFFF;");

       
        HBox topLayout = new HBox();
        topLayout.setAlignment(Pos.CENTER_LEFT);
        topLayout.setSpacing(10);

       
        Image backButtonImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\back1.png"); 
        Button backButton = new Button();
        ImageView backImageView = new ImageView(backButtonImage);
        backImageView.setFitHeight(30);
        backImageView.setFitWidth(30);
        backButton.setGraphic(backImageView);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnAction(e -> goBack(primaryStage));

   
        Image logoImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\Logo.jpg"); 
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(60); 
        logoView.setFitWidth(60); 
        logoView.setPreserveRatio(true);

      
        topLayout.getChildren().addAll(backButton, logoView);
        mainLayout.setTop(topLayout);

        GridPane dishGrid = new GridPane();
        dishGrid.setHgap(10);
        dishGrid.setVgap(10);
        dishGrid.setAlignment(Pos.TOP_CENTER);

        loadDishes(restaurantId, dishGrid);

       
        ScrollPane scrollPane = new ScrollPane(dishGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E0FFFF;");
        mainLayout.setCenter(scrollPane);

        Scene scene = new Scene(mainLayout, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadDishes(int restaurantId, GridPane dishGrid) {
        try {
            
            String query = "SELECT d.id, d.name, d.image_path FROM dishes d " +
                    "JOIN restaurant_dish rd ON d.id = rd.dish_id " +
                    "WHERE rd.restaurant_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            int rowIndex = 0;

            
            while (rs.next()) {
                int dishId = rs.getInt("id");
                String dishName = rs.getString("name");
                String imagePath = rs.getString("image_path");

               
                VBox dishItem = new VBox();
                dishItem.setAlignment(Pos.CENTER);
                dishItem.setSpacing(10);
                dishItem.setPrefWidth(200);

                // Load and add dish image
                if (imagePath != null && !imagePath.isEmpty()) {
                    Image dishImage = new Image("file:" + imagePath);
                    ImageView imageView = new ImageView(dishImage);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    imageView.setPreserveRatio(true);
                    dishItem.getChildren().add(imageView);
                }

               
                Button dishButton = createStyledButton(dishName, Color.web("#1E90FF"));
                dishButton.setPrefWidth(460);
                dishButton.setPrefHeight(50);
                dishButton.setOnAction(e -> displayPrices(dishId, dishName)); 

                dishItem.getChildren().add(dishButton);
                dishGrid.add(dishItem, 0, rowIndex++);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayPrices(int dishId, String dishName) {
       
        RestaurantDishPriceApp priceApp = new RestaurantDishPriceApp(dishId, restaurantName, dishName);
        try {
            priceApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Button createStyledButton(String text, Color bgColor) {
        Button button = new Button(text);
        button.setFont(new Font("Verdana", 16));
        button.setStyle("-fx-background-color: " + toHexString(bgColor) + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + toHexString(bgColor.darker()) + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + toHexString(bgColor) + "; -fx-text-fill: white;"));
        return button;
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    private void goBack(Stage primaryStage) {
        primaryStage.close(); 

        // Open the RestaurantSelection window
        RestaurantSelection restaurantSelection = new RestaurantSelection();  
        Stage restaurantSelectionStage = new Stage(); 
        try {
            restaurantSelection.start(restaurantSelectionStage); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            // Establish connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantes1_data", "root", "518815"); 
            RestaurantDishes restaurantDishes = new RestaurantDishes(1, "Restaurant Name", connection);
            restaurantDishes.start(new Stage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
