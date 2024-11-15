public class Car extends Vehicle{
    private String carID;
    private String carType;
    private static int counter = 1;
    public Car(Road road, String driverStyle, String carType){
        super(road,driverStyle);
        this.carID = "C" + (++counter);
        this.road=road;
        this.carType=carType;
    }

    public String getCarID(){
        return this.carID;
    }

    public String getCarType(){
        return this.carType;
    }

    public void setCarType(String carType){
        this.carType = carType;
    }



}