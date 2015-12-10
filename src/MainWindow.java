import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class MainWindow extends JFrame implements ActionListener {
	JButton m_addRevoluteButton;
	JButton m_addLineButton;
	JButton m_clear;
	MainArea m_mainArea;

	public HashSet<Solid> m_solids;
	public HashSet<Joint> m_joints;

	//Constructor
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("J'aime la baguette");

		Container pane = getContentPane();

		JToolBar toolBar = new JToolBar();
		pane.add(toolBar, BorderLayout.PAGE_START);

		m_addRevoluteButton = new JButton("Add Revolute");
		m_addRevoluteButton.addActionListener(this);
		toolBar.add(m_addRevoluteButton);

		m_addLineButton = new JButton("Add Line");
		m_addLineButton.addActionListener(this);
		toolBar.add(m_addLineButton);

		m_clear = new JButton ("Clear");
		m_clear.addActionListener(this);
		toolBar.add(m_clear);

		m_mainArea = new MainArea(this);
		pane.add(m_mainArea, BorderLayout.CENTER);

		m_solids = new HashSet<Solid>();
		m_joints = new HashSet<Joint>();

		pack();
		setSize(1200,700);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_addRevoluteButton) {
			m_mainArea.m_mode = MainArea.Mode.REVOLUTE;
		} else if (e.getSource() == m_addLineButton) {
			m_mainArea.m_mode = MainArea.Mode.LINE1;
		} else if (e.getSource() == m_clear) {
			m_solids.clear();
			m_joints.clear();
			m_mainArea.repaint();
		}
	}
}
