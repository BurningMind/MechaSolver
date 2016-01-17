import java.awt.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.*;

abstract public class Joint implements Serializable {

    public Solid m_anchor;
    public ArrayList<Solid> m_freeSolids;
    public HashSet<Constraint> m_constraints;
    public Point m_position;

    public int m_id=0;

    public boolean m_defined = false;
    public boolean m_visited = false;

    //Constructor
    public Joint(Solid anchor, Solid firstFreeSolid, Point position, int id) {
        m_anchor = anchor;
        m_freeSolids = new ArrayList<Solid>();
        if (firstFreeSolid != null) {
            m_freeSolids.add(firstFreeSolid);
        }
        m_position = position;
        m_constraints = new HashSet<Constraint>();
        m_id = id;
    }

    abstract public void draw(Graphics g);

    /*public Point getAbsolutePosition() {
        double rot = m_s1.getAbsoluteRotation();
        Point absPos = m_s1.getAbsolutePosition();
        int x = (int)(absPos.m_x + (m_pS1.m_x * Math.cos(rot) - m_pS1.m_y * Math.sin(rot)));
        int y = (int)(absPos.m_y + (m_pS1.m_x * Math.sin(rot) + m_pS1.m_y * Math.cos(rot)));

		return new Point(x , y);
	}*/

    public boolean hasFixedConstraint() {
        for (Constraint c : m_constraints) {
            if (c instanceof Fixed) {
                return true;
            }
        }
        return false;
    }

    public Pair<Distance, Angle> hasOneDistanceAndLinkedAngle(Joint except, Joint priority) {
        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    for (Constraint c2 : ((Distance)c).m_origin.m_constraints) {
                        if (c2 instanceof Angle) {
                            if (((Distance)c).m_origin.m_freeSolids.get(((Angle)c2).m_freeSolid) == m_anchor) {
                                return new Pair<Distance, Angle>((Distance)c, (Angle)c2);
                            }
                        }
                    }
                }
            }
        }

        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == except || ((Distance)c).m_origin == this) {
                    continue;
                }

                for (Constraint c2 : ((Distance)c).m_origin.m_constraints) {
                    if (c2 instanceof Angle) {
                        if (((Distance)c).m_origin.m_freeSolids.get(((Angle)c2).m_freeSolid) == m_anchor) {
                            return new Pair<Distance, Angle>((Distance)c, (Angle)c2);
                        }
                    }
                }
            }
        }

        return null;
    }

    public Distance hasDistanceConstraint(Joint except, Joint priority) {
        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    return (Distance)c;
                }
            }
        }

        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == except || ((Distance)c).m_origin == this) {
                    continue;
                }
                return (Distance)c;
            }
        }
        return null;
    }

    public Alignment hasAlignmentConstraint(Joint except, Joint priority) {
        for (Constraint c : m_constraints) {
            if (c instanceof Alignment) {
                if (((Alignment)c).m_origin == priority || ((Alignment)c).m_origin.m_defined) {
                    return (Alignment)c;
                }
            }
        }

        for (Constraint c : m_constraints) {
            if (c instanceof Alignment) {
                if (((Alignment)c).m_origin == except || ((Alignment)c).m_origin == this) {
                    continue;
                }
                return (Alignment)c;
            }
        }
        return null;
    }

    public Pair<Distance, Distance> hasTwoDistanceConstraints(Joint except, Joint priority) {
        int counter = 0;
        Distance d1 = null;
        Distance d2 = null;

        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    if (counter == 0) {
                        d1 = (Distance)c;
                    } else if (counter == 1 && ((Distance)c).m_origin != d1.m_origin) {
                        d2 = (Distance)c;
                    } else {
                        break;
                    }
                    counter++;
                }
            }
        }

        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == except || ((Distance)c).m_origin == this || ((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    continue;
                }

                if (counter == 0) {
                    d1 = (Distance)c;
                } else if (counter == 1 && ((Distance)c).m_origin != d1.m_origin) {
                    d2 = (Distance)c;
                } else {
                    break;
                }
                counter++;
            }
        }

        if (counter < 2) {
            return null;
        } else {
            return new Pair<Distance, Distance>(d1, d2);
        }
    }

    public Pair<Distance, Alignment> hasOneDistanceAndOneAlignmentConstraints(Joint except, Joint priority) {
        boolean found_dist = false;
        boolean found_align = false;
        Distance d = null;
        Alignment a = null;
        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    if (!found_dist) {
                        d = (Distance)c;
                        found_dist = true;
                    } else {
                        continue;
                    }
                }
            } else if (c instanceof Alignment) {
                if (((Alignment)c).m_origin == priority || ((Alignment)c).m_origin.m_defined) {
                    if (!found_align) {
                        a = (Alignment)c;
                        found_align = true;
                    } else {
                        continue;
                    }
                }
            }
        }

        for (Constraint c : m_constraints) {
            if (c instanceof Distance) {
                if (((Distance)c).m_origin == except || ((Distance)c).m_origin == this || ((Distance)c).m_origin == priority || ((Distance)c).m_origin.m_defined) {
                    continue;
                }

                if (!found_dist) {
                    d = (Distance)c;
                    found_dist = true;
                } else {
                    continue;
                }
            } else if (c instanceof Alignment) {
                if (((Alignment)c).m_origin == except || ((Alignment)c).m_origin == this || ((Alignment)c).m_origin == priority || ((Alignment)c).m_origin.m_defined) {
                    continue;
                }

                if (!found_align) {
                    a = (Alignment)c;
                    found_align = true;
                } else {
                    continue;
                }
            }
        }

        if (!found_dist || !found_align) {
            return null;
        } else {
            return new Pair<Distance, Alignment>(d, a);
        }
    }
}
