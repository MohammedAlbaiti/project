import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private int simulationTime;
    private int numberOfCars;
    private int numberOfPedestrian;
    private int numberOfLanes;
    private int numberOfRoads;
    
    private Stage phase1Window;
    private Stage phase2Window;
    private TrafficSimulation phase1Simulation;
    private TrafficSimulation phase2Simulation;
    
    @Override
    public void start(Stage primaryStage) {
        showInputForm(primaryStage);
    }
    
    private void showInputForm(Stage primaryStage) {
        // Creates a form for the user to input simulation parameters

        // A VBox is a layout container that arranges its child nodes (UI elements)
        // in a single column. The parameter (10) specifies the vertical spacing
        // between elements in pixels
        VBox inputForm = new VBox(10); 

        inputForm.setPadding(new Insets(20)); // Creates an empty border (Insets) between the layout and the edges of the window
        inputForm.setAlignment(Pos.CENTER); // Centers the elements within the form
        
        // Text fields for user input
        TextField timeField = new TextField();
        TextField carsField = new TextField();
        TextField lanesField = new TextField();
        TextField roadsField = new TextField();
        TextField pedestrianField = new TextField();
        
        // Add labels and text fields to the form
        inputForm.getChildren().addAll(
            new Label("Simulation Time (seconds):"),
            timeField,
            new Label("Number of Cars:"),
            carsField,
            new Label("Number of Pedestrian:"),
            pedestrianField,
            new Label("Number of Lanes:"),
            lanesField,
            new Label("Number of Roads:"),
            roadsField
        );

        // Button to submit the form
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                // Parse user input from the text fields
                simulationTime = Integer.parseInt(timeField.getText());
                numberOfCars = Integer.parseInt(carsField.getText());
                numberOfPedestrian = Integer.parseInt(pedestrianField.getText());
                numberOfLanes = Integer.parseInt(lanesField.getText());
                numberOfRoads = Integer.parseInt(roadsField.getText());
                
                // Validate that all inputs are positive numbers
                if (simulationTime <= 0 || numberOfCars <= 0 || numberOfPedestrian<=0 || 
                    numberOfLanes <= 0 || numberOfRoads <= 0) {
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
                alert.showAndWait(); // Wait for the user to acknowledge the error #### why was .show() not used???
            }
        });
        
        // Add the submit button to the form. This appends the button to the
        // end of the VBox's children list, placing it below the text fields
        inputForm.getChildren().add(submitButton);
        
        // Set up the scene for the input form and show it
        Scene scene = new Scene(inputForm, 300, 400);
        primaryStage.setTitle("Simulation Configuration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showControlPanel() {
        Stage controlStage = new Stage();
        VBox controlPanel = new VBox(20);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setAlignment(Pos.CENTER);
        
        Button phase1Button = new Button("Start Phase 1");
        Button phase2Button = new Button("Start Phase 2");
        Button compareButton = new Button("Compare Results");
        
        phase1Button.setOnAction(e -> startPhase1Simulation());
        phase2Button.setOnAction(e -> startPhase2Simulation());
        compareButton.setOnAction(e -> compareResults());
        
        controlPanel.getChildren().addAll(
            phase1Button,
            phase2Button,
            compareButton
        );
        
        Scene scene = new Scene(controlPanel, 250, 200);
        controlStage.setTitle("Simulation Control Panel");
        controlStage.setScene(scene);
        controlStage.show();
    }
    
    private void startPhase1Simulation() {
        if (phase1Window != null) {
            if (phase1Simulation != null) {
                phase1Simulation.stopSimulation();
            }
            phase1Window.close();
        }
        
        phase1Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", simulationTime);
        phase1Simulation = new TrafficSimulation(road, numberOfCars, numberOfPedestrian, simulationTime);
        Scene simulationScene = phase1Simulation.createSimulationScene();
        
        phase1Window.setTitle("Phase 1 Simulation");
        phase1Window.setScene(simulationScene);
        phase1Window.show();
    }
    
    private void startPhase2Simulation() {
        if (phase2Window != null) {
            if (phase2Simulation != null) {
                phase2Simulation.stopSimulation();
            }
            phase2Window.close();
        }
        
        phase2Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "enhanced", simulationTime);
        phase2Simulation = new TrafficSimulation(road, numberOfCars,numberOfPedestrian, simulationTime);
        Scene simulationScene = phase2Simulation.createSimulationScene();
        
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
        
        Text comparisonText = new Text(comparison.toString());
        comparePanel.getChildren().add(comparisonText);
        
        Scene scene = new Scene(comparePanel, 300, 400);
        compareStage.setTitle("Comparison Results");
        compareStage.setScene(scene);
        compareStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
