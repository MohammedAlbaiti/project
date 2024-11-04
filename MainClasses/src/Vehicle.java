public class Vehicle extends MovingObjects{
    private String driverStyle;

    public Vehicle(Road road, String driverStyle){
        this.driverStyle = driverStyle
    }

    public String getDriverStyle(){
        return this.driverStyle;
    }

    public void setDriverStyle(String driverStyle){
        this.driverStyle = driverStyle;
    }

    public double getObjectSpeed(){
        return super.objectSpeed;
    }

    public void setObjectSpeed(double objectSpeed){
        super.objectSpeed = objectSpeed;
    }

    public double getObjectDimension(){
        return super.objectDimension;
    }

    public void setObjectDimension(double objectDimension){
        super.objectDimension = objectDimension;
    }

    public void move(){}

    public void stop(){}

}