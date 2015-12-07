public class CoordSystem {

    public Vector m_vecX, m_vecY, m_vecZ;
    public Point m_origin;
    public double m_angleToGround;

    //Constructor
    public CoordSystem (Point origin, double angleToGround) {
        m_vecX = new Vector ( origin, angleToGround);
        m_vecY = new Vector ( origin, angleToGround + Math.PI/2.0);
        m_origin = origin;
        m_angleToGround = m_vecX.m_angleToGround;
    }

    //Returns the angle with respect to another CoordSystem
    public double rotation ( CoordSystem otherCoordSystem) {
        double retRotation = (double) (otherCoordSystem.m_angleToGround - m_angleToGround);
        return retRotation;
    }
}
