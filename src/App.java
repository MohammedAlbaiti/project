import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Image image1 = new Image("file:src/resources/Group 45.png");
        Image image2 = new Image("file:src/resources/Group 36.png");
        Image image3 = new Image("file:src/resources/image 107.png");
        Image image7 = new Image("file:src/resources/Group 44.png");

        // Define initial widths for each image based on the provided sizes
        double width1 = 145.63;
        double width2 = 112.45;
        double width3 = 26.0;

        double totalWidth = 0;

        // Create an HBox to arrange images horizontally with zero spacing and padding
        HBox mapContainer = new HBox(0); // Set spacing to zero
        mapContainer.setStyle("-fx-padding: 0;"); // Ensure padding is zero

        int numberOfRoads = 2;
        int numberOfLanes = 1;

        for (int i = numberOfRoads; i > 0; i--) {
            totalWidth += width1;
            mapContainer.getChildren().add(new ImageView(image1)); // Add a new instance
            totalWidth += width2;
            mapContainer.getChildren().add(new ImageView(image2)); // Add a new instance

            for (int j = numberOfLanes - 1; j > 0; j--) {
                totalWidth += width3;
                mapContainer.getChildren().add(new ImageView(image3)); // Add a new instance
                totalWidth += width2;
                mapContainer.getChildren().add(new ImageView(image2)); // Add a new instance
            }

            totalWidth += width1;
            mapContainer.getChildren().add(new ImageView(image7)); // Add a new instance
        }

        // Set the Scene and add the HBox to it
        Scene scene = new Scene(mapContainer, totalWidth, 464); // Adjust initial size as desired
        primaryStage.setTitle("Concatenated Image Map without Gaps");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
