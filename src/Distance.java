public class Distance extends Constraint {
	public Joint m_origin;
	public double m_dist;

	//Constructor
	public Distance(Joint origin, double dist) {
		m_origin = origin;
		m_dist = dist;
	}
}
