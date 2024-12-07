import javafx.scene.image.ImageView;

public class Vehicle extends MovingObjects {
    private String driverStyle;
    private Road road;
    // New attribute to track time
    private long startTime;
    private long endTime;
    private long timeTaken;
    private double idealTime;
    private double timeDifferance=0;
    protected  ImageView vehicleView;
    public Vehicle(Road road, String driverStyle, double objectSpeed, double objectWidth, double objectHeight) {
        super(objectSpeed, objectWidth,objectHeight);
        this.driverStyle = driverStyle;
        this.road = road;
        startTimer();  // Start the timer when the object is created
        caluclateIdealTime();
    }
    protected double getIdealTime(){
        return this.idealTime;
    }
    private void caluclateIdealTime(){
        double d =road.getObjectHeight();
        double speed = getObjectSpeed()*42;
        this.idealTime = Math.round((d / speed) * 100.0) / 100.0;
    }
    public double getTimeDifferance() {
        return timeDifferance;
    }
    // Method to start the timer
    private void startTimer() {
        this.startTime = System.currentTimeMillis();  // Capture the start time in nanoseconds
    }
    public void caluclateTimeDifferance(){
        timeDifferance = getTimeTaken() - getIdealTime();
    }
    // Method to stop the timer and calculate the elapsed time
    public void stopTimer() {
        this.endTime = System.currentTimeMillis();  // Capture the end time
        this.timeTaken = endTime - startTime;  // Calculate the time taken in nanoseconds
        caluclateTimeDifferance();
    }

    // Getter for timeTaken
    public double getTimeTaken() {
        return this.timeTaken/1000.0;  // Return the time taken in nanoseconds
    }
    public String getDriverStyle() {
        return this.driverStyle;
    }

    public void setDriverStyle(String driverStyle) {
        this.driverStyle = driverStyle;
    }

    public Road getRoad() {
        return this.road;
    }

    @Override
    public double getObjectSpeed() {
        return this.objectSpeed;
    }

    @Override
    public void setObjectSpeed(double objectSpeed) {
        this.objectSpeed = objectSpeed;
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
    public void move() {
        if(!getAccidentHappen()){

      
        if(getObjectDirection().equals("up")){
            setYCOO(getYCOO()-getObjectSpeed());
            vehicleView.setY(getYCOO());
        }
        else{
            setYCOO(getYCOO()+getObjectSpeed());
            vehicleView.setY(getYCOO());
        }

    }
    }
    @Override
    public void stop() {
        vehicleView.setY(getYCOO());
    }

    public ImageView getVehicleView() {
        return vehicleView;
    }
    
    @Override
    public String toString() {
        return String.format(
            "%-15s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10s",
            driverStyle, 
            getObjectSpeed(), 
            getObjectWidth(), 
            getObjectHeight(), 
            getXCOO(), 
            getYCOO(), 
            getObjectDirection()
        );
    }

}
