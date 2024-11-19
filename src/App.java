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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private int simulationTime;
    private int numberOfCars;
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
        VBox inputForm = new VBox(10);
        inputForm.setPadding(new Insets(20));
        inputForm.setAlignment(Pos.CENTER);
        
        TextField timeField = new TextField();
        TextField carsField = new TextField();
        TextField lanesField = new TextField();
        TextField roadsField = new TextField();
        
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
        
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                simulationTime = Integer.parseInt(timeField.getText());
                numberOfCars = Integer.parseInt(carsField.getText());
                numberOfLanes = Integer.parseInt(lanesField.getText());
                numberOfRoads = Integer.parseInt(roadsField.getText());
                
                if (simulationTime <= 0 || numberOfCars <= 0 || 
                    numberOfLanes <= 0 || numberOfRoads <= 0) {
                    throw new NumberFormatException();
                }
                
                showControlPanel();
                primaryStage.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Please enter valid positive numbers for all fields.");
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
            if (phase1Simulation != null) {
                phase1Simulation.stopSimulation();
            }
            phase1Window.close();
        }
        
        phase1Window = new Stage();
        Road road = new Road(numberOfRoads, numberOfLanes, "normal", simulationTime);
        phase1Simulation = new TrafficSimulation(road, numberOfCars, simulationTime);
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
        phase2Simulation = new TrafficSimulation(road, numberOfCars, simulationTime);
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
    
    public TrafficSimulation(Road road, int numberOfCars, int simulationDuration) {
        this.road = road;
        this.numberOfCars = numberOfCars;
        this.simulationDuration = simulationDuration;
    }
    
    public Scene createSimulationScene() {
        Pane mapContainer = road.createMap();
        double totalWidth = road.getObjectWidth();
        
        generateVehicles(mapContainer, numberOfCars);
        generatePedestrians(mapContainer, 15);
        
        passedCarsText = new Text(20, 30, "Passed Cars: 0");
        passedPedestrianText = new Text(20, 50, "Passed Pedestrians: 0");
        timeRemainingText = new Text(20, 70, "Time Remaining: " + simulationDuration + "s");
        
        mapContainer.getChildren().addAll(passedCarsText, passedPedestrianText, timeRemainingText);
        
        startAnimation(mapContainer);
        
        return new Scene(mapContainer, totalWidth, road.getObjectHeight());
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
                if (pedestrianX < road.getObjectWidth() - 30) {
                    pedestrian.move();
                }
            } else {
                pedestrian.stop();
            }
            
            if (pedestrianX >= road.getObjectWidth() - 30) {
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
        double RIGHTMOST_LANE_X = road.getRightMostLane().get(0);
        double RIGHTMOST_LANE_X1 = 0;
        if(road.getRightMostLane().size()==2){
            RIGHTMOST_LANE_X1 = road.getRightMostLane().get(road.getRightMostLane().size()-1);
            // double RIGHTMOST_LANE_X = road.getRightMostLane().get(0);
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
                y = road.getObjectHeight() + startingY + (cars.size() * ySpacing);
            }
            
            Vehicle vehicle = createCar(x, y, direction);
            ImageView vehicleImageView = vehicle.getVehicleView();
            
            // Rotate vehicle based on direction
            if (direction.equals("down")) {
                vehicleImageView.setRotate(180);
            }
            
            cars.add(vehicle);
            mapContainer.getChildren().add(vehicleImageView);
        }
    }
    
    private void generatePedestrians(Pane mapContainer, int numberOfPedestrians) {
        for (int i = 0; i < numberOfPedestrians; i++) {
            Pedestrian pedestrian = new Pedestrian(road, "normal");
            pedestrian.setObjectDireciton("right");
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
