import java.awt.*;

public class Rectangle extends Solid {
    public double m_width, m_height;

	//Constructor
	public Rectangle(Point origin, double width, double height, double angleToGround) {
		super (origin, angleToGround);
		m_height = height;
		m_width = width;
	}

	public void draw(Graphics g) {
		g.drawRect((int)m_coordSystem.m_origin.m_x, (int)m_coordSystem.m_origin.m_y, (int)m_width, (int)m_height);
	}
}
