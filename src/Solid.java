import java.awt.*;
import java.util.HashSet;
import java.io.*;

abstract public class Solid implements Serializable {
    public boolean m_isGround;
    public HashSet<Joint> m_joints;
    public Point m_position;
    public int m_offsetx = 0;
    public int m_offsety = 0;
    public double m_angle;

    //Constructor
    public Solid (Point origin, double angle) {
        m_joints = new HashSet<Joint>();

        m_position = origin;
        m_angle = angle;
    }

    abstract public void draw(Graphics g);

    abstract public Point getClosePoint(Point p, double snapping_distance);

	/*public Point getAbsoluteOrigin() {
        double rot = m_coordSystem.m_reference.getAbsoluteRotation();
        Point absPos = m_coordSystem.m_reference.getAbsolutePosition();

		int x = absPos.m_x + (int)(m_coordSystem.m_origin.m_x * Math.cos(rot) - m_coordSystem.m_origin.m_y * Math.sin(rot));
		int y = absPos.m_y + (int)(m_coordSystem.m_origin.m_x * Math.sin(rot) + m_coordSystem.m_origin.m_y * Math.cos(rot));

		return new Point(x, y);
	}

    public Point getAbsolutePosition() {
		int x = getAbsoluteOrigin().m_x;
        if (m_coordSystem.m_transX != null) {
            x += m_coordSystem.m_transX.m_value;
        }
        int y = getAbsoluteOrigin().m_y;
        if (m_coordSystem.m_transY != null) {
            y += m_coordSystem.m_transY.m_value;
        }

		return new Point(x, y);
	}

    public double getAbsoluteRotation() {
	    double rot = 0.0;
        if (m_coordSystem.m_rotZ != null) {
            rot = m_coordSystem.m_rotZ.m_value + m_coordSystem.m_reference.getAbsoluteRotation();;
        }

		return rot;
	}*/

    //Takes another Solid and returns whether the two solids are fixed
    public boolean isFixed (Solid otherSolid) {
        return true;
    }
}
