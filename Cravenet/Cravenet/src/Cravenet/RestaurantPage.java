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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import javax.swing.JOptionPane;

public class RestaurantPage extends Application {
    private Connection connection;
    private int dishId;

    public RestaurantPage(int dishId, Connection connection) {
        this.dishId = dishId;
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Available Restaurants");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #E0FFFF;");

        HBox topLayout = new HBox();
        topLayout.setPadding(new Insets(10, 10, 10, 10));

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Image backButtonImage = new Image("file:/C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/back1.png");
        Button backButton = new Button();
        ImageView backImageView = new ImageView(backButtonImage);
        backImageView.setFitHeight(30);
        backImageView.setFitWidth(30);
        backButton.setGraphic(backImageView);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnAction(e -> goBack(primaryStage));

        Image logoImage = new Image("file:/C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/Logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(100);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);

        topLayout.getChildren().addAll(backButton, leftSpacer, logoView, rightSpacer);
        mainLayout.setTop(topLayout);

        GridPane restaurantGrid = new GridPane();
        restaurantGrid.setHgap(10);
        restaurantGrid.setVgap(10);
        restaurantGrid.setAlignment(Pos.CENTER);

        loadRestaurants(dishId, restaurantGrid);

        ScrollPane scrollPane = new ScrollPane(restaurantGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E0FFFF;");
        mainLayout.setCenter(scrollPane);

        Scene scene = new Scene(mainLayout, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRestaurants(int dishId, GridPane restaurantGrid) {
        restaurantGrid.getChildren().clear();

        try {
            String query = "SELECT DISTINCT r.name FROM restaurants r " +
                           "JOIN restaurant_dish rd ON r.id = rd.restaurant_id " +
                           "WHERE rd.dish_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, dishId);
            ResultSet rs = pstmt.executeQuery();

            int rowIndex = 0;

            while (rs.next()) {
                String restaurantName = rs.getString("name");

                String imagePath = "C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Restaurant\\" + restaurantName.replace(" ", "_") + ".jpg";

                VBox restaurantItem = new VBox();
                restaurantItem.setAlignment(Pos.CENTER);
                restaurantItem.setSpacing(10);
                restaurantItem.setPrefWidth(200);

                Image restaurantImage = new Image("file:" + imagePath);
                restaurantImage.errorProperty().addListener((obs, oldError, newError) -> {});

                ImageView imageView = new ImageView(restaurantImage);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
                restaurantItem.getChildren().add(imageView);

                Button restaurantButton = createStyledButton(restaurantName, Color.web("#1E90FF"));
                restaurantButton.setOnAction(e -> displayPrices(restaurantName));

                restaurantItem.getChildren().add(restaurantButton);
                restaurantGrid.add(restaurantItem, 0, rowIndex++);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayPrices(String restaurantName) {
        String query = "SELECT d.name FROM dishes d " +
                       "JOIN restaurant_dish rd ON d.id = rd.dish_id " +
                       "JOIN restaurants r ON r.id = rd.restaurant_id " +
                       "WHERE r.name = ?";
        String actualDishName = "";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, restaurantName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                actualDishName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double swiggyPrice = getPrice("swiggy_price", restaurantName);
        double zomatoPrice = getPrice("zomato_price", restaurantName);

        PriceComparisonPage priceComparisonPage = new PriceComparisonPage(restaurantName, actualDishName, swiggyPrice, zomatoPrice, dishId, connection);
        try {
            priceComparisonPage.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getPrice(String priceType, String restaurantName) {
        String query = "SELECT pd." + priceType + " FROM price_data pd " +
                       "JOIN restaurants r ON pd.restaurant_id = r.id " +
                       "WHERE r.name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, restaurantName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(priceType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
        DishSelection dishSelection = new DishSelection();
        try {
            dishSelection.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/restaurantes1_data";
            String user = "root";
            String password = "518815";
            
            conn = DriverManager.getConnection(url, user, password);
            RestaurantPage restaurantPage = new RestaurantPage(1, conn);
            restaurantPage.start(new Stage());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
