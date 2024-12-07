import java.util.ArrayList;
import java.util.Arrays;
import java.security.SecureRandom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Truck extends Vehicle {
    private String truckID;
    private String truckType;
    private static int counter = 1;
private static final ArrayList<String> TRUCK_TYPES = new ArrayList<>(Arrays.asList(
    "Classic Cargo", "Blue Transport", "Emergency Rescue", "Red Delivery", "Green Hauler", "Sky Blue Transport"
));
    private static final ArrayList<Image> truckImages = new ArrayList<>(Arrays.asList(new Image("file:src/resources/truck.png"),new Image("file:src/resources/truck2.png"),new Image("file:src/resources/truck3.png"),new Image("file:src/resources/truck4.png"),new Image("file:src/resources/truck5.png"),new Image("file:src/resources/truck6.png")));
    private static final SecureRandom random = new SecureRandom();

    public Truck(Road road, String driverStyle) {
        super(road, driverStyle, 2, 60,130);
        this.truckID = "T" + (counter++);
        
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
        int i = random.nextInt(TRUCK_TYPES.size());
        this.truckType = TRUCK_TYPES.get(i);
        Image truckImage = truckImages.get(i);
        vehicleView = new ImageView(truckImage);
        
        vehicleView.setFitWidth((int)getObjectWidth());
        vehicleView.setFitHeight((int)getObjectHeight());
        vehicleView.setX(xCoo);
        vehicleView.setY(yCoo);
        vehicleView.setUserData(this.truckType);
        
    }

    @Override
    public String toString() {
        return String.format(
            "%-5s %-25s %-10s %-10.2f %-10s %-10.2f %-10.2f %-10s",  // Added timeTaken to the format
            truckID, 
            truckType,
            getDriverStyle(), 
            getObjectSpeed(), 
            getObjectDirection(),
            getTimeTaken(),
            getIdealTime(),
            isObjectPassed()
        );
    }

}
