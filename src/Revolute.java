import java.awt.*;

public class Revolute extends Joint {

    //Constructor
    public Revolute (Solid s1, Solid s2, Point pS1, Point pS2, Point pContact) {
        super(s1, s2, pS1, pS2, pContact);
        m_rotZ = true;
        m_transX = false;
        m_transY = false;
    }

    public void draw(Graphics g) {
        g.drawOval(m_pContact.m_x, m_pContact.m_y, 10, 10);
    }
}
