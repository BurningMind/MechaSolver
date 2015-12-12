import java.awt.*;

public class Ground extends Solid {
	public Ground() {
		super(null);
		m_isGround = true;
	}

	public Point getClosePoint(Point p, double snapping_distance) {
		return null;
	}

	@Override
	public Point getAbsoluteOrigin() {
		return new Point(0, 0);
	}

	@Override
	public Point getAbsolutePosition() {
		return new Point(0, 0);
	}

	@Override
	public double getAbsoluteRotation() {
		return 0.0;
	}

	public void draw(Graphics g) {

	}
}
