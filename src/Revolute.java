import java.awt.*;

public class Revolute extends Joint {

    //Constructor
    public Revolute (Solid s1, Solid s2, Point pS1, Point pS2, String name, double angle) {
        super(s1, s2, pS1, pS2);
        m_rotZ = new Parameter(name, angle);
    }

    public void draw(Graphics g) {
        Point absPos = getAbsolutePosition();
        g.drawOval(absPos.m_x - 5, absPos.m_y - 5, 10, 10);
    }
}
