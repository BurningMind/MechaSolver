public class Revolute extends Joint {

    public Revolute (Solid s1, Solid s2 ) {
        super(s1, s2);
        m_rotZ = true;
        m_transX = false;
        m_transY = false;
    }
}
