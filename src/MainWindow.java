import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener {
	JButton m_addSolidButton;
	MainArea m_mainArea;
	NewSolidDialog m_newSolidDialog;

	//Constructor
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_newSolidDialog = new NewSolidDialog(this);

		Container pane = getContentPane();

		JToolBar toolBar = new JToolBar();
		pane.add(toolBar, BorderLayout.PAGE_START);

		m_addSolidButton = new JButton("Add Solid");
		m_addSolidButton.addActionListener(this);
		toolBar.add(m_addSolidButton);

		m_mainArea = new MainArea();
		pane.add(m_mainArea, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_addSolidButton) {
			m_newSolidDialog.setVisible(true);
		}
	}

	public void addSolid(Solid solid) {
		m_mainArea.addSolid(solid);
	}
}
