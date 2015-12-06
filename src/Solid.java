import java.awt.*;

abstract public class Solid {
    public int m_nbSolids, m_nbJoints;
    public double m_width, m_height, m_angleToGround;
    public Point m_origin;
    public CoordSystem m_coordSystem;

    public Solid (Point origin, double angleToGround) {
        m_origin = origin;
        m_coordSystem.m_angleToGround = m_angleToGround;
    }

    abstract public void draw(Graphics g);

    public boolean isFixed (Solid otherSolid) {
        return true;
    }

    public double rotation ( CoordSystem otherCoordSystem) {
        double retRotation = (double) (otherCoordSystem.m_vecX.m_angleToGround - m_vecX.m_angleToGround);
        return retRotation;
    }
}
