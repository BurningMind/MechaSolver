public class CoordSystem {

    public Parameter m_transX, m_transY, m_rotZ;
    public Point m_origin;
    public Solid m_reference;

    //Constructor
    public CoordSystem (Solid reference, Point origin, Parameter transX, Parameter transY, Parameter rotZ) {
        m_reference = reference;
        m_origin = origin;
        m_transX = transX;
        m_transY = transY;
        m_rotZ = rotZ;
    }
}
