import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Road implements GeneralRules {
    private int numberOfPassedCars = 0;
    private int numberOfPassedPedestrians;
    private int numberOfAccidents;
    private String trafficState;
    private int numberOfLanes;
    private double accidentDelay;
    // private double objectDimension;
    private int numberOfRoads;
    private double totalWidth;
    private double objectWidth;
    private double objectHeight;

    public Road(int numberOfRoads,int numberOfLanes, String trafficState, double accidentDelay) {
        this.numberOfLanes = numberOfLanes;
        this.trafficState = trafficState;
        this.accidentDelay = accidentDelay;
        this.numberOfRoads = numberOfRoads;
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
    public Pane createMap() {
        // double totalWidth = 0;
        Pane mapContainer = new Pane();
        mapContainer.setStyle("-fx-padding: 0; -fx-background-color: #232329;");
        
        Image walkingsideLeft = new Image("file:src/resources/walkingsideLeft.png");
        Image movingStreet = new Image("file:src/resources/moving_street.png");
        Image separatorLines = new Image("file:src/resources/speratorLines.png");
        Image walkingsideRight = new Image("file:src/resources/walkingsideRight.png");

        double width1 = 146;
        double width2 = 113;
        double width3 = 26;

        int numberOfRoads = this.numberOfRoads;
        int numberOfLanes = this.numberOfLanes;
        double currentX = 0;

        for (int i = numberOfRoads; i > 0; i--) {
            // Add left walking side
            ImageView leftWalk = new ImageView(walkingsideLeft);
            leftWalk.setLayoutX(currentX);
            leftWalk.setFitWidth(width1);
            mapContainer.getChildren().add(leftWalk);
            currentX += width1;

            // Add lanes
            for (int j = 0; j < numberOfLanes; j++) {
                ImageView street = new ImageView(movingStreet);
                street.setLayoutX(currentX);
                street.setFitWidth(width2);
                mapContainer.getChildren().add(street);
                currentX += width2;

                if (j < numberOfLanes - 1) {
                    ImageView separator = new ImageView(separatorLines);
                    separator.setLayoutX(currentX);
                    separator.setFitWidth(width3);
                    mapContainer.getChildren().add(separator);
                    currentX += width3;
                }
            }

            // Add right walking side
            ImageView rightWalk = new ImageView(walkingsideRight);
            rightWalk.setLayoutX(currentX);
            rightWalk.setFitWidth(width1);
            mapContainer.getChildren().add(rightWalk);
            currentX += width1;
        }
        this.totalWidth=currentX;
        return mapContainer;
    }
    public double getTotalWidth(){
        // System.out.println(totalWidth);
        return totalWidth;
    }
}