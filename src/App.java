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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


// Update the main App class's comparison method
public class App extends Application {
        // Configuration variables
    private int simulationTime;
    private int numberOfCars;
    private int numberOfLanes;
    private int numberOfRoads;
    
    // Keep track of simulation windows
    private Stage phase1Window;
    private Stage phase2Window;
    
    @Override
    public void start(Stage primaryStage) {
        showInputForm(primaryStage);
    }
    
    private void showInputForm(Stage primaryStage) {
        VBox inputForm = new VBox(10);
        inputForm.setPadding(new Insets(20));
        inputForm.setAlignment(Pos.CENTER);
        
        // Create input fields
        TextField timeField = new TextField();
        TextField carsField = new TextField();
        TextField lanesField = new TextField();
        TextField roadsField = new TextField();
        
        // Add labels and fields
        inputForm.getChildren().addAll(
            new Label("Simulation Time (seconds):"),
            timeField,
            new Label("Number of Cars:"),
            carsField,
            new Label("Number of Lanes:"),
            lanesField,
            new Label("Number of Roads:"),
            roadsField
        );
        
        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                simulationTime = Integer.parseInt(timeField.getText());
                numberOfCars = Integer.parseInt(carsField.getText());
                numberOfLanes = Integer.parseInt(lanesField.getText());
                numberOfRoads = Integer.parseInt(roadsField.getText());
                
                showControlPanel();
                primaryStage.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Please enter valid numbers for all fields.");
                alert.showAndWait();
            }
        });
        
        inputForm.getChildren().add(submitButton);
        
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
            phase1Window.close();
        }
        
        phase1Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", simulationTime);
        TrafficSimulation simulation = new TrafficSimulation(road, numberOfCars);
        Scene simulationScene = simulation.createSimulationScene();
        
        phase1Window.setTitle("Phase 1 Simulation");
        phase1Window.setScene(simulationScene);
        phase1Window.show();
    }
    
    private void startPhase2Simulation() {
        if (phase2Window != null) {
            phase2Window.close();
        }
        
        phase2Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "enhanced", simulationTime);
        TrafficSimulation simulation = new TrafficSimulation(road, numberOfCars);
        Scene simulationScene = simulation.createSimulationScene();
        
        phase2Window.setTitle("Phase 2 Simulation");
        phase2Window.setScene(simulationScene);
        phase2Window.show();
    }
    
    private void compareResults() {
        Stage compareStage = new Stage();
        VBox comparePanel = new VBox(10);
        comparePanel.setPadding(new Insets(20));
        comparePanel.setAlignment(Pos.CENTER);
        
        StringBuilder comparison = new StringBuilder();
        comparison.append("Simulation Results\n\n");
        
        // Get results from both simulations
        TrafficSimulation phase1Sim = (TrafficSimulation) phase1Window.getScene().getUserData();
        TrafficSimulation phase2Sim = (TrafficSimulation) phase2Window.getScene().getUserData();
        
        if (phase1Sim != null && phase2Sim != null) {
            comparison.append(String.format("Phase 1:\n" +
                "Passed Cars: %d\n" +
                "Passed Pedestrians: %d\n\n", 
                phase1Sim.getPassedCars(),
                phase1Sim.getPassedPedestrians()));
                
            comparison.append(String.format("Phase 2:\n" +
                "Passed Cars: %d\n" +
                "Passed Pedestrians: %d\n\n",
                phase2Sim.getPassedCars(),
                phase2Sim.getPassedPedestrians()));
                
            // Calculate improvement percentages
            double carImprovement = ((double)(phase2Sim.getPassedCars() - phase1Sim.getPassedCars()) / 
                                   phase1Sim.getPassedCars()) * 100;
            double pedImprovement = ((double)(phase2Sim.getPassedPedestrians() - phase1Sim.getPassedPedestrians()) / 
                                   phase1Sim.getPassedPedestrians()) * 100;
                                   
            comparison.append(String.format("Improvements:\n" +
                "Cars: %.1f%%\n" +
                "Pedestrians: %.1f%%",
                carImprovement,
                pedImprovement));
        } else {
            comparison.append("Please run both phases before comparing results.");
        }
        
        Text comparisonText = new Text(comparison.toString());
        comparePanel.getChildren().add(comparisonText);
        
        Scene scene = new Scene(comparePanel, 300, 400);
        compareStage.setTitle("Comparison Results");
        compareStage.setScene(scene);
        compareStage.show();
    }
    
    private void startPhase1Simulation() {
        if (phase1Window != null) {
            TrafficSimulation oldSim = (TrafficSimulation) phase1Window.getScene().getUserData();
            if (oldSim != null) {
                oldSim.stopSimulation();
            }
            phase1Window.close();
        }
        
        phase1Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", simulationTime);
        TrafficSimulation simulation = new TrafficSimulation(road, numberOfCars);
        Scene simulationScene = simulation.createSimulationScene();
        simulationScene.setUserData(simulation);
        
        phase1Window.setTitle("Phase 1 Simulation");
        phase1Window.setScene(simulationScene);
        phase1Window.show();
    }
    
    private void startPhase2Simulation() {
        if (phase2Window != null) {
            TrafficSimulation oldSim = (TrafficSimulation) phase2Window.getScene().getUserData();
            if (oldSim != null) {
                oldSim.stopSimulation();
            }
            phase2Window.close();
        }
        
        phase2Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "enhanced", simulationTime);
        TrafficSimulation simulation = new TrafficSimulation(road, numberOfCars);
        Scene simulationScene = simulation.createSimulationScene();
        simulationScene.setUserData(simulation);
        
        phase2Window.setTitle("Phase 2 Simulation");
        phase2Window.setScene(simulationScene);
        phase2Window.show();
    }
}
class TrafficSimulation {
    private Road road;
    private int numberOfCars;
    private List<Vehicle> cars = new ArrayList<>();
    private List<Pedestrian> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    private static final int RIGHTMOST_LANE_X = 600;
    private Text passedCarsText;
    private Text passedPedestrianText;
    private AnimationTimer animationTimer;
    
