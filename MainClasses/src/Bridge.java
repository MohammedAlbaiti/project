public class Bridge implements GeneralRules {
    private boolean bridgeStatus;
    private double objectDimension;

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
    public double getObjectDimension() {
        return this.objectDimension;
    }

    @Override
    public void setObjectDimension(double objectDimension) {
        this.objectDimension = objectDimension;
    }
}
