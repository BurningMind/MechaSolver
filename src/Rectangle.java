import java.awt.*;

public class Rectangle extends Solid {
    public double m_width, m_height;

	//Constructor
	public Rectangle(Point origin, double width, double height, double angleToGround) {
		super (null);
		m_height = height;
		m_width = width;
	}

	public void draw(Graphics g) {
		g.drawRect((int)m_coordSystem.m_origin.m_x, (int)m_coordSystem.m_origin.m_y, (int)m_width, (int)m_height);
	}

    public Point getAbsoluteOrigin() {
        return new Point(0, 0);
    }

    public Point getClosePoint(Point p) {
		return null;
	}
}
