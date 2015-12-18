public class Vector {

    private Point m_a, m_b;

    //Constructor
    public Vector (Point a, Point b) {
        m_a = a;
        m_b = b;
    }

    public int getX() {
        return m_b.m_x - m_a.m_x;
    }

    public int getY() {
        return m_b.m_y - m_a.m_y;
    }
}
