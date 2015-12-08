import javax.swing.JPanel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainArea extends JPanel implements MouseInputListener {
	public enum Mode {
		NONE,
		REVOLUTE,
		LINE1,
		LINE2
	}

	public MainWindow m_mainWindow;
	public Mode m_mode = Mode.NONE;

	private Point m_tempPoint;

	public MainArea(MainWindow mainWindow) {
		m_mainWindow = mainWindow;
		addMouseListener(this);
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (Solid s : m_mainWindow.m_solids) {
			s.draw(g);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (m_mode == Mode.LINE1) {
			m_tempPoint = new Point(e.getX(), e.getY());
			m_mode = Mode.LINE2;
		} else if (m_mode == Mode.LINE2) {
			int x = e.getX() - m_tempPoint.m_x;
			int y = e.getY() - m_tempPoint.m_y;
			m_mainWindow.m_solids.add(new Line(m_tempPoint, Math.sqrt(x * x + y * y), Math.atan2(y, x)));

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
