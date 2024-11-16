import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
// import Road;
public class App extends Application {
    Road road = new Road(4 , "normal", 5);
    private double totalWidth = 0;
    private List<ImageView> cars = new ArrayList<>();
    private List<ImageView> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    private int passedCarsCount = 0;
    private List<String> vehicleTypes = List.of("car", "car2", "car3", "car4", "truck", "truck2");
    private List<String> pedestrianTypes = List.of("pedestrian", "pedestrian2", "pedestrian3", "pedestrian4", "pedestrian5");
    private static final int RIGHTMOST_LANE_X = 600;
    private Text passedCarsText;

    @Override
    public void start(Stage primaryStage) {
        // Create the map
        Pane mapContainer = createMap();
        
        // Generate initial vehicles and pedestrians
        generateCars(mapContainer, 15);
        generatePedestrians(mapContainer, 15);
        
        // Create the counter text
        passedCarsText = new Text(20, 30, "Passed Cars: 0");
        mapContainer.getChildren().add(passedCarsText);
        
        // Animation timer for movement
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Move all vehicles
                for (int i = 0; i < cars.size(); i++) {
                    ImageView car = cars.get(i);
                    double carX = car.getX();
                    double carY = car.getY();
                    boolean carStopped = false;
                    //fe
                    // Get the current vehicle type
                    String vehicleType = (String) car.getUserData();
                    double vehicleHeight = vehicleType.startsWith("truck") ? 130 : 110;
                    
                    // Check for vehicle collision
                    for (int j = 0; j < cars.size(); j++) {
                        if (i == j) continue;
                        
                        ImageView otherCar = cars.get(j);
                        double otherX = otherCar.getX();
                        double otherY = otherCar.getY();
                        String otherVehicleType = (String) otherCar.getUserData();
                        double otherVehicleHeight = otherVehicleType.startsWith("truck") ? 130 : 110;
                        
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
                    for (ImageView pedestrian : pedestrians) {
                        double pedestrianX = pedestrian.getX() - 10;
                        double pedestrianY = pedestrian.getY() + 10;
                        
                        if (Math.abs(carX - pedestrianX) < 30 && 
                            pedestrianY <= carY + vehicleHeight && 
                            pedestrianY >= carY - vehicleHeight) {
                            pedestrianInPath = true;
                            break;
                        }
                    }
                    
                    // Move vehicle
                    if (carStopped || pedestrianInPath) {
                        car.setY(carY);
                    } else {
                        // Trucks move slightly slower than cars
                        double speed = vehicleType.startsWith("truck") ? 1.5 : 2;
                        car.setY(carY - speed);
                    }
                    
                    // Handle vehicle reaching the top
                    if (car.getY() < -vehicleHeight) {
                        mapContainer.getChildren().remove(car);
                        cars.remove(i);
                        i--;
                        
                        passedCarsCount++;
                        passedCarsText.setText("Passed Cars: " + passedCarsCount);
                        
                        generateCars(mapContainer, 1);
                    }
                }
                
                // Move pedestrians
                for (int i = 0; i < pedestrians.size(); i++) {
                    ImageView pedestrian = pedestrians.get(i);
                    double pedestrianX = pedestrian.getX();
                    double pedestrianY = pedestrian.getY();
                    
                    boolean carInFront = false;
                    for (ImageView car : cars) {
                        double carX = car.getX() - 10;
                        double carY = car.getY() - 20;
                        String vehicleType = (String) car.getUserData();
                        double vehicleHeight = vehicleType.startsWith("truck") ? 130 : 110;
                        
                        if (Math.abs(carX - pedestrianX) < 50 && 
                            pedestrianY <= carY + vehicleHeight && 
                            pedestrianY >= carY - 50) {
                            carInFront = true;
                            break;
                        }
                    }
                    
                    if (!carInFront) {
                        if (pedestrianX < totalWidth - 30) {
                            pedestrian.setX(pedestrianX + 1);
                        }
                    }
                    
                    if (pedestrianX >= totalWidth - 30) {
                        pedestrian.setX(0);
                        pedestrian.setY(random.nextInt(420));
                        // Change pedestrian type when reaching the end
                        String newPedestrianType = pedestrianTypes.get(random.nextInt(pedestrianTypes.size()));
                        Image newPedestrianImage = new Image("file:src/resources/" + newPedestrianType + ".png");
                        pedestrian.setImage(newPedestrianImage);
                        pedestrian.setUserData(newPedestrianType);
                    }
                }
            }
        };
        animationTimer.start();
        
