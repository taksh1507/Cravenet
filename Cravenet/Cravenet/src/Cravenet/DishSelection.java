package Cravenet;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.util.HashMap;

public class DishSelection extends Application {
    private Connection connection;
    private Button backButton;

    private final String[] dishOrder = {
        "Pizza", "Burger", "Pasta", "Noodles", "Biryani", "Pav Bhaji",
        "Khichdi", "Paratha", "Cake", "Ice Cream", "Dosa", "Vada Pav",
        "Idli", "Sandwich", "Milkshake", "Fries", "Juice", "Cold Drink"
    };

    private final String[] imagePaths = {
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\pizza.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\burger.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\pasta.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\noodles.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\biryani.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Pavbhaji.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\khicdi.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\paratha.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\cake.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Ice cream.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Dosa.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Vada pav.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\IDLI.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Sandwich.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Milkshakes.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Fries.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\Juices.jpg",
        "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Mini project\\cold drink.jpg"
    };

    public DishSelection() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dish Viewer");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        Image logoImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location image\\Logo.jpg");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setFitHeight(100);

        VBox dishContainer = new VBox();
        dishContainer.setSpacing(10);
        dishContainer.setPadding(new Insets(10));

        loadDishes(dishContainer);

        ScrollPane scrollPane = new ScrollPane(dishContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(420);

        root.setCenter(scrollPane);

        backButton = new Button();
        backButton.setStyle("-fx-background-color: transparent;");
        Image backArrowImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\back1.png");
        ImageView backArrowView = new ImageView(backArrowImage);
        backArrowView.setFitWidth(20);
        backArrowView.setFitHeight(20);
        backButton.setGraphic(backArrowView);

        backButton.setOnMouseEntered(e -> backButton.setOpacity(0.7));
        backButton.setOnMouseExited(e -> backButton.setOpacity(1.0));
        backButton.setOnAction(e -> goBack(primaryStage));

        HBox topLeftBox = new HBox(backButton);
        topLeftBox.setAlignment(Pos.TOP_LEFT);
        topLeftBox.setPadding(new Insets(10));

        HBox logoBox = new HBox(logoView);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(10));

        VBox topBox = new VBox(logoBox, topLeftBox);
        root.setTop(topBox);

        Scene scene = new Scene(root, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadDishes(VBox dishContainer) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantes1_data", "root", "518815");
            Statement stmt = connection.createStatement();
            String sql = "SELECT d.id AS dish_id, d.name AS dish_name, GROUP_CONCAT(r.name) AS restaurant_names " +
                         "FROM dishes d " +
                         "JOIN restaurant_dish rd ON d.id = rd.dish_id " +
                         "JOIN restaurants r ON rd.restaurant_id = r.id " +
                         "GROUP BY d.id";
            ResultSet rs = stmt.executeQuery(sql);
            HashMap<String, Integer> dishIdMap = new HashMap<>();

            while (rs.next()) {
                String dishName = rs.getString("dish_name").trim();
                int dishId = rs.getInt("dish_id");
                dishIdMap.put(dishName, dishId);
            }

            for (int i = 0; i < dishOrder.length; i++) {
                String dishName = dishOrder[i].trim();
                if (dishIdMap.containsKey(dishName)) {
                    int dishId = dishIdMap.get(dishName);
                    createDishItem(dishContainer, dishId, dishName, i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDishItem(VBox dishContainer, int dishId, String dishName, int index) {
        HBox dishItemBox = new HBox();
        dishItemBox.setSpacing(10);
        dishItemBox.setAlignment(Pos.CENTER_LEFT);
        dishItemBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-background-color: white; -fx-border-width: 1;");

        Image dishImage = new Image(imagePaths[index]);
        ImageView dishImageView = new ImageView(dishImage);
        dishImageView.setFitWidth(80);
        dishImageView.setFitHeight(80);

        Button dishButton = new Button(dishName);
        dishButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-font-size: 14px;");
        dishButton.setOnAction(e -> openRestaurantPage(dishId));

        dishItemBox.getChildren().addAll(dishImageView, dishButton);
        dishContainer.getChildren().add(dishItemBox);
    }

    private void goBack(Stage currentStage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
        fadeOut.setNode(currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            currentStage.close();
            try {
                new SearchButton().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }

    private void openRestaurantPage(int dishId) {
        try {
            RestaurantPage restaurantPage = new RestaurantPage(dishId, connection);
            Stage restaurantStage = new Stage();
            restaurantPage.start(restaurantStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
