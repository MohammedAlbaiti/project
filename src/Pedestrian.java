import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pedestrian extends MovingObjects {
    // Attributes
    private String pedestrianID;
    private boolean crossingStatus;
    private String pedestrianStyle;
    private static int counter=1;
    private static final SecureRandom random = new SecureRandom();
    private final List<String> pedestrianTypes = new ArrayList<>(List.of("pedestrian", "pedestrian2", "pedestrian3", "pedestrian4", "pedestrian5"));
    private ImageView pedestrianView;

    private Road road;
    // Constructor
    public Pedestrian(Road road, String pedestrianStyle) {
        super(random.nextInt(3) + 1,30,50);
        // System.out.println("pedestrian");
        this.road=road;
        this.pedestrianID = "P" + (++counter);
        this.pedestrianStyle = pedestrianStyle;
        this.crossingStatus = false; // Default to not crossing
        
    }
    
    // Getters and Setters
    public String getPedestrianID() {
        return pedestrianID;
    }
    
    public boolean getCrossingStatus() {
        return crossingStatus;
    }
    
    public void setCrossingStatus(boolean crossingStatus) {
        this.crossingStatus = crossingStatus;
    }
    
    public String getPedestrianStyle() {
        return pedestrianStyle;
    }
    
    public void setPedestrianStyle(String pedestrianStyle) {
        this.pedestrianStyle = pedestrianStyle;
    }

    @Override
    public void move() {
        setXCOO(getXCOO()+getObjectSpeed());
        pedestrianView.setX(getXCOO());
    }

    @Override
    public void stop() {
        pedestrianView.setX(getXCOO());
    }
    @Override
    public void setObjectHeight(double objectHeight){
        this.objectHeight=objectHeight;
    }
    @Override
    public  void setObjectWidth(double objectWidth){
        this.objectWidth=objectWidth;
    }
    @Override
    public double getObjectWidth(){
        return objectWidth;
    }
    @Override
    public double getObjectHeight(){
        return objectHeight;
    }
    @Override
    public double getObjectSpeed() {
        return this.objectSpeed;
    }

    @Override
    public void setObjectSpeed(double objectSpeed) {
        this.objectSpeed = objectSpeed;
    }

        public void createPedestrian() {
        // xCoo = random.nextBoolean() ? 0 : road.getTotalWidth() - 145.63;
        xCoo = 0;
        yCoo = random.nextInt(400);
        // System.out.println("pedestrian");
        String pedestrianType = pedestrianTypes.get(random.nextInt(pedestrianTypes.size()));
        Image pedestrianImage = new Image("file:src/resources/" + pedestrianType + ".png");
        pedestrianView = new ImageView(pedestrianImage);
        pedestrianView.setFitWidth((int)getObjectWidth());
        pedestrianView.setFitHeight((int)getObjectHeight());
        pedestrianView.setX(xCoo);
        pedestrianView.setY(yCoo);
        pedestrianView.setUserData(pedestrianType);
        
    }
    public ImageView getImageView(){
        return pedestrianView;
    }
}
