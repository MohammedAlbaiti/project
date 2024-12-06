import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Font;

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

    // Display the input form to get simulation parameters
    private void showInputForm(Stage primaryStage) {
        VBox inputForm = new VBox(10); // Create a VBox layout with spacing of 10
        inputForm.setPadding(new Insets(20)); // Set padding around the form
        inputForm.setAlignment(Pos.CENTER); // Center align the elements

        // Create text fields and combo box for user inputs
        TextField timeField = new TextField();
        TextField carsField = new TextField();
        TextField lanesField = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("1", "2"); // Allow selection of 1 or 2 roads
        comboBox.setPromptText("Select the number of roads");
        TextField pedestrianField = new TextField();

        // Create labels for each input field
        Label timeLabel = createLabel("Simulation Time (seconds):");
        Label carsLabel = createLabel("Number of Cars:");
        Label pedestrianLabel = createLabel("Number of Pedestrian:");
        Label lanesLabel = createLabel("Number of Lanes:");
        Label roadsLabel = createLabel("Number of Roads:");

        // Add labels and input fields to the form
        inputForm.getChildren().addAll(
            timeLabel, timeField,
            carsLabel, carsField,
            pedestrianLabel, pedestrianField,
            lanesLabel, lanesField,
            roadsLabel, comboBox
        );

        // Create a submit button to validate and process user input
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                // Parse input values
                simulationTime = Integer.parseInt(timeField.getText());
                numberOfCars = Integer.parseInt(carsField.getText());
                numberOfPedestrian = Integer.parseInt(pedestrianField.getText());
                numberOfLanes = Integer.parseInt(lanesField.getText());
                numberOfRoads = Integer.parseInt(comboBox.getValue());

                // Validate inputs
                if (simulationTime <= 0 || numberOfCars <= 0 || numberOfPedestrian <= 0 || numberOfLanes <= 0) {
                    throw new NumberFormatException();
                }

                // Show control panel after successful input
                showControlPanel();
                primaryStage.close(); // Close the input form window
            } catch (NumberFormatException ex) {
                // Show an alert if input is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Please enter valid positive numbers for all fields.");
                alert.showAndWait();
            }
        });

        // Add the submit button to the form
        inputForm.getChildren().add(submitButton);

        // Create a scene for the input form
        Scene scene = new Scene(inputForm, 500, 600);
        primaryStage.setTitle("Simulation Configuration");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);

        // Position the window in the center of the screen
        Screen screen = Screen.getPrimary();
        primaryStage.setX(screen.getBounds().getWidth() / 2 - 200);
        primaryStage.setY(50);

        primaryStage.show(); // Display the input form window
    }

    // Helper method to create labels with consistent styling
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 16));  // Set a fixed font size of 16
        return label;
    }

    // Display the control panel to start simulations
    private void showControlPanel() {
        Stage controlStage = new Stage();
        VBox controlPanel = new VBox(20); // Create a VBox layout with spacing of 20
        controlPanel.setPadding(new Insets(20)); // Set padding around the panel
        controlPanel.setAlignment(Pos.CENTER); // Center align the elements

        // Create buttons for controlling the simulations
        Button phase1Button = new Button("Start Phase 1");
        Button phase2Button = new Button("Start Phase 2");
        Button compareButton = new Button("Compare Results");

        // Set consistent font size for buttons
        phase1Button.setFont(Font.font("Arial", 16));
        phase2Button.setFont(Font.font("Arial", 16));
        compareButton.setFont(Font.font("Arial", 16));

        // Set actions for each button
        phase1Button.setOnAction(e -> startPhase1Simulation());
        phase2Button.setOnAction(e -> startPhase2Simulation());
        compareButton.setOnAction(e -> compareResults());

        // Add buttons to the control panel
        controlPanel.getChildren().addAll(phase1Button, phase2Button, compareButton);

        // Create a scene for the control panel
        Scene scene = new Scene(controlPanel, 250, 200);
        controlStage.setTitle("Simulation Control Panel");
        controlStage.setScene(scene);
        controlStage.setMinWidth(300);
        controlStage.setMinHeight(200);
        controlStage.setWidth(400);
        controlStage.setHeight(300);

        // Position the window in the center of the screen
        Screen screen = Screen.getPrimary();
        controlStage.setX(screen.getBounds().getWidth() / 2 - 200);
        controlStage.setY(100);

        controlStage.show(); // Display the control panel window
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
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", simulationTime);
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

    // Compare the results of phase 1 and phase 2 simulations
    private void compareResults() {
        // Check if both phases have been run
        if (phase1Simulation == null || phase2Simulation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Simulation");
            alert.setContentText("Please run both phases before comparing results.");
            alert.showAndWait();
            return;
        }

        // Create a new window for displaying comparison results
        Stage compareStage = new Stage();
        VBox comparePanel = new VBox(10); // Create a VBox layout with spacing of 10
        comparePanel.setPadding(new Insets(20)); // Set padding around the panel
        comparePanel.setAlignment(Pos.CENTER); // Center align the elements

        // Prepare comparison results
        StringBuilder comparison = new StringBuilder();
        comparison.append("Simulation Results\n\n");

        // Append phase 1 results
        comparison.append(String.format("Phase 1:\n" +
            "Passed Cars: %d\n" +
            "Passed Pedestrians: %d\n\n", 
            phase1Simulation.getPassedCars(),
            phase1Simulation.getPassedPedestrians()));

        // Append phase 2 results
        comparison.append(String.format("Phase 2:\n" +
            "Passed Cars: %d\n" +
            "Passed Pedestrians: %d\n\n",
            phase2Simulation.getPassedCars(),
            phase2Simulation.getPassedPedestrians()));

        // Calculate and append improvements if applicable
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

        // Display the comparison results
        comparePanel.getChildren().add(createLabel(comparison.toString()));

        Scene scene = new Scene(comparePanel, 300, 400);
        compareStage.setTitle("Comparison Results");
        compareStage.setScene(scene);
        compareStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