        // Set up the scene
        Scene scene = new Scene(mapContainer, totalWidth, 464);
        primaryStage.setTitle("Traffic Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createMap() {
        Pane mapContainer = new Pane();
        mapContainer.setStyle("-fx-padding: 0; -fx-background-color: #232329;");
        
        Image walkingsideLeft = new Image("file:src/resources/walkingsideLeft.png");
        Image movingStreet = new Image("file:src/resources/moving_street.png");
        Image separatorLines = new Image("file:src/resources/speratorLines.png");
        Image walkingsideRight = new Image("file:src/resources/walkingsideRight.png");

        double width1 = 146;
        double width2 = 113;
        double width3 = 26;

        int numberOfRoads = 1;
        int numberOfLanes = 4;
        double currentX = 0;

        for (int i = numberOfRoads; i > 0; i--) {
            // Add left walking side
            ImageView leftWalk = new ImageView(walkingsideLeft);
            leftWalk.setLayoutX(currentX);
            leftWalk.setFitWidth(width1);
            mapContainer.getChildren().add(leftWalk);
            currentX += width1;

            // Add lanes
            for (int j = 0; j < numberOfLanes; j++) {
                ImageView street = new ImageView(movingStreet);
                street.setLayoutX(currentX);
                street.setFitWidth(width2);
                mapContainer.getChildren().add(street);
                currentX += width2;

                if (j < numberOfLanes - 1) {
                    ImageView separator = new ImageView(separatorLines);
                    separator.setLayoutX(currentX);
                    separator.setFitWidth(width3);
                    mapContainer.getChildren().add(separator);
                    currentX += width3;
                }
            }

            // Add right walking side
            ImageView rightWalk = new ImageView(walkingsideRight);
            rightWalk.setLayoutX(currentX);
            rightWalk.setFitWidth(width1);
            mapContainer.getChildren().add(rightWalk);
            currentX += width1;
        }

        totalWidth = currentX;
        return mapContainer;
    }
    private ImageView createCar(double x, double y) {
        String vehicleType;
        
        // If it's the rightmost lane, 40% chance for trucks, otherwise only cars
        if (x == RIGHTMOST_LANE_X) {
            if (random.nextDouble() < 0.4) {
                // Choose between truck types
                vehicleType = random.nextBoolean() ? "truck" : "truck2";
            } else {
                // Choose between car types
                vehicleType = "car" + (random.nextInt(4) + 1);
            }
        } else {
            // Choose between car types for other lanes
            vehicleType = "car" + (random.nextInt(4) + 1);
        }
        
        Image vehicleImage = new Image("file:src/resources/" + vehicleType + ".png");
        ImageView vehicleView = new ImageView(vehicleImage);
        
        if (vehicleType.startsWith("truck")) {
            vehicleView.setFitWidth(60);
            vehicleView.setFitHeight(130);
        } else {
            vehicleView.setFitWidth(50);
            vehicleView.setFitHeight(110);
        }
        
        vehicleView.setX(x);
        vehicleView.setY(y);
        vehicleView.setUserData(vehicleType);
        
        return vehicleView;
    }
    private void generateCars(Pane mapContainer, int numberOfCars) {
        double startingY = 320;
        double ySpacing = 130;
        
        int[] carXPositions = {180, 340, 460, 600};
        
        for (int i = 0; i < numberOfCars; i++) {
            int laneIndex = random.nextInt(carXPositions.length);
            int x = carXPositions[laneIndex];
            double y = startingY + (cars.size() * ySpacing);
            
            ImageView vehicle = createCar(x, y);
            cars.add(vehicle);
            mapContainer.getChildren().add(vehicle);
        }
    }

    private void generatePedestrians(Pane mapContainer, int numberOfPedestrians) {
        for (int i = 0; i < numberOfPedestrians; i++) {
            double x = random.nextBoolean() ? 0 : totalWidth - 145.63;
            double y = random.nextInt(100) + 350;
            
            ImageView pedestrian = createPedestrian(x, y);
            pedestrians.add(pedestrian);
            mapContainer.getChildren().add(pedestrian);
        }
    }

    private ImageView createPedestrian(double x, double y) {
        String pedestrianType = pedestrianTypes.get(random.nextInt(pedestrianTypes.size()));
        Image pedestrianImage = new Image("file:src/resources/" + pedestrianType + ".png");
        ImageView pedestrianView = new ImageView(pedestrianImage);
        pedestrianView.setFitWidth(30);
        pedestrianView.setFitHeight(50);
        pedestrianView.setX(x);
        pedestrianView.setY(y);
        pedestrianView.setUserData(pedestrianType);
        return pedestrianView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}