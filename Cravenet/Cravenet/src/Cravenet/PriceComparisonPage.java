package Cravenet;

import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceComparisonPage extends Application {
    private String restaurantName;
    private String dishName;
    private double swiggyPrice;
    private double zomatoPrice;
    private int dishId;
    private Connection connection;
    private ObservableList<String> orderHistory;

    public PriceComparisonPage(String restaurantName, String dishName, double swiggyPrice, double zomatoPrice, int dishId, Connection connection) {
        this.restaurantName = restaurantName;
        this.dishName = dishName;
        this.swiggyPrice = swiggyPrice;
        this.zomatoPrice = zomatoPrice;
        this.dishId = dishId;
        this.connection = connection;
        this.orderHistory = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Price Comparison");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        Image restaurantLogo = new Image("file:/path/to/restaurant_logo.png");
        ImageView restaurantLogoView = new ImageView(restaurantLogo);
        restaurantLogoView.setFitHeight(100);
        restaurantLogoView.setPreserveRatio(true);

        Image swiggyLogo = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\swiggy.jpg");
        ImageView swiggyLogoView = new ImageView(swiggyLogo);
        swiggyLogoView.setFitHeight(30);
        swiggyLogoView.setPreserveRatio(true);

        Image zomatoLogo = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\zomato.jpg");
        ImageView zomatoLogoView = new ImageView(zomatoLogo);
        zomatoLogoView.setFitHeight(30);
        zomatoLogoView.setPreserveRatio(true);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER_LEFT);

        Label restaurantLabel = new Label("Restaurant: " + restaurantName);
        restaurantLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #1E90FF; -fx-font-weight: bold;");

        Label dishLabel = new Label("Dish: " + dishName);
        dishLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #2F4F4F; -fx-font-weight: bold;");

        HBox swiggyLayout = new HBox(10, swiggyLogoView, new Label("Swiggy Price: " + (swiggyPrice >= 0 ? "₹" + swiggyPrice : "N/A")));
        swiggyLayout.setAlignment(Pos.CENTER_LEFT);
        swiggyLayout.setStyle("-fx-font-size: 18px; -fx-text-fill: #FF8C00;");

        HBox zomatoLayout = new HBox(10, zomatoLogoView, new Label("Zomato Price: " + (zomatoPrice >= 0 ? "₹" + zomatoPrice : "N/A")));
        zomatoLayout.setAlignment(Pos.CENTER_LEFT);
        zomatoLayout.setStyle("-fx-font-size: 18px; -fx-text-fill: #FF4500;");

        layout.getChildren().addAll(restaurantLabel, dishLabel, swiggyLayout, zomatoLayout);

        String cheaperService;
        double cheapestPrice;

        if (swiggyPrice >= 0 && zomatoPrice >= 0) {
            if (swiggyPrice < zomatoPrice) {
                cheaperService = "Swiggy";
                cheapestPrice = swiggyPrice;
            } else {
                cheaperService = "Zomato";
                cheapestPrice = zomatoPrice;
            }
        } else if (swiggyPrice >= 0) {
            cheaperService = "Swiggy";
            cheapestPrice = swiggyPrice;
        } else if (zomatoPrice >= 0) {
            cheaperService = "Zomato";
            cheapestPrice = zomatoPrice;
        } else {
            cheaperService = "None";
            cheapestPrice = -1;
        }

        Label comparisonLabel = new Label("Cheapest Service: " + cheaperService + " (₹" + (cheapestPrice >= 0 ? cheapestPrice : "N/A") + ")");
        comparisonLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2E8B57;");
        layout.getChildren().add(comparisonLabel);

        Button swiggyButton = new Button("Order on Swiggy");
        Button zomatoButton = new Button("Order on Zomato");

        swiggyButton.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #FF8C00; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        zomatoButton.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #FF4500; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        swiggyButton.setDisable(!cheaperService.equals("Swiggy"));
        zomatoButton.setDisable(!cheaperService.equals("Zomato"));

        swiggyButton.setOnAction(e -> placeOrder("Swiggy"));
        zomatoButton.setOnAction(e -> placeOrder("Zomato"));

        HBox buttonLayout = new HBox(10, swiggyButton, zomatoButton);
        buttonLayout.setSpacing(20);
        buttonLayout.setAlignment(Pos.CENTER_LEFT);

        Button goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #4682B4; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        goBackButton.setOnAction(e -> handleGoBack(primaryStage));

        Button orderHistoryButton = new Button("Show Order History");
        orderHistoryButton.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #6A5ACD; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        orderHistoryButton.setOnAction(e -> {
            fetchOrderHistory();
            showOrderHistoryPopup();
        });

        VBox vbox = new VBox(10, restaurantLogoView, layout, comparisonLabel, buttonLayout, goBackButton, orderHistoryButton);
        vbox.setAlignment(Pos.CENTER_LEFT);
        mainLayout.setCenter(vbox);

        Scene scene = new Scene(mainLayout, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void placeOrder(String service) {
        String message = "Order placed successfully on " + service + " for " + dishName + " from " + restaurantName + "!";
        JOptionPane.showMessageDialog(null, message);
        saveOrderToDatabase(service);
        orderHistory.add(message);
    }

    private void saveOrderToDatabase(String service) {
        String sql = "INSERT INTO orders (dish_name, restaurant_name, platform, order_time) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dishName);
            stmt.setString(2, restaurantName);
            stmt.setString(3, service);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchOrderHistory() {
        orderHistory.clear();
        String sql = "SELECT dish_name, restaurant_name, platform, order_time FROM orders ORDER BY order_time DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String record = rs.getString("order_time") + " - " + rs.getString("platform") + " - " + rs.getString("dish_name") + " from " + rs.getString("restaurant_name");
                orderHistory.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showOrderHistoryPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Order History");

        VBox popupLayout = new VBox();
        popupLayout.setPadding(new Insets(20));
        popupLayout.setSpacing(10);

        Label titleLabel = new Label("Order History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> orderHistoryView = new ListView<>(orderHistory);
        orderHistoryView.setPrefHeight(300);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupStage.close());

        popupLayout.getChildren().addAll(titleLabel, orderHistoryView, closeButton);
        popupLayout.setAlignment(Pos.CENTER);

        Scene popupScene = new Scene(popupLayout, 400, 400);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private void handleGoBack(Stage primaryStage) {
        primaryStage.close();
        RestaurantPage restaurantPage = new RestaurantPage(dishId, connection);
        try {
            restaurantPage.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
