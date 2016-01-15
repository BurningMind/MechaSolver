import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainWindow extends JFrame implements ActionListener, ChangeListener {
	JButton m_addRevoluteButton;
	JButton m_addPrismaticButton;
	JButton m_addLineButton;
	JButton m_addEngineButton;
	JButton m_setSnapping;
	JButton m_clear;

	JLabel m_dispSolids;

	JPanel m_insideProgram;
	JPanel m_infoIP;
	JPanel m_exeIP;

	MainArea m_mainArea;

	public ArrayList<Solid> m_solids;
	public ArrayList<Joint> m_joints;
	public ArrayList<MySlider> m_sliders;
	public ArrayList<JTextField> m_minTextFields;
	public ArrayList<JTextField> m_maxTextFields;

	public HashSet<Constraint> m_tempConstraints;
	public HashMap<Point, Point> m_tempPos;
	public boolean m_hasSolution;
	public boolean settingValue;
	public Ground m_ground;
	public final Dimension DIM_INSIDEPROG = new Dimension (300, 400);
	public final Dimension DIM_FIELD = new Dimension (50, 25);

	//Constructor
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MechaSolver");

		Container pane = getContentPane();

		m_mainArea = new MainArea(this);
		pane.add(m_mainArea, BorderLayout.CENTER);

		m_insideProgram = new JPanel();
		m_insideProgram.setLayout(new BoxLayout(m_insideProgram, BoxLayout.Y_AXIS));
		m_insideProgram.setMinimumSize(DIM_INSIDEPROG);
		m_insideProgram.setPreferredSize(DIM_INSIDEPROG);

		m_infoIP = new JPanel();
		m_infoIP.setLayout(new BoxLayout(m_infoIP, BoxLayout.Y_AXIS));
		m_infoIP.setMinimumSize(new Dimension(300, 400));
		m_infoIP.setPreferredSize(new Dimension(300, 400));

		m_exeIP = new JPanel();
		m_exeIP.setBackground(Color.GRAY);

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

		m_addEngineButton = new JButton("Add Engine");
		m_addEngineButton.addActionListener(this);
		toolBar.add(m_addEngineButton);

		m_setSnapping = new JButton("Enable/Disable Snapping");
		m_setSnapping.addActionListener(this);
		toolBar.add(m_setSnapping);

		m_clear = new JButton ("Clear");
		m_clear.addActionListener(this);
		toolBar.add(m_clear);

		m_solids = new ArrayList<Solid>();
		m_sliders = new ArrayList<MySlider>();
		m_joints = new ArrayList<Joint>();
		m_minTextFields = new ArrayList<JTextField>();
		m_maxTextFields = new ArrayList<JTextField>();
		m_tempConstraints = new HashSet<Constraint>();
		m_tempPos = new HashMap<Point, Point>();
		m_ground = new Ground();

		pack();
		setSize(1200,700);
		setVisible(true);
	}

	public void addSolid (Solid solid) {
		m_solids.add(solid);

		for (Joint j : solid.m_joints) {
			if (j.m_position == solid.m_position) {
				double angleInRad = 0.0;
				if (j.m_anchor.m_isGround) {
	                angleInRad = (j.m_freeSolid.m_angle - j.m_anchor.m_angle) % (Math.PI*2);
	            } else {
	                angleInRad = (j.m_freeSolid.m_angle - (j.m_anchor.m_angle - Math.PI)+Math.PI*2) % (Math.PI*2);
	            }

				settingValue = true;
				m_sliders.get(j.m_id).setValue((int)Math.toDegrees(angleInRad));
				settingValue = false;
				break;
			}
		}
	}

	public void addJoint (Joint joint) {
		m_joints.add(joint);

		JPanel jointPanel = new JPanel();
		jointPanel.setLayout(new BoxLayout(jointPanel, BoxLayout.Y_AXIS));
		jointPanel.setMaximumSize(new Dimension(300, 100));
		jointPanel.setPreferredSize(new Dimension(300, 100));

		JPanel textFields = new JPanel();
		textFields.setLayout(new BoxLayout(textFields, BoxLayout.X_AXIS));
		textFields.setMaximumSize(new Dimension(290, 100));
		textFields.setPreferredSize(new Dimension(290, 100));

		JTextField minTextField = new JTextField();
		minTextField.setPreferredSize(DIM_FIELD);
		minTextField.setMaximumSize(DIM_FIELD);
		minTextField.addActionListener(this);
		m_minTextFields.add(minTextField);

		JTextField maxTextField = new JTextField();
		maxTextField.setPreferredSize(DIM_FIELD);
		maxTextField.setMaximumSize(DIM_FIELD);
		maxTextField.addActionListener(this);
		m_maxTextFields.add(maxTextField);

		MySlider slider = new MySlider(MySlider.HORIZONTAL, 0, 360, 0, m_sliders.size());
		slider.addChangeListener(this);

		jointPanel.add(slider);
		jointPanel.add(new JLabel("Joint " + m_joints.size()));
		m_sliders.add(slider);
		textFields.add(minTextField);
		textFields.add(maxTextField);

		jointPanel.add(textFields);
		m_infoIP.add(jointPanel);
		m_infoIP.revalidate();

		repaint();
	}

	public void stateChanged(ChangeEvent e) {
		MySlider slider = (MySlider)e.getSource();

		if (!settingValue) {
			if (m_joints.get(slider.m_id) instanceof Revolute) {
				setJointAngle(m_joints.get(slider.m_id), Math.toRadians(slider.getValue()));
			} else if (m_joints.get(slider.m_id) instanceof Prismatic) {
				setJointDistance(m_joints.get(slider.m_id), slider.getValue());
			}

			for (MySlider s : m_sliders) {
				if (s != slider) {
					Joint joint = m_joints.get(s.m_id);
					double angleInRad = 0.0;
					if (joint.m_anchor.m_isGround) {
						angleInRad = (joint.m_freeSolid.m_angle - joint.m_anchor.m_angle) % (Math.PI*2);
					} else {
						angleInRad = (joint.m_freeSolid.m_angle - (joint.m_anchor.m_angle - Math.PI)+Math.PI*2) % (Math.PI*2);
					}
					settingValue = true;
					s.setValue((int)Math.toDegrees(angleInRad));
					settingValue = false;
				}
			}
			repaint();
		}



	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_addRevoluteButton) {
			m_mainArea.m_mode = MainArea.Mode.REVOLUTE;
		} else if (e.getSource() == m_addPrismaticButton) {
			m_mainArea.m_mode = MainArea.Mode.PRISMATIC;
		} else if (e.getSource() == m_addLineButton) {
			m_mainArea.m_mode = MainArea.Mode.LINE1;
		} else if (e.getSource() == m_addEngineButton) {
			m_mainArea.m_mode = MainArea.Mode.ENGINE;
			System.out.println("Engine mode");
		}else if (e.getSource() == m_setSnapping) {
			m_mainArea.m_snap = !m_mainArea.m_snap;
		} else if (e.getSource() == m_clear) {
			m_solids.clear();
			m_joints.clear();
			m_mainArea.repaint();
			m_infoIP.removeAll();
			m_sliders.clear();
			m_joints.clear();
			repaint();
			m_mainArea.m_mode = MainArea.Mode.NONE;
		} else if (e.getSource() instanceof JTextField ) {
			boolean isMinText = false;
			int nb = 0;

			for (JTextField textField : m_minTextFields) {
				if (e.getSource() == textField) {
					isMinText = true;
					System.out.println(nb);
					String value = m_minTextFields.get(nb).getText();
					m_sliders.get(nb).setMinimum(Integer.parseInt(value));
					nb=0;
					break;
				}
				nb++;
			}
			if (!isMinText) {
				nb=0;
				for (JTextField textField : m_maxTextFields) {
					if (e.getSource() == textField) {
						System.out.println(nb);
						String value = m_maxTextFields.get(nb).getText();
						m_sliders.get(nb).setMaximum(Integer.parseInt(value));
						break;
					}
					nb++;
				}
			}


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
		m_hasSolution = true;
		m_tempPos.clear();
		m_tempConstraints.add(c);

		if (c instanceof Angle && j instanceof Revolute) {
			j.m_constraints.add(c);

			/*if (j.hasFixedConstraint()) {
				Distance d = j.hasDistanceConstraint(null, null);
				if (d != null) {
					Constraint c1 = new Alignment(j, new Vector(j.m_position, new Point(j.m_position.m_x + (int)(d.m_dist * Math.cos(-((Angle)c).m_angle)), j.m_position.m_y + (int)(d.m_dist * Math.sin(-((Angle)c).m_angle)))));
					m_tempConstraints.add(c1);
					d.m_origin.m_constraints.add(c1);
				}
				return;
			}*/

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
				double angle =(Math.atan2(pair2.b.m_direction.m_y, pair2.b.m_direction.m_x) + ((Angle)c).m_angle + Math.PI * 2) % (Math.PI*2);
				System.out.println(angle);
				int x = pair2.b.m_origin.m_position.m_x + (int)(Math.cos(angle) * pair2.a.m_dist);
				int y = pair2.b.m_origin.m_position.m_y + (int)(Math.sin(angle) * pair2.a.m_dist);

				if (!pair2.a.m_origin.hasFixedConstraint()) {
					Constraint c1 = new Alignment(new Prismatic(null, null, new Point(x, y), -42), pair2.b.m_direction);				m_tempConstraints.add(c1);
					pair2.a.m_origin.m_constraints.add(c1);
				}
				return;
			}
		} else if (c instanceof Distance && ((Distance)c).m_origin instanceof Prismatic) {
			j.m_constraints.add(c);
		} else if (c instanceof Distance && j instanceof Prismatic) {
			j.m_constraints.add(c);
		}
	}

	public void setJointAngle(Joint joint, double angle) {
		removeConstraints();
		for (Joint j : m_joints) {
			j.m_defined = j.hasFixedConstraint();
			j.m_visited = false;
		}
		setConstraint(new Angle(angle), joint);
		solveConstraints(joint, null);

		if (!m_hasSolution) {
			for (Map.Entry<Point, Point> entry : m_tempPos.entrySet()) {
				entry.getKey().m_x = entry.getValue().m_x;
				entry.getKey().m_y = entry.getValue().m_y;
			}
		}

		if (joint.m_freeSolid.m_joints.size() == 1) {
			if (joint.m_anchor.m_isGround) {
				joint.m_freeSolid.m_angle = (angle + joint.m_anchor.m_angle + Math.PI * 2) % (Math.PI * 2);
			} else {
				joint.m_freeSolid.m_angle = (angle - Math.PI + joint.m_anchor.m_angle + Math.PI * 2) % (Math.PI * 2);
			}
		} else {
			for (Solid s : m_solids) {
				for (Joint j : s.m_joints) {
					if (j instanceof Prismatic) {
						continue;
					}

					if (j.m_position != s.m_position) {
						int d_x = j.m_position.m_x - s.m_position.m_x;
						int d_y = j.m_position.m_y - s.m_position.m_y;

						s.m_angle =(Math.atan2(-d_y, d_x)+Math.PI * 2) % (Math.PI * 2);
						break;
					}
				}
			}
		}
	}

	public void setJointDistance(Joint joint, double dist) {
		removeConstraints();
		for (Joint j : m_joints) {
			j.m_defined = j.hasFixedConstraint();
			j.m_visited = false;
		}

		Alignment align = joint.hasAlignmentConstraint(null, null);
		if (align != null) {
			setConstraint(new Distance(joint, dist + ((Line)joint.m_freeSolid).m_length), align.m_origin);
			setConstraint(new Distance(align.m_origin, dist + ((Line)joint.m_freeSolid).m_length), joint);
			solveConstraints(joint, null);
		} else {
			joint.m_freeSolid.m_offsetx = (int)(dist * Math.cos(joint.m_freeSolid.m_angle));
			joint.m_freeSolid.m_offsety = (int)(dist * Math.sin(joint.m_freeSolid.m_angle));
		}

		if (!m_hasSolution) {
			for (Map.Entry<Point, Point> entry : m_tempPos.entrySet()) {
				entry.getKey().m_x = entry.getValue().m_x;
				entry.getKey().m_y = entry.getValue().m_y;
			}
		}

		for (Solid s : m_solids) {
			for (Joint j : s.m_joints) {
				if (j instanceof Prismatic) {
					continue;
				}

				if (s.m_joints.size() == 1) {
					s.m_angle = j.m_anchor.m_angle;
					break;
				}

				if (j.m_position != s.m_position) {
					int d_x = j.m_position.m_x - s.m_position.m_x;
					int d_y = j.m_position.m_y - s.m_position.m_y;

					s.m_angle = Math.atan2(-d_y, d_x);
					break;
				}
			}
		}
	}

	public void solveConstraints(Joint j, Joint parent) {
		System.out.print("Joint " + j.m_id + ": ");

		if (j.m_visited) {
			return;
		}
		j.m_visited = true;

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
		Pair<Distance, Angle> pair3;

		if (parent != null && parent.m_defined) {
			System.out.print("... with parent defined ");
			pair = j.hasTwoDistanceConstraints(null, parent);
			pair2 = j.hasOneDistanceAndOneAlignmentConstraints(null, parent);
			pair3 = j.hasOneDistanceAndLinkedAngle(null, parent);

			if (pair3 != null) {
				System.out.print("... with dist angle ");
				Point new_point = ConstraintSolver.solveDistanceAngle(pair3.a, pair3.b);

				m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
				j.m_position.m_x = new_point.m_x;
				j.m_position.m_y = new_point.m_y;

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
			} else if (pair2 != null) {
				System.out.print("... with dist align ");
				if (pair2.a.m_origin == parent && pair2.b.m_origin == parent) {
					System.out.print("... with both from parent ");
					Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

					if (new_points != null && m_hasSolution) {
						if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[0].m_x;
							j.m_position.m_y = new_points[0].m_y;
						} else {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[1].m_x;
							j.m_position.m_y = new_points[1].m_y;
						}
					} else {
						m_hasSolution = false;
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

					if (new_points != null && m_hasSolution) {
						double old_angle = j.m_position.angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
						double new_angle0 = new_points[0].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
						double new_angle1 = new_points[1].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);

						if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[0].m_x;
							j.m_position.m_y = new_points[0].m_y;
						} else if (Math.abs(old_angle - new_angle0) > Math.abs(old_angle - new_angle1)) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[1].m_x;
							j.m_position.m_y = new_points[1].m_y;
						} else {
							if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
								m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
								j.m_position.m_x = new_points[0].m_x;
								j.m_position.m_y = new_points[0].m_y;
							} else {
								m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
								j.m_position.m_x = new_points[1].m_x;
								j.m_position.m_y = new_points[1].m_y;
							}
						}
					} else {
						m_hasSolution = false;
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

				if (new_points != null && m_hasSolution) {
					double old_angle = j.m_position.angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
					double new_angle0 = new_points[0].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
					double new_angle1 = new_points[1].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);

					if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[0].m_x;
						j.m_position.m_y = new_points[0].m_y;
					} else if (Math.abs(old_angle - new_angle0) > Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[1].m_x;
						j.m_position.m_y = new_points[1].m_y;
					} else {
						if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[0].m_x;
							j.m_position.m_y = new_points[0].m_y;
						} else {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[1].m_x;
							j.m_position.m_y = new_points[1].m_y;
						}
					}
				} else {
					m_hasSolution = false;
				}

				j.m_defined = true;
				System.out.println(" done dist dist");
			}
		} else {
			System.out.print("... with parent not defined ");
			pair = j.hasTwoDistanceConstraints(parent, null);
			pair2 = j.hasOneDistanceAndOneAlignmentConstraints(parent, null);
			pair3 = null;

			if (pair != null) {
				System.out.println("... with dist dist ");
				solveConstraints(pair.a.m_origin, j);
				solveConstraints(pair.b.m_origin, j);
				Point[] new_points = ConstraintSolver.solveDistanceDistance(pair.a, pair.b);

				if (new_points != null && m_hasSolution) {
					double old_angle = j.m_position.angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
					double new_angle0 = new_points[0].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);
					double new_angle1 = new_points[1].angle(pair.a.m_origin.m_position, pair.b.m_origin.m_position);

					if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[0].m_x;
						j.m_position.m_y = new_points[0].m_y;
					} else if (Math.abs(old_angle - new_angle0) > Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[1].m_x;
						j.m_position.m_y = new_points[1].m_y;
					} else {
						if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[0].m_x;
							j.m_position.m_y = new_points[0].m_y;
						} else {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[1].m_x;
							j.m_position.m_y = new_points[1].m_y;
						}
					}
				} else {
					m_hasSolution = false;
				}

				j.m_defined = true;
				System.out.println(" done dist dist");
			} else if (pair2 != null) {
				System.out.println("... with dist align");
				solveConstraints(pair2.a.m_origin, j);
				solveConstraints(pair2.b.m_origin, j);
				Point[] new_points = ConstraintSolver.solveDistanceAlignment(pair2.a, pair2.b);

				if (new_points != null && m_hasSolution) {
					double old_angle = j.m_position.angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
					double new_angle0 = new_points[0].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);
					double new_angle1 = new_points[1].angle(pair2.a.m_origin.m_position, pair2.b.m_origin.m_position);

					if (Math.abs(old_angle - new_angle0) < Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[0].m_x;
						j.m_position.m_y = new_points[0].m_y;
					} else if (Math.abs(old_angle - new_angle0) > Math.abs(old_angle - new_angle1)) {
						m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
						j.m_position.m_x = new_points[1].m_x;
						j.m_position.m_y = new_points[1].m_y;
					} else {
						if (j.m_position.distance(new_points[0]) < j.m_position.distance(new_points[1])) {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[0].m_x;
							j.m_position.m_y = new_points[0].m_y;
						} else {
							m_tempPos.put(j.m_position, new Point(j.m_position.m_x, j.m_position.m_y));
							j.m_position.m_x = new_points[1].m_x;
							j.m_position.m_y = new_points[1].m_y;
						}
					}
				} else {
					m_hasSolution = false;
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
