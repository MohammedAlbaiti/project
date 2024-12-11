import java.util.Optional;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * Traffic Simulation Application
 * This application allows users to configure and run traffic simulations,
 * and compare results across different simulation phases.
 */
public class App extends Application {

    // Simulation parameters
    private int simulationTime;
    private int numberOfVehicles;
    private int numberOfPedestrian;
    private int numberOfLanes;
    private int numberOfPaths;
    private boolean autoVehiclesGeneration;
    private boolean autoPedestriansGeneration;
    private double roadWidth;
    private DialogPane dialog;
    // Windows and simulation instances for phases
    private Stage phase1Window;
    private Stage phase2Window;
    private TrafficSimulation phase1Simulation;
    private TrafficSimulation phase2Simulation;

   

    /**
     * Entry point for the JavaFX application.
     * @param primaryStage the primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        showInputForm(primaryStage); // Show the input form to gather user inputs
    }


    /**
     * Displays the input form to gather user inputs.
     * @param primaryStage the primary stage for the application.
     */
    private void showInputForm(Stage primaryStage) {
    // Get the primary screen
    Screen screen = Screen.getPrimary();

    // Get the screen bounds
    Rectangle2D screenBounds = screen.getBounds();
    double screenWidth = screenBounds.getWidth();

    // Create the input form layout (VBox)
    VBox inputForm = new VBox(10);
    inputForm.setPadding(new Insets(20)); // Padding between edges
    inputForm.setAlignment(Pos.CENTER); // Align elements to the top-left

    // Text fields for user input
    TextField timeField = new TextField();
    TextField vehiclesField = new TextField();
    TextField lanesField = new TextField();
    ObservableList<String> inputNumberOfPaths = FXCollections.observableArrayList("1", "2");
    ComboBox<String> comboBox = new ComboBox<>(inputNumberOfPaths);
    comboBox.setPromptText("Select the number of paths");
    TextField pedestrianField = new TextField();

    // Create CheckBoxes
    CheckBox vehiclesCheckBox = new CheckBox("Auto regeneration vehicles");
    bindFontSizeToCheckBox(vehiclesCheckBox, primaryStage); // Dynamic resizing
    HBox vehiclesCheckBoxContainer = new HBox(vehiclesCheckBox);
    vehiclesCheckBoxContainer.setAlignment(Pos.CENTER_LEFT); // Align to left

    CheckBox pedestrianCheckBox = new CheckBox("Auto regeneration pedestrians");
    bindFontSizeToCheckBox(pedestrianCheckBox, primaryStage); // Dynamic resizing
    HBox pedestrianCheckBoxContainer = new HBox(pedestrianCheckBox);
    pedestrianCheckBoxContainer.setAlignment(Pos.CENTER_LEFT); // Align to left

    // Add labels and text fields to the form
    inputForm.getChildren().addAll(
        createLabelWithDynamicFont("Simulation Time (seconds):", primaryStage),
        timeField,
        createLabelWithDynamicFont("Number of Vehicles:", primaryStage),
        vehiclesField,
        vehiclesCheckBoxContainer, // Add vehicle checkbox container
        createLabelWithDynamicFont("Number of Pedestrians:", primaryStage),
        pedestrianField,
        pedestrianCheckBoxContainer, // Add pedestrian checkbox container
        createLabelWithDynamicFont("Number of Lanes:", primaryStage),
        lanesField,
        createLabelWithDynamicFont("Number of Paths:", primaryStage),
        comboBox
    );

    // Bind the font size of the TextFields to the window size
    bindFontSizeToTextField(timeField, primaryStage);
    bindFontSizeToTextField(vehiclesField, primaryStage);
    bindFontSizeToTextField(lanesField, primaryStage);
    bindFontSizeToTextField(pedestrianField, primaryStage);

    // Button to submit the form
    // Create tooltips for the checkboxes
    Tooltip vehiclesTooltip = new Tooltip("Automatically generate vehicles after each vehicle passed.");
    Tooltip pedestriansTooltip = new Tooltip("Automatically generate pedestrians after each pedestrian passed.");
    // Set the tooltips to the checkboxes
    vehiclesCheckBox.setTooltip(vehiclesTooltip);
    pedestrianCheckBox.setTooltip(pedestriansTooltip);

// Button configuration remains the same
Button submitButton = new Button("Submit");
submitButton.setOnAction(e -> {
    try {
        // Parse user input from the text fields
        simulationTime = Integer.parseInt(timeField.getText());
        numberOfVehicles = Integer.parseInt(vehiclesField.getText());
        numberOfPedestrian = Integer.parseInt(pedestrianField.getText());
        numberOfLanes = Integer.parseInt(lanesField.getText());
        numberOfPaths = Integer.parseInt(comboBox.getValue());
        autoVehiclesGeneration = vehiclesCheckBox.isSelected();
        autoPedestriansGeneration = pedestrianCheckBox.isSelected();
        // Validate that all inputs are positive numbers
        if (simulationTime <= 0 || numberOfVehicles <= 0 || numberOfPedestrian <= 0 || numberOfLanes <= 0) {
            throw new NumberFormatException();
        } else {
            Road road = new Road(numberOfPaths, numberOfLanes, "normal", 5000);
            road.createMap();
            roadWidth = road.getObjectWidth();
            if (roadWidth > screenWidth) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        // If validation is successful, proceed to the next stage
        showControlPanel(primaryStage); // Transition to the control panel for the simulation
        primaryStage.close(); // Close the current input form stage
    } catch (NumberFormatException ex) {
        // Display an error alert if the input is invalid
        Alert alert = new Alert(Alert.AlertType.ERROR,"Error",ButtonType.OK);
        alert.setTitle("Invalid Input");
        alert.setContentText("Please enter valid positive numbers for all fields.");
        dialog = alert.getDialogPane();
        dialog.getStylesheets().add(getClass().getResource("dialog.css").toString());
        dialog.getStyleClass().add("dialog");
        Stage dialogStage = (Stage) dialog.getScene().getWindow();
        Image icon = new Image("icon.jpg"); // Path to the icon file
        dialogStage.getIcons().add(icon);
        alert.showAndWait(); // Wait for the user to acknowledge the error
    } catch (ArrayIndexOutOfBoundsException ex) {
    // Create a confirmation alert
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Road Width Exceeds Screen Width",ButtonType.YES,ButtonType.NO);
    alert.setTitle("Screen Width Issue");
    alert.setHeaderText("Road Width Exceeds Screen Width");
    alert.setContentText(
        "Your screen width cannot show this path. Please try to create a road with fewer lanes or paths.\n" +
        "Your screen width: " + screenWidth + "\n" +
        "Your requested road width: " + roadWidth + "\n\n" +
        "Would you like to continue anyway?"
    );
    dialog = alert.getDialogPane();
    Stage dialogStage = (Stage) dialog.getScene().getWindow();
    Image icon = new Image("icon.jpg"); // Path to the icon file
    dialogStage.getIcons().add(icon);
    dialog.getStylesheets().add(getClass().getResource("dialog.css").toString());
    dialog.getStyleClass().addAll("dialog");
    // Wait for the user's response
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.YES) {
        showControlPanel(primaryStage); // Transition to the control panel for the simulation
        primaryStage.close(); // Close the current input form stage
    }
}

});

    // Add the submit button to the form
    inputForm.getChildren().add(submitButton);

    // Make the VBox responsive by binding the width and height
    inputForm.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.8)); // 80% of window width
    inputForm.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.8)); // 80% of window height

    // Set up the scene for the input form and show it
    Scene scene = new Scene(inputForm, 400, 600);
    scene.getStylesheets().add(getClass().getResource("traffic_styles.css").toExternalForm());

    // Set the window title and scene
    primaryStage.setTitle("Simulation Configuration");
    primaryStage.setScene(scene);

    Image icon = new Image("icon.jpg");
    primaryStage.getIcons().add(icon);

    // Set the minimum size for the window (prevents shrinking too much)
    primaryStage.setMinWidth(300); // Minimum width of the window
    primaryStage.setMinHeight(600); // Minimum height of the window
    primaryStage.setWidth(400); // Default width
    primaryStage.setHeight(700); // Default height

    // Set initial position on screen
    primaryStage.setX((screenWidth / 2) - 200); // Set initial X position
    primaryStage.setY(0); // Set initial Y position

    // Show the stage (window)
    primaryStage.show();
}
    /**
     * Binds the font size of a CheckBox to the window size dynamically.
     * @param checkBox the CheckBox to bind the font size.
     * @param primaryStage the primary stage to monitor size changes.
     */
    private void bindFontSizeToCheckBox(CheckBox checkBox, Stage primaryStage) {
        double windowHeight = primaryStage.getHeight();
        double windowWidth = primaryStage.getWidth();
    
        if (windowHeight >= windowWidth) {
            // Bind the font size based on the width of the stage
            checkBox.styleProperty().bind(Bindings.format(
                "-fx-font-size: %.0f;",
                Bindings.when(primaryStage.widthProperty().greaterThan(0))
                       .then(primaryStage.widthProperty().multiply(0.02))
                       .otherwise(16), // Default font size
                Bindings.when(primaryStage.widthProperty().greaterThan(0))
                       .then(primaryStage.widthProperty().multiply(0.05))
                       .otherwise(30), // Default height
                Bindings.when(primaryStage.widthProperty().greaterThan(0))
                       .then(primaryStage.widthProperty().multiply(0.05))
                       .otherwise(30)  // Default width
            ));
        } else {
            // Bind the font size based on the height of the stage
            checkBox.styleProperty().bind(Bindings.format(
                "-fx-font-size: %.0f;",
                Bindings.when(primaryStage.heightProperty().greaterThan(0))
                       .then(primaryStage.heightProperty().multiply(0.02))
                       .otherwise(16), // Default font size
                Bindings.when(primaryStage.heightProperty().greaterThan(0))
                       .then(primaryStage.heightProperty().multiply(0.05))
                       .otherwise(30), // Default height
                Bindings.when(primaryStage.heightProperty().greaterThan(0))
                       .then(primaryStage.heightProperty().multiply(0.05))
                       .otherwise(30)  // Default width
            ));
        }
    }
    
    /**
     * Binds the font size of a TextField to the window size dynamically.
     * @param textField the TextField to bind the font size.
     * @param primaryStage the primary stage to monitor size changes.
     */
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
    

    /**
     * Creates a Label with font size dynamically bound to the window size.
     * @param text the text for the label.
     * @param primaryStage the primary stage to monitor size changes.
     * @return a Label with dynamically bound font size.
     */
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
    
    /**
     * Displays the control panel for managing simulations.
     * @param primStage the primary stage for transitioning back to the input form.
     */
    private void showControlPanel(Stage primStage) {
        Stage controlStage = new Stage();
        
        Image icon = new Image("icon.jpg"); // Path to the icon file
        controlStage.getIcons().add(icon);
        VBox controlPanel = new VBox(20);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setAlignment(Pos.CENTER);
    
        // Create buttons for control panel
        Button phase1Button = new Button("Start Phase 1");
        Button phase2Button = new Button("Start Phase 2");
        Button compareButton = new Button("Compare Results");
        Button back = new Button("Back");
        // Bind the font size of each button to the window size (width)
        bindFontSizeToButton(phase1Button, controlStage);
        bindFontSizeToButton(phase2Button, controlStage);
        bindFontSizeToButton(compareButton, controlStage);
        bindFontSizeToButton(back,controlStage);
        // Add actions to buttons
        phase1Button.setOnAction(e -> startPhase1Simulation());
        phase2Button.setOnAction(e -> startPhase2Simulation());
        compareButton.setOnAction(e -> compareResults());
        back.setOnAction(e -> showInputForm(primStage));
        // Add buttons to the control panel
        controlPanel.getChildren().addAll(
            phase1Button,
            phase2Button,
            compareButton,
            back
        );
    
        // Create the scene for the control panel
        Scene scene = new Scene(controlPanel, 250, 200);
        scene.getStylesheets().add(getClass().getResource("traffic_styles.css").toExternalForm());
        controlStage.setTitle("Simulation Control Panel");
        controlStage.setScene(scene);
        
        // Set the minimum size for the window
        controlStage.setMinWidth(300);  // Minimum width of the control panel window
        controlStage.setMinHeight(300); // Minimum height of the control panel window
        
        // Set the default size for the window
        controlStage.setWidth(400);     // Default width
        controlStage.setHeight(300);    // Default height
                // Get the primary screen
        Screen screen = Screen.getPrimary();

        // Get the screen bounds
        Rectangle2D screenBounds = screen.getBounds();
        double screenWidth = screenBounds.getWidth();
        // Set initial position on screen
        controlStage.setX((screenWidth/2)-200);         // Set initial X position
        controlStage.setY(100);         // Set initial Y position
        
        // Show the control panel window
        controlStage.show();
    }
    
    /**
     * Binds the font size of a Button to the window size dynamically.
     * @param button the Button to bind the font size.
     * @param controlStage the stage to monitor size changes.
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
    

    /**
     * Starts the Phase 1 simulation.
     * Opens a new window for Phase 1 and initializes the simulation.
     */
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

        phase1Window.setResizable(false);
        Image icon = new Image("icon.jpg"); // Path to the icon file
        phase1Window.getIcons().add(icon);
        Road road = new Road(numberOfPaths, numberOfLanes, "normal", 5000);
        road.getPedestrianBridge().disableBridge(); // Disable pedestrian bridge
        phase1Simulation = new TrafficSimulation(road, numberOfVehicles, numberOfPedestrian, simulationTime, autoVehiclesGeneration, autoPedestriansGeneration);
        phase1Window.setOnCloseRequest(event -> {
            phase1Simulation.windowClosed();
        });
        // Create and display the simulation scene
        Scene simulationScene = phase1Simulation.createSimulationScene();
        simulationScene.getStylesheets().add(getClass().getResource("mainWindowButton.css").toExternalForm());
        phase1Window.setTitle("Phase 1 Simulation");
        phase1Window.setScene(simulationScene);
        phase1Window.show();
    }

    /**
     * Starts the Phase 2 simulation.
     * Opens a new window for Phase 2 and initializes the simulation.
     */
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

        phase2Window.setResizable(false);
        Image icon = new Image("icon.jpg"); // Path to the icon file
        phase2Window.getIcons().add(icon);
        Road road = new Road(numberOfPaths, numberOfLanes, "enhanced", 5000);
        road.getPedestrianBridge().enableBridge(); // Enable pedestrian bridge
        phase2Simulation = new TrafficSimulation(road, numberOfVehicles, numberOfPedestrian, simulationTime, autoVehiclesGeneration, autoPedestriansGeneration);
        phase2Window.setOnCloseRequest(event -> {
            phase2Simulation.windowClosed();
        });
        Scene simulationScene = phase2Simulation.createSimulationScene();
        simulationScene.getStylesheets().add(getClass().getResource("mainWindowButton.css").toExternalForm());

        // Display the simulation scene
        phase2Window.setTitle("Phase 2 Simulation");
        phase2Window.setScene(simulationScene);
        phase2Window.show();
    }


    /**
     * Compares the results of Phase 1 and Phase 2 simulations.
     * Displays a new window with a comparison summary.
     */
    private void compareResults() {
        if (phase1Simulation == null || phase2Simulation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Simulation");
            alert.setContentText("Please run both phases before comparing results.");
            dialog = alert.getDialogPane();
            dialog.getStylesheets().add(getClass().getResource("dialog.css").toString());
            dialog.getStyleClass().add("dialog");
            Stage dialogStage = (Stage) dialog.getScene().getWindow();
            Image icon = new Image("icon.jpg"); // Path to the icon file
            dialogStage.getIcons().add(icon);
            alert.showAndWait();
            return;
        }
        phase1Simulation.calculateAverdifferanceTime();
        phase2Simulation.calculateAverdifferanceTime();
        Stage compareStage = new Stage();
        Image icon = new Image("icon.jpg"); // Path to the icon file
        compareStage.getIcons().add(icon);
    
        VBox comparePanel = new VBox(10);
        comparePanel.setPadding(new Insets(20));
        comparePanel.setAlignment(Pos.CENTER);
    
        StringBuilder comparison = new StringBuilder();
        comparison.append("Simulation Results\n\n");
    
        comparison.append(String.format("Phase 1:\n" +
            "Passed Vehicles: %d\n" +
            "Passed Pedestrians: %d\n" +
            "Average Differance Time for Vehicles: %.2f\n" +
            "Average Differance Time for Pedestrian: %.2f\n"+
            "Accidents: %d\n\n",
            phase1Simulation.getPassedVehicles(),
            phase1Simulation.getPassedPedestrians(),
            phase1Simulation.getAverageVehicleDifferanceTime(),
            phase1Simulation.getAveragePedestrianDifferanceTime(),
            phase1Simulation.getRoad().getNumberOfAccidents()));
    
        comparison.append(String.format("Phase 2:\n" +
            "Passed Vehicles: %d\n" +
            "Passed Pedestrians: %d\n" +
            "Average Differance Time for Vehicles: %.2f\n" +
            "Average Differance Time for Pedestrian: %.2f\n"+
            "Accidents: %d\n\n",
            phase2Simulation.getPassedVehicles(),
            phase2Simulation.getPassedPedestrians(),
            phase2Simulation.getAverageVehicleDifferanceTime(),
            phase2Simulation.getAveragePedestrianDifferanceTime(),
            phase2Simulation.getRoad().getNumberOfAccidents()));
    
        if (phase1Simulation.getPassedVehicles() > 0 && phase1Simulation.getPassedPedestrians() > 0) {
            double vehicleImprovement = ((double)(phase2Simulation.getPassedVehicles() - phase1Simulation.getPassedVehicles()) /
                               phase1Simulation.getPassedVehicles()) * 100;
            double pedImprovement = ((double)(phase2Simulation.getPassedPedestrians() - phase1Simulation.getPassedPedestrians()) /
                               phase1Simulation.getPassedPedestrians()) * 100;
            double pedDiffImprovement = -((double)(phase2Simulation.getAveragePedestrianDifferanceTime() - phase1Simulation.getAveragePedestrianDifferanceTime()) /
                               phase1Simulation.getAveragePedestrianDifferanceTime()) * 100;
            double vehicleDiffImprovement = -((double)(phase2Simulation.getAverageVehicleDifferanceTime() - phase1Simulation.getAverageVehicleDifferanceTime()) /
                               phase1Simulation.getAverageVehicleDifferanceTime()) * 100;
            double accidnetImprovement;
            if(phase1Simulation.getRoad().getNumberOfAccidents()==0){
                accidnetImprovement = phase2Simulation.getRoad().getNumberOfAccidents()*100.00;
            }
            else{
                accidnetImprovement = -((double)(phase2Simulation.getRoad().getNumberOfAccidents() - phase1Simulation.getRoad().getNumberOfAccidents()) /
                phase1Simulation.getRoad().getNumberOfAccidents()) * 100;
            }

            comparison.append(String.format("Improvements:\n" +
                "Vehicles: %.1f%%\n" +
                "Pedestrians: %.1f%%\n" +
                "Average Differance Time for Vehicles: %.2f%%\n" +
                "Average Differance Time for Pedestrian: %.2f%%\n"+
                "Accidents: %.2f%%",
                vehicleImprovement,
                pedImprovement,
                vehicleDiffImprovement,
                pedDiffImprovement,
                accidnetImprovement));
                createImprovementChart(vehicleImprovement,pedImprovement,vehicleDiffImprovement,pedDiffImprovement,accidnetImprovement, phase1Simulation,phase2Simulation);
        }
        
        Label comparisonLabel = createLabelWithDynamicFont(comparison.toString(), compareStage);
       
        comparisonLabel.getStylesheets().add("-fx-text-fill: black;");
        comparePanel.getChildren().add(comparisonLabel);
    
        // Create a responsive background rectangle
        Rectangle background = new Rectangle();
        background.setFill(Color.rgb(211, 211, 211, 0.8)); // Semi-transparent light gray
        background.setArcWidth(20);
        background.setArcHeight(20);
    
        // Bind the rectangle's width and height to the stage's dimensions
        background.widthProperty().bind(compareStage.widthProperty().multiply(0.95)); // Adjust width
        background.heightProperty().bind(compareStage.heightProperty().multiply(0.95)); // Adjust height
        
        // Use StackPane to layer the background and content
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, comparePanel);
    
        Scene scene = new Scene(stackPane, 400, 500);
        scene.getStylesheets().add(getClass().getResource("compareResultStyles.css").toExternalForm());
        compareStage.setTitle("Comparison Results");
        compareStage.setScene(scene);
        compareStage.show();
    }
    


    public void createImprovementChart(double vehicleImprovement,double pedestrianImprovement,double vehicleDiffImprovement,double pedestrianDiffImprovement,double accidnetImprovement,TrafficSimulation phase1,TrafficSimulation phase2) {
        // Create the X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Improvement Type");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Improvement (%)");

        // Create the BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Phase 2 vs Phase 1 Improvement");
        barChart.getStylesheets().add(getClass().getResource("chartStyle.css").toExternalForm());
        barChart.setLegendVisible(false);
        // Create a series of data for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Improvements");

        // Add data points to the series
        series.getData().add(new XYChart.Data<>("Vehicle Improvement", vehicleImprovement));
        series.getData().add(new XYChart.Data<>("Pedestrian Improvement", pedestrianImprovement));
        series.getData().add(new XYChart.Data<>("Vehicle Diff Improvement", vehicleDiffImprovement));
        series.getData().add(new XYChart.Data<>("Pedestrian Diff Improvement", pedestrianDiffImprovement));
        series.getData().add(new XYChart.Data<>("Accident Improvement", accidnetImprovement));
        
        // Add the series to the chart
        barChart.getData().add(series);

        // Add tooltips to each bar
        for (XYChart.Data<String, Number> data : series.getData()) {
            String improvementType = data.getXValue();
            String tooltipText = "";

            // Set the tooltip text based on the improvement type using if-else statements
            if (improvementType.equals("Vehicle Improvement")) {
                tooltipText = "Phase 1: " + phase1.getRoad().getNumberOfPassedVehicles() + " Vehicles\nPhase 2: " + phase2.getRoad().getNumberOfPassedVehicles() + " Vehicles";
            } else if (improvementType.equals("Pedestrian Improvement")) {
                tooltipText = "Phase 1: " + phase1.getRoad().getNumberOfPassedPedestrians() + " Pedestrian\nPhase 2: " + phase2.getRoad().getNumberOfPassedPedestrians() + " Pedestrian";
            } else if (improvementType.equals("Pedestrian Diff Improvement")) {
                tooltipText = "Phase 1: " + String.format("%.2f", phase1.getAveragePedestrianDifferanceTime()) + "s\nPhase 2: " + String.format("%.2f", phase2.getAveragePedestrianDifferanceTime()) + "s";
            } else if (improvementType.equals("Vehicle Diff Improvement")) {
                tooltipText = "Phase 1: " + String.format("%.2f", phase1.getAverageVehicleDifferanceTime()) + "s\nPhase 2: " + String.format("%.2f", phase2.getAverageVehicleDifferanceTime()) + "s";
            } else if (improvementType.equals("Accident Improvement")) {
                tooltipText = "Phase 1: " + phase1.getRoad().getNumberOfAccidents() + " Accidents\nPhase 2: " + phase2.getRoad().getNumberOfAccidents() + " Accidents";
            }

            // Create the tooltip and set it for the current data point
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(data.getNode(), tooltip);

            // Add mouse hover event for dynamic tooltip display (optional, but useful for styling)
            data.getNode().setOnMouseEntered(event -> {
                Tooltip.install(data.getNode(), tooltip);
            });

            // Optional: Remove tooltip on mouse exit
            data.getNode().setOnMouseExited(event -> {
                Tooltip.uninstall(data.getNode(), tooltip);
            });
        }
        // Create a scene and display the chart (assuming this is called from a method that has access to the Stage)
        javafx.scene.Scene scene = new javafx.scene.Scene(barChart, 800, 600);
        javafx.stage.Stage stage = new javafx.stage.Stage();
        stage.setTitle("Improvement Chart");
        stage.setScene(scene);
        Image icon = new Image("icon.jpg"); // Path to the icon file
        stage.getIcons().add(icon);
        stage.show();
    }

    

    /**
     * Main method to launch the JavaFX application.
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
