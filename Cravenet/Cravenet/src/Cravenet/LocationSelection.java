package Cravenet;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;

public class LocationSelection extends Application {

    private ComboBox<String> locationDropdown;
    private Button proceedButton;
    private ImageView locationIcon;
    private Label feedbackLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Select Location");

       
        VBox contentPane = new VBox(15);
        contentPane.setPadding(new Insets(20));
        contentPane.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        contentPane.setAlignment(Pos.CENTER); // Center align the content

        
        Label titleLabel = new Label("Welcome to Cravenet");
        titleLabel.setFont(Font.font("Verdana", 22));  
        titleLabel.setTextFill(Color.DARKBLUE);

       
        Label instructionLabel = new Label("Please select your location:");
        instructionLabel.setFont(Font.font("Verdana", 14));  
        instructionLabel.setTextFill(Color.DARKSLATEGRAY);

      
        locationIcon = new ImageView(new Image("file:/C:/Users/ashish/OneDrive/Documents/Desktop/Cravenet/Location Image/Map.jpg"));
        locationIcon.setFitHeight(200);  
        locationIcon.setFitWidth(200);
        locationIcon.setPreserveRatio(true); 

    
        locationDropdown = new ComboBox<>();
        locationDropdown.getItems().addAll("Mumbai", "Delhi", "Kolkata", "Hyderabad", "Surat", "Ahmedabad", "Pune");
        locationDropdown.setStyle("-fx-font-size: 14px; -fx-border-color: DODGERBLUE; -fx-border-width: 1; -fx-background-color: WHITE;");
        locationDropdown.setPrefWidth(200);  
        
        Tooltip locationTooltip = new Tooltip("Choose your location from the dropdown");
        locationDropdown.setTooltip(locationTooltip);

       
        proceedButton = new Button("Proceed");
        proceedButton.setFont(Font.font("Verdana", 14)); 
        proceedButton.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        proceedButton.setPrefWidth(200);  
        proceedButton.setOnMouseEntered(e -> proceedButton.setStyle("-fx-background-color: LIGHTBLUE; -fx-text-fill: white; -fx-font-weight: bold;"));
        proceedButton.setOnMouseExited(e -> proceedButton.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: white; -fx-font-weight: bold;"));

     
        feedbackLabel = new Label("");
        feedbackLabel.setFont(Font.font("Verdana", 12));
        feedbackLabel.setTextFill(Color.DARKRED);

        
        proceedButton.setOnAction(e -> handleCitySelection(primaryStage));

       
        contentPane.getChildren().addAll(titleLabel, instructionLabel, locationIcon, locationDropdown, proceedButton, feedbackLabel);

      
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), contentPane);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();

        
        Scene scene = new Scene(contentPane, 360, 640); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCitySelection(Stage primaryStage) {
        String selectedLocation = locationDropdown.getValue();

        if (selectedLocation != null) {
            if (selectedLocation.equals("Mumbai")) {
                redirectToMumbaiScene(primaryStage);
            } else {
                feedbackLabel.setText("City not available at this moment");
                feedbackLabel.setTextFill(Color.RED);  
            }
        } else {
            feedbackLabel.setText("Please select a city!");
            feedbackLabel.setTextFill(Color.RED); 
        }
    }

    private void redirectToMumbaiScene(Stage primaryStage) {
       
        SearchButton searchButton = new SearchButton();
        searchButton.start(primaryStage);    }

    public void show() {
        
    }
}
