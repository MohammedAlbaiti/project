public class Truck extends Vehicle{
    private int truckID;
    private String truckType;

    public Truck(road:Road, driverStyle:String, truckType:String){

    }

    public int getTruckID(){
        return this.truckID;
    }

    public String getTruckType(){
        return this.truckType;
    }

    public void setTruckType(String truckType){
        this.truckType = truckType;
    }



}