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

			Line new_line;

			if (joint != null) {
				new_line = new Line(m_tempJoint.m_pContact, joint.m_pContact);
				joint.m_s2 = new_line;
			} else {
				new_line = new Line(m_tempJoint.m_pContact, new Point(e.getX(), e.getY()));
			}

			m_mainWindow.m_solids.add(new_line);

			m_tempJoint.m_s2 = new_line;

			repaint();

			m_mode = Mode.LINE1;
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
				solid = new Ground();
			}
 			if (point == null) {
				point = new Point(e.getX(), e.getY());
			}

			if (m_mode == Mode.REVOLUTE) {
				m_mainWindow.m_joints.add(new Revolute(solid, null, point, new Point(0, 0), point));
			} else if (m_mode == Mode.PRISMATIC) {
				m_mainWindow.m_joints.add(new Prismatic(solid, null, point, new Point(0, 0), point));
			}

			repaint();

			//m_mode = Mode.NONE;
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}
