public class Vector {

    public Point m_origin;
    public double m_angleToGround;

    public Vector (Point origin, double angleToGround) {
        m_origin = origin;
        m_angleToGround = angleToGround;

    }

    public double angle ( Vector otherVector) {
        double retRotation = (double) (otherVector.m_angleToGround - m_angleToGround);
        return retRotation;
    }
}
