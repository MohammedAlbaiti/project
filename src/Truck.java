import java.util.ArrayList;
import java.util.Arrays;
import java.security.SecureRandom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Truck extends Vehicle {
    private String truckID;
    private String truckType;
    private static int counter = 1;
    private static final ArrayList<String> TRUCK_TYPES = new ArrayList<>(Arrays.asList("truck", "truck2"));

    private static final SecureRandom random = new SecureRandom();
    // private ImageView truckView;
    // private static final double DEFAULT_TRUCK_SPEED = 2;
    // private static final double DEFAULT_TRUCK_DIMENSION = 130.0;

    public Truck(Road road, String driverStyle) {
        super(road, driverStyle, 2, 60,130);
        // System.out.println("truck");
        this.truckID = "T" + (counter++);
        this.truckType = TRUCK_TYPES.get(random.nextInt(TRUCK_TYPES.size()));
    }

    public String getTruckID() {
        return this.truckID;
    }

    public String getTruckType() {
        return this.truckType;
    }

    public void setTruckType(String truckType) {
        if (TRUCK_TYPES.contains(truckType)) {
            this.truckType = truckType;
        } else {
            throw new IllegalArgumentException("Invalid truck type");
        }
    }

    public void createTruck(double x, double y) {
        xCoo=x;
        yCoo=y;
        // System.out.println("truck");
        Image truckImage = new Image("file:src/resources/" + truckType + ".png");
        vehicleView = new ImageView(truckImage);
        
        vehicleView.setFitWidth((int)getObjectWidth());
        vehicleView.setFitHeight((int)getObjectHeight());
        vehicleView.setX(xCoo);
        vehicleView.setY(yCoo);
        vehicleView.setUserData(this.truckType);
        
    }
    // public ImageView getTruckView(){
    //     return truckView;
    // }
    // @Override
    // public void move() {
    //     System.out.println("Truck " + truckID + " is moving at speed: " + getObjectSpeed());
    // }
}
