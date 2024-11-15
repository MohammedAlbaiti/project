class Road implements GeneralRules {
    private int numberOfPassedCars;
    private int numberOfPassedPedestrians;
    private int numberOfAccidents;
    private String trafficState;
    private int numberOfLanes;
    private double accidentDelay;
    private double objectDimension;

    public Road(int numberOfLanes, String trafficState, double accidentDelay) {
        this.numberOfLanes = numberOfLanes;
        this.trafficState = trafficState;
        this.accidentDelay = accidentDelay;
    }

    public int getNumberOfPassedCars() {
        return numberOfPassedCars;
    }

    public void increaseNumberOfPassedCars(int increment) {
        numberOfPassedCars += increment;
    }

    public int getNumberOfPassedPedestrians() {
        return numberOfPassedPedestrians;
    }

    public void increaseNumberOfPassedPedestrians(int increment) {
        numberOfPassedPedestrians += increment;
    }

    public int getNumberOfAccidents() {
        return numberOfAccidents;
    }

    public void increaseNumberOfAccidents(int increment) {
        numberOfAccidents += increment;
    }

    public String getTrafficState() {
        return trafficState;
    }

    public void setTrafficState(String trafficState) {
        this.trafficState = trafficState;
    }

    public double getAccidentDelay() {
        return accidentDelay;
    }

    public void setAccidentDelay(double accidentDelay) {
        this.accidentDelay = accidentDelay;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public void setNumberOfLanes(int numberOfLanes) {
        this.numberOfLanes = numberOfLanes;
    }

    public void addBridge() {
        
    }

    public void updateTrafficState(String trafficState) {
        this.trafficState = trafficState;
    }

    public double getObjectDimension() {
        return objectDimension;
    }

    public void setObjectDimension(double objectDimension) {
        this.objectDimension = objectDimension;
    }
}