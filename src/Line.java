import java.awt.*;

public class Line extends Solid {
	public Point m_p2;

	//Constructor
	public Line(Point origin, Point p2) {
		super (origin, Math.atan2(p2.m_y, p2.m_x));
		m_p2 = p2;
	}

	public void draw(Graphics g) {
		g.drawLine((int)m_coordSystem.m_origin.m_x, (int)m_coordSystem.m_origin.m_y, (int)(m_p2.m_x), (int)(m_p2.m_y));
	}

	public Point getClosePoint(Point p) {
		int op_x = p.m_x - m_coordSystem.m_origin.m_x;
		int op_y = p.m_y - m_coordSystem.m_origin.m_y;

		int u_x = m_p2.m_x - m_coordSystem.m_origin.m_x;
		int u_y = m_p2.m_y - m_coordSystem.m_origin.m_y;

		double new_l = (op_x * u_x + op_y * u_y) / Math.sqrt(u_x * u_x + u_y * u_y);

		if (new_l < 0) {
			new_l = 0;
		} else if (new_l > Math.sqrt(u_x * u_x + u_y * u_y)) {
			new_l = Math.sqrt(u_x * u_x + u_y * u_y);
		}

		int new_x = m_coordSystem.m_origin.m_x + (int)((double)u_x * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		int new_y = m_coordSystem.m_origin.m_y + (int)((double)u_y * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		double dist = Math.sqrt((p.m_x - new_x) * (p.m_x - new_x) + (p.m_y - new_y) * (p.m_y - new_y));

		if (dist <= 10) {
			return new Point(new_x, new_y);
		} else {
			return null;
		}
	}
}
