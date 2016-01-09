public class ConstraintSolver {
	/*public static Point[] solve(Constraint c1, Constraint c2) {
		if (c1 instanceof Distance && c2 instanceof Alignment) {
			return solveDistanceAlignment((Distance)c1, (Alignment)c2);
		} else if (c1 instanceof Alignment && c2 instanceof Distance) {
			return solveDistanceAlignment((Distance)c2, (Alignment)c1);
		}
	}*/

	// Solutions here: http://www.wolframalpha.com/input/?i=sqrt%28%28a+-+%28e+%2B+x*c%29%29%5E2+%2B+%28b+-+%28f+%2B+x*d%29%29%5E2%29+%3D+g
	public static Point[] solveDistanceAlignment(Distance distance, Alignment alignment) {
		Point[] solutions = new Point[2];

		double a = distance.m_origin.m_position.m_x;
		double b = distance.m_origin.m_position.m_y;

		double c = alignment.m_direction.getX();
		double d = alignment.m_direction.getY();

		double e = alignment.m_origin.m_position.m_x;
		double f = alignment.m_origin.m_position.m_y;

		double g = distance.m_dist;

		double eq_a = c*c + d*d;
		double eq_b = -2*a*c - 2*b*d + 2*c*e + 2*d*f;
		double eq_c = a*a - 2*a*e + b*b - 2*b*f + e*e + f*f - g*g;

		double delta = eq_b*eq_b - 4*eq_a*eq_c;

		double x1 = (-eq_b + Math.sqrt(delta)) / (2*eq_a);
		double x2 = (-eq_b - Math.sqrt(delta)) / (2*eq_a);

		solutions[0] = new Point((int)(c*x1 + e), (int)(d*x1 + f));
		solutions[1] = new Point((int)(c*x2 + e), (int)(d*x2 + f));

		if (Math.abs(solutions[0].distance(distance.m_origin.m_position) - g) >= 1.0 || Math.abs(solutions[1].distance(distance.m_origin.m_position) - g) >= 1.0) { // We round because we might not have the same EXACT distances
			return null;
		}

		return solutions;
	}

	public static Point[] solveDistanceDistance(Distance distance1, Distance distance2) {
		Point[] solutions = new Point[2];

		double a = distance1.m_origin.m_position.m_x;
		double b = distance1.m_origin.m_position.m_y;

		double c = distance2.m_origin.m_position.m_x;
		double d = distance2.m_origin.m_position.m_y;

		double e = distance1.m_dist;
		double f = distance2.m_dist;

		if (e + f < distance1.m_origin.m_position.distance(distance2.m_origin.m_position)) {
			return null;
		}

		double x1;
		double y1;
		double x2;
		double y2;
		
		if (b != d) {
			double alpha = (c - a)/(b - d);
			double beta = (-(a*a) + c*c - b*b + d*d + e*e - f*f) / (-2*(b - d));

			double eq_a = 1 + alpha*alpha;
			double eq_b = 2*alpha*beta - 2*a - 2*alpha*b;
			double eq_c = a*a + beta*beta - 2*beta*b + b*b - e*e;

			double delta = eq_b*eq_b - 4*eq_a*eq_c;

			x1 = (-eq_b + Math.sqrt(delta)) / (2*eq_a);
			x2 = (-eq_b - Math.sqrt(delta)) / (2*eq_a);

			y1 = x1*alpha + beta;
			y2 = x2*alpha + beta;
		} else {
			x1 = (-(a*a) + c*c - b*b + d*d + e*e - f*f) / (-2*(a - c));
			x2 = x1;

			double eq_a = 1;
			double eq_b = -2*b;
			double eq_c = b*b + x1*x1 - 2*x1*a + a*a - e*e;

			double delta = eq_b*eq_b - 4*eq_a*eq_c;

			y1 = (-eq_b + Math.sqrt(delta)) / (2*eq_a);
			y2 = (-eq_b - Math.sqrt(delta)) / (2*eq_a);
		}

		solutions[0] = new Point((int)x1, (int)y1);
		solutions[1] = new Point((int)x2, (int)y2);

		return solutions;
	}

	private static Point[] solveAlignmentAlignment(Alignment alignment1, Alignment alignment2) {
		return new Point[2];
	}
}
