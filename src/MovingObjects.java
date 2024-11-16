public abstract class MovingObjects implements GeneralRules {
    protected double objectSpeed;
    protected double objectWidth;
    protected double objectHeight;

    public MovingObjects(double objectSpeed, double objectWidth, double objectHeight) {
        this.objectSpeed = objectSpeed;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;
    }

    // Required abstract methods were missing concrete implementations
    public abstract double getObjectSpeed();
    public abstract void setObjectSpeed(double objectSpeed);
    public abstract void move();
    public abstract void stop();
    public abstract void setObjectHeight(double objectHeight);
    public abstract void setObjectWidth(double objectWidth);
    public abstract double getObjectWidth();
    public abstract double getObjectHeight();
}