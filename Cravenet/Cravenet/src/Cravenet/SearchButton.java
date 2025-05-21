package Cravenet;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class SearchButton extends Application {

    private TextField searchField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CRAVENET - Search");

       
        VBox contentPane = new VBox();
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setStyle("-fx-background-color: #E0FFFF;"); // Light Cyan background color

       
        Image logoImage = new Image("file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\Logo.png"); // Replace with actual path
        ImageView logoLabel = new ImageView(logoImage);
        logoLabel.setFitHeight(70);
        logoLabel.setFitWidth(100);
        contentPane.getChildren().add(logoLabel);

        
        Label titleLabel = new Label("CRAVENET");
        titleLabel.setFont(Font.font("Verdana", 28));
        titleLabel.setTextFill(Color.web("#1E90FF"));
        contentPane.getChildren().add(titleLabel);

       
        HBox topLayout = new HBox();
        topLayout.setAlignment(Pos.TOP_LEFT);
        topLayout.setPadding(new Insets(10));
        contentPane.getChildren().add(topLayout);

        
        Button viewOrdersButton = new Button("View Orders");
        viewOrdersButton.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        viewOrdersButton.setOnAction(e -> openOrderHistoryPage(primaryStage));
        topLayout.getChildren().add(viewOrdersButton);

      
        VBox searchPanel = new VBox(10);
        searchPanel.setStyle("-fx-background-color: white; -fx-border-color: #4682B4; -fx-border-width: 1; -fx-background-radius: 10; -fx-border-radius: 10;");
        searchPanel.setAlignment(Pos.CENTER);
        searchPanel.setPadding(new Insets(20));
        searchPanel.setMinWidth(360); 
        searchPanel.setMaxWidth(360); 

        contentPane.getChildren().add(searchPanel);

       
        searchField = new TextField();
        searchField.setPromptText("Enter your search...");
        searchField.setStyle("-fx-font-size: 14px; -fx-background-color: WHITE; -fx-border-color: DODGERBLUE; -fx-border-width: 1;");
        searchField.setPrefWidth(300);
        
        
        Tooltip searchTooltip = new Tooltip("Type your dish or restaurant name here");
        searchField.setTooltip(searchTooltip);
        searchPanel.getChildren().add(searchField);

       
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> {

            System.out.println("Searching for: " + searchField.getText());
        });
        searchPanel.getChildren().add(searchButton);

        // Add image buttons with labels to the search panel
        searchPanel.getChildren().add(createImageButtonWithLabel("Dishes", "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\dish.jpg", primaryStage));
        searchPanel.getChildren().add(createImageButtonWithLabel("Restaurant", "file:C:\\Users\\ashish\\OneDrive\\Documents\\Desktop\\Cravenet\\Location Image\\Restaurant.jpg", primaryStage));

      
        Scene scene = new Scene(contentPane, 360, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
        
       
        primaryStage.setResizable(false);
    }

    private VBox createImageButtonWithLabel(String text, String imagePath, Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5); 
        vbox.setPrefHeight(150); 

     
        Button button = new Button();
        button.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setPrefWidth(300);
        
      
        Image img = new Image(imagePath);
        
       
        if (img.isError()) {
            System.out.println("Error loading image: " + imagePath);
        } else {
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(100); 
            imageView.setPreserveRatio(true);
            
           
            button.setGraphic(imageView);
            button.setContentDisplay(javafx.scene.control.ContentDisplay.TOP); 
        }

       
        button.setOnAction(e -> {
            if (text.equals("Dishes")) {
                openDishSelectionPage(primaryStage);
            } else {
                openRestaurantSelectionPage(primaryStage); 
            }
        });

      
        Label label = new Label(text);
        label.setFont(Font.font("Verdana", 14));
        label.setTextFill(Color.web("#1E90FF"));

       
        vbox.getChildren().addAll(button, label);
        return vbox;
    }

    private void openDishSelectionPage(Stage primaryStage) {
      
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
        fadeOut.setNode(primaryStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        fadeOut.setOnFinished(event -> {
         
            DishSelection dishSelection = new DishSelection(); 
            dishSelection.start(primaryStage); 
        });

        fadeOut.play();
    }

    private void openRestaurantSelectionPage(Stage primaryStage) {
       
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
        fadeOut.setNode(primaryStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        fadeOut.setOnFinished(event -> {
           
            RestaurantSelection restaurantSelection = new RestaurantSelection();
            restaurantSelection.start(primaryStage);
        });

        fadeOut.play();
    }

    private void openOrderHistoryPage(Stage primaryStage) {
 
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5));
        fadeOut.setNode(primaryStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        fadeOut.setOnFinished(event -> {
            
            OrderHistory orderHistory = new OrderHistory(); 
            orderHistory.start(primaryStage); 
        });

        fadeOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
