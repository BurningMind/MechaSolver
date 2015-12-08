import java.awt.*;

abstract public class Joint {

    public Solid m_s1, m_s2;
    public boolean m_transX, m_transY, m_rotZ;
    public Point m_pS1, m_pS2, m_pContact;

    //Constructor
    public Joint(Solid s1, Solid s2, Point pS1, Point pS2, Point pContact) {
        m_s1 = s1;
        m_s2 = s2;
        m_pS1 = pS1;
        m_pS2 = pS2;
        m_pContact = pContact;
    }

    abstract public void draw(Graphics g);
}
