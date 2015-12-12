import java.awt.*;

public class Line extends Solid {
	public double m_length;
	public int m_direction;

	//Constructor
	public Line(Joint joint, double length) {
		super (joint);
		m_direction = 1;
		m_length = length;
	}

	public void draw(Graphics g) {
		double rot = getAbsoluteRotation();
		Point a = getAbsolutePosition();
		g.drawLine((int)a.m_x, (int)a.m_y, (int)(a.m_x + m_direction * m_length * Math.cos(rot)), (int)(a.m_y + m_direction * m_length * Math.sin(rot)));
	}

	public Point getClosePoint(Point p, double snapping_distance) {
		Point a = getAbsolutePosition();
		int op_x = p.m_x - a.m_x;
		int op_y = p.m_y - a.m_y;

		double rot = getAbsoluteRotation();

		int u_x = (int)(a.m_x + m_direction * m_length * Math.cos(rot)) - a.m_x;
		int u_y = (int)(a.m_y + m_direction * m_length * Math.sin(rot)) - a.m_y;

		double new_l = (op_x * u_x + op_y * u_y) / Math.sqrt(u_x * u_x + u_y * u_y);

		if (new_l < 0) {
			new_l = 0;
		} else if (new_l > Math.sqrt(u_x * u_x + u_y * u_y)) {
			new_l = Math.sqrt(u_x * u_x + u_y * u_y);
		}

		int new_x = a.m_x + (int)((double)u_x * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		int new_y = a.m_y + (int)((double)u_y * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		double dist = Math.sqrt((p.m_x - new_x) * (p.m_x - new_x) + (p.m_y - new_y) * (p.m_y - new_y));

		if (dist <= snapping_distance) {
			return new Point(m_direction * (int)new_l, 0);
		} else {
			return null;
		}
	}
}