    public TrafficSimulation(Road road, int numberOfCars) {
        this.road = road;
        this.numberOfCars = numberOfCars;
    }
    
    public Scene createSimulationScene() {
        Pane mapContainer = road.createMap();
        double totalWidth = road.getTotalWidth();
        
        // Generate initial vehicles and pedestrians
        generateVehicles(mapContainer, numberOfCars);
        generatePedestrians(mapContainer, 15);
        
        // Create the counter text
        passedCarsText = new Text(20, 30, "Passed Cars: 0");
        mapContainer.getChildren().add(passedCarsText);
        passedPedestrianText = new Text(20, 40, "Passed Pedestrian: 0");
        mapContainer.getChildren().add(passedPedestrianText);
        
        // Create and start animation timer
        startAnimation(mapContainer);
        
        return new Scene(mapContainer, totalWidth, 480);
    }
    
    private void startAnimation(Pane mapContainer) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateVehicles(mapContainer);
                updatePedestrians(mapContainer);
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
                
                // Check if vehicles are in the same lane
                if (Math.abs(carX - otherX) < 20) {
                    double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + 20;
                    if (otherY < carY && carY - otherY < minSafeDistance) {
                        carStopped = true;
                        break;
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
            
            // Handle vehicle reaching the top
            if (car.getYCOO() < -vehicleHeight) {
                mapContainer.getChildren().remove(car.getVehicleView());
                cars.remove(i);
                i--;
                
                road.increaseNumberOfPassedCars(1);
                passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                
                generateVehicles(mapContainer, 1);
            }
        }
    }
    
    private void updatePedestrians(Pane mapContainer) {
        for (int i = 0; i < pedestrians.size(); i++) {
            Pedestrian pedestrian = pedestrians.get(i);
            double pedestrianX = pedestrian.getXCOO();
            double pedestrianY = pedestrian.getYCOO();
            
            boolean carInFront = false;
            for (Vehicle car : cars) {
                double carX = car.getXCOO() - 10;
                double carY = car.getYCOO() - 20;
                
                double vehicleHeight = (car instanceof Car) ? 110 : 130;
                
                if (Math.abs(carX - pedestrianX) < 50 && 
                    pedestrianY <= carY + vehicleHeight && 
                    pedestrianY >= carY - 50) {
                    carInFront = true;
                    break;
                }
            }
            
            if (!carInFront) {
                if (pedestrianX < road.getTotalWidth() - 30) {
                    pedestrian.move();
                }
            } else {
                pedestrian.stop();
            }
            
            if (pedestrianX >= road.getTotalWidth() - 30) {
                mapContainer.getChildren().remove(pedestrian.getImageView());
                pedestrians.remove(i);
                i--;
                road.increaseNumberOfPassedPedestrians(1);
                passedPedestrianText.setText("Passed Pedestrian: " + road.getNumberOfPassedPedestrians());
                generatePedestrians(mapContainer, 1);
            }
        }
    }
    
    private Vehicle createCar(double x, double y) {
        if (x == RIGHTMOST_LANE_X && random.nextDouble() < 0.4) {
            Truck truck = new Truck(road, "normal");
            truck.createTruck(x, y);
            return truck;
        }
        Car car = new Car(road, "normal");
        car.createCarVehicle(x, y);
        return car;
    }
    
    private void generateVehicles(Pane mapContainer, int numberOfVehicles) {
        double startingY = 320;
        double ySpacing = 130;
        
        int[] lanePositions = {180, 340, 460, 600};
        
        for (int i = 0; i < numberOfVehicles; i++) {
            int laneIndex = random.nextInt(lanePositions.length);
            int x = lanePositions[laneIndex];
            double y = startingY + (cars.size() * ySpacing);
            
            Vehicle vehicle = createCar(x, y);
            ImageView vehicleImageView = vehicle.getVehicleView();
            
            cars.add(vehicle);
            mapContainer.getChildren().add(vehicleImageView);
        }
    }
    
    private void generatePedestrians(Pane mapContainer, int numberOfPedestrians) {
        for (int i = 0; i < numberOfPedestrians; i++) {
            Pedestrian pedestrian = new Pedestrian(road, "normal");
            pedestrian.createPedestrian();
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
