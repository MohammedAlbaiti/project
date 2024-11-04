public class Bridge implements GenerRules{
    private boolean bridgeStatus;
    private double objectDimension;

    public Bridge(boolean bridgeStatus){}
    
    public boolean isBridgeStatus(){
        return this.bridgeStatus;
    }
    // you can reomve it
    public void setBridgeStatus(boolean bridgeStatus){
        this.bridgeStatus = bridgeStatus;
    }

    public void enableBridge(){
        this.bridgeStatus = true;
    }

    public void disableBridge(){
        this.bridgeStatus = false;
    }

    public double getObjectDimension(){
        return this.objectDimension
    }

    public void setObjectDimension(double objectDimension){
        this.objectDimension = objectDimension;
    }
}