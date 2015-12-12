import java.awt.*;

abstract public class Joint {

    public Solid m_s1, m_s2;
    public Parameter m_transX, m_transY, m_rotZ;
    public Point m_pS1, m_pS2;

    //Constructor
    public Joint(Solid s1, Solid s2, Point pS1, Point pS2) {
        m_s1 = s1;
        m_s2 = s2;
        m_pS1 = pS1;
        m_pS2 = pS2;
    }

    abstract public void draw(Graphics g);

    public Point getAbsolutePosition() {
        double rot = m_s1.getAbsoluteRotation();
        Point absPos = m_s1.getAbsolutePosition();
        int x = (int)(absPos.m_x + (m_pS1.m_x * Math.cos(rot) - m_pS1.m_y * Math.sin(rot)));
        int y = (int)(absPos.m_y + (m_pS1.m_x * Math.sin(rot) + m_pS1.m_y * Math.cos(rot)));

		return new Point(x , y);
	}
}
