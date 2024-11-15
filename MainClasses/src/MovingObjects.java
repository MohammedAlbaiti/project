public abstract class MovingObjects implements GeneralRules {
    protected double objectSpeed;
    protected double objectDimension;

    public abstract double getObjectSpeed();
    public abstract void setObjectSpeed(double objectSpeed);
    public abstract void move();
    public abstract void stop();

    @Override
    public abstract double getObjectDimension();

    @Override
    public abstract void setObjectDimension(double objectDimension);
}
