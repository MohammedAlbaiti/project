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

public class App extends Application {
    private Road road = new Road(1, 4, "normal", 5);
    private double totalWidth;
    private List<ImageView> cars = new ArrayList<>();
    private List<ImageView> pedestrians = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    // private int passedCarsCount = 0;
    // private List<String> carTypes = List.of("car", "car2", "car3", "car4");
    // private List<String> truckTypes = List.of("truck", "truck2");
    private List<String> pedestrianTypes = List.of("pedestrian", "pedestrian2", "pedestrian3", "pedestrian4", "pedestrian5");
    private static final int RIGHTMOST_LANE_X = 600;
    private Text passedCarsText;

    @Override
    public void start(Stage primaryStage) {
        // Create the map
        Pane mapContainer = road.createMap();
        totalWidth = road.getTotalWidth();
        
        // Generate initial vehicles and pedestrians
        generateVehicles(mapContainer, 15);
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
                        
                        // passedCarsCount++;
                        road.increaseNumberOfPassedCars(1);
                        passedCarsText.setText("Passed Cars: " + road.getNumberOfPassedCars());
                        
                        generateVehicles(mapContainer, 1);
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

    private ImageView createCar(double x, double y) {
        // Determine if we should create a truck (only in rightmost lane)
        if (x == RIGHTMOST_LANE_X && random.nextDouble() < 0.4) {
            Truck truck = new Truck(road,"normal");
            return truck.createTruck(x, y);
        }
        Car car = new Car(road,"normal");
        return car.createCarVehicle(x, y);
    }

    private void generateVehicles(Pane mapContainer, int numberOfVehicles) {
        double startingY = 320;
        double ySpacing = 130;
        
        int[] lanePositions = {180, 340, 460, 600};
        
        for (int i = 0; i < numberOfVehicles; i++) {
            int laneIndex = random.nextInt(lanePositions.length);
            int x = lanePositions[laneIndex];
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
            Pedestrian pedestrian = new Pedestrian(road, "normal");
            ImageView pedestrianView = pedestrian.createPedestrian(x, y);
            pedestrians.add(pedestrianView);
            mapContainer.getChildren().add(pedestrianView);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}