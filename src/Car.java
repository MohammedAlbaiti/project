import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.List;

public class Car extends Vehicle {
    private String carID;
    private String carType;
    private static int counter = 1;
    private static final SecureRandom random = new SecureRandom();
    private ArrayList<String> carTypes = new ArrayList<>(Arrays.asList("car", "car2", "car3", "car4", "car5","car6", "car7","car8","car9","car10","car11","car12","car13","car14"));
    private static ArrayList<Image> carImages = new ArrayList<>(Arrays.asList(new Image("file:src/resources/car.png"),new Image("file:src/resources/car10.png"),new Image("file:src/resources/car2.png"),new Image("file:src/resources/car3.png"),new Image("file:src/resources/car4.png"),new Image("file:src/resources/car5.png"),new Image("file:src/resources/car6.png"),new Image("file:src/resources/car7.png"),new Image("file:src/resources/car8.png"),new Image("file:src/resources/car9.png"),new Image("file:src/resources/car11.png"),new Image("file:src/resources/car12.png"),new Image("file:src/resources/car13.png"),new Image("file:src/resources/car14.png")));
    private Image objectImage;



    // private ImageView carView;

    public Car(Road road, String driverStyle) {
        super(road, driverStyle, random.nextInt(4) + 2, 50,110);
        // System.out.println("car");
        this.carID = "C" + (counter++);
    }

    public String getCarID() {
        return this.carID;
    }

    public String getCarType() {
        return this.carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    // @Override
    // public void move() {
    //     System.out.println("Car " + carID + " is moving at speed: " + getObjectSpeed());
    // }
    
    public void createCarVehicle(double x, double y) {
        xCoo=x;
        yCoo=y;
        // System.out.println("car");
        int i = random.nextInt(carTypes.size());
        String carType = carTypes.get(i);
        objectImage = carImages.get(i);
        // Image carImage = new Image("file:src/resources/" + carType + ".png");
        vehicleView = new ImageView(objectImage);
        
        vehicleView.setFitWidth((int)getObjectWidth());
        vehicleView.setFitHeight((int)getObjectHeight());
        vehicleView.setX(xCoo);
        vehicleView.setY(yCoo);
        vehicleView.setUserData(carType);
        
        
    // }
    // public ImageView getCarView() {
    //     return carView;
    // }
}}