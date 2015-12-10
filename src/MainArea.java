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

	private Joint m_tempJoint;

	public MainArea(MainWindow mainWindow) {
		m_mainWindow = mainWindow;
		addMouseListener(this);
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (Solid s : m_mainWindow.m_solids) {
			s.draw(g);
		}
		for (Joint j : m_mainWindow.m_joints) {
			j.draw(g);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (m_mode == Mode.LINE1) {
			Joint joint = null;
			for (Joint j : m_mainWindow.m_joints) {
				int x = e.getX() - j.m_pContact.m_x;
				int y = e.getY() - j.m_pContact.m_y;
				if (Math.sqrt(x*x + y*y) <= 50) {
					joint = j;
					break;
				}
			}

			if (joint == null) {
				return;
			}

			m_tempJoint = joint;
			m_mode = Mode.LINE2;
		} else if (m_mode == Mode.LINE2) {
			Joint joint = null;
			for (Joint j : m_mainWindow.m_joints) {
				int x = e.getX() - j.m_pContact.m_x;
				int y = e.getY() - j.m_pContact.m_y;
				if (Math.sqrt(x*x + y*y) <= 50) {
					joint = j;
					break;
				}
			}

			Point p;
			if (joint == null) {
				p = new Point(e.getX(), e.getY());
			} else {
				p = joint.m_pContact;
			}

			int d_x = p.m_x - m_tempJoint.m_pContact.m_x;
			int d_y = p.m_y - m_tempJoint.m_pContact.m_y	;
			Line new_line = new Line(m_tempJoint, Math.sqrt(d_x * d_x + d_y * d_y));

			if (joint != null) {
				new_line.m_joints.add(joint);
				joint.m_s2 = new_line;

				new_line.m_coordSystem.m_transX = joint.m_transX;
				new_line.m_coordSystem.m_transY = joint.m_transY;
				new_line.m_coordSystem.m_rotZ = joint.m_rotZ;

				m_tempJoint.m_transX = joint.m_transX;
				m_tempJoint.m_transY = joint.m_transY;
				m_tempJoint.m_rotZ = joint.m_rotZ;
			}

			if (new_line.m_coordSystem.m_rotZ != null) {
				new_line.m_coordSystem.m_rotZ.m_value = Math.atan2(d_y, d_x);
			}

			m_mainWindow.m_solids.add(new_line);

			m_tempJoint.m_s2 = new_line;

			repaint();

			m_mode = Mode.NONE;
		} else if (m_mode == Mode.REVOLUTE || m_mode == Mode.PRISMATIC) {
			Solid solid = null;
			Point point = null;
			for (Solid s : m_mainWindow.m_solids) {
				Point p = s.getClosePoint(new Point(e.getX(), e.getY()));
				if (p != null) {
					solid = s;
					point = p;
					break;
				}
			}

			if (solid == null) {
				solid = m_mainWindow.m_ground;
			}

 			if (point == null) {
				point = new Point(e.getX(), e.getY());
			}

			double rot = 0.0;
			if (solid.m_coordSystem.m_rotZ != null) {
				rot = solid.m_coordSystem.m_rotZ.m_value;
			}

			point.m_x = (int)(point.m_x * Math.cos(rot) - point.m_y * Math.sin(rot));
			point.m_y = (int)(point.m_x * Math.sin(rot) + point.m_y * Math.cos(rot));

			Joint joint;
			if (m_mode == Mode.REVOLUTE) {
				joint = new Revolute(solid, null, point, new Point(0, 0), new Point(solid.m_coordSystem.m_origin.m_x + point.m_x, solid.m_coordSystem.m_origin.m_y + point.m_y), "rev", 0.0);
			} else if (m_mode == Mode.PRISMATIC) {
				joint = new Prismatic(solid, null, point, new Point(0, 0), new Point(solid.m_coordSystem.m_origin.m_x + point.m_x, solid.m_coordSystem.m_origin.m_y + point.m_y), "pris", 0.0);
			} else {
				return;
			}

			m_mainWindow.m_joints.add(joint);
			solid.m_joints.add(joint);

			repaint();

			m_mode = Mode.NONE;
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}
