public class Bridge implements GeneralRules {
    private boolean bridgeStatus;
    // private double objectDimension;
    private double objectWidth;
    private double objectHeight=480;
    public Bridge(boolean bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
    }

    public boolean isBridgeStatus() {
        return this.bridgeStatus;
    }

    public void setBridgeStatus(boolean bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
    }

    public void enableBridge() {
        this.bridgeStatus = true;
        
    }

    public void disableBridge() {
        this.bridgeStatus = false;
        
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

    // @Override
    // public double getObjectDimension() {
    //     return this.objectDimension;
    // }

    // @Override
    // public void setObjectDimension(double objectDimension) {
    //     this.objectDimension = objectDimension;
    // }
}
