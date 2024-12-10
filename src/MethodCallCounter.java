import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MethodCallCounter extends Application {

    private int methodCallCount = 0; // Counter for method calls
    private long lastTime = System.nanoTime(); // To track elapsed time
    private Label counterLabel = new Label("Calls per second: 0");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10, counterLabel);
        Scene scene = new Scene(root, 300, 200);

        // AnimationTimer to update count every second
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) { // One second (in nanoseconds)
                    counterLabel.setText("Calls per second: " + methodCallCount);
                    methodCallCount = 0; // Reset the counter
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        // Simulate a method being called repeatedly
        new Thread(() -> {
            while (true) {
                callYourMethod();
                try {
                    Thread.sleep(10); // Adjust the sleep time to change the call frequency
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Method Call Counter");
        primaryStage.show();
    }

    // Example method to track calls
    private void callYourMethod() {
        methodCallCount++;
    }
}
