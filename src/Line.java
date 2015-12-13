import java.awt.*;

public class Line extends Solid {
	public double m_length;
	public int m_direction;
	public final int WIDTH_LINE = 2;

	//Constructor
	public Line(Joint joint, double length) {
		super (joint);
		m_direction = 1;
		m_length = length;
	}

	public Point[] getRectangle () {
		double rot = getAbsoluteRotation();
		Point a = getAbsolutePosition();
		Point topLeft = new Point (
		(int)(a.m_x + (-1) * m_direction * WIDTH_LINE * Math.cos(rot+Math.PI/2)),
		(int)(a.m_y + (-1) * m_direction * WIDTH_LINE * Math.sin(rot+Math.PI/2)));

		Point botLeft = new Point (
		(int)(a.m_x + m_direction * WIDTH_LINE * Math.cos(rot+Math.PI/2)),
		(int)(a.m_y + m_direction * WIDTH_LINE * Math.sin(rot+Math.PI/2)));

		Point topRight = new Point (
		(int) ((a.m_x + m_direction * m_length * Math.cos(rot)) + (-1) * m_direction * WIDTH_LINE * Math.cos(rot+Math.PI/2)),
		(int) ((a.m_y + m_direction * m_length * Math.sin(rot)) + (-1) * m_direction * WIDTH_LINE * Math.sin(rot+Math.PI/2)));

		Point botRight = new Point (
		(int) ((a.m_x + m_direction * m_length * Math.cos(rot)) + m_direction * WIDTH_LINE * Math.cos(rot+Math.PI/2)),
		(int) ((a.m_y + m_direction * m_length * Math.sin(rot)) + m_direction * WIDTH_LINE * Math.sin(rot+Math.PI/2)));

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
