public class Pedestrian extends MovingObjects {
    // Attributes
    private int pedestrianID;
    private boolean crossingStatus;
    private String pedestrianStyle;
    
    // Constructor
    public Pedestrian(Road road, String pedestrianStyle) {
        this.pedestrianStyle = pedestrianStyle;
        this.crossingStatus = false; // Default to not crossing
        this.objectSpeed = 0.0;      // Default speed
        this.objectDimension = 0.0;  // Default dimension
    }
    
    // Getters and Setters
    public int getPedestrianID() {
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
