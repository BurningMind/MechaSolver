import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class MainWindow extends JFrame implements ActionListener {
	JButton m_addRevoluteButton;
	JButton m_addPrismaticButton;
	JButton m_addLineButton;
	JButton m_setAngleButton;
	JButton m_clear;

	JLabel m_dispSolids;

	JPanel m_insideProgram;
	JPanel m_infoIP;
	JPanel m_exeIP;

	MainArea m_mainArea;

	public HashSet<Solid> m_solids;
	public HashSet<Joint> m_joints;
	public HashSet<Constraint> m_tempConstraints;
	public Ground m_ground;
	public final Dimension DIM_INSIDEPROG = new Dimension (300, 400);

	//Constructor
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MechaSolver");

		Container pane = getContentPane();

		m_mainArea = new MainArea(this);
		pane.add(m_mainArea, BorderLayout.CENTER);

		m_insideProgram = new JPanel();
		m_insideProgram.setLayout(null);
		m_insideProgram.setBackground(Color.BLACK);
		m_insideProgram.setMinimumSize(DIM_INSIDEPROG);
		m_insideProgram.setPreferredSize(DIM_INSIDEPROG);

		m_infoIP = new JPanel();
		m_infoIP.setBackground(Color.GRAY);
		m_infoIP.setBounds(10, 10, 280, 300);

		m_exeIP = new JPanel();
		m_exeIP.setBackground(Color.GRAY);
		m_exeIP.setBounds(10, 320, 280, 300);

		m_insideProgram.add(m_infoIP);
		m_insideProgram.add(m_exeIP);
		pane.add(m_insideProgram, BorderLayout.LINE_END);

		JToolBar toolBar = new JToolBar();
		pane.add(toolBar, BorderLayout.PAGE_START);

		m_addRevoluteButton = new JButton("Add Revolute");
		m_addRevoluteButton.addActionListener(this);
		toolBar.add(m_addRevoluteButton);

		m_addPrismaticButton = new JButton("Add Prismatic");
		m_addPrismaticButton.addActionListener(this);
		toolBar.add(m_addPrismaticButton);

		m_addLineButton = new JButton("Add Line");
		m_addLineButton.addActionListener(this);
		toolBar.add(m_addLineButton);

		m_setAngleButton = new JButton("Set Angle");
		m_setAngleButton.addActionListener(this);
		toolBar.add(m_setAngleButton);

		m_clear = new JButton ("Clear");
		m_clear.addActionListener(this);
		toolBar.add(m_clear);

		m_solids = new HashSet<Solid>();
		m_joints = new HashSet<Joint>();
		m_tempConstraints = new HashSet<Constraint>();
		m_ground = new Ground();

		pack();
		setSize(1200,700);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_addRevoluteButton) {
			m_mainArea.m_mode = MainArea.Mode.REVOLUTE;
		} else if (e.getSource() == m_addPrismaticButton) {
			m_mainArea.m_mode = MainArea.Mode.PRISMATIC;
		} else if (e.getSource() == m_addLineButton) {
			m_mainArea.m_mode = MainArea.Mode.LINE1;
		} else if (e.getSource() == m_setAngleButton) {
			m_mainArea.m_mode = MainArea.Mode.SETANGLE;
		} else if (e.getSource() == m_clear) {
			m_solids.clear();
			m_joints.clear();
			m_mainArea.repaint();
			m_mainArea.m_mode = MainArea.Mode.NONE;
		}
	}

	public void removeConstraints() {
		for (Joint j : m_joints) {
			for (Constraint c : m_tempConstraints) {
				j.m_constraints.remove(c);
			}
		}

		m_tempConstraints.clear();
	}

	public void setConstraint(Constraint c, Joint j) {
		m_tempConstraints.add(c);

		if (c instanceof Angle) {
			if (j.hasFixedConstraint()) {
				Distance d = j.hasDistanceConstraint(null, null);
				if (d != null) {
					Constraint c1 = new Alignment(j, new Vector(j.m_position, new Point(j.m_position.m_x + (int)(d.m_dist * Math.cos(-((Angle)c).m_angle)), j.m_position.m_y + (int)(d.m_dist * Math.sin(-((Angle)c).m_angle)))));
					m_tempConstraints.add(c1);
					d.m_origin.m_constraints.add(c1);
				}

				return;
			}

			Pair<Distance, Distance> pair = j.hasTwoDistanceConstraints(null, null);
			if (pair != null) {
				double dist = Math.sqrt(Math.pow(pair.a.m_dist, 2) + Math.pow(pair.b.m_dist, 2) - 2*pair.a.m_dist*pair.b.m_dist*Math.cos(((Angle)c).m_angle));

				if (!pair.a.m_origin.hasFixedConstraint()) {
					Constraint c1 = new Distance(pair.b.m_origin, dist);
					m_tempConstraints.add(c1);
					pair.a.m_origin.m_constraints.add(c1);
				}

				if (!pair.b.m_origin.hasFixedConstraint()) {
					Constraint c2 = new Distance(pair.a.m_origin, dist);
					m_tempConstraints.add(c2);
					pair.b.m_origin.m_constraints.add(c2);
				}

				return;
			}

			Pair<Distance, Alignment> pair2 = j.hasOneDistanceAndOneAlignmentConstraints(null, null);
			if (pair2 != null) {
				double angle = Math.atan2(pair2.b.m_direction.getY(), pair2.b.m_direction.getX()) + ((Angle)c).m_angle;
				int x = pair2.b.m_origin.m_position.m_x + (int)(Math.cos(angle) * pair2.a.m_dist);
				int y = pair2.b.m_origin.m_position.m_y + (int)(Math.sin(angle) * pair2.a.m_dist);

				if (!pair2.a.m_origin.hasFixedConstraint()) {
					Constraint c1 = new Alignment(new Prismatic(null, null, new Point(x, y), "temp"), pair2.b.m_direction);				m_tempConstraints.add(c1);
					pair2.a.m_origin.m_constraints.add(c1);
				}

				return;
			}
		}
	}

	public void addSolid (Solid solid) {
		m_solids.add(solid);

	}


	public void solveConstraints(Joint j, Joint parent) {
		System.out.print("Joint " + j.m_name + ": ");

		if (j.m_defined && parent != null && parent.m_defined) {
			System.out.println("ignore");
			return;
		}

		if (j.hasFixedConstraint()) {
			System.out.print("fixed");
			Distance d = j.hasDistanceConstraint(parent, null);

			if (d != null) {
				System.out.print("... with dist ");
				solveConstraints(d.m_origin, j);
			}

			System.out.println(" done fixed");

			return;
		}

		Pair<Distance, Distance> pair;
		Pair<Distance, Alignment> pair2;

		if (parent != null && parent.m_defined) {
			System.out.print("... with parent defined ");
			pair = j.hasTwoDistanceConstraints(null, parent);
			pair2 = j.hasOneDistanceAndOneAlignmentConstraints(null, parent);

			if (pair2 != null) {
				System.out.print("... with dist align ");
				if (pair2.a.m_origin == parent && pair2.b.m_origin == parent) {
					System.out.print("... with both from parent ");
					Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

					if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
						j.m_position.m_x = new_points[0].m_x;
						j.m_position.m_y = new_points[0].m_y;
					} else {
						j.m_position.m_x = new_points[1].m_x;
						j.m_position.m_y = new_points[1].m_y;
					}

					j.m_defined = true;

					for (Constraint c : j.m_constraints) {
						if (c instanceof Distance) {
							if (((Distance)c).m_origin == parent) {
								continue;
							}

							System.out.print("dist");
							solveConstraints(((Distance)c).m_origin, j);
						} else if (c instanceof Alignment) {
							if (((Alignment)c).m_origin == parent) {
								continue;
							}

							System.out.print("align");
							solveConstraints(((Alignment)c).m_origin, j);
						}
					}
				} else {
					if (pair2.a.m_origin != parent) {
						System.out.print("... with a not from parent ");
						solveConstraints(pair2.a.m_origin, j);
					}
					if (pair2.b.m_origin != parent) {
						System.out.print("... with b not from parent ");
						solveConstraints(pair2.b.m_origin, j);
					}

					Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

					double old_angle = j.m_position.angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
					double new_angle0 = new_points[0].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
					double new_angle1 = new_points[1].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);

					if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
						j.m_position.m_x = new_points[0].m_x;
						j.m_position.m_y = new_points[0].m_y;
					} else {
						j.m_position.m_x = new_points[1].m_x;
						j.m_position.m_y = new_points[1].m_y;
					}

					j.m_defined = true;
				}
				System.out.println(" done dist align ");
			} else if (pair != null) {
				System.out.print("... with dist dist ");
				if (pair.a.m_origin != parent) {
					System.out.print("... with a not from parent ");
					solveConstraints(pair.a.m_origin, j);
				}
				if (pair.b.m_origin != parent) {
					System.out.print("... with b not from parent ");
					solveConstraints(pair.b.m_origin, j);
				}

				Point[] new_points = ConstraintSolver.solveDistanceDistance(pair.a, pair.b);

				double old_angle = j.m_position.angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
				double new_angle0 = new_points[0].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
				double new_angle1 = new_points[1].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);

				if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
					j.m_position.m_x = new_points[0].m_x;
					j.m_position.m_y = new_points[0].m_y;
				} else {
					j.m_position.m_x = new_points[1].m_x;
					j.m_position.m_y = new_points[1].m_y;
				}

				j.m_defined = true;
				System.out.println(" done dist dist");
			}
		} else {
			System.out.print("... with parent not defined ");
			pair = j.hasTwoDistanceConstraints(parent, null);
			pair2 = j.hasOneDistanceAndOneAlignmentConstraints(parent, null);

			if (pair != null) {
				System.out.println("... with dist dist ");
				solveConstraints(pair.a.m_origin, j);
				solveConstraints(pair.b.m_origin, j);
				Point[] new_points = ConstraintSolver.solveDistanceDistance(pair.a, pair.b);

				double old_angle = j.m_position.angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
				double new_angle0 = new_points[0].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
				double new_angle1 = new_points[1].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);

				if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
					j.m_position.m_x = new_points[0].m_x;
					j.m_position.m_y = new_points[0].m_y;
				} else {
					j.m_position.m_x = new_points[1].m_x;
					j.m_position.m_y = new_points[1].m_y;
				}

				j.m_defined = true;
				System.out.println(" done dist dist");
			} else if (pair2 != null) {
				System.out.println("... with dist align");
				solveConstraints(pair2.a.m_origin, j);
				solveConstraints(pair2.b.m_origin, j);
				Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

				double old_angle = j.m_position.angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
				double new_angle0 = new_points[0].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
				double new_angle1 = new_points[1].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);

				if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
					j.m_position.m_x = new_points[0].m_x;
					j.m_position.m_y = new_points[0].m_y;
				} else {
					j.m_position.m_x = new_points[1].m_x;
					j.m_position.m_y = new_points[1].m_y;
				}

				j.m_defined = true;
				System.out.println(" done dist align");
			}
		}

		if (pair != null || pair2 != null) {
			return;
		}

		System.out.print("other: ");
		for (Constraint c : j.m_constraints) {
			if (c instanceof Distance) {
				if (((Distance)c).m_origin == parent) {
					continue;
				}

				System.out.print("dist");
				solveConstraints(((Distance)c).m_origin, j);
			} else if (c instanceof Alignment) {
				if (((Alignment)c).m_origin == parent) {
					continue;
				}

				System.out.print("align");
				solveConstraints(((Alignment)c).m_origin, j);
			}
		}
		System.out.println();
	}
}
