public class Alignment extends Constraint {
	public Joint m_origin;
	public Vector m_direction;

	//Constructor
	public Alignment(Joint origin, Vector direction) {
		m_origin = origin;
		m_direction = direction;
	}
}
