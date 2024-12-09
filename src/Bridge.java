import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bridge implements GeneralRules {
    private boolean bridgeStatus;
    // private double objectDimension;
    private double objectWidth;
    private double objectHeight=480;
    private final ImageView bridgeImageView = new ImageView(new Image("file:src/resources/bridge.png"));
    private final double[] YBoundary = new double[2];
    // private final double[] topLeftCoo = new double[2];
    // private final double[] topRightCoo = new double[2];
    // private final double[] bottomLeftCoo = new double[2];
    // private final double[] bottomRightCoo = new double[2];
    public Bridge(boolean bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
    }
    public ImageView getBridgeImageView(){
        return bridgeImageView;
    }
    public boolean isBridgeStatus() {
        return this.bridgeStatus;
    }
    public void resizeBridge(double roadWidth){
        bridgeImageView.setFitWidth(roadWidth-186);
        bridgeImageView.setFitHeight(150);
        setObjectWidth(roadWidth-186);
        setObjectHeight(150);
    }
    public void setBridgeCoordinates(){
        bridgeImageView.setX(93);
        bridgeImageView.setY(80);
        calculateYBoundries();
    }
    public void placeBridgeInMap(Pane mapContainer){
        mapContainer.getChildren().add(bridgeImageView);
    }
    public void setBridgeStatus(boolean bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
    }
    // public void calculateBridgeBoundries(){
    //     ImageView bridgeView = getBridgeImageView();
    //     topLeftCoo[0] = bridgeView.getX();
    //     topLeftCoo[1] = bridgeView.getY();
    //     topRightCoo[0] = topLeftCoo[0]+getObjectWidth();
    //     topRightCoo[1] = topLeftCoo[1];
    //     bottomLeftCoo[0] = bridgeView.getX();
    //     bottomLeftCoo[1] = topLeftCoo[1] + getObjectHeight();
    //     bottomRightCoo[0] = topRightCoo[0];
    //     bottomRightCoo[1] = bottomLeftCoo[1];
    // }
    public double[] getYBoundary() {
        return YBoundary;
    }
    private void calculateYBoundries(){
        YBoundary[0] = getBridgeImageView().getY();
        YBoundary[1] = YBoundary[0] + getObjectHeight();
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
}
