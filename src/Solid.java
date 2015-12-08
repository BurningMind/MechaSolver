import java.awt.*;
import java.util.ArrayList;

abstract public class Solid {
    public int m_nbSolids, m_nbJoints;
    public ArrayList<Point> m_points;
    public CoordSystem m_coordSystem;

    //Constructor
    public Solid (Point origin, double angleToGround) {
        m_coordSystem = new CoordSystem (origin, angleToGround);
        m_points = new ArrayList<Point>();
    }

    abstract public void draw(Graphics g);

    //Takes another Solid and returns whether the two solids are fixed
    public boolean isFixed (Solid otherSolid) {
        return true;
    }

    //Returns the angle with respect to another Solid
    public double rotation ( Solid otherSolid) {
        double retRotation = (double) (otherSolid.m_coordSystem.m_angleToGround - m_coordSystem.m_angleToGround);
        return retRotation;
    }
}
