public class Prismatic extends Joint {

    //Constructor
    public Prismatic (Solid s1, Solid s2) {
        super(s1, s2);
        m_rotZ = false;
        m_transX = true;
        m_transY = true;
    }
}
