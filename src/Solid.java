import java.awt.*;

abstract public class Solid {
    public int m_nbSolids, m_nbJoints;
    public double m_width, m_height, m_angleToGround;
    public Point m_origin;
    public CoordSystem m_coordSystem;

    //Constructor
    public Solid (Point origin, double angleToGround) {
        m_origin = origin;
        //m_coordSystem.m_angleToGround = m_angleToGround; FIXME: causes NullPtrExcept because m_coordSystem is not initialized.
    }

    abstract public void draw(Graphics g);

    //Takes another Solid and returns whether the two solids are fixed
    public boolean isFixed (Solid otherSolid) {
        return true;
    }

    //Returns the angle with respect to another Solid
    public double rotation ( Solid otherSolid) {
        double retRotation = (double) (otherSolid.m_angleToGround - m_angleToGround);
        return retRotation;
    }
}
