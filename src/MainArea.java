import javax.swing.JPanel;
import java.util.HashSet;
import java.awt.*;

public class MainArea extends JPanel {
	private HashSet<Solid> m_solids;

	public MainArea() {
		m_solids = new HashSet<Solid>();
	}

	public void addSolid(Solid solid) {
		m_solids.add(solid);
		repaint();
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		for (Solid s : m_solids) {
			s.draw(g);
		}
	}
}
