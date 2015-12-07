public class Vector {

    public Point m_origin;
    public double m_angleToGround;

    //Constructor
    public Vector (Point origin, double angleToGround) {
        m_origin = origin;
        m_angleToGround = angleToGround;
    }

    //Returns the angle with respect to another Vector
    public double angle ( Vector otherVector) {
        double retRotation = (double) (otherVector.m_angleToGround - m_angleToGround);
        return retRotation;
    }
}
