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

    public Car(Road road, String driverStyle) {
        super(road, driverStyle, 4, 50,110);
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

    @Override
    public void move() {
        System.out.println("Car " + carID + " is moving at speed: " + getObjectSpeed());
    }
    
    public ImageView createCarVehicle(double x, double y) {
        System.out.println("car");
        String carType = carTypes.get(random.nextInt(carTypes.size()));
        Image carImage = new Image("file:src/resources/" + carType + ".png");
        ImageView carView = new ImageView(carImage);
        
        carView.setFitWidth((int)getObjectWidth());
        carView.setFitHeight((int)getObjectHeight());
        carView.setX(x);
        carView.setY(y);
        carView.setUserData(carType);
        
        return carView;
    }
}