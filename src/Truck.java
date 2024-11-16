public class Truck extends Vehicle{
    private String truckID;
    private String truckType;
    private static int counter=1;
    public Truck(Road road, String driverStyle, String truckType){
        super(road,driverStyle);
        this.truckID = "T" + (++counter);
        this.truckType=truckType;
    }

    public String getTruckID(){
        return this.truckID;
    }

    public String getTruckType(){
        return this.truckType;
    }

    public void setTruckType(String truckType){
        this.truckType = truckType;
    }



}