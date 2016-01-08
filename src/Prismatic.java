import java.awt.*;

public class Prismatic extends Joint {
    public Prismatic(Solid anchor, Solid freeSolid, Point position, int id) {
        super(anchor, freeSolid, position, id);
    }

    public void draw(Graphics g) {
        if (m_anchor.m_isGround) {
            g.drawRect(m_position.m_x - 5, m_position.m_y - 5, 10, 10);
            g.drawLine(m_position.m_x, m_position.m_y+5, m_position.m_x, m_position.m_y+20);
            g.drawLine(m_position.m_x-10, m_position.m_y+20, m_position.m_x+10, m_position.m_y+20);
            for (int i = -15; i<=5; i=i+5) {
                g.drawLine(m_position.m_x+i, m_position.m_y+25, m_position.m_x+i+5, m_position.m_y+20);
            }
        } else {
            g.drawRect(m_position.m_x - 5, m_position.m_y - 5, 10, 10);
        }

        if (m_anchor != null && m_freeSolid != null) {
            int x_offset = (int)(Math.sin(Math.atan2(m_freeSolid.m_offsety, m_freeSolid.m_offsetx)) * 10.0);
            int y_offset = (int)(Math.cos(Math.atan2(m_freeSolid.m_offsety, m_freeSolid.m_offsetx)) * 10.0);
            g.drawLine(m_position.m_x + x_offset, m_position.m_y + y_offset, m_position.m_x + m_freeSolid.m_offsetx + x_offset, m_position.m_y + m_freeSolid.m_offsety + y_offset);

            g.drawString(String.valueOf(Math.sqrt(m_freeSolid.m_offsetx * m_freeSolid.m_offsetx + m_freeSolid.m_offsety * m_freeSolid.m_offsety)), m_position.m_x + 20, m_position.m_y + 20);
        }
    }
}
