import java.awt.*;

abstract public class Solid {
    public int m_nbSolids, m_nbJoints;
    public double m_width, m_height;
    public Point m_origin;
    public CoordSystem m_coordSystem;

    public Solid () {

    }

    abstract public void draw(Graphics g);

    public boolean isFixed (Solid otherSolid) {
        return true;
    }
}
