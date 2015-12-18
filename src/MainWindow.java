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
				Distance d = j.hasDistanceConstraint(null);
				if (d != null) {
					d.m_origin.m_constraints.add(new Alignment(j, new Vector(j.m_position, new Point((int)(d.m_dist * Math.cos(((Angle)c).m_angle)), (int)(d.m_dist * Math.sin(((Angle)c).m_angle))))));
				}
			}

			Pair<Distance, Distance> pair = j.hasTwoDistanceConstraints(null);
			if (pair != null) {
				double dist = Math.sqrt(Math.pow(pair.a.m_dist, 2) + Math.pow(pair.b.m_dist, 2) - 2*pair.a.m_dist*pair.b.m_dist*Math.cos(((Angle)c).m_angle));

				Constraint c1 = new Distance(pair.b.m_origin, dist);
				m_tempConstraints.add(c1);
				pair.a.m_origin.m_constraints.add(c1);
				Constraint c2 = new Distance(pair.a.m_origin, dist);
				m_tempConstraints.add(c2);
				pair.b.m_origin.m_constraints.add(c2);
			}

			Pair<Distance, Alignment> pair2 = j.hasOneDistanceAndOneAlignmentConstraints(null);
			if (pair2 != null) {
				double angle = Math.atan2(pair2.b.m_direction.getY(), pair2.b.m_direction.getX()) + ((Angle)c).m_angle;
				int x = pair2.b.m_origin.m_position.m_x + (int)(Math.cos(angle) * pair2.a.m_dist);
				int y = pair2.b.m_origin.m_position.m_y + (int)(Math.sin(angle) * pair2.a.m_dist);

				Constraint c1 = new Alignment(new Prismatic(null, null, new Point(x, y), "temp"), pair2.b.m_direction);				m_tempConstraints.add(c1);
				pair2.a.m_origin.m_constraints.add(c1);
			}
		}
	}

	public void addSolid (Solid solid) {
		m_solids.add(solid);

	}


	public void solveConstraints(Joint j, Joint parent) {
		System.out.print("Solid " + j.m_name + ": ");
		if (j.hasFixedConstraint()) {
			Distance d = j.hasDistanceConstraint(parent);

			if (d != null) {
				solveConstraints(d.m_origin, j);
			}

			System.out.println("fixed");

			return;
		}

		Pair<Distance, Distance> pair = j.hasTwoDistanceConstraints(parent);
		Pair<Distance, Alignment> pair2 = j.hasOneDistanceAndOneAlignmentConstraints(parent);
		if (pair != null) {
			System.out.println("dist dist");
			solveConstraints(pair.a.m_origin, j);
			solveConstraints(pair.b.m_origin, j);
			Point[] new_points = ConstraintSolver.solveDistanceDistance(pair.a, pair.b);

			j.m_position.m_x = new_points[1].m_x;
			j.m_position.m_y = new_points[1].m_y;
		}

		if (pair2 != null) {
			System.out.println("dist align");
			solveConstraints(pair2.a.m_origin, j);
			solveConstraints(pair2.b.m_origin, j);
			Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

			j.m_position.m_x = new_points[1].m_x;
			j.m_position.m_y = new_points[1].m_y;
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
