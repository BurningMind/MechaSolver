import java.awt.*;

public class Line extends Solid {
	public double m_length;
	public final int WIDTH_LINE = 2;

	//Constructor
	public Line(Point origin, double length, double angle) {
		super (origin, angle);
		m_length = length;
	}

	public Point[] getRectangle () {
		Point topLeft = new Point (
		(int)(m_position.m_x + m_offsetx + (-1) * WIDTH_LINE * Math.cos(-m_angle+Math.PI/2)),
		(int)(m_position.m_y + m_offsety + (-1) * WIDTH_LINE * Math.sin(-m_angle+Math.PI/2)));

		Point botLeft = new Point (
		(int)(m_position.m_x + m_offsetx + WIDTH_LINE * Math.cos(-m_angle+Math.PI/2)),
		(int)(m_position.m_y + m_offsety + WIDTH_LINE * Math.sin(-m_angle+Math.PI/2)));

		Point topRight = new Point (
		(int) ((m_position.m_x + m_offsetx + m_length * Math.cos(-m_angle)) + (-1) * WIDTH_LINE * Math.cos(-m_angle+Math.PI/2)),
		(int) ((m_position.m_y + m_offsety + m_length * Math.sin(-m_angle)) + (-1) * WIDTH_LINE * Math.sin(-m_angle+Math.PI/2)));

		Point botRight = new Point (
		(int) ((m_position.m_x + m_offsetx + m_length * Math.cos(-m_angle)) + WIDTH_LINE * Math.cos(-m_angle+Math.PI/2)),
		(int) ((m_position.m_y + m_offsety + m_length * Math.sin(-m_angle)) + WIDTH_LINE * Math.sin(-m_angle+Math.PI/2)));

		Point [] pointsRectangle = new Point [4];
		pointsRectangle [0] = topLeft;
		pointsRectangle [1] = botLeft;
		pointsRectangle [2] = botRight;
		pointsRectangle [3] = topRight;
		return pointsRectangle;
	}

	public int[] getXCoord (Point[] points) {
		int [] xCoord = new int[4];
		for (int i = 0; i<4; i++) {
			xCoord[i]=points[i].m_x;
		}
		return xCoord;
	}

	public int[] getYCoord (Point[] points) {
		int [] yCoord = new int[4];
		for (int i = 0; i<4; i++) {
			yCoord[i]=points[i].m_y;
		}
		return yCoord;
	}

	public void draw(Graphics g) {
		g.fillPolygon(getXCoord(getRectangle()), getYCoord(getRectangle()), 4);
	}

	public Point getClosePoint(Point p, double snapping_distance) {
		int op_x = p.m_x - (m_position.m_x + m_offsetx);
		int op_y = p.m_y - (m_position.m_y + m_offsety);

		int u_x = (int)(m_length * Math.cos(-m_angle));
		int u_y = (int)(m_length * Math.sin(-m_angle));

		double new_l = (op_x * u_x + op_y * u_y) / Math.sqrt(u_x * u_x + u_y * u_y);

		if (new_l < 0) {
			new_l = 0;
		} else if (new_l > Math.sqrt(u_x * u_x + u_y * u_y)) {
			new_l = Math.sqrt(u_x * u_x + u_y * u_y);
		}

		int new_x = m_position.m_x + m_offsetx + (int)((double)u_x * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		int new_y = m_position.m_y + m_offsety + (int)((double)u_y * (double)new_l / (double)(Math.sqrt(u_x * u_x + u_y * u_y)));

		double dist = Math.sqrt((p.m_x - new_x) * (p.m_x - new_x) + (p.m_y - new_y) * (p.m_y - new_y));

		if (dist <= snapping_distance) {
			return new Point(new_x, new_y);
		} else {
			return null;
		}
	}
}
