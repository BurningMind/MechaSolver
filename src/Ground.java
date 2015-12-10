import java.awt.*;

public class Ground extends Solid {
	public Ground() {
		super(new Point(0, 0), 0.0);
		m_isGround = true;
	}

	public Point getClosePoint(Point p) {
		return null;
	}

	public void draw(Graphics g) {

	}
}
