import java.awt.*;

public class Line extends Solid {
	public double m_length;

	//Constructor
	public Line(Point origin, double length, double angleToGround) {
		super (origin, angleToGround);
		m_length = length;
	}

	public void draw(Graphics g) {
		g.drawLine((int)m_coordSystem.m_origin.m_x, (int)m_coordSystem.m_origin.m_y, (int)(m_coordSystem.m_origin.m_x + Math.cos(m_coordSystem.m_angleToGround) * m_length), (int)(m_coordSystem.m_origin.m_y + Math.sin(m_coordSystem.m_angleToGround) * m_length));
	}
}
