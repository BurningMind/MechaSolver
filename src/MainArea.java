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

	public final double SNAPPING_DISTANCE = 20.0;

	public MainWindow m_mainWindow;
	public Mode m_mode = Mode.NONE;
	public boolean m_snap = true;

	private Joint m_solidCreationJoint;
	private Solid m_tempSolid;
	private Joint m_tempJoint;

	private Joint m_verticalGuide = null;
	private Joint m_horizontalGuide = null;

	//Constructor
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

		if (m_horizontalGuide != null) {
			g.drawLine(0, m_horizontalGuide.m_position.m_y, getWidth(), m_horizontalGuide.m_position.m_y);
		}

		if (m_verticalGuide != null) {
			g.drawLine(m_verticalGuide.m_position.m_x, 0, m_verticalGuide.m_position.m_x, getHeight());
		}
	}

	public Pair<Joint, Point> getNearbyJoint(Point p) {
		// We check distances until we find a joint within our radius, else we return a null
		Joint joint = null;
		Point point = null;

		m_horizontalGuide = null;
		m_verticalGuide = null;

		for (Joint j : m_mainWindow.m_joints) {
			if (p.distance(j.m_position) <= SNAPPING_DISTANCE) {
				joint = j;
				point = j.m_position;
				break;
			}
		}

		if (point == null && m_snap) { // TODO: support both snapping methods at the same time
			int x = p.m_x;
			int y = p.m_y;

			for (Joint j : m_mainWindow.m_joints) {
				if (Math.abs(j.m_position.m_x - p.m_x) < SNAPPING_DISTANCE && x == p.m_x) {
					x = j.m_position.m_x;
					m_verticalGuide = j;
				}

				if (Math.abs(j.m_position.m_y - p.m_y) < SNAPPING_DISTANCE && y == p.m_y) {
					y = j.m_position.m_y;
					m_horizontalGuide = j;
				}
			}

			if (x != p.m_x || y != p.m_y) {
				point = new Point(x, y);
			}
		}

		return new Pair<Joint, Point>(joint, point);
	}

	public Pair<Solid, Point> getNearbySolidAndPoint(Point p) {
		// We get a point from a nearby solid onto it
		Solid solid = null;
		Point point = null;

		m_horizontalGuide = null;
		m_verticalGuide = null;

		for (Solid s : m_mainWindow.m_solids) {
			Point new_p = s.getClosePoint(p, SNAPPING_DISTANCE);
			if (new_p != null) {
				solid = s;
				point = new_p;
				break;
			}
		}

		if (point == null && m_snap) { // TODO: support both snapping methods at the same time
			int x = p.m_x;
			int y = p.m_y;

			for (Joint j : m_mainWindow.m_joints) {
				if (Math.abs(j.m_position.m_x - p.m_x) < SNAPPING_DISTANCE && x == p.m_x) {
					x = j.m_position.m_x;
					m_verticalGuide = j;
				}

				if (Math.abs(j.m_position.m_y - p.m_y) < SNAPPING_DISTANCE && y == p.m_y) {
					y = j.m_position.m_y;
					m_horizontalGuide = j;
				}
			}

			if (x != p.m_x || y != p.m_y) {
				point = new Point(x, y);
			}
		}

		return new Pair<Solid, Point>(solid, point);
	}

	public Pair<Joint, Solid> getInCreationJoint(Point current_point) {
		// We get a point from a nearby solid onto it
		Pair<Solid, Point> pair = getNearbySolidAndPoint(current_point);
		Solid solid = pair.a;
		Point point = pair.b;

		// Otherwise we default to the ground
		if (solid == null) {
			solid = m_mainWindow.m_ground;
		}

		if (point == null) {
			point = current_point;
		}

		// We create the joint
		Joint joint;
		if (m_mode == Mode.REVOLUTE) {
			joint = new Revolute(solid, null, point, m_mainWindow.m_joints.size());
		} else if (m_mode == Mode.PRISMATIC) {
			joint = new Prismatic(solid, null, point, m_mainWindow.m_joints.size());
		} else {
			return null;
		}

		return new Pair<Joint, Solid>(joint, solid);
	}

	public Pair<Line, Joint> getInCreationLine(Point current_point) {
		Pair<Joint, Point> pair = getNearbyJoint(current_point); // Get nearby joint for snapping

		Joint joint = pair.a;
		Point point = pair.b;

		if (joint == null && point == null) {
			point = current_point;
		}

		// We create the line object between the joint and the new point
		int d_x = point.m_x - m_solidCreationJoint.m_position.m_x;
		int d_y = point.m_y - m_solidCreationJoint.m_position.m_y;

		Line new_line = new Line(m_solidCreationJoint.m_position, Math.sqrt(d_x * d_x + d_y * d_y), Math.atan2(-d_y, d_x));

		if (m_solidCreationJoint instanceof Prismatic) {
			if (d_x > 0) {
				new_line.m_angle = 0;
			} else {
				new_line.m_angle = Math.PI;
			}

			new_line.m_length = Math.abs(d_x);
		}

		return new Pair<Line, Joint>(new_line, joint);
	}

	public void mouseClicked(MouseEvent e) {
		if (m_mode == Mode.LINE1) { // First click when creating a line
			Pair<Joint, Point> pair = getNearbyJoint(new Point(e.getX(), e.getY())); // Get nearby joint for snapping

			if (pair.a == null) { // We can only start a solid on a joint
				return;
			}

			m_solidCreationJoint = pair.a; // Store the joint for the second click
			m_mode = Mode.LINE2;
		} else if (m_mode == Mode.LINE2) { // Second click when creating a line
			Pair<Line, Joint> pair = getInCreationLine(new Point(e.getX(), e.getY()));
			Line new_line = pair.a;
			Joint joint = pair.b;

			if (joint != null) { // If we snap to a second joint
				new_line.m_joints.add(joint);
				joint.m_freeSolid = new_line;

				if (m_solidCreationJoint instanceof Revolute && joint instanceof Revolute) {
					m_solidCreationJoint.m_constraints.add(new Distance(joint, joint.m_position.distance(m_solidCreationJoint.m_position)));
					joint.m_constraints.add(new Distance(m_solidCreationJoint, joint.m_position.distance(m_solidCreationJoint.m_position)));
				} else if (m_solidCreationJoint instanceof Prismatic || joint instanceof Prismatic) {
					m_solidCreationJoint.m_constraints.add(new Alignment(joint, new Vector(joint.m_position.m_x - m_solidCreationJoint.m_position.m_x, joint.m_position.m_y - m_solidCreationJoint.m_position.m_y)));
					joint.m_constraints.add(new Alignment(m_solidCreationJoint, new Vector(m_solidCreationJoint.m_position.m_x - joint.m_position.m_x, m_solidCreationJoint.m_position.m_y - joint.m_position.m_y)));
				}

				if (m_solidCreationJoint instanceof Prismatic && joint instanceof Revolute) {
					new_line.m_position = joint.m_position;
					new_line.m_angle = new_line.m_angle - Math.PI;
				}
			}

			new_line.m_joints.add(m_solidCreationJoint);

			// We add the line to the initial joint
			m_solidCreationJoint.m_freeSolid = new_line;
			m_mainWindow.addSolid(new_line);

			m_mode = Mode.LINE1;
		} else if (m_mode == Mode.REVOLUTE || m_mode == Mode.PRISMATIC) { // We create a joint
			Pair<Joint, Solid> pair = getInCreationJoint(new Point(e.getX(), e.getY()));
			Joint joint = pair.a;
			Solid solid = pair.b;

			if (joint == null || solid == null) {
				return;
			}

			if (solid.m_isGround) {
				joint.m_constraints.add(new Fixed());
			} else {
				solid.m_joints.add(joint);
			}

			for (Joint j : solid.m_joints) {
				if (j instanceof Revolute && joint instanceof Revolute) {
					j.m_constraints.add(new Distance(joint, joint.m_position.distance(j.m_position)));
					joint.m_constraints.add(new Distance(j, joint.m_position.distance(j.m_position)));
				} else if (j instanceof Prismatic || joint instanceof Prismatic) {
					j.m_constraints.add(new Alignment(joint, new Vector(j.m_position.m_x - joint.m_position.m_x, j.m_position.m_y - joint.m_position.m_y)));
					joint.m_constraints.add(new Alignment(j, new Vector(joint.m_position.m_x - j.m_position.m_x, joint.m_position.m_y - j.m_position.m_y)));
				}

				if (j instanceof Prismatic && joint instanceof Revolute) {
					solid.m_position = joint.m_position;
					solid.m_angle = (solid.m_angle - Math.PI + Math.PI*2) % (Math.PI * 2);
				}
			}

			m_mainWindow.addJoint(joint);
		}

		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		m_tempSolid = null;
		m_tempJoint = null;
		if (m_mode == Mode.LINE2) {
			Line new_line = getInCreationLine(new Point(e.getX(), e.getY())).a;

			if (new_line == null) {
				return;
			}

			m_tempSolid = new_line;
		} else if (m_mode == Mode.REVOLUTE || m_mode == Mode.PRISMATIC) {
			Joint joint = getInCreationJoint(new Point(e.getX(), e.getY())).a;

			if (joint == null) {
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
