import java.awt.*;

public class Rectangle extends Solid {
	public int m_w, m_h;

	public Rectangle(int x, int y, int w, int h) {
		m_x = x;
		m_y = y;
		m_w = w;
		m_h = h;
	}

	public void draw(Graphics g) {
		g.drawRect(m_x, m_y, m_w, m_h);
	}
}
