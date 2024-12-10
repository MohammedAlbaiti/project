import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class Car extends Vehicle {
    private String carID;
    private String carType;
    private static int counter = 1;
    private static final SecureRandom random = new SecureRandom();
    private static final ArrayList<String> carTypes = new ArrayList<>(Arrays.asList("Classic Sedan", "Sporty Red", "Midnight Black", "Crimson Racer", "Ocean Blue","Pearl White", "Sunset Orange","Steel Grey","Desert Gold","Neon Green","Royal Purple","Yellow Taxi","Police Cruiser","Charcoal Sedan"));
    private static final ArrayList<Image> carImages = new ArrayList<>(Arrays.asList(
        new Image("file:src/resources/car.png"),
        new Image("file:src/resources/car10.png"),
        new Image("file:src/resources/car2.png"),
        new Image("file:src/resources/car3.png"),
        new Image("file:src/resources/car4.png"),
        new Image("file:src/resources/car5.png"),
        new Image("file:src/resources/car6.png"),
        new Image("file:src/resources/car7.png"),
        new Image("file:src/resources/car8.png"),
        new Image("file:src/resources/car9.png"),
        new Image("file:src/resources/car11.png"),
        new Image("file:src/resources/car12.png"),
        new Image("file:src/resources/car13.png"),
        new Image("file:src/resources/car14.png")
    ));
    private Image objectImage;



    public Car(Road road, String driverStyle) {
        super(road, driverStyle, random.nextInt(4) + 2, 50, 110);
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

    public void createCarVehicle(double x, double y) {
        xCoo = x;
        yCoo = y;

        // Randomly select a car type and image
        int i = random.nextInt(carTypes.size());
        carType = carTypes.get(i);
        objectImage = carImages.get(i);

        // Create the ImageView
        vehicleView = new ImageView(objectImage);
        vehicleView.setFitWidth((int) getObjectWidth());
        vehicleView.setFitHeight((int) getObjectHeight());
        vehicleView.setX(xCoo);
        vehicleView.setY(yCoo);
        vehicleView.setUserData(carType);
    }

    @Override
    public String toString() {
        return String.format(
            "%-5s %-25s %-10s %-10.2f %-10s %-10.2f %-10.2f %-10.2f %-10s %-10s",  // Added timeTaken to the format
            carID, carType, getDriverStyle(), getObjectSpeed(), getObjectDirection(), getTimeTaken(), getIdealTime(),getTimeDifferance(),isObjectPassed(),getAccidentHappen()   // Added timeTaken here
        );
    }
    
}
