import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Load images
        Image image1 = new Image("file:src/resources/map_part1.png");
        Image image2 = new Image("file:src/resources/map_part2.png");
        Image image3 = new Image("file:src/resources/map_part3.png");

        // Create ImageView objects for each image
        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);

        // Create an HBox to arrange images horizontally
        HBox mapContainer = new HBox();
        mapContainer.getChildren().addAll(imageView1, imageView2, imageView3);

        // Bind each image’s width to one-third of the window width
        imageView1.fitWidthProperty().bind(Bindings.divide(primaryStage.widthProperty(), 3));
        imageView2.fitWidthProperty().bind(Bindings.divide(primaryStage.widthProperty(), 3));
        imageView3.fitWidthProperty().bind(Bindings.divide(primaryStage.widthProperty(), 3));

        // Preserve the aspect ratio for each image
        imageView1.setPreserveRatio(true);
        imageView2.setPreserveRatio(true);
        imageView3.setPreserveRatio(true);

        // Set the Scene and add the HBox to it
        Scene scene = new Scene(mapContainer, 800, 400); // Adjust initial size as desired
        primaryStage.setTitle("Dynamic Resizing Map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
