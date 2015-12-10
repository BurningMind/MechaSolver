import java.awt.*;

public class Prismatic extends Joint {

    //Constructor
    public Prismatic (Solid s1, Solid s2, Point pS1, Point pS2, Point pContact, String name, double translation) {
        super(s1, s2, pS1, pS2, pContact);
        m_transX = new Parameter(name, translation);
    }

    public void draw(Graphics g) {
        g.drawRect(m_pContact.m_x - 5, m_pContact.m_y - 5, 10, 10);
    }
}
