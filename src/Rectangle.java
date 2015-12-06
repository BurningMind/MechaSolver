import java.awt.*;

public class Rectangle extends Solid {

	public Rectangle(Point origin, int height, int width, double angleToGround) {
		super (origin, angleToGround);
		m_origin = origin;
		m_height = height;
		m_width = width;
	}

	public void draw(Graphics g) {
		g.drawRect((int)m_origin.m_x, (int)m_origin.m_y, (int)m_width, (int)m_height);
	}
}
