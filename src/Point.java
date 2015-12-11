public class Point {

    public int m_x, m_y;

    //Constructor
    public Point (int x, int y) {
        m_x = x;
        m_y = y;
    }

    public double distance ( Point otherPoint) {
        double dx = m_x - otherPoint.m_x;
        double dy = m_y - otherPoint.m_y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
