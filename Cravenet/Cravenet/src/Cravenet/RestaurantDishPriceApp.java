package Cravenet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDishPriceApp extends Application {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurantes1_data";
    private static final String USER = "root";
    private static final String PASSWORD = "518815";

    private Label swiggyPriceLabel;
    private Label zomatoPriceLabel;
    private Label comparisonLabel;
    private Button swiggyOrderButton;
    private Button zomatoOrderButton;
    private Button goBackButton;
    private Button orderHistoryButton;

    private int dishId;
    private String restaurantName;
    private String dishName;

    public RestaurantDishPriceApp(int dishId, String restaurantName, String dishName) {
        this.dishId = dishId;
        this.restaurantName = restaurantName;
        this.dishName = dishName;
    }

  
    public void start(Stage primaryStage) {
        Label titleLabel = new Label(restaurantName);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #1E90FF; -fx-font-weight: bold;");

        Label dishNameLabel = new Label(dishName);
        dishNameLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2F4F4F; -fx-font-weight: bold; -fx-padding: 10 0 20 0;");

        swiggyPriceLabel = new Label("Swiggy Price: ");
        swiggyPriceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FF8C00;");
        zomatoPriceLabel = new Label("Zomato Price: ");
        zomatoPriceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FF4500;");
        
        comparisonLabel = new Label();
        comparisonLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2E8B57; -fx-padding: 10 0;");

        swiggyOrderButton = new Button("Place Order on Swiggy");
        zomatoOrderButton = new Button("Place Order on Zomato");
        orderHistoryButton = new Button("View Order History");

        swiggyOrderButton.setOnAction(e -> placeOrder("Swiggy", dishName));
        zomatoOrderButton.setOnAction(e -> placeOrder("Zomato", dishName));
        orderHistoryButton.setOnAction(e -> showOrderHistory(primaryStage));

        goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        goBackButton.setOnAction(e -> primaryStage.close());

        Image swiggyLogo = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\swiggy.jpg");
        ImageView swiggyLogoView = new ImageView(swiggyLogo);
        swiggyLogoView.setFitHeight(30);
        swiggyLogoView.setPreserveRatio(true);

        Image zomatoLogo = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\zomato.jpg");
        ImageView zomatoLogoView = new ImageView(zomatoLogo);
        zomatoLogoView.setFitHeight(30);
        zomatoLogoView.setPreserveRatio(true);

        Image restaurantLogo = new Image("file:/C:/path/to/restaurant_logo.png");
        ImageView restaurantLogoView = new ImageView(restaurantLogo);
        restaurantLogoView.setFitHeight(50);
        restaurantLogoView.setPreserveRatio(true);

        HBox swiggyLayout = new HBox(10, swiggyLogoView, swiggyPriceLabel);
        HBox zomatoLayout = new HBox(10, zomatoLogoView, zomatoPriceLabel);

        swiggyOrderButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        zomatoOrderButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");

        VBox layout = new VBox(15, restaurantLogoView, titleLabel, dishNameLabel, swiggyLayout, zomatoLayout, comparisonLabel, swiggyOrderButton, zomatoOrderButton, orderHistoryButton, goBackButton);
        layout.setStyle("-fx-background-color: #E0FFFF;");
        layout.setPadding(new Insets(20));
        layout.setSpacing(15);
        Scene scene = new Scene(layout, 360, 640);

        primaryStage.setTitle("Dish Price Finder");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadPrices();
    }

    private void loadPrices() {
        String query = "SELECT pd.swiggy_price, pd.zomato_price " +
                       "FROM price_data pd " +
                       "JOIN dishes d ON pd.dish_id = d.id " +
                       "JOIN restaurants r ON pd.restaurant_id = r.id " +
                       "WHERE d.id = ? AND r.name = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, dishId);
            preparedStatement.setString(2, restaurantName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double swiggyPrice = resultSet.getDouble("swiggy_price");
                    double zomatoPrice = resultSet.getDouble("zomato_price");
                    swiggyPriceLabel.setText("Swiggy Price: ₹" + swiggyPrice);
                    zomatoPriceLabel.setText("Zomato Price: ₹" + zomatoPrice);

                    comparePrices(swiggyPrice, zomatoPrice);
                } else {
                    swiggyPriceLabel.setText("No price data found.");
                    zomatoPriceLabel.setText("");
                    comparisonLabel.setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void comparePrices(double swiggyPrice, double zomatoPrice) {
        if (swiggyPrice < zomatoPrice) {
            comparisonLabel.setText("Swiggy is cheaper.");
            zomatoOrderButton.setDisable(true);
        } else if (zomatoPrice < swiggyPrice) {
            comparisonLabel.setText("Zomato is cheaper.");
            swiggyOrderButton.setDisable(true);
        } else {
            comparisonLabel.setText("Both prices are the same.");
            swiggyOrderButton.setDisable(false);
            zomatoOrderButton.setDisable(false);
        }
    }

    private void placeOrder(String platform, String dishName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String insertOrderQuery = "INSERT INTO orders (dish_name, restaurant_name, platform) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderQuery)) {
                preparedStatement.setString(1, dishName);
                preparedStatement.setString(2, restaurantName);
                preparedStatement.setString(3, platform);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Order placed for " + dishName + " on " + platform);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Order Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to place the order. Please try again.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while placing the order.");
            alert.showAndWait();
        }
    }

    private void showOrderHistory(Stage primaryStage) {
        Stage orderHistoryStage = new Stage();
        VBox orderHistoryLayout = new VBox(10);
        orderHistoryLayout.setPadding(new Insets(10));

        Label titleLabel = new Label("Order History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        List<String> orders = fetchOrderHistory();

        ListView<String> orderListView = new ListView<>();
        orderListView.getItems().addAll(orders);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> orderHistoryStage.close());

        orderHistoryLayout.getChildren().addAll(titleLabel, orderListView, closeButton);
        Scene orderHistoryScene = new Scene(orderHistoryLayout, 400, 300);
        orderHistoryStage.setTitle("Order History");
        orderHistoryStage.setScene(orderHistoryScene);
        orderHistoryStage.show();
    }

    private List<String> fetchOrderHistory() {
        List<String> orders = new ArrayList<>();
        String query = "SELECT dish_name, restaurant_name, platform, order_time FROM orders ORDER BY order_time DESC";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String dishName = resultSet.getString("dish_name");
                String restaurantName = resultSet.getString("restaurant_name");
                String platform = resultSet.getString("platform");
                String orderTime = resultSet.getString("order_time");
                orders.add(String.format("%s from %s on %s - %s", dishName, restaurantName, platform, orderTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
