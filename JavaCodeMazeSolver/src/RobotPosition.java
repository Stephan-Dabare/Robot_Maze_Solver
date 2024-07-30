// Robot position class.
public class RobotPosition {
    private int x;
    private int y;
    private RobotPosition prePosition;

    public RobotPosition(int x, int y) { // Robot position container
        this.x = x;
        this.y = y;
    }

    // Get methods to get robot positions
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    // Get method to get the previous position.
    public RobotPosition getPrePosition() {
        return prePosition;
    }

    // Set method to get the previous position.
    public void setPrePosition(RobotPosition prePosition){
        this.prePosition = prePosition;
    }
}
