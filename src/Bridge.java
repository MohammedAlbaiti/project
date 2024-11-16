public class Bridge implements GeneralRules {
    private boolean bridgeStatus;
    // private double objectDimension;
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

    // TODO: either add these methods in the parent class or just ignore them and continue with the added methods below
    // @Override
    // public double getObjectDimension() {
    //     return this.objectDimension;
    // }

    // @Override
    // public void setObjectDimension(double objectDimension) {
    //     this.objectDimension = objectDimension;
    // }

    @Override
    public double getObjectWidth() {
        throw new UnsupportedOperationException("Unimplemented method 'getObjectWidth'");
    }

    @Override
    public double getObjectHeight() {
        throw new UnsupportedOperationException("Unimplemented method 'getObjectHeight'");
    }

    @Override
    public void setObjectHeight(double objectHeight) {
        throw new UnsupportedOperationException("Unimplemented method 'setObjectHeight'");
    }

    @Override
    public void setObjectWidth(double objectWidth) {
        throw new UnsupportedOperationException("Unimplemented method 'setObjectWidth'");
    }
}
