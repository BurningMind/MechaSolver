public class Joint {

public Solid m_s1, m_s2;
public boolean m_transX, m_transY, m_rotZ;
public Point m_pS1, m_pS2, m_pContact;

    //Constructor
    public Joint(Solid s1, Solid s2) {
        m_s1 = s1;
        m_s2 = s2;
    }
}
