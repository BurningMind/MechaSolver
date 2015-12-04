import java.awt.*;

abstract public class Solid {
    public int nbSolids, nbJoints;
    public double height, length;
    public Point origin;

    public int m_x, m_y;

    public Solid () {

    }

    abstract public void draw(Graphics g);

    public boolean isFixed (Solid otherSolid) {

    }
}
