public class Car extends Vehicle{
    private int carID;
    private String carType;

    public Car(road:Road, driverStyle:String, carType:String){

    }

    public int getCarID(){
        return this.carID;
    }

    public String getCarType(){
        return this.carType;
    }

    public void setCarType(String carType){
        this.carType = carType;
    }



}