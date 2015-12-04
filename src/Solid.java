import java.awt.*;

abstract public class Solid {
    public boolean attachedToGround;
    public int nbSolids, nbJoints, nbParameters;

    public int m_x, m_y;

    public Solid () {

    }

    abstract public void draw(Graphics g);
}
