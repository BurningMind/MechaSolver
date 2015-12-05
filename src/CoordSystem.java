public class CoordSystem {

    public Vector m_vecX, m_vecY, m_vecZ;
    public Point m_origin;

    public CoordSystem (Vector vecX, Vector vecY, Vector vecZ, Point origin) {
        m_vecX = vecX;
        m_vecY = vecY;
        m_vecZ = vecZ;
        m_origin = origin;
    }
}
