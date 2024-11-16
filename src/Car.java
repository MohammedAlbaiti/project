import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.security.SecureRandom;
import java.util.List;

public class Car extends Vehicle {
    private String carID;
    private String carType;
    private static int counter = 1;
    private static final SecureRandom random = new SecureRandom();
    private List<String> carTypes = List.of("car", "car2", "car3", "car4");
    private static Image carImage = new Image("file:src/resources/car.png");
    private static Image carImage2 = new Image("file:src/resources/car2.png");
    private static Image carImag3 = new Image("file:src/resources/car3.png");
    private static Image carImage4 = new Image("file:src/resources/car4.png");
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
        String carType = carTypes.get(random.nextInt(carTypes.size()));
        if (carType.equals("car")){
            objectImage=carImage;
        }
        else if (carType.equals("car2")){
            objectImage=carImage2;
        }
        else if (carType.equals("car3")){
            objectImage=carImag3;
        }
        else{
            objectImage=carImage4;
        }
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