import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // Simulation parameters
    private int simulationTime;
    private int numberOfCars;
    private int numberOfPedestrian;
    private int numberOfLanes;
    private int numberOfRoads;

    // Windows and simulation instances for phases
    private Stage phase1Window;
    private Stage phase2Window;
    private TrafficSimulation phase1Simulation;
    private TrafficSimulation phase2Simulation;

    // Entry point for JavaFX application
    @Override
    public void start(Stage primaryStage) {
        showInputForm(primaryStage); // Show the input form to gather user inputs
    }

    private void showInputForm(Stage primaryStage) {
        // Create the input form layout (VBox)
        VBox inputForm = new VBox(10);
        inputForm.setPadding(new Insets(20)); // Padding between edges
        inputForm.setAlignment(Pos.CENTER); // Center the elements within the form

        // Text fields for user input
        TextField timeField = new TextField();
        TextField carsField = new TextField();
        TextField lanesField = new TextField();
        ObservableList<String> inputNumberOfRoads = FXCollections.observableArrayList("1", "2");
        ComboBox<String> comboBox = new ComboBox<>(inputNumberOfRoads);
        comboBox.setPromptText("Select the number of roads");
        TextField pedestrianField = new TextField();
    
        // Add labels and text fields to the form
        inputForm.getChildren().addAll(
            createLabelWithDynamicFont("Simulation Time (seconds):", primaryStage),
            timeField,
            createLabelWithDynamicFont("Number of Cars:", primaryStage),
            carsField,
            createLabelWithDynamicFont("Number of Pedestrian:", primaryStage),
            pedestrianField,
            createLabelWithDynamicFont("Number of Lanes:", primaryStage),
            lanesField,
            createLabelWithDynamicFont("Number of Roads:", primaryStage),
            comboBox
        );
    
        // Bind the font size of the TextFields to the window size
        bindFontSizeToTextField(timeField, primaryStage);
        bindFontSizeToTextField(carsField, primaryStage);
        bindFontSizeToTextField(lanesField, primaryStage);
        bindFontSizeToTextField(pedestrianField, primaryStage);
    
        // Button to submit the form
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                // Parse user input from the text fields
                simulationTime = Integer.parseInt(timeField.getText());
                numberOfCars = Integer.parseInt(carsField.getText());
                numberOfPedestrian = Integer.parseInt(pedestrianField.getText());
                numberOfLanes = Integer.parseInt(lanesField.getText());
                numberOfRoads = Integer.parseInt(comboBox.getValue());
    
                // Validate that all inputs are positive numbers
                if (simulationTime <= 0 || numberOfCars <= 0 || numberOfPedestrian <= 0 || 
                    numberOfLanes <= 0) {
                    throw new NumberFormatException();
                }
    
                // If validation is successful, proceed to the next stage
                showControlPanel(); // Transition to the control panel for the simulation
                primaryStage.close(); // Close the current input form stage
            } catch (NumberFormatException ex) {
                // Display an error alert if the input is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Please enter valid positive numbers for all fields.");
                alert.showAndWait(); // Wait for the user to acknowledge the error
            }
        });
    
        // Add the submit button to the form
        inputForm.getChildren().add(submitButton);
    
        // Make the VBox responsive by binding the width and height
        inputForm.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.8)); // 80% of window width
        inputForm.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.8)); // 80% of window height
    
        // Set up the scene for the input form and show it
        Scene scene = new Scene(inputForm, 500, 600);
        scene.getStylesheets().add(getClass().getResource("traffic_styles.css").toExternalForm());
    
        // Set the window title and scene
        primaryStage.setTitle("Simulation Configuration");
        primaryStage.setScene(scene);
    
        // Set the minimum size for the window (prevents shrinking too much)
        primaryStage.setMinWidth(300);  // Minimum width of the window
        primaryStage.setMinHeight(600); // Minimum height of the window
        // primaryStage.setMaxWidth(800);
        // Set the default size for the window
        primaryStage.setWidth(400);     // Default width
        primaryStage.setHeight(600);    // Default height
        // Get the primary screen
        Screen screen = Screen.getPrimary();

        // Get the screen bounds
        Rectangle2D screenBounds = screen.getBounds();
        double screenWidth = screenBounds.getWidth();
        // double screenHeight = screenBounds.getHeight();
        // Set initial position on screen
        primaryStage.setX((screenWidth/2)-200);         // Set initial X position
        primaryStage.setY(50);         // Set initial Y position
        
        // Show the stage (window)
        primaryStage.show();
    }
    
    private void bindFontSizeToTextField(TextField textField, Stage primaryStage) {
        double windowHeight = primaryStage.getHeight();
        double windowWidth = primaryStage.getWidth();
        if(windowHeight>=windowWidth){
        // Bind the font size to the width of the stage, with a fallback value to avoid NaN
        textField.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
            Bindings.when(primaryStage.widthProperty().greaterThan(0))
                   .then(primaryStage.widthProperty().multiply(0.02))
                   .otherwise(16))); // Default size in case the width is 0 or invalid
        }
        else{
                    // Bind the font size to the width of the stage, with a fallback value to avoid NaN
        textField.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
        Bindings.when(primaryStage.heightProperty().greaterThan(0))
               .then(primaryStage.heightProperty().multiply(0.02))
               .otherwise(16))); // Default size in case the width is 0 or invalid
        }

    }
    
    private Label createLabelWithDynamicFont(String text, Stage primaryStage) {
        Label label = new Label(text);
        double windowHeight = primaryStage.getHeight();
        double windowWidth = primaryStage.getWidth();
        if(windowHeight>=windowWidth){
        // Bind the font size to the width of the stage, with a fallback value to avoid NaN
        label.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
            Bindings.when(primaryStage.widthProperty().greaterThan(0))
                   .then(primaryStage.widthProperty().multiply(0.025))
                   .otherwise(16))); // Default size in case the width is 0 or invalid
        }
        else{
                    // Bind the font size to the width of the stage, with a fallback value to avoid NaN
        label.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
        Bindings.when(primaryStage.heightProperty().greaterThan(0))
               .then(primaryStage.heightProperty().multiply(0.025))
               .otherwise(16))); // Default size in case the width is 0 or invalid
        }
        return label;
    }
    

    private void showControlPanel() {
        Stage controlStage = new Stage();
        VBox controlPanel = new VBox(20);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setAlignment(Pos.CENTER);
    
        // Create buttons for control panel
        Button phase1Button = new Button("Start Phase 1");
        Button phase2Button = new Button("Start Phase 2");
        Button compareButton = new Button("Compare Results");
        
        // Bind the font size of each button to the window size (width)
        bindFontSizeToButton(phase1Button, controlStage);
        bindFontSizeToButton(phase2Button, controlStage);
        bindFontSizeToButton(compareButton, controlStage);
    
        // Add actions to buttons
        phase1Button.setOnAction(e -> startPhase1Simulation());
        phase2Button.setOnAction(e -> startPhase2Simulation());
        compareButton.setOnAction(e -> compareResults());
    
        // Add buttons to the control panel
        controlPanel.getChildren().addAll(
            phase1Button,
            phase2Button,
            compareButton
        );
    
        // Create the scene for the control panel
        Scene scene = new Scene(controlPanel, 250, 200);
        scene.getStylesheets().add(getClass().getResource("traffic_styles.css").toExternalForm());
        controlStage.setTitle("Simulation Control Panel");
        controlStage.setScene(scene);
        
        // Set the minimum size for the window
        controlStage.setMinWidth(300);  // Minimum width of the control panel window
        controlStage.setMinHeight(200); // Minimum height of the control panel window
        
        // Set the default size for the window
        controlStage.setWidth(400);     // Default width
        controlStage.setHeight(300);    // Default height
                // Get the primary screen
        Screen screen = Screen.getPrimary();

        // Get the screen bounds
        Rectangle2D screenBounds = screen.getBounds();
        double screenWidth = screenBounds.getWidth();
        // double screenHeight = screenBounds.getHeight();
        // Set initial position on screen
        controlStage.setX((screenWidth/2)-200);         // Set initial X position
        controlStage.setY(100);         // Set initial Y position
        
        // Show the control panel window
        controlStage.show();
    }
    
    /**
     * Helper method to bind the font size of a Button to the window width.
     * @param button the Button to apply the font size binding to
     * @param controlStage the control window Stage to bind the font size to
     */
    private void bindFontSizeToButton(Button button, Stage controlStage) {
        double windowHeight = controlStage.getHeight();
        double windowWidth = controlStage.getWidth();
        if(windowHeight>=windowWidth){
            button.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
            Bindings.when(controlStage.widthProperty().greaterThan(0))
                   .then(controlStage.widthProperty().multiply(0.05))
                   .otherwise(16))); // Default size in case the width is 0 or invalid
        }
        else{
            button.styleProperty().bind(Bindings.format("-fx-font-size: %.0f;", 
            Bindings.when(controlStage.heightProperty().greaterThan(0))
                   .then(controlStage.heightProperty().multiply(0.05))
                   .otherwise(16))); // Default size in case the width is 0 or invalid
        }
    }
    
    // Start phase 1 simulation
    private void startPhase1Simulation() {
        // Close previous phase 1 window if it exists
        if (phase1Window != null) {
            if (phase1Simulation != null) {
                phase1Simulation.stopSimulation(); // Stop ongoing simulation
            }
            phase1Window.close();
        }

        // Create a new window for phase 1 simulation
        phase1Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", 5000);
        phase1Simulation = new TrafficSimulation(road, numberOfCars, numberOfPedestrian, simulationTime);

        // Create and display the simulation scene
        Scene simulationScene = phase1Simulation.createSimulationScene();
        phase1Window.setTitle("Phase 1 Simulation");
        phase1Window.setScene(simulationScene);
        phase1Window.show();
    }

    // Start phase 2 simulation
    private void startPhase2Simulation() {
        // Close previous phase 2 window if it exists
        if (phase2Window != null) {
            if (phase2Simulation != null) {
                phase2Simulation.stopSimulation(); // Stop ongoing simulation
            }
            phase2Window.close();
        }

        // Create a new window for phase 2 simulation
        phase2Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "enhanced", simulationTime);
        phase2Simulation = new TrafficSimulation(road, numberOfCars, numberOfPedestrian, simulationTime);
        Scene simulationScene = phase2Simulation.createSimulationScene();

        // Display the simulation scene
        phase2Window.setTitle("Phase 2 Simulation");
        phase2Window.setScene(simulationScene);
        phase2Window.show();
    }

    private void compareResults() {
        if (phase1Simulation == null || phase2Simulation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Simulation");
            alert.setContentText("Please run both phases before comparing results.");
            alert.showAndWait();
            return;
        }
        
        Stage compareStage = new Stage();
        VBox comparePanel = new VBox(10);
        comparePanel.setPadding(new Insets(20));
        comparePanel.setAlignment(Pos.CENTER);
        
        StringBuilder comparison = new StringBuilder();
        comparison.append("Simulation Results\n\n");
        
        comparison.append(String.format("Phase 1:\n" +
            "Passed Cars: %d\n" +
            "Passed Pedestrians: %d\n\n", 
            phase1Simulation.getPassedCars(),
            phase1Simulation.getPassedPedestrians()));
            
        comparison.append(String.format("Phase 2:\n" +
            "Passed Cars: %d\n" +
            "Passed Pedestrians: %d\n\n",
            phase2Simulation.getPassedCars(),
            phase2Simulation.getPassedPedestrians()));
            
        if (phase1Simulation.getPassedCars() > 0 && phase1Simulation.getPassedPedestrians() > 0) {
            double carImprovement = ((double)(phase2Simulation.getPassedCars() - phase1Simulation.getPassedCars()) / 
                               phase1Simulation.getPassedCars()) * 100;
            double pedImprovement = ((double)(phase2Simulation.getPassedPedestrians() - phase1Simulation.getPassedPedestrians()) / 
                               phase1Simulation.getPassedPedestrians()) * 100;
                               
            comparison.append(String.format("Improvements:\n" +
                "Cars: %.1f%%\n" +
                "Pedestrians: %.1f%%",
                carImprovement,
                pedImprovement));
        }
        // createLabelWithDynamicFont(comparison.toString(), compareStage);
        comparePanel.getChildren().add(createLabelWithDynamicFont(comparison.toString(), compareStage)
        );
        
        Scene scene = new Scene(comparePanel, 300, 400);
        scene.getStylesheets().add(getClass().getResource("traffic_styles.css").toExternalForm());

        compareStage.setTitle("Comparison Results");
        compareStage.setScene(scene);
        compareStage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
