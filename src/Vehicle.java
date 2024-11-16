
public class Vehicle extends MovingObjects {
    private String driverStyle;
    private Road road;

    public Vehicle(Road road, String driverStyle, double objectSpeed, double objectWidth, double objectHeight) {
        super(objectSpeed, objectWidth,objectHeight);
        this.driverStyle = driverStyle;
        this.road = road;
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
        System.out.println("Vehicle is moving at speed: " + objectSpeed);
    }

    @Override
    public void stop() {
        System.out.println("Vehicle has stopped.");
        this.objectSpeed = 0;
    }
}