class TrafficSimulation {
    private Road road;
    private int numberOfCars;
    private List<Vehicle> cars = new ArrayList<>();
    private List<Pedestrian> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    // private static final ArrayList<Double> RIGHTMOST_LANE_X= new ArrayList<>();
    private Text passedCarsText;
    private Text passedPedestrianText;
    private Text timeRemainingText;
    private AnimationTimer animationTimer;
    private long startTime;
    private int simulationDuration;
    private boolean isSimulationComplete = false;
    private ArrayList<Double> lanePositions;
    private int numberOfPedestrian;
    
    public TrafficSimulation(Road road, int numberOfCars,int numberOfPedestrian, int simulationDuration) {
        this.road = road;
        this.numberOfCars = numberOfCars;
        this.numberOfPedestrian = numberOfPedestrian;
        this.simulationDuration = simulationDuration;
    }
    
    public Scene createSimulationScene() {
        Pane mapContainer = road.createMap();
        double totalWidth = road.getObjectWidth();
        
        generateVehicles(mapContainer, numberOfCars);
        generatePedestrians(mapContainer, numberOfPedestrian);
        
        passedCarsText = new Text(15, 25, "Passed Cars: 0");
        passedPedestrianText = new Text(15, 45, "Passed Pedestrians: 0");
        timeRemainingText = new Text(15, 65, "Time Remaining: " + simulationDuration + "s");

        passedCarsText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        passedPedestrianText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        timeRemainingText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));

        Pane dataContaioner = new Pane();
        Rectangle bottomRect = new Rectangle(146, 80, Color.rgb(120, 126, 186));
        Rectangle topRect = new Rectangle(136, 70, Color.rgb(180, 188, 217));
        topRect.setX(5);
        topRect.setY(5);
        dataContaioner.getChildren().addAll(bottomRect , topRect, passedCarsText, passedPedestrianText, timeRemainingText);
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(mapContainer, dataContaioner);
        
        startAnimation(mapContainer);
        
        return new Scene(stackPane, totalWidth, road.getObjectHeight());
    }
    
    private void startAnimation(Pane mapContainer) {
        startTime = System.nanoTime();
        
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isSimulationComplete) {
                    int elapsedSeconds = (int)((now - startTime) / 1e9);
                    int remainingTime = simulationDuration - elapsedSeconds;
                    
                    if (remainingTime <= 0) {
                        stopSimulation();
                        isSimulationComplete = true;
                        return;
                    }
                    
                    timeRemainingText.setText("Time Remaining: " + remainingTime + "s");
                    updateVehicles(mapContainer);
                    updatePedestrians(mapContainer);
                }
            }
        };
        animationTimer.start();
    }
    
    private void updateVehicles(Pane mapContainer) {


        for (int i = 0; i < cars.size(); i++) {
            Vehicle car = cars.get(i);
            double carX = car.getXCOO();
            double carY = car.getYCOO();
            boolean carStopped = false;
            
            double vehicleHeight = (car instanceof Car) ? 110 : 130;

            // Check for vehicle collision
            for (int j = 0; j < cars.size(); j++) {
                if (i == j) continue;
                
                Vehicle otherCar = cars.get(j);
                double otherX = otherCar.getXCOO();
                double otherY = otherCar.getYCOO();
                double otherVehicleHeight = (otherCar instanceof Car) ? 110 : 130;
                if(road.getNumberOfRoads()==1 || car.getObjectDirection().equals("up")){
                // Check if vehicles are in the same lane
                if (Math.abs(carX - otherX) < 20) {
                    double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + 20;
                    if (otherY < carY && carY - otherY < minSafeDistance) {
                        carStopped = true;
                        break;
                    }
                }
                }
                else{
                // Check if vehicles are in the same lane
                if (Math.abs(carX - otherX) < 20) {
                    double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + 20;
                    if (otherY > carY && -carY + otherY < minSafeDistance) {
                        carStopped = true;
                        break;
                    }
                }
                }
            }
            
            // Check for pedestrians
            boolean pedestrianInPath = false;
            for (Pedestrian pedestrian : pedestrians) {
                double pedestrianX = pedestrian.getXCOO() - 10;
                double pedestrianY = pedestrian.getYCOO() + 10;
                
                if (Math.abs(carX - pedestrianX) < 30 && 
                    pedestrianY <= carY + vehicleHeight && 
                    pedestrianY >= carY - vehicleHeight) {
                    pedestrianInPath = true;
                    break;
                }
            }
            
            // Move vehicle
            if (carStopped || pedestrianInPath) {
                car.stop();
            } else {
                car.move();
            }
            if(road.getNumberOfRoads()==1 || car.getObjectDirection().equals("up")){
            // Handle vehicle reaching the top
            if (car.getYCOO() < -vehicleHeight) {
                mapContainer.getChildren().remove(car.getVehicleView());
                cars.remove(i);
                i--;
                
                road.increaseNumberOfPassedCars(1);
                passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                
                generateVehicles(mapContainer, 1);}

            }
            else{
                // Handle vehicle reaching the top
                if (car.getYCOO() > road.getObjectHeight()+vehicleHeight) {
                    mapContainer.getChildren().remove(car.getVehicleView());
                    cars.remove(i);
                    i--;
                    
                    road.increaseNumberOfPassedCars(1);
                    passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                    
                    generateVehicles(mapContainer, 1);
                }
        }}
    }

    private void updatePedestrians(Pane mapContainer) {
        for (int i = 0; i < pedestrians.size(); i++) {
            Pedestrian pedestrian = pedestrians.get(i);
            double pedestrianX = pedestrian.getXCOO(); // x-coordinate of left edge of the pedestrian pic. Becareful, for a pedestrian moving right, the x-coordinate points to his back, and for a pedestrian moving left, it points to his face.
            double pedestrianY = pedestrian.getYCOO(); // y-coordinate of top edge of the pedestrian pic. Becareful, for a pedestrian moving right, the y-coordinate points to his right hand, and for a pedestrian moving left, it points to his left hand.
            boolean carInFront = false;
    
            // Check if there's a car in front of the pedestrian
            for (Vehicle car : cars) {
                double carX = car.getXCOO(); // x-coordinate of left edge of the car pic. Becareful, for a car moving up, the x-coordinate points to the driver's left hand, and for a car moving down, it points to its driver's right hand.
                double carY = car.getYCOO(); // y-coordinate of top edge of the car pic. Becareful, for a car moving up, the y-coordinate points to its front bumper, and for a car moving down, it points to its rear bumper.
                double vehicleHeight = (car instanceof Car) ? 110 : 130;
    
                if (pedestrian.getObjectDirection().equals("right")) {
                    if (pedestrianX + pedestrian.getObjectWidth() > carX - 20 && // subtract 20 from carX for a safe distance 
                        pedestrianX + pedestrian.getObjectWidth() < carX) {      // if you want to move left in x-axis you should subtract, and add to move right

                        if (car.getObjectDirection().equals("up")) {
                            if (pedestrianY + pedestrian.getObjectHeight() >= carY - 50 && // subtract 50 from carY for a safe distance 
                                pedestrianY <= carY + vehicleHeight) {                     // if you want to move up in y-axis you should subtract, and add to move down

                                carInFront = true;
                                break;
                            }
                        } else {
                            if (pedestrianY + pedestrian.getObjectHeight() >= carY &&
                                pedestrianY <= carY + vehicleHeight + 50) {
                                carInFront = true;
                                break;
                            }
                        }
                    }
                } 
                else { // Moving left
                    if (pedestrianX > carX + car.getObjectWidth() &&
                        pedestrianX < carX + car.getObjectWidth() + 20) {
    
                        if (car.getObjectDirection().equals("up")) {
                            if (pedestrianY + pedestrian.getObjectHeight() >= carY - 50 &&
                                pedestrianY <= carY + vehicleHeight) {
                                carInFront = true;
                                break;
                            }
                        } else {
                            if (pedestrianY + pedestrian.getObjectHeight() >= carY &&
                                pedestrianY <= carY + vehicleHeight + 50) {
                                carInFront = true;
                                break;
                            }
                        }
                    }
                }
            }
    
            // Move or stop the pedestrian based on car detection
            if (!carInFront) {
                if (pedestrian.getObjectDirection().equals("right") && pedestrianX < road.getObjectWidth() || 
                    pedestrian.getObjectDirection().equals("left") && pedestrianX > -pedestrian.getObjectWidth()) {
                    pedestrian.move();
                }
            } else {
                pedestrian.stop();
            }
    
            // Check if the pedestrian has passed the road bounds
            if ((pedestrian.getObjectDirection().equals("right") && pedestrianX >= road.getObjectWidth()) || 
                (pedestrian.getObjectDirection().equals("left") && pedestrianX <= -pedestrian.getObjectWidth())) {
                mapContainer.getChildren().remove(pedestrian.getImageView());
                pedestrians.remove(i);
                i--;
                road.increaseNumberOfPassedPedestrians(1);
                passedPedestrianText.setText("Passed Pedestrian: " + road.getNumberOfPassedPedestrians());
                generatePedestrians(mapContainer, 1);
            }
        }
    }
    
    
    private Vehicle createCar(double x, double y, String direciton) {
        double RIGHTMOST_LANE_X = 0;
        double RIGHTMOST_LANE_X1 = 0;
        if(road.getNumberOfRoads()==2){
            RIGHTMOST_LANE_X1 = road.getRightMostLane().get(road.getRightMostLane().size()-1)+10;
            RIGHTMOST_LANE_X = road.getRightMostLane().get(0)+10;
            // double RIGHTMOST_LANE_X = road.getRightMostLane().get(0);
        }
        else{
            RIGHTMOST_LANE_X = road.getRightMostLane().get(0)+10;

        }
        // double RIGHTMOST_LANE_X = 
        if ((x == RIGHTMOST_LANE_X || x==RIGHTMOST_LANE_X1) && random.nextDouble() < 0.4) {
            Truck truck = new Truck(road, "normal");
            truck.setObjectDireciton(direciton);
            truck.createTruck(x, y);
            return truck;
        }
        Car car = new Car(road, "normal");
        car.setObjectDireciton(direciton);
        car.createCarVehicle(x, y);
        return car;
    }
    
    private void generateVehicles(Pane mapContainer, int numberOfVehicles) {
        double startingY = 0;
        double ySpacing = 130;
        
        // Get lane positions and adjust for vehicle positioning
        lanePositions = road.getXCooForLanes();
        for (int i = 0; i < lanePositions.size(); i++) {
            lanePositions.set(i, lanePositions.get(i) + 10);
        }
    
        // For two roads, vehicles in the first half of lanes should go down, others go up
        int halfSizeLanePositions = lanePositions.size() / 2;
    
        for (int i = 0; i < numberOfVehicles; i++) {
            int laneIndex = random.nextInt(lanePositions.size());
            Double x = lanePositions.get(laneIndex);
            
            // Adjust Y position based on direction
            String direction;
            double y;
            
            if (road.getNumberOfRoads() == 2) {
                if (laneIndex < halfSizeLanePositions) {
                    direction = "down";
                    y = -startingY - (cars.size() * ySpacing); // Start from top for downward vehicles
                } else {
                    direction = "up";
                    y = road.getObjectHeight() + startingY + (cars.size() * ySpacing); // Start from bottom for upward vehicles
                }
            } else {
                // Single road - all vehicles go up
                direction = "up";
                y = road.getObjectHeight() + startingY + (cars.size() * ySpacing); /////////////////// what is the purpose of multiplyig ySpacing with car.size()? you can use ySpacig only
            }
            
            Vehicle vehicle = createCar(x, y, direction);
            ImageView vehicleImageView = vehicle.getVehicleView();
            
            // Rotate vehicle based on direction
            if (direction.equals("down")) {
                vehicleImageView.setRotate(180); // the rotation won't change the coordinates (x, y)
            }
            
            cars.add(vehicle);
            mapContainer.getChildren().add(vehicleImageView);
        }
    }
    
    private void generatePedestrians(Pane mapContainer, int numberOfPedestrians) {
        for (int i = 0; i < numberOfPedestrians; i++) {
            Pedestrian pedestrian = new Pedestrian(road, "normal");
            boolean right = random.nextBoolean();
            if(right){
                pedestrian.setObjectDireciton("right");
                pedestrian.createPedestrian();
            }
            else{
                pedestrian.setObjectDireciton("left");
                pedestrian.createPedestrian();
                pedestrian.getImageView().setRotate(180); // the rotation won't change the coordinates (x, y)
            }
           
            pedestrians.add(pedestrian);
            mapContainer.getChildren().add(pedestrian.getImageView());
        }
    }
    
    public void stopSimulation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
    
    public int getPassedCars() {
        return road.getNumberOfPassedCars();
    }
    
    public int getPassedPedestrians() {
        return road.getNumberOfPassedPedestrians();
    }
}
