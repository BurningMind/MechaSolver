public class CoordSystem {

    public Vector m_vecX, m_vecY, m_vecZ;
    public Point m_origin;
    public double m_angleToGround;

    public CoordSystem (Vector vecX, Vector vecY, Vector vecZ, Point origin) {
        m_vecX = vecX;
        m_vecY = vecY;
        m_vecZ = vecZ;
        m_origin = origin;
        m_angleToGround = m_vecX.m_angleToGround;
    }

    public double rotation ( CoordSystem otherCoordSystem) {
        double retRotation = (double) (otherCoordSystem.m_angleToGround - m_angleToGround);
        return retRotation;
    }
}
