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

    abstract public Point getClosePoint(Point p);
    abstract public Point getAbsoluteOrigin();

    //Takes another Solid and returns whether the two solids are fixed
    public boolean isFixed (Solid otherSolid) {
        return true;
    }
}
