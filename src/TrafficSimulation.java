import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import java.security.SecureRandom;
// import java.time.Duration;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrafficSimulation {
    private Road road;
    private int numberOfCars;
    private List<GeneralRules> objecList = new ArrayList<>();
    private List<Vehicle> cars = new ArrayList<>();
    private List<Pedestrian> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    // private static final ArrayList<Double> RIGHTMOST_LANE_X= new ArrayList<>();
    private Text passedCarsText;
    private Text passedPedestrianText;
    private Text timeRemainingText;
    private Text numberOfAccidents;
    private AnimationTimer animationTimer;
    private long startTime;
    private int simulationDuration;
    private boolean isSimulationComplete = false;
    private ArrayList<Double> lanePositions;
    private int numberOfPedestrian;
    public static boolean isMuted = false; // Track whether sounds are muted
    private boolean isPaused = false; // Track whether the simulation is paused
    //  private boolean isPaused = false;
    // private long startTime;
    private long pauseStartTime = 0;
    private long totalPausedTime = 0;
    // private AnimationTimer animationTimer;
    // private boolean isSimulationComplete = false;
    public TrafficSimulation(Road road, int numberOfCars, int numberOfPedestrian, int simulationDuration) {
        this.road = road;
        this.numberOfCars = numberOfCars;
        this.numberOfPedestrian = numberOfPedestrian;
        this.simulationDuration = simulationDuration;
        objecList.add(road);
    }

    public Scene createSimulationScene() {
        SoundPlayer.playGeneralSounds(simulationDuration);
        Pane mapContainer = road.createMap();
        double totalWidth = road.getObjectWidth();

        generateVehicles(mapContainer, numberOfCars);
        generatePedestrians(mapContainer, numberOfPedestrian);

        passedCarsText = new Text(15, 25, "Passed Cars: 0");
        passedPedestrianText = new Text(15, 45, "Passed Pedestrians: 0");
        timeRemainingText = new Text(15, 65, "Time Remaining: " + simulationDuration + "s");
        numberOfAccidents = new Text(15, 85, "Accidents: 0");
        passedCarsText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        passedPedestrianText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        timeRemainingText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        numberOfAccidents.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));

        Pane dataContainer = new Pane();
        Rectangle bottomRect = new Rectangle(146, 100, Color.rgb(120, 126, 186));
        Rectangle topRect = new Rectangle(136, 90, Color.rgb(180, 188, 217));
        topRect.setX(5);
        topRect.setY(5);
        dataContainer.getChildren().addAll(bottomRect, topRect, passedCarsText, passedPedestrianText, numberOfAccidents, timeRemainingText);

        // Pause Button
        Button pauseButton = new Button("Pause");
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(110);
        pauseButton.setOnAction(e -> togglePause(pauseButton));
        dataContainer.getChildren().add(pauseButton);

        // Mute Button
        Button muteButton = new Button("Mute");
        muteButton.setLayoutX(15);
        muteButton.setLayoutY(140);
        muteButton.setOnAction(e -> toggleMute(muteButton));
        dataContainer.getChildren().add(muteButton);

        // Create Lane Buttons
        List<Double> laneXCoordinates = road.getXCooForLanes(); // Assuming this method returns X-coordinates for lanes
        for (int i = 0; i < laneXCoordinates.size(); i++) {
            double laneX = laneXCoordinates.get(i);
            Button laneButton = new Button("Add Car Lane " + (i + 1));
            laneButton.setLayoutX(15);
            laneButton.setLayoutY(180 + i * 30); // Adjust Y position dynamically for each button
            int laneIndex = i; // To capture the index for the lambda expression
            laneButton.setOnAction(e -> createVehicleInLane(laneIndex, laneX, mapContainer));
            dataContainer.getChildren().add(laneButton);
        }

        // Create Pedestrian Button
        Button createPedestrianButton = new Button("Create Pedestrian");
        createPedestrianButton.setLayoutX(15);
        createPedestrianButton.setLayoutY(180 + laneXCoordinates.size() * 30 + 20); // Position below the lane buttons
        createPedestrianButton.setOnAction(e -> generatePedestrians(mapContainer,1)); // Method to create a pedestrian
        dataContainer.getChildren().add(createPedestrianButton);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(mapContainer, dataContainer);

        startAnimation(mapContainer,simulationDuration);

        return new Scene(stackPane, totalWidth, road.getObjectHeight());
    }
    
    private void toggleMute(Button muteButton) {
        isMuted = !isMuted;
        if(isMuted){
            SoundPlayer.stopGeneralSounds();
        }
        else{
            SoundPlayer.playGeneralSounds(simulationDuration);
        }
        muteButton.setText(isMuted ? "Unmute" : "Mute");
        // System.out.println("Sound " + (isMuted ? "muted" : "unmuted"));
    }
    private void createVehicleInLane(int laneIndex, double laneX, Pane mapContainer) {

            // Adjust Y position based on direction
            String direction;
            double y;
            int halfSizeLanePositions = lanePositions.size() / 2;
            if (road.getNumberOfRoads() == 2) {
                if (laneIndex < halfSizeLanePositions) {
                    direction = "down";
                    y = -130; // Start from top for downward vehicles
                    // y = -ySpacing;
                } else {
                    direction = "up";
                    y = road.getObjectHeight(); // Start from bottom for upward vehicles
                    // y = road.getObjectHeight() + 20;
                }
            } 
            else {
                // Single road - all vehicles go up
                direction = "up";
                y = road.getObjectHeight(); /////////////////// what is the purpose of multiplyig ySpacing with car.size()? you can use ySpacig only
            }
            
        Vehicle newVehicle = createCar(laneX+10, y, direction); // Adjust the Y position to fit the road
        objecList.add(newVehicle);
        // Rotate vehicle based on direction
        if (direction.equals("down")) {
            newVehicle.getVehicleView().setRotate(180); // the rotation won't change the coordinates (x, y)
        }
        cars.add(newVehicle);
        mapContainer.getChildren().add(newVehicle.getVehicleView());
        
    }
    
    private void togglePause(Button pauseButton) {
        if (isPaused) {
            // Resume the animation
            animationTimer.start();
            pauseButton.setText("Pause");
            totalPausedTime += System.nanoTime() - pauseStartTime; // Add paused duration
            pauseStartTime = 0; // Reset pause start time
        } else {
            // Pause the animation
            animationTimer.stop();
            pauseButton.setText("Resume");
            pauseStartTime = System.nanoTime(); // Record when the pause starts
        }
        isPaused = !isPaused;
    }

    private void startAnimation(Pane mapContainer, int simulationDuration) {
        startTime = System.nanoTime();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && !isSimulationComplete) {
                    // Calculate elapsed time excluding paused duration
                    int elapsedSeconds = (int) ((now - startTime - totalPausedTime) / 1e9);
                    int remainingTime = simulationDuration - elapsedSeconds;

                    if (remainingTime <= 0) {
                        SoundPlayer.stopGeneralSounds();
                        stopSimulation();
                        isSimulationComplete = true;
                        simulationSummary();
                        simulationSamrary();
                        return;
                    }

                    // Update the remaining time on the UI
                    timeRemainingText.setText("Time Remaining: " + remainingTime + "s");

                    // Update simulation elements
                    updateVehicles(mapContainer);
                    updatePedestrians(mapContainer);
                }
            }
        };
        animationTimer.start();
    }
    private void simulationSamrary(){
        System.out.println(objecList.get(0));
        System.out.println(String.format(
            "%-5s %-25s %-10s %-10s %-10s %-10s %-10s %-10s",
            "ID", "Type", "Style", "Speed", "Direction", "TimeTaken", "IdealTime", "Passed"
        )
        );
        for (int i=1; i < objecList.size(); i++) {
            System.out.println(objecList.get(i));
        }
        
    }
    private void simulationSummary() {
    Stage primaryStage = new Stage();
    primaryStage.setTitle("Simulation Summary");

    // Create TableView
    TableView<GeneralRules> tableView = new TableView<>();

    // Create columns
    TableColumn<GeneralRules, String> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Car) 
            return new SimpleStringProperty(((Car) value).getCarID());
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(((Pedestrian) value).getPedestrianID());
        if (value instanceof Truck) 
            return new SimpleStringProperty(((Truck) value).getTruckID());
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Car) 
            return new SimpleStringProperty(((Car) value).getCarType());
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(((Pedestrian) value).getPedestrianType());
        if (value instanceof Truck) 
            return new SimpleStringProperty(((Truck) value).getTruckType());
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> styleColumn = new TableColumn<>("Style");
    styleColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Vehicle) 
            return new SimpleStringProperty(((Vehicle) value).getDriverStyle());
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(((Pedestrian) value).getPedestrianType());
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> speedColumn = new TableColumn<>("Speed");
    speedColumn.setCellValueFactory(cellData -> {
        if (cellData.getValue() instanceof MovingObjects) 
            return new SimpleStringProperty(String.valueOf(((MovingObjects) cellData.getValue()).getObjectSpeed()));
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> directionColumn = new TableColumn<>("Direction");
    directionColumn.setCellValueFactory(cellData -> {
        if (cellData.getValue() instanceof MovingObjects) 
            return new SimpleStringProperty(((MovingObjects) cellData.getValue()).getObjectDirection());
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> timeTakenColumn = new TableColumn<>("Time Taken (s)");
    timeTakenColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Vehicle) 
            return new SimpleStringProperty(String.valueOf(((Vehicle) value).getTimeTaken()));
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(String.valueOf(((Pedestrian) value).getTimeTaken()));
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> idealTimeColumn = new TableColumn<>("Ideal Time (s)");
    idealTimeColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Vehicle) 
            return new SimpleStringProperty(String.valueOf(((Vehicle) value).getIdealTime()));
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(String.valueOf(((Pedestrian) value).getIdealTime()));
        return new SimpleStringProperty("Unknown");
    });

    // Corrected "Passed" column
    TableColumn<GeneralRules, String> passedColumn = new TableColumn<>("Passed");
    passedColumn.setCellValueFactory(cellData -> {
        if (cellData.getValue() instanceof MovingObjects) 
            return new SimpleStringProperty(String.valueOf(((MovingObjects) cellData.getValue()).isObjectPassed()));
        return new SimpleStringProperty("Unknown");
    });

    // Create a list of columns and add them to the TableView
    List<TableColumn<GeneralRules, ?>> columns = Arrays.asList(
        idColumn, typeColumn, styleColumn, speedColumn, 
        directionColumn, timeTakenColumn, idealTimeColumn, passedColumn
    );
    tableView.getColumns().addAll(columns);

    // Populate TableView with data
    // Ensure objectList is correctly defined and populated (observable list of GeneralRules objects)
    ObservableList<GeneralRules> data = FXCollections.observableArrayList(
        objecList.subList(1, objecList.size()) // Skipping the first element if needed
    );
    tableView.setItems(data);

    // Create scene
    VBox vbox = new VBox(tableView);
    Scene scene = new Scene(vbox, 800, 400);
    // scene.getStylesheets().add(getClass().getResource("table_styles.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void updateVehicles(Pane mapContainer) {

        Image accidentFlagImage = new Image("file:src/resources/accidentFlag.png");
        ImageView accidentFlagView = new ImageView(accidentFlagImage);
        accidentFlagView.setFitWidth(30);
        accidentFlagView.setFitHeight(30);

        for (int i = 0; i < cars.size(); i++) {
            Vehicle car = cars.get(i);
            if(car.getAccidentHappen()){
                continue;
            }
            String driverStyle = car.getDriverStyle();
            boolean carStopped = false;
            double vehicleHeight = (car instanceof Car) ? 110 : 130;
            boolean pedestrianInPath = false;

            if(driverStyle.equals("normal") || driverStyle.equals("careful")){
                double carX = car.getXCOO();
                double carY = car.getYCOO();
    
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
                        double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + (driverStyle.equals("normal") ? 30 : 80);
                        if (otherY < carY && carY - otherY < minSafeDistance) {
                            carStopped = true;
                            break;
                        }
                    }
                    }
                    else{
                    // Check if vehicles are in the same lane
                    if (Math.abs(carX - otherX) < 20) {
                        double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + (driverStyle.equals("normal") ? 30 : 80);
                        if (otherY > carY && -carY + otherY < minSafeDistance) {
                            carStopped = true;
                            break;
                        }
                    }
                    }
                }
                
                // Check for pedestrians
                
                for (Pedestrian pedestrian : pedestrians) {
                    double pedestrianX = pedestrian.getXCOO();
                    double pedestrianY = pedestrian.getYCOO();
                    
                    if (car.getObjectDirection() == "down"){
                        if (carX + car.getObjectWidth() > pedestrianX && 
                            pedestrianX + pedestrian.getObjectWidth() > carX &&
                            pedestrianY > carY + car.getObjectHeight() &&
                            pedestrianY + pedestrian.getObjectHeight() < carY + car.getObjectHeight() + pedestrian.getObjectHeight() + 30){
                                pedestrianInPath = true;
                                break;
                            }

                    }
                    if (car.getObjectDirection() == "up"){
                        if (pedestrianX + pedestrian.getObjectWidth() > carX &&
                            pedestrianX < carX + car.getObjectWidth() && 
                            pedestrianY > carY - pedestrian.getObjectHeight() - 30 &&
                            pedestrianY + pedestrian.getObjectHeight() < carY){
                                pedestrianInPath = true;
                                break;
                            }

                    }
                }
            }
            
            else{
                double carX = car.getXCOO();
                double carY = car.getYCOO();
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
                            double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + 10;
                            if (otherY < carY && carY - otherY < minSafeDistance) {
                                carStopped = true;
                                break;
                            }
                        }
                    }
                    else{
                        // Check if vehicles are in the same lane
                        if (Math.abs(carX - otherX) < 20) {
                            double minSafeDistance = Math.max(vehicleHeight, otherVehicleHeight) + 10;
                            if (otherY > carY && -carY + otherY < minSafeDistance) {
                                carStopped = true;
                                break;
                            }
                        }
                    }
                }
                
                // Check for pedestrians
                
                for (Pedestrian pedestrian : pedestrians) {
                    double pedestrianX = pedestrian.getXCOO();
                    double pedestrianY = pedestrian.getYCOO();

                    if (pedestrianX + pedestrian.getObjectWidth() > carX + 15 &&
                        pedestrianX < carX + car.getObjectWidth() - 15 &&
                        pedestrianY + pedestrian.getObjectHeight() > carY + 15 &&
                        pedestrianY < carY + car.getObjectHeight() - 15 ){
            
                        car.setAccidentHappen(true); 
                        pedestrian.setAccidentHappen(true);
                        road.increaseNumberOfAccidents(1);
                        numberOfAccidents.setText("Accidents: " + road.getNumberOfAccidents());
                        
                        accidentFlagView.setX(carX + (car.getObjectWidth() == 50 ? 10 : 15));
                        accidentFlagView.setY(carY + (car.getObjectWidth() == 110  ? 40 : 50));
                        mapContainer.getChildren().add(accidentFlagView);
                        
                        // FadeTransition carFadeOut = new FadeTransition(Duration.seconds(5), car.getVehicleView());
                        // carFadeOut.setFromValue(1.0); 
                        // carFadeOut.setToValue(0.0);   
                        
                        // FadeTransition pedestrianFadeOut = new FadeTransition(Duration.seconds(5), pedestrian.getImageView());
                        // pedestrianFadeOut.setFromValue(1.0);
                        // pedestrianFadeOut.setToValue(0.0);   
                        
                        Timeline timeline = new Timeline();

                        // Calculate the duration of each cycle (5 seconds / 10 cycles)
                        double cycleDuration = 5.0 / 10.0; // in seconds

                        for (int k = 0; k < 10; k++) {
                        // Fade in
                            timeline.getKeyFrames().add(
                                new KeyFrame(
                                    Duration.seconds(k * cycleDuration),
                                    new KeyValue(accidentFlagView.opacityProperty(), 1.0),
                                    new KeyValue(pedestrian.getImageView().opacityProperty(), 1.0),
                                    new KeyValue(car.getVehicleView().opacityProperty(), 1.0)
                                )
                            );

                            // Fade out
                            timeline.getKeyFrames().add(
                                new KeyFrame(
                                    Duration.seconds((k + 0.5) * cycleDuration), // Halfway through the cycle
                                    new KeyValue(accidentFlagView.opacityProperty(), 0.0),
                                    new KeyValue(pedestrian.getImageView().opacityProperty(), 0.0),
                                    new KeyValue(car.getVehicleView().opacityProperty(), 0.0)
                                )
                            );
                        }
                        
                        timeline.setCycleCount(1);
                        timeline.setOnFinished(e -> { 
                            mapContainer.getChildren().remove(car.getVehicleView()); // Remove from the parent container (Pane)
                            mapContainer.getChildren().remove(pedestrian.getImageView());// System.out.println("ImageView removed after " + delayMillis + "ms.");
                            mapContainer.getChildren().remove(accidentFlagView); 
                            cars.remove(car);
                            pedestrians.remove(pedestrian);
                        });

                        timeline.play();
                        pedestrianInPath=true;
                        break;
                        
                        // carStopped = true;
                        // mapContainer.getChildren().remove(car.getVehicleView());
                        // // cars.remove(i);
                        
                        // mapContainer.getChildren().remove(pedestrian.getImageView());
                        // pedestrians.remove(pedestrian);
                            
                        // // if(pedestrian.getPedestrianStyle().equals("carless")){
                        //     if(car.getObjectDirection().equals("up") && pedestrianY-carY>0 && pedestrianY-carY<=20){
                        //         System.out.println(1);
                        //         pedestrian.setAccidentHappen(true);
                        //         car.setAccidentHappen(true);
                        //         road.increaseNumberOfAccidents(1);
                        //         numberOfAccidents.setText("Accidents: "+road.getNumberOfAccidents());
                        //                 // Create a PauseTransition that will last for the specified delay
                        //         PauseTransition pause = new PauseTransition(Duration.millis(10000));
    
                        //         // Set the action to remove the ImageView after the pause
                        //         pause.setOnFinished(event -> {
                        //             mapContainer.getChildren().remove(car.getVehicleView()); // Remove from the parent container (Pane)
                        //             mapContainer.getChildren().remove(pedestrian.getImageView());// System.out.println("ImageView removed after " + delayMillis + "ms.");
                        //             cars.remove(car);
                        //             pedestrians.remove(pedestrian);
                        //         });
    
                        //         // Start the pause (it runs asynchronously)
                        //         pause.play();
                        //         pedestrianInPath=true;
                        //         break;
                        //     }
                            
                        //     else if(car.getObjectDirection().equals("down") && 
                        //     Math.abs(carX - pedestrianX) < 20 && 
                        //     Math.abs(carY - pedestrianY) <= vehicleHeight && 
                        //     pedestrianY - carY >= 10){
                        //         System.out.println(1);
                        //         pedestrian.setAccidentHappen(true);
                        //         car.setAccidentHappen(true);
                        //         pedestrianInPath = true;
                        //         road.increaseNumberOfAccidents(1);
                        //         numberOfAccidents.setText("Accidents: "+road.getNumberOfAccidents());

                        //                 // Create a PauseTransition that will last for the specified delay
                        //         PauseTransition pause = new PauseTransition(Duration.millis(10000));
    
                        //         // Set the action to remove the ImageView after the pause
                        //         pause.setOnFinished(event -> {
                        //             mapContainer.getChildren().remove(car.getVehicleView()); // Remove from the parent container (Pane)
                        //             mapContainer.getChildren().remove(pedestrian.getImageView());// System.out.println("ImageView removed after " + delayMillis + "ms.");
                        //             cars.remove(car);
                        //             pedestrians.remove(pedestrian);
                        //         });
    
                        //         // Start the pause (it runs asynchronously)
                        //         pause.play();
                        //         break;
                        //     }
                        // // }
                        // else{
                        //     pedestrianInPath=true;
                        //     break;
                        // }
                    // }
                    }
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
                car.stopTimer();
                // System.out.println("Car null: " + (car == null));
                if (car != null) {
                    try {
                        // System.out.println("Driver Style: " + car.getDriverStyle());
                        // System.out.println("Speed: " + car.getObjectSpeed());
                        // System.out.println(car.toString());
                    } catch (Exception e) {
                        System.err.println("Error accessing car properties: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                mapContainer.getChildren().remove(car.getVehicleView());
                cars.remove(i);
                i--;
                car.setObjectPassed();
                road.increaseNumberOfPassedCars(1);
                passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                
                // generateVehicles(mapContainer, 1);
            }

            }
            else{
                // Handle vehicle reaching the top
                if (car.getYCOO() > road.getObjectHeight()+vehicleHeight) {
                    car.stopTimer();
                    // System.out.println("Car null: " + (car == null));
                    if (car != null) {
                        try {
                            // System.out.println("Driver Style: " + car.getDriverStyle());
                            // System.out.println("Speed: " + car.getObjectSpeed());

                            // System.out.println(car.toString());
                        } catch (Exception e) {
                            System.err.println("Error accessing car properties: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    mapContainer.getChildren().remove(car.getVehicleView());
                    cars.remove(i);
                    i--;
                    car.setObjectPassed();
                    road.increaseNumberOfPassedCars(1);
                    passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                    
                    // generateVehicles(mapContainer, 1);
                }
            }
        }
    }
    

    private void updatePedestrians(Pane mapContainer) {
        for (int i = 0; i < pedestrians.size(); i++) {
            Pedestrian pedestrian = pedestrians.get(i);
            boolean carInFront = false;
            double pedestrianX = pedestrian.getXCOO(); // x-coordinate of left edge of the pedestrian pic. Becareful, for a pedestrian moving right, the x-coordinate points to his back, and for a pedestrian moving left, it points to his face.
            double pedestrianY = pedestrian.getYCOO(); // y-coordinate of top edge of the pedestrian pic. Becareful, for a pedestrian moving right, the y-coordinate points to his right hand, and for a pedestrian moving left, it points to his left hand.
                            // Check if there's a car in front of the pedestrian
            for (Vehicle car : cars) {
                double carX = car.getXCOO(); // x-coordinate of left edge of the car pic. Becareful, for a car moving up, the x-coordinate points to the driver's left hand, and for a car moving down, it points to its driver's right hand.
                double carY = car.getYCOO(); // y-coordinate of top edge of the car pic. Becareful, for a car moving up, the y-coordinate points to its front bumper, and for a car moving down, it points to its rear bumper.
                double vehicleHeight = (car instanceof Car) ? 110 : 130;
    
                if(pedestrian.getPedestrianStyle().equals("normal")){

        

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
                else if(pedestrian.getPedestrianStyle().equals("careful")){

        

                    if (pedestrian.getObjectDirection().equals("right")) {
                        if (pedestrianX + pedestrian.getObjectWidth() > carX - 30 && // subtract 20 from carX for a safe distance 
                            pedestrianX + pedestrian.getObjectWidth() < carX) {      // if you want to move left in x-axis you should subtract, and add to move right
    
                            if (car.getObjectDirection().equals("up")) {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY - 70 && // subtract 50 from carY for a safe distance 
                                    pedestrianY <= carY + vehicleHeight) {                     // if you want to move up in y-axis you should subtract, and add to move down
    
                                    carInFront = true;
                                    break;
                                }
                            } else {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY &&
                                    pedestrianY <= carY + vehicleHeight + 70) {
                                    carInFront = true;
                                    break;
                                }
                            }
                        }
                    } 
                    else { // Moving left
                        if (pedestrianX > carX + car.getObjectWidth() &&
                            pedestrianX < carX + car.getObjectWidth() + 30) {
        
                            if (car.getObjectDirection().equals("up")) {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY - 70 &&
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
                else{

        

                    if (pedestrian.getObjectDirection().equals("right")) {
                        if (pedestrianX + pedestrian.getObjectWidth() > carX - 10 && // subtract 20 from carX for a safe distance 
                            pedestrianX + pedestrian.getObjectWidth() < carX) {      // if you want to move left in x-axis you should subtract, and add to move right
    
                            if (car.getObjectDirection().equals("up")) {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY - 30 && // subtract 50 from carY for a safe distance 
                                    pedestrianY <= carY + vehicleHeight) {                     // if you want to move up in y-axis you should subtract, and add to move down
    
                                    carInFront = true;
                                    break;
                                }
                            } else {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY &&
                                    pedestrianY <= carY + vehicleHeight + 10) {
                                    carInFront = true;
                                    break;
                                }
                            }
                        }
                    } 
                    else { // Moving left
                        if (pedestrianX > carX + car.getObjectWidth() &&
                            pedestrianX < carX + car.getObjectWidth() + 10) {
        
                            if (car.getObjectDirection().equals("up")) {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY - 30 &&
                                    pedestrianY <= carY + vehicleHeight) {
                                    carInFront = true;
                                    break;
                                }
                            } else {
                                if (pedestrianY + pedestrian.getObjectHeight() >= carY &&
                                    pedestrianY <= carY + vehicleHeight + 30) {
                                    carInFront = true;
                                    break;
                                }
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
                pedestrian.stopTimer();
                pedestrian.setObjectPassed();
                road.increaseNumberOfPassedPedestrians(1);
                passedPedestrianText.setText("Passed Pedestrian: " + road.getNumberOfPassedPedestrians());
                generatePedestrians(mapContainer, 1);
            }
        }
    }
    
    
    private Vehicle createCar(double x, double y, String direciton) {
        double RIGHTMOST_LANE_X = 0;
        double RIGHTMOST_LANE_X1 = 0;
        String driverStyle;
        int value = random.nextInt(100)+1;
        if(value<=50){  //50 %
            driverStyle="normal";
        }
        else if(value<=80){ // 30 %
            driverStyle="careful";
        }
        else{// 20 %
            driverStyle="carless";
        }
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
            Truck truck = new Truck(road, driverStyle);
            truck.setObjectDireciton(direciton);
            truck.createTruck(x, y);
            return truck;
        }
        Car car = new Car(road, driverStyle);
        car.setObjectDireciton(direciton);
        car.createCarVehicle(x, y);
        return car;
    }
    
    private void generateVehicles(Pane mapContainer, int numberOfVehicles) {

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
                    y = -130; // Start from top for downward vehicles
                    // y = -ySpacing;
                } else {
                    direction = "up";
                    y = road.getObjectHeight(); // Start from bottom for upward vehicles
                    // y = road.getObjectHeight() + 20;
                }
            } else {
                // Single road - all vehicles go up
                direction = "up";
                y = road.getObjectHeight(); /////////////////// what is the purpose of multiplyig ySpacing with car.size()? you can use ySpacig only
            }
            
            Vehicle vehicle = createCar(x, y, direction);
            ImageView vehicleImageView = vehicle.getVehicleView();
            objecList.add(vehicle);
            // Rotate vehicle based on direction
            if (direction.equals("down")) {
                vehicleImageView.setRotate(180); // the rotation won't change the coordinates (x, y)
            }
            
            cars.add(vehicle);
            mapContainer.getChildren().add(vehicleImageView);
        }
    }
    
    private void generatePedestrians(Pane mapContainer, int numberOfPedestrians) {
        String pedestrianStyle;
        for (int i = 0; i < numberOfPedestrians; i++) {
            int value = random.nextInt(100)+1;

            if(value<=50){
                pedestrianStyle="normal";
            }
            else if(value<=90){
                pedestrianStyle="careful";
            }
            else{
                pedestrianStyle="carless";
            }
            // System.out.println(pedestrianStyle);
            Pedestrian pedestrian = new Pedestrian(road, pedestrianStyle);
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
           objecList.add(pedestrian);
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