import java.util.List;
import java.security.SecureRandom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Truck extends Vehicle {
    private String truckID;
    private String truckType;
    private static int counter = 1;
    private static final List<String> TRUCK_TYPES = List.of("truck", "truck2");
    private static final SecureRandom random = new SecureRandom();
    
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

    public ImageView createTruck(double x, double y) {
        System.out.println("truck");
        Image truckImage = new Image("file:src/resources/" + truckType + ".png");
        ImageView truckView = new ImageView(truckImage);
        
        truckView.setFitWidth((int)getObjectWidth());
        truckView.setFitHeight((int)getObjectHeight());
        truckView.setX(x);
        truckView.setY(y);
        truckView.setUserData(this.truckType);
        
        return truckView;
    }

    @Override
    public void move() {
        System.out.println("Truck " + truckID + " is moving at speed: " + getObjectSpeed());
    }
}
