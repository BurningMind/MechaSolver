import java.awt.*;

public class Prismatic extends Joint {

    //Constructor
    public Prismatic (Solid s1, Solid s2, Point pS1, Point pS2, String name, double translation) {
        super(s1, s2, pS1, pS2);
        m_transX = new Parameter(name, translation);
    }

    public void draw(Graphics g) {
        Point absPos = getAbsolutePosition();
        g.drawRect(absPos.m_x - 5, absPos.m_y - 5, 10, 10);
    }
}
