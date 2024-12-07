public abstract class MovingObjects implements GeneralRules {
    protected double objectSpeed;
    protected double objectWidth;
    protected double objectHeight;
    protected double xCoo;
    protected double yCoo;
    protected String direciton;
    private boolean accidentHappen = false;
    private boolean isPassed = false;
    
    public MovingObjects(double objectSpeed, double objectWidth, double objectHeight) {
        this.objectSpeed = objectSpeed;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;
    }

    public abstract double getObjectSpeed();
    public abstract void setObjectSpeed(double objectSpeed);
    public abstract void move();
    public abstract void stop();
    public abstract void setObjectHeight(double objectHeight);
    public abstract void setObjectWidth(double objectWidth);
    public abstract double getObjectWidth();
    public abstract double getObjectHeight();
    public void setAccidentHappen(boolean accidentHappen){
        this.accidentHappen=accidentHappen;
    }
    public boolean getAccidentHappen(){
        return accidentHappen;
    }
    public double getXCOO(){
        return xCoo;
    }
    public void increaseXCOO(double increment){
        xCoo+=increment;
    }
    public void setXCOO(double xCoo){
        this.xCoo=xCoo;
    }
    public double getYCOO(){
        return yCoo;
    }
    public void increaseYCOO(double increment){
        yCoo+=increment;
    }
    public void setYCOO(double yCoo){
        this.yCoo=yCoo;
    }
    public void setObjectDireciton(String direction){
        this.direciton = direction;
    }
    public String getObjectDirection(){
        return this.direciton;
    }
    public void setObjectPassed(){
        isPassed=true;
    }
    public boolean isObjectPassed(){
        return isPassed;
    }
    
}