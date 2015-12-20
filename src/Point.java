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

    public double angle(Point p1, Point p2) {
        return Math.atan2(p2.m_x - m_x, p2.m_y - m_y) - Math.atan2(p1.m_x - m_x, p1.m_y - m_y);
    }
}
