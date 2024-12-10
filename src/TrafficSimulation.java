import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrafficSimulation {
    private Road road;
    private int numberOfVehicles;
    private List<GeneralRules> objecList = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Pedestrian> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    private Text passedVehiclesText;
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
    private long pauseStartTime = 0;
    private long totalPausedTime = 0;
    private boolean autoVehiclesGeneration;
    private boolean autoPedestriansGeneration;
    private double averagePedestrianDifferanceTime;
    private double averageVehicleDifferanceTime;
    private ArrayList<Button> addVehicleButtons = new ArrayList<>();
    private Rectangle hideRectangle;
    public TrafficSimulation(Road road, int numberOfVehicles, int numberOfPedestrian, int simulationDuration, boolean autoVehiclesGeneration, boolean autoPedestriansGeneration) {
        this.road = road;
        this.numberOfVehicles = numberOfVehicles;
        this.numberOfPedestrian = numberOfPedestrian;
        this.simulationDuration = simulationDuration;
        this.autoVehiclesGeneration = autoVehiclesGeneration;
        this.autoPedestriansGeneration = autoPedestriansGeneration;
        objecList.add(road);
    }

    public Scene createSimulationScene() {
        SoundPlayer.playGeneralSounds(simulationDuration);
        Pane mapContainer = road.createMap();
        double totalWidth = road.getObjectWidth();
        double roadHeight = road.getObjectHeight();

        
        passedVehiclesText = new Text(15, 25, "Passed Vehicles: 0");
        passedPedestrianText = new Text(15, 45, "Passed Pedestrians: 0");
        timeRemainingText = new Text(15, 65, "Time Remaining: " + simulationDuration + "s");
        numberOfAccidents = new Text(15, 85, "Accidents: 0");
        passedVehiclesText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        passedPedestrianText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        timeRemainingText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        numberOfAccidents.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 11));
        hideRectangle = new Rectangle(totalWidth, 100, Color.rgb(43, 123, 50));
        hideRectangle.setY(roadHeight);
        Pane dataContainer = new Pane();
        Rectangle bottomRect = new Rectangle(146, 100, Color.rgb(120, 126, 186));
        Rectangle topRect = new Rectangle(136, 90, Color.rgb(180, 188, 217));
        topRect.setX(5);
        topRect.setY(5);
        dataContainer.getChildren().addAll(bottomRect, topRect, passedVehiclesText, passedPedestrianText, numberOfAccidents, timeRemainingText);

        // Pause Button
        Button pauseButton = new Button("Pause");
        pauseButton.getStyleClass().add("custom-button");
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(110);
        pauseButton.setOnAction(e -> togglePause(pauseButton));
        dataContainer.getChildren().add(pauseButton);

        // Mute Button
        Button muteButton = new Button("Mute");
        muteButton.getStyleClass().add("custom-button");
        muteButton.setLayoutX(15);
        muteButton.setLayoutY(150);
        muteButton.setOnAction(e -> toggleMute(muteButton));
        dataContainer.getChildren().add(muteButton);
        mapContainer.getChildren().add(hideRectangle);
        // Create Lane Buttons
        List<Double> laneXCoordinates = road.getXCooForLanes(); // Assuming this method returns X-coordinates for lanes
        for (Double laneX : laneXCoordinates) {
            Button laneButton = new Button("Add Vehicle");
            laneButton.getStyleClass().add("custom-button");
            laneButton.setLayoutX(laneX-15);
            laneButton.setLayoutY(roadHeight+40); // Adjust Y position dynamically for each button
            laneButton.setOnAction(e -> createVehicleInLane(laneXCoordinates.indexOf(laneX), laneX, mapContainer));
            mapContainer.getChildren().add(laneButton);
            addVehicleButtons.add(laneButton);
        }
        generateVehicles(mapContainer, numberOfVehicles);
        generatePedestrians(mapContainer, numberOfPedestrian);
        // Create Pedestrian Button
        Button createPedestrianButton = new Button("Create Pedestrian");
        createPedestrianButton.getStyleClass().add("custom-button");
        createPedestrianButton.setLayoutX(15);
        createPedestrianButton.setLayoutY(190); // Position below the lane buttons
        createPedestrianButton.setOnAction(e -> generatePedestrians(mapContainer,1)); // Method to create a pedestrian
        mapContainer.setPrefWidth(totalWidth); // Ensure this matches the actual width of the road

        dataContainer.getChildren().add(createPedestrianButton);
        ScrollPane scrollPane = new ScrollPane(mapContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Disable vertical scroll bar
        // Optional: Set the preferred viewport size
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setPrefViewportHeight(200);
        StackPane stackPane = new StackPane();
        
        stackPane.getChildren().add(scrollPane);
        stackPane.getChildren().add(dataContainer);

        StackPane.setAlignment(dataContainer, Pos.BOTTOM_CENTER);
        dataContainer.setPickOnBounds(false); // Only block clicks on actual elements, not the empty area

        
        startAnimation(mapContainer,simulationDuration);

        return new Scene(stackPane, totalWidth-10, road.getObjectHeight()+100);
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
    }
    private void createVehicleInLane(int laneIndex, double laneX, Pane mapContainer) {

            // Adjust Y position based on direction
            String direction;
            double y;
            int halfSizeLanePositions = lanePositions.size() / 2;
            if (road.getNumberOfPaths() == 2) {
                if (laneIndex < halfSizeLanePositions) {
                    direction = "down";
                    y = -130; 
                } else {
                    direction = "up";
                    y = road.getObjectHeight(); 
                }
            } 
            else {
                // Single road - all vehicles go up
                direction = "up";
                y = road.getObjectHeight(); 
            }
            
        Vehicle newVehicle = creatVehicle(laneX+10, y, direction); 
        objecList.add(newVehicle);
        // Rotate vehicle based on direction
        if (direction.equals("down")) {
            newVehicle.getVehicleView().setRotate(180); 
        }
        vehicles.add(newVehicle);
        mapContainer.getChildren().add(newVehicle.getVehicleView());
        road.getPedestrianBridge().getBridgeImageView().toFront();
        // bgImageView.toFront();
        makePedestrianInUpperLayer();
        
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
    Image icon = new Image("icon.jpg"); // Path to the icon file
    primaryStage.getIcons().add(icon);
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
            return new SimpleStringProperty(((Vehicle) value).getDriverStyle().toUpperCase());
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(((Pedestrian) value).getPedestrianStyle().toUpperCase());
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
            return new SimpleStringProperty(((MovingObjects) cellData.getValue()).getObjectDirection().toUpperCase());
        return new SimpleStringProperty("Unknown");
    });

    TableColumn<GeneralRules, String> timeTakenColumn = new TableColumn<>("Time Taken (s)");
    timeTakenColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Vehicle) 
            return new SimpleStringProperty(String.format("%-10.2f",((Vehicle) value).getTimeTaken()));
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(String.format("%-10.2f",((Pedestrian) value).getTimeTaken()));
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

    TableColumn<GeneralRules, String> timeDifferanceColumn = new TableColumn<>("Time Differance (s)");
    timeDifferanceColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof Vehicle) 
            return new SimpleStringProperty(String.format("%-10.2f",((Vehicle) value).getTimeDifferance()));
        if (value instanceof Pedestrian) 
            return new SimpleStringProperty(String.format("%-10.2f",((Pedestrian) value).getTimeDifferance()));
        return new SimpleStringProperty("Unknown");
    });

    // Corrected "Passed" column
    TableColumn<GeneralRules, String> passedColumn = new TableColumn<>("Passed");
    passedColumn.setCellValueFactory(cellData -> {
        if (cellData.getValue() instanceof MovingObjects) 
            return new SimpleStringProperty(String.valueOf(((MovingObjects) cellData.getValue()).isObjectPassed()));
        return new SimpleStringProperty("Unknown");
    });

    
    TableColumn<GeneralRules, String> getAccidentColumn = new TableColumn<>("Accident Happen");
    getAccidentColumn.setCellValueFactory(cellData -> {
        GeneralRules value = cellData.getValue();
        if (value instanceof MovingObjects) 
            return new SimpleStringProperty(String.valueOf(((MovingObjects) cellData.getValue()).getAccidentHappen()));
        return new SimpleStringProperty("Unknown");
    });
    // Create a list of columns and add them to the TableView
    List<TableColumn<GeneralRules, ?>> columns = Arrays.asList(
        idColumn, typeColumn, styleColumn, speedColumn, 
        directionColumn, timeTakenColumn, idealTimeColumn, timeDifferanceColumn, passedColumn, getAccidentColumn
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
    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void updateVehicles(Pane mapContainer) {
        Image accidentFlagImage = new Image("file:src/resources/accidentFlag.png");
        ImageView accidentFlagView = new ImageView(accidentFlagImage);
        accidentFlagView.setFitWidth(30);
        accidentFlagView.setFitHeight(30);

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            if (vehicle.getAccidentHappen()) {
                continue;
            }

            boolean otherVheicleInFront = checkIfOtherVheicleInFrontOfVehicle(vehicle, i);
            boolean pedestrianBlocking = checkIfPedestrianInVehiclePath(vehicle, mapContainer, accidentFlagView);

            // Move or stop the vehicle based on obstacle detection
            if (otherVheicleInFront || pedestrianBlocking) {
                vehicle.stop();
            } else {
                moveVehicleIfWithinRoadBounds(vehicle);
            }

            // Handle vehicles that exit the road bounds
            if (isVehicleOutOfRoadBounds(vehicle)) {
                removeVehicleFromMap(mapContainer, vehicle, i);
                i--;
            }
        }
    }

    private boolean checkIfOtherVheicleInFrontOfVehicle(Vehicle vehicle, int currentIndex) {
        int frontSafeDistance = calculateSafeDistancesForVehicle(vehicle.getDriverStyle());

        for (int j = 0; j < vehicles.size(); j++) {
            if (currentIndex == j) continue;

            Vehicle otherVehicle = vehicles.get(j);
            double vehicleHeight = (vehicle instanceof Car) ? 110 : 130;
            double minSafeDistance = Math.max(vehicleHeight, 
                (otherVehicle instanceof Car ? 110 : 130)) + frontSafeDistance;

            if (isVehicleTooCloseToAnotherVehicle(vehicle, otherVehicle, minSafeDistance)) {
                return true;
            }
        }
        return false;
    }

    private int calculateSafeDistancesForVehicle(String driverStyle) {
        if (driverStyle.equals("careful")) {
            return random.nextInt(20) + 80;
        }
        else if (driverStyle.equals("normal")) {
            return random.nextInt(15) + 30;
        }
        else{
            return random.nextInt(10) + 10;
        }
    }

    private boolean isVehicleTooCloseToAnotherVehicle(Vehicle vehicle, Vehicle otherVehicle, double minSafeDistance) {
        double vehicleX = vehicle.getXCOO();
        double vehicleY = vehicle.getYCOO();
        double otherX = otherVehicle.getXCOO();
        double otherY = otherVehicle.getYCOO();

        if (Math.abs(vehicleX - otherX) < 20) {
            if (vehicle.getObjectDirection().equals("up")) {
                return otherY < vehicleY && vehicleY - otherY < minSafeDistance;
            } else {
                return otherY > vehicleY && -vehicleY + otherY < minSafeDistance;
            }
        }
        return false;
    }

    private boolean checkIfPedestrianInVehiclePath(Vehicle vehicle, Pane mapContainer, ImageView accidentFlagView) {
        for (Pedestrian pedestrian : pedestrians) {
            if ((isPedestrianBlockingVehicle(vehicle, pedestrian) && !road.getPedestrianBridge().isBridgeStatus())  || (road.getPedestrianBridge().isBridgeStatus() && isPedestrianBlockingVehicle(vehicle, pedestrian) && pedestrian.getPedestrianStyle().equals("carless"))) {
                return true;
            } else if (isPedestrianCausingAccidentWithVehicle(vehicle, pedestrian) && (!road.getPedestrianBridge().isBridgeStatus()|| pedestrian.getPedestrianStyle().equals("carless"))) {
                handleAccident(vehicle, pedestrian, mapContainer, accidentFlagView);
                return false;
            }
        }
        return false;
    }

    private boolean isPedestrianBlockingVehicle(Vehicle vehicle, Pedestrian pedestrian) {
        double vehicleX = vehicle.getXCOO();
        double vehicleY = vehicle.getYCOO();
        double pedestrianX = pedestrian.getXCOO();
        double pedestrianY = pedestrian.getYCOO();

        if (vehicle.getDriverStyle().equals("normal") || vehicle.getDriverStyle().equals("careful")) {
            if (vehicle.getObjectDirection().equals("down")) {
                return vehicleX + vehicle.getObjectWidth() > pedestrianX &&
                       pedestrianX + pedestrian.getObjectWidth() > vehicleX &&
                       pedestrianY > vehicleY + vehicle.getObjectHeight() &&
                       pedestrianY < vehicleY + vehicle.getObjectHeight() + pedestrian.getObjectHeight() + 30;
            } else if (vehicle.getObjectDirection().equals("up")) {
                return pedestrianX + pedestrian.getObjectWidth() > vehicleX &&
                       pedestrianX < vehicleX + vehicle.getObjectWidth() &&
                       pedestrianY > vehicleY - pedestrian.getObjectHeight() - 30 &&
                       pedestrianY < vehicleY;
            }
        }
        return false;
    }

    private boolean isPedestrianCausingAccidentWithVehicle(Vehicle vehicle, Pedestrian pedestrian) {
        double vehicleX = vehicle.getXCOO();
        double vehicleY = vehicle.getYCOO();
        double pedestrianX = pedestrian.getXCOO();
        double pedestrianY = pedestrian.getYCOO();

        return pedestrianX + pedestrian.getObjectWidth() > vehicleX + 15 &&
               pedestrianX < vehicleX + vehicle.getObjectWidth() - 15 &&
               pedestrianY + pedestrian.getObjectHeight() > vehicleY + 15 &&
               pedestrianY < vehicleY + vehicle.getObjectHeight() - 15;
    }


    private void handleAccident(Vehicle vehicle, Pedestrian pedestrian, Pane mapContainer, ImageView accidentFlagView) {
        vehicle.setAccidentHappen(true);
        pedestrian.setAccidentHappen(true);
        road.increaseNumberOfAccidents(1);
        numberOfAccidents.setText("Accidents: " + road.getNumberOfAccidents());

        accidentFlagView.setX(vehicle.getXCOO() + (vehicle.getObjectWidth() == 50 ? 10 : 15));
        accidentFlagView.setY(vehicle.getYCOO() + (vehicle.getObjectWidth() == 110 ? 40 : 50));
        mapContainer.getChildren().add(accidentFlagView);

        startAccidentAnimation(vehicle, pedestrian, mapContainer, accidentFlagView);
    }

    private void startAccidentAnimation(Vehicle vehicle, Pedestrian pedestrian, Pane mapContainer, ImageView accidentFlagView) {
        Timeline timeline = new Timeline();
        double cycleDuration = 5.0 / 10.0;

        for (int k = 0; k < 10; k++) {
            timeline.getKeyFrames().add(
                new KeyFrame(
                    Duration.seconds(k * cycleDuration),
                    new KeyValue(accidentFlagView.opacityProperty(), 1.0),
                    new KeyValue(pedestrian.getImageView().opacityProperty(), 1.0),
                    new KeyValue(vehicle.getVehicleView().opacityProperty(), 1.0)
                )
            );

            timeline.getKeyFrames().add(
                new KeyFrame(
                    Duration.seconds((k + 0.5) * cycleDuration),
                    new KeyValue(accidentFlagView.opacityProperty(), 0.0),
                    new KeyValue(pedestrian.getImageView().opacityProperty(), 0.0),
                    new KeyValue(vehicle.getVehicleView().opacityProperty(), 0.0)
                )
            );
        }

        timeline.setCycleCount(1);
        timeline.setOnFinished(e -> {
            mapContainer.getChildren().removeAll(vehicle.getVehicleView(), pedestrian.getImageView(), accidentFlagView);
            vehicles.remove(vehicle);
            pedestrians.remove(pedestrian);
        });

        timeline.play();
    }

    private void moveVehicleIfWithinRoadBounds(Vehicle vehicle) {
        vehicle.move();
    }

    private boolean isVehicleOutOfRoadBounds(Vehicle vehicle) {
        double vehicleY = vehicle.getYCOO();
        return (road.getNumberOfPaths() == 1 || vehicle.getObjectDirection().equals("up")) ?
               vehicleY < -vehicle.getObjectHeight() :
               vehicleY > road.getObjectHeight() + vehicle.getObjectHeight();
    }

    private void removeVehicleFromMap(Pane mapContainer, Vehicle vehicle, int index) {
        mapContainer.getChildren().remove(vehicle.getVehicleView());
        vehicles.remove(index);
        vehicle.stopTimer();
        vehicle.setObjectPassed();
        road.increaseNumberOfPassedVehicles(1);
        passedVehiclesText.setText("Passed Vehicles: " + road.getNumberOfPassedVehicles());
        if(autoVehiclesGeneration){
            generateVehicles(mapContainer, 1);
        }
    }

    public void calculateAverdifferanceTime(){
        int vehicleCounter = 0;
        int pedestrianCounter = 0;
        double totalVehicleDifferanceTime = 0;
        double totalPedestrianDifferanceTime = 0;
        for(GeneralRules object:objecList){
            if(object instanceof Vehicle){
                Vehicle vehicle = (Vehicle)object;
                if(vehicle.getTimeTaken()!=0){
                    vehicleCounter++;
                    totalVehicleDifferanceTime+=vehicle.getTimeDifferance();
                }
            }
            else if(object instanceof Pedestrian){
                Pedestrian pedestrian = (Pedestrian)object;
                if(pedestrian.getTimeTaken()!=0){
                    pedestrianCounter++;
                    totalPedestrianDifferanceTime+=pedestrian.getTimeDifferance();
                }
            }
        }
        averagePedestrianDifferanceTime=totalPedestrianDifferanceTime/pedestrianCounter;
        averageVehicleDifferanceTime=totalVehicleDifferanceTime/vehicleCounter;
    }
    public double getAverageVehicleDifferanceTime() {
        return averageVehicleDifferanceTime;
    }
    public double getAveragePedestrianDifferanceTime() {
        return averagePedestrianDifferanceTime;
    }
    private void updatePedestrians(Pane mapContainer) {
        boolean condition = road.getPedestrianBridge().isBridgeStatus();
        for (int i = 0; i < pedestrians.size(); i++) {
            Pedestrian pedestrian = pedestrians.get(i);
            if(condition && !pedestrian.getPedestrianStyle().equals("carless")){
                goToBridge(pedestrian);
            }
            else{
                boolean vehicleInFront = checkIfVehicleInFrontOfPedestria(pedestrian);
    
                // Move or stop the pedestrian based on car detection
                if (!vehicleInFront) {
                    movePedestrianIfWithinBounds(pedestrian);
                } else {
                    pedestrian.stop();
                }
            }

    
            // Handle pedestrians that pass the road bounds
            if (isPedestrianOutOfBounds(pedestrian)) {
                removePedestrian(mapContainer, pedestrian, i);
                i--;
            }
        }
    }
    private void goToBridge(Pedestrian pedestrian){ 
        double[] bridgeBoundry = road.getPedestrianBridge().getYBoundary();
        double pedestrianY = pedestrian.getYCOO();
        double pedestrianX = pedestrian.getXCOO();
        if(pedestrianX<0 || pedestrianX+pedestrian.getObjectWidth()>=road.getObjectWidth()){
            pedestrian.move();
        }
        if(pedestrianY>=bridgeBoundry[1]-pedestrian.getObjectHeight()-20){
            pedestrian.moveUp();
        }
        else if(pedestrianY+pedestrian.getObjectHeight()<=bridgeBoundry[0]+ pedestrian.getObjectHeight()+20){
            pedestrian.moveDown();
        }
        else{
            pedestrian.move();
        }
    }
    // Check if there's a car in front of the pedestrian
    private boolean checkIfVehicleInFrontOfPedestria(Pedestrian pedestrian) {
        boolean vehicleInFront = false;
        int[] safeDistances = calculateSafeDistancesForPedestrians(pedestrian.getPedestrianStyle());
        int xSafeDistance = safeDistances[0];
        int ySafeDistance = safeDistances[1];
    
        double pedestrianX = pedestrian.getXCOO();
        double pedestrianY = pedestrian.getYCOO();
    
        for (Vehicle vehicle : vehicles) {
            double vehicleX = vehicle.getXCOO();
            double vehicleY = vehicle.getYCOO();
            double vehicleHeight = (vehicle instanceof Car) ? 110 : 130;
    
            if (isVehicleBlockingPedestrian(pedestrian, vehicle, pedestrianX, pedestrianY, vehicleX, vehicleY, vehicleHeight, xSafeDistance, ySafeDistance)) {
                vehicleInFront = true;
                break;
            }
        }
        return vehicleInFront;
    }
    
    // Get safe distances based on pedestrian style
    private int[] calculateSafeDistancesForPedestrians(String pedestrianStyle) {
        int xSafeDistance, ySafeDistance;
        if (pedestrianStyle.equals("normal")) {
            xSafeDistance = random.nextInt(5) + 20;
            ySafeDistance = random.nextInt(5) + 50;
        }
        else if (pedestrianStyle.equals("careful")) {
            xSafeDistance = random.nextInt(5) + 30;
            ySafeDistance = random.nextInt(5) + 70;
        }
        else {
            xSafeDistance = random.nextInt(5) + 10;
            ySafeDistance = random.nextInt(5) + 30;
        }
        return new int[]{xSafeDistance, ySafeDistance};
    }
    
    // Check if a car is blocking the pedestrian's path
