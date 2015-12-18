import java.awt.*;

public class Rectangle extends Solid {
    public double m_width, m_height;

	//Constructor
	public Rectangle(Point origin, double width, double height, double angleToGround) {
		super(new Point(0, 0), 0.0);
		m_height = height;
		m_width = width;
	}

	public void draw(Graphics g) {
		g.drawRect((int)m_position.m_x, (int)m_position.m_y, (int)m_width, (int)m_height);
	}

    public Point getClosePoint(Point p, double snapping_distance) {
		return null;
	}
}
