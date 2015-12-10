import java.awt.*;

public class Ground extends Solid {
	public Ground() {
		super(null);
		m_isGround = true;
	}

	public Point getClosePoint(Point p) {
		return null;
	}

	public Point getAbsoluteOrigin() {
		return new Point(0, 0);
	}

	public void draw(Graphics g) {

	}
}
