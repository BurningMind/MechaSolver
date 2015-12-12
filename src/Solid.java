import java.awt.*;
import java.util.HashSet;

abstract public class Solid {
    public boolean m_isGround;
    public int m_nbSolids, m_nbJoints;
    public HashSet<Joint> m_joints;
    public CoordSystem m_coordSystem;

    //Constructor
    public Solid (Joint joint) {
        m_joints = new HashSet<Joint>();

        if (joint == null) {
            m_coordSystem = new CoordSystem(null, new Point(0, 0), null, null, null);
        } else {
            m_coordSystem = new CoordSystem (joint.m_s1, joint.m_pS1, joint.m_transX, joint.m_transY, joint.m_rotZ);
            m_joints.add(joint);
        }
    }

    abstract public void draw(Graphics g);

    abstract public Point getClosePoint(Point p, double snapping_distance);

	public Point getAbsoluteOrigin() {
		int x = m_coordSystem.m_origin.m_x + m_coordSystem.m_reference.getAbsoluteOrigin().m_x;
		int y = m_coordSystem.m_origin.m_y + m_coordSystem.m_reference.getAbsoluteOrigin().m_y;

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
            rot = m_coordSystem.m_rotZ.m_value;
        }

        rot += m_coordSystem.m_reference.getAbsoluteRotation();

		return rot;
	}

    //Takes another Solid and returns whether the two solids are fixed
    public boolean isFixed (Solid otherSolid) {
        return true;
    }
}