private boolean isVehicleBlockingPedestrian(Pedestrian pedestrian, Vehicle vehicle, double pedestrianX, double pedestrianY, double vehicleX, double vehicleY, double vehicleHeight, int xSafeDistance, int ySafeDistance) {
    if (pedestrian.getObjectDirection().equals("right")) {
        // Check X-axis conditions for a pedestrian moving right
        boolean isXOverlap = pedestrianX + pedestrian.getObjectWidth() > vehicleX - xSafeDistance &&
                             pedestrianX + pedestrian.getObjectWidth() < vehicleX;
        // Check Y-axis overlap based on the vehicle's direction
        boolean isYOverlap = vehicle.getObjectDirection().equals("up")
                ? pedestrianY + pedestrian.getObjectHeight() >= vehicleY - ySafeDistance &&
                  pedestrianY <= vehicleY + vehicleHeight
                : pedestrianY + pedestrian.getObjectHeight() >= vehicleY &&
                  pedestrianY <= vehicleY + vehicleHeight + ySafeDistance;

        return isXOverlap && isYOverlap;
    } else {
        // Check X-axis conditions for a pedestrian moving left
        boolean isXOverlap = pedestrianX > vehicleX + vehicle.getObjectWidth() &&
                             pedestrianX < vehicleX + vehicle.getObjectWidth() + xSafeDistance;
        // Check Y-axis overlap based on the vehicle's direction
        boolean isYOverlap = vehicle.getObjectDirection().equals("up")
                ? pedestrianY + pedestrian.getObjectHeight() >= vehicleY - ySafeDistance &&
                  pedestrianY <= vehicleY + vehicleHeight
                : pedestrianY + pedestrian.getObjectHeight() >= vehicleY &&
                  pedestrianY <= vehicleY + vehicleHeight + ySafeDistance;

        return isXOverlap && isYOverlap;
    }
}

    
    // Move pedestrian if within bounds
    private void movePedestrianIfWithinBounds(Pedestrian pedestrian) {
        double pedestrianX = pedestrian.getXCOO();
        if ((pedestrian.getObjectDirection().equals("right") && pedestrianX < road.getObjectWidth()) ||
            (pedestrian.getObjectDirection().equals("left") && pedestrianX > -pedestrian.getObjectWidth())) {
            pedestrian.move();
        }
    }
    
    // Check if the pedestrian has passed the road bounds
    private boolean isPedestrianOutOfBounds(Pedestrian pedestrian) {
        double pedestrianX = pedestrian.getXCOO();
        return (pedestrian.getObjectDirection().equals("right") && pedestrianX >= road.getObjectWidth()) ||
               (pedestrian.getObjectDirection().equals("left") && pedestrianX <= -pedestrian.getObjectWidth());
    }
    
    // Remove pedestrian from the map and update counters
    private void removePedestrian(Pane mapContainer, Pedestrian pedestrian, int index) {
        mapContainer.getChildren().remove(pedestrian.getImageView());
        pedestrians.remove(index);
        pedestrian.stopTimer();
        pedestrian.setObjectPassed();
        road.increaseNumberOfPassedPedestrians(1);
        passedPedestrianText.setText("Passed Pedestrian: " + road.getNumberOfPassedPedestrians());
        if(autoPedestriansGeneration){
            generatePedestrians(mapContainer, 1);
        }
    }
    
    private Vehicle creatVehicle(double x, double y, String direciton) {
        double RIGHTMOST_LANE_X = 0;
        double RIGHTMOST_LANE_X1 = 0;
        String driverStyle;
        int value = random.nextInt(100)+1;
        if(value<=50){  //50 %
            driverStyle="normal";
        }
        else if(value<=90){ // 30 %
            driverStyle="careful";
        }
        else{// 20 %
            driverStyle="carless";
        }
        if(road.getNumberOfPaths()==2){
            RIGHTMOST_LANE_X1 = road.getRightMostLane().get(road.getRightMostLane().size()-1)+10;
            RIGHTMOST_LANE_X = road.getRightMostLane().get(0)+10;
        }
        else{
            RIGHTMOST_LANE_X = road.getRightMostLane().get(0)+10;

        }
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
    
        // For two paths, vehicles in the first half of lanes should go down, others go up
        int halfSizeLanePositions = lanePositions.size() / 2;
    
        for (int i = 0; i < numberOfVehicles; i++) {
            int laneIndex = random.nextInt(lanePositions.size());
            Double x = lanePositions.get(laneIndex);
            
            // Adjust Y position based on direction
            String direction;
            double y;
            
            if (road.getNumberOfPaths() == 2) {
                if (laneIndex < halfSizeLanePositions) {
                    direction = "down";
                    y = -130; // Start from top for downward vehicles
                    // y = -ySpacing;
                } else {
                    direction = "up";
                    y = road.getObjectHeight(); // Start from bottom for upward vehicles
                    
                }
            } else {
                // Single road - all vehicles go up
                direction = "up";
                y = road.getObjectHeight(); 
            }
            
            Vehicle vehicle = creatVehicle(x, y, direction);
            ImageView vehicleImageView = vehicle.getVehicleView();
            objecList.add(vehicle);
            // Rotate vehicle based on direction
            if (direction.equals("down")) {
                vehicleImageView.setRotate(180); 
            }
            
            vehicles.add(vehicle);
            mapContainer.getChildren().add(vehicleImageView);
            road.getPedestrianBridge().getBridgeImageView().toFront();
            makePedestrianInUpperLayer();
            makeRectangleButtonsUpperLayer();
        }
    }
    private void makeRectangleButtonsUpperLayer(){
        hideRectangle.toFront();
        for(Button btn:addVehicleButtons){
            btn.toFront();
        }
    }
    private void makePedestrianInUpperLayer(){
        for(Pedestrian pedestrian: pedestrians){
                pedestrian.getImageView().toFront();
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
            Pedestrian pedestrian = new Pedestrian(road, pedestrianStyle);
            boolean right = random.nextBoolean();
            if(right){
                pedestrian.setObjectDireciton("right");
                pedestrian.createPedestrian(road.getPedestrianBridge().isBridgeStatus());
            }
            else{
                pedestrian.setObjectDireciton("left");
                pedestrian.createPedestrian(road.getPedestrianBridge().isBridgeStatus());
                pedestrian.getImageView().setRotate(180);
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
    
    public int getPassedVehicles() {
        return road.getNumberOfPassedVehicles();
    }
    
    public int getPassedPedestrians() {
        return road.getNumberOfPassedPedestrians();
    }

    public int getNumberOfAccidents() {
        return road.getNumberOfAccidents();
    }
    public Road getRoad(){
        return road;
    }
}