import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = getContentPane();

		JToolBar toolBar = new JToolBar();
		pane.add(toolBar, BorderLayout.PAGE_START);

		JButton sampleButton = new JButton("Test");
		toolBar.add(sampleButton);

		MainArea mainArea = new MainArea();
		pane.add(mainArea, BorderLayout.CENTER);

		Point oRect = new Point (0,0);
		Rectangle rect = new Rectangle(oRect, 100, 100);
		mainArea.addSolid(rect);

		pack();
		setVisible(true);
	}
}
