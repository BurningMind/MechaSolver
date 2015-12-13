import java.awt.*;

public class Revolute extends Joint {

    //Constructor
    public Revolute (Solid s1, Solid s2, Point pS1, Point pS2, String name, double angle) {
        super(s1, s2, pS1, pS2);
        m_rotZ = new Parameter(name, angle);
    }

    public void draw(Graphics g) {
        Point absPos = getAbsolutePosition();
        if (m_s1.m_isGround) {
            g.drawOval(absPos.m_x - 5, absPos.m_y - 5, 10, 10);
            g.drawLine(absPos.m_x, absPos.m_y+5, absPos.m_x, absPos.m_y+20);
            g.drawLine(absPos.m_x-10, absPos.m_y+20, absPos.m_x+10, absPos.m_y+20);
            for (int i = -15; i<=5; i=i+5) {
                g.drawLine(absPos.m_x+i, absPos.m_y+25, absPos.m_x+i+5, absPos.m_y+20);
            }
        } else {
            g.drawOval(absPos.m_x - 5, absPos.m_y - 5, 10, 10);
        }
    }
}
