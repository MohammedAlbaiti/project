import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pedestrian extends MovingObjects {
    // Attributes
    private String pedestrianID;
    private boolean crossingStatus;
    private String pedestrianStyle;
    private static int counter=1;
    String pedestrianType;
    private long startTime;
    private long endTime;
    private long timeTaken;
    private double idealTime;
    private double timeDifferance=0;
    private double distance = 0;
    private static final SecureRandom random = new SecureRandom();
private final ArrayList<String> pedestrianTypes = new ArrayList<>(Arrays.asList(
    "Dark Dressed Walker", "Purple Shirted Woman", "Casual Dressed Person", "Pink Shirt Walker", "Light Grey Dressed Walker"
));
    private ImageView pedestrianView;

    private Road road;
    // Constructor
    public Pedestrian(Road road, String pedestrianStyle) {
        super(random.nextInt(3) + 1,30,50);
        setObjectDireciton("right");
        this.road=road;
        this.pedestrianID = "P" + (++counter);
        this.pedestrianStyle = pedestrianStyle;
        this.crossingStatus = false; // Default to not crossing
        startTimer();
        
    }
    public void caluclateTimeDifferance(){
        timeDifferance = getTimeTaken() - getIdealTime();
    }
    public double getTimeDifferance(){
        return timeDifferance;
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
    public void moveUp(){
            distance+=getObjectSpeed();
            pedestrianView.setRotate(270);
            setYCOO(getYCOO()-getObjectSpeed());
            pedestrianView.setY(getYCOO());
    }
    public void moveDown(){
            distance+=getObjectSpeed();
            pedestrianView.setRotate(90);
            setYCOO(getYCOO()+getObjectSpeed());
            pedestrianView.setY(getYCOO());
    }
    @Override
    public void move() {
        if(!getAccidentHappen()){
            distance+=getObjectSpeed();
        if(getObjectDirection().equals("right")){

            setXCOO(getXCOO()+getObjectSpeed());
            pedestrianView.setRotate(0);
            pedestrianView.setX(getXCOO());
        }
        else{
            setXCOO(getXCOO()-getObjectSpeed());
            pedestrianView.setRotate(180);
            pedestrianView.setX(getXCOO());
        }
    }
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

    public void createPedestrian(boolean isBridgeStatus) {
        if(isBridgeStatus){
            if(getObjectDirection().equals("right")){
                xCoo = -30;
            }
            else{
                xCoo = road.getObjectWidth();
            }
            if(getPedestrianStyle().equals("carless")){
                yCoo = random.nextInt(100)+300;
            }
            else{
                yCoo = random.nextInt(400);
            }
        }
        else{
            if(getObjectDirection().equals("right")){
                xCoo = -30 ;
            }
            else{
                xCoo = road.getObjectWidth();
            }
            yCoo = random.nextInt(400);
        }

        
        
        pedestrianType = pedestrianTypes.get(random.nextInt(pedestrianTypes.size()));
        Image pedestrianImage = new Image("file:src/resources/" + pedestrianType + ".png");
        pedestrianView = new ImageView(pedestrianImage);
        pedestrianView.setFitWidth(getObjectWidth());
        pedestrianView.setFitHeight(getObjectHeight());
        pedestrianView.setX(xCoo);
        pedestrianView.setY(yCoo);
        pedestrianView.setUserData(pedestrianType);
    }
    public ImageView getImageView(){
        return pedestrianView;
    }
    protected double getIdealTime(){
        return this.idealTime;
    }
    private void caluclateIdealTime(){
        double speed = getObjectSpeed()*68;
        this.idealTime = Math.round((distance / speed) * 100.0) / 100.0;
    }
    // Method to start the timer
    private void startTimer() {
        this.startTime = System.currentTimeMillis();  // Capture the start time in nanoseconds
    }

    // Method to stop the timer and calculate the elapsed time
    public void stopTimer() {
        this.endTime = System.currentTimeMillis();  // Capture the end time
        this.timeTaken = endTime - startTime;  // Calculate the time taken in nanoseconds
        caluclateIdealTime();
        caluclateTimeDifferance();
    }

    // Getter for timeTaken
    public double getTimeTaken() {
        return Math.round(this.timeTaken/10) / 100.0;  // Return the time taken in nanoseconds
    }
    public String getPedestrianType() {
        return pedestrianType;
    }
    @Override
    public String toString() {
        return String.format(
            "%-5s %-25s %-10s %-10.2f %-10s %-10.2f %-10.2f %-10s",  // Added timeTaken to the format
            pedestrianID, pedestrianType, pedestrianStyle, getObjectSpeed(), getObjectDirection(), getTimeTaken(), getIdealTime(),isObjectPassed()
        );
    }
}
