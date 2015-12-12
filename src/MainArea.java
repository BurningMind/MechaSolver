import javax.swing.JPanel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainArea extends JPanel implements MouseInputListener {
	public enum Mode {
		NONE,
		REVOLUTE,
		PRISMATIC,
		LINE1,
		LINE2
	}

	public MainWindow m_mainWindow;
	public Mode m_mode = Mode.NONE;

	private Joint m_solidCreationJoint;
	private Solid m_tempSolid;
	private Joint m_tempJoint;

	public MainArea(MainWindow mainWindow) {
		m_mainWindow = mainWindow;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (Solid s : m_mainWindow.m_solids) {
			s.draw(g);
		}
		for (Joint j : m_mainWindow.m_joints) {
			j.draw(g);
		}

		// Draw the temporary solid displayed when creating it
		if (m_tempSolid!=null) {
			m_tempSolid.draw(g);
		}

		// Draw the temporary joint displayed when creating it
		if (m_tempJoint!=null) {
			m_tempJoint.draw(g);
		}
	}

	public Joint getNearbyJoint(Point p) {
		// We check distances until we find a joint within our radius, else we return a null
		Joint joint = null;
		for (Joint j : m_mainWindow.m_joints) {
			Point absPos = j.getAbsolutePosition();
			int x = p.m_x - absPos.m_x;
			int y = p.m_y - absPos.m_y;
			if (Math.sqrt(x*x + y*y) <= 50) {
				joint = j;
				break;
			}
		}

		return joint;
	}

	public void getNearbySolidAndPoint(Point p, Solid _solid, Point _point) {
		// We get a point from a nearby solid onto it
		Solid solid = null;
		Point point = null;
		for (Solid s : m_mainWindow.m_solids) {
			Point new_p = s.getClosePoint(p);
			if (p != null) {
				solid = s;
				point = new_p;
				break;
			}
		}

		_solid = solid;
		_point = point;
	}

	public void createSecondLink(Joint joint, Solid solid) {
		solid.m_joints.add(joint);
		joint.m_s2 = solid;

		// FIXME: doesn't behave correctly                   |
		solid.m_coordSystem.m_transX = joint.m_transX; // |
		solid.m_coordSystem.m_transY = joint.m_transY; // |
		solid.m_coordSystem.m_rotZ = joint.m_rotZ;     // |
		                                                  // |
		m_solidCreationJoint.m_transX = joint.m_transX;            // |
		m_solidCreationJoint.m_transY = joint.m_transY;            // |
		m_solidCreationJoint.m_rotZ = joint.m_rotZ;                // v
	}

	public void mouseClicked(MouseEvent e) {
		if (m_mode == Mode.LINE1) { // First click when creating a line
			Joint joint = getNearbyJoint(new Point(e.getX(), e.getY())); // Get nearby joint for snapping

			if (joint == null) { // We can only start a solid on a joint
				return;
			}

			m_solidCreationJoint = joint; // Store the joint for the second click
			m_mode = Mode.LINE2;
		} else if (m_mode == Mode.LINE2) { // Second click when creating a line
			Joint joint = getNearbyJoint(new Point(e.getX(), e.getY())); // Get nearby joint for snapping

			Point p;
			if (joint == null) { // If we don't snap, create the solid freely
				p = new Point(e.getX(), e.getY());
			} else { // If we snap, set the second point to the joint
				p = joint.getAbsolutePosition();
			}

			// We create the line object between the joint and the new point
			Point absPos = m_solidCreationJoint.getAbsolutePosition();
			int d_x = p.m_x - absPos.m_x;
			int d_y = p.m_y - absPos.m_y;
			Line new_line = new Line(m_solidCreationJoint, Math.sqrt(d_x * d_x + d_y * d_y));


			if (joint != null) { // If we snap to a second joint
				createSecondLink(joint, new_line);
			}

			// If the line is attached to a revolute, then we rotate its parameter to match the line drawn
			if (new_line.m_coordSystem.m_rotZ != null) {
				new_line.m_coordSystem.m_rotZ.m_value = Math.atan2(d_y, d_x);
			}

			m_mainWindow.m_solids.add(new_line);

			// We add the line to the initial joint
			m_solidCreationJoint.m_s2 = new_line;

			repaint();

			m_mode = Mode.LINE1;
		} else if (m_mode == Mode.REVOLUTE || m_mode == Mode.PRISMATIC) { // We create a joint
			// We get a point from a nearby solid onto it
			Solid solid = null;
			Point point = null;
			getNearbySolidAndPoint(new Point(e.getX(), e.getY()), solid, point);

			// Otherwise we default to the ground
			if (solid == null) {
				solid = m_mainWindow.m_ground;
			}
			if (point == null) {
				point = new Point(e.getX(), e.getY());
			}

			// We create the joint
			Joint joint;
			if (m_mode == Mode.REVOLUTE) {
				joint = new Revolute(solid, null, point, new Point(0, 0), "rev", 0.0);
			} else if (m_mode == Mode.PRISMATIC) {
				joint = new Prismatic(solid, null, point, new Point(0, 0), "pris", 0.0);
			} else {
				return;
			}

			m_mainWindow.m_joints.add(joint);
			solid.m_joints.add(joint);

			repaint();

			//m_mode = Mode.NONE;
		}
	}

	public void mouseMoved(MouseEvent e) {
		m_tempSolid = null;
		m_tempJoint = null;
		if (m_mode == Mode.LINE2) {
			Point absPos = m_solidCreationJoint.getAbsolutePosition();

			Joint joint = getNearbyJoint(new Point(e.getX(), e.getY()));
			Point p;
			if (joint!=null) {
				Point secondAbsPos = joint.getAbsolutePosition();
				p = new Point (secondAbsPos.m_x, secondAbsPos.m_y);
			} else {
				p = new Point (e.getX(), e.getY());
			}

			int d_x = p.m_x - absPos.m_x;
			int d_y = p.m_y - absPos.m_y;
			Line new_line = new Line(m_solidCreationJoint, Math.sqrt(d_x * d_x + d_y * d_y));

			// If the line is attached to a revolute, then we rotate its parameter to match the line drawn
			if (new_line.m_coordSystem.m_rotZ != null) {
				new_line.m_coordSystem.m_rotZ.m_value = Math.atan2(d_y, d_x);
			} else { // else we normalize its length
				if (new_line.m_coordSystem.m_transX != null && new_line.m_coordSystem.m_transY == null) {
					new_line.m_length = d_x;
				} else if (new_line.m_coordSystem.m_transX == null && new_line.m_coordSystem.m_transY != null) {
					new_line.m_length = d_y;
				}
			}

			m_tempSolid = new_line;
		} else if (m_mode == Mode.REVOLUTE || m_mode == Mode.PRISMATIC) {
			// We get a point from a nearby solid onto it
			Solid solid = null;
			Point point = null;
			getNearbySolidAndPoint(new Point(e.getX(), e.getY()), solid, point);

			// Otherwise we default to the ground
			if (solid == null) {
				solid = m_mainWindow.m_ground;
			}
			if (point == null) {
				point = new Point(e.getX(), e.getY());
			}

			// We create the joint
			Joint joint;
			if (m_mode == Mode.REVOLUTE) {
				joint = new Revolute(solid, null, point, new Point(0, 0), "rev", 0.0);
			} else if (m_mode == Mode.PRISMATIC) {
				joint = new Prismatic(solid, null, point, new Point(0, 0), "pris", 0.0);
			} else {
				return;
			}

			m_tempJoint = joint;
		}

		repaint();
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}
