public class Vehicle extends MovingObjects {
    private String driverStyle;
    private Road road;

    public Vehicle(Road road, String driverStyle) {
        this.driverStyle = driverStyle;
        this.road = road;
    }

    public String getDriverStyle() {
        return this.driverStyle;
    }

    public void setDriverStyle(String driverStyle) {
        this.driverStyle = driverStyle;
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
    public double getObjectDimension() {
        return this.objectDimension;
    }

    @Override
    public void setObjectDimension(double objectDimension) {
        this.objectDimension = objectDimension;
    }

    @Override
    public void move() {
        System.out.println("Vehicle is moving.");
    }

    @Override
    public void stop() {
        System.out.println("Vehicle has stopped.");
    }
}
