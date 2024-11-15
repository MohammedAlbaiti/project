public class Pedestrian extends MovingObjects {
    // Attributes
    private String pedestrianID;
    private boolean crossingStatus;
    private String pedestrianStyle;
    private static int counter=1;
    Road road;
    // Constructor
    public Pedestrian(Road road, String pedestrianStyle) {
        this.road=road;
        this.pedestrianID = "P" + (++counter);
        this.pedestrianStyle = pedestrianStyle;
        this.crossingStatus = false; // Default to not crossing
        this.objectSpeed = 0.0;      // Default speed
        this.objectDimension = 0.0;  // Default dimension
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

    public void move() {
        System.out.println("Pedestrian is moving.");

    }

    public void stop() {
        System.out.println("Pedestrian has stopped.");

    }
}
