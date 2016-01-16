import java.awt.*;

public class Revolute extends Joint {
    public Revolute(Solid anchor, Solid firstFreeSolid, Point position, int id) {
        super(anchor, firstFreeSolid, position, id);
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

        if (m_anchor != null) {
            int radius_offset = 0;
            for (Solid freeSolid : m_freeSolids) {
                double angle = 0.0;
                if (m_anchor.m_isGround) {
                    angle = (freeSolid.m_angle - m_anchor.m_angle+Math.PI*2) % (Math.PI*2);
                    g.drawArc(m_position.m_x - (40 + 10*radius_offset), m_position.m_y - (40 + 10*radius_offset), 80 + 20*radius_offset, 80 + 20*radius_offset, (int)Math.toDegrees(m_anchor.m_angle), (int)Math.toDegrees(angle));
                } else {
                    angle =( freeSolid.m_angle - (m_anchor.m_angle - Math.PI) + Math.PI *2 ) % (Math.PI *2);
                    g.drawArc(m_position.m_x - (40 + 10*radius_offset), m_position.m_y - (40 + 10*radius_offset), 80 + 20*radius_offset, 80 + 20*radius_offset, (int)Math.toDegrees(m_anchor.m_angle - Math.PI), (int)Math.toDegrees(angle));
                }

                g.drawString(String.valueOf((int)Math.toDegrees(angle)), m_position.m_x + 20 + 20*radius_offset, m_position.m_y + 20 + 20*radius_offset);

                radius_offset++;
            }
        }
    }
}
