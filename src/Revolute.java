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

        if (m_anchor != null && m_freeSolid != null) {
            double angle = 0.0;
            if (m_anchor.m_isGround) {
                angle = m_freeSolid.m_angle - m_anchor.m_angle;
                g.drawArc(m_position.m_x - 40, m_position.m_y - 40, 80, 80, (int)Math.toDegrees(m_anchor.m_angle), (int)Math.toDegrees(angle));
            } else {
                angle = m_freeSolid.m_angle - (m_anchor.m_angle - Math.PI);
                System.out.println(Math.toDegrees(m_freeSolid.m_angle));
                System.out.println(Math.toDegrees(m_anchor.m_angle));
                g.drawArc(m_position.m_x - 40, m_position.m_y - 40, 80, 80, (int)Math.toDegrees(m_anchor.m_angle - Math.PI), (int)Math.toDegrees(angle));
            }

            g.drawString(String.valueOf((int)Math.toDegrees(angle)), m_position.m_x + 20, m_position.m_y + 20);
        }
    }
}
