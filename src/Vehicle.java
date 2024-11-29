import javafx.scene.image.ImageView;

public class Vehicle extends MovingObjects {
    private String driverStyle;
    private Road road;
    protected  ImageView vehicleView;
    
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
        if(getObjectDirection().equals("up")){
            setYCOO(getYCOO()-getObjectSpeed());
            vehicleView.setY(getYCOO());
        }
        else{
            setYCOO(getYCOO()+getObjectSpeed());
            vehicleView.setY(getYCOO());
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
            "Vehicle{DriverStyle='%s', Speed=%.2f, Width=%.2f, Height=%.2f, X=%.2f, Y=%.2f, Direction='%s'}",
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
