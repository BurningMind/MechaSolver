import java.awt.*;

abstract public class Solid {
    public int m_nbSolids, m_nbJoints;
    public double m_height, m_length;
    public Point m_origin;

    public int m_x, m_y;

    public Solid () {

    }

    abstract public void draw(Graphics g);

    public boolean isFixed (Solid otherSolid) {

    }
}
