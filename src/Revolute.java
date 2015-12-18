import java.awt.*;

public class Revolute extends Joint {
    public Revolute(Solid anchor, Solid freeSolid, Point position, String name) {
        super(anchor, freeSolid, position, name);
    }

    public void draw(Graphics g) {
        if (m_anchor.m_isGround) {
            g.drawOval(m_position.m_x - 5, m_position.m_y - 5, 10, 10);
            g.drawLine(m_position.m_x, m_position.m_y+5, m_position.m_x, m_position.m_y+20);
            g.drawLine(m_position.m_x-10, m_position.m_y+20, m_position.m_x+10, m_position.m_y+20);
            for (int i = -15; i<=5; i=i+5) {
                g.drawLine(m_position.m_x+i, m_position.m_y+25, m_position.m_x+i+5, m_position.m_y+20);
            }
        } else {
            g.drawOval(m_position.m_x - 5, m_position.m_y - 5, 10, 10);
        }
    }
}
