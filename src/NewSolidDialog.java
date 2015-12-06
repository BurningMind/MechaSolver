import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

public class NewSolidDialog extends JDialog implements ActionListener {
	MainWindow m_mainWindow;
	JComboBox<String> m_comboBox;

	JPanel m_cards;
	JPanel m_rectCard;

	JFormattedTextField m_rectWField;
	JFormattedTextField m_rectHField;

	JButton m_okButton;
	JButton m_cancelButton;

	public NewSolidDialog(MainWindow mainWindow) {
		m_mainWindow = mainWindow;

		Container pane = getContentPane();

		String[] solidTypes = {"Rectangle"};

		m_comboBox = new JComboBox<String>(solidTypes);
		m_comboBox.addActionListener(this);

		pane.add(m_comboBox, BorderLayout.PAGE_START);

		m_rectCard = new JPanel();
		m_rectCard.setLayout(new GridLayout(0, 2));
		m_rectWField = new JFormattedTextField(NumberFormat.getNumberInstance());
		m_rectHField = new JFormattedTextField(NumberFormat.getNumberInstance());
		m_rectCard.add(new JLabel("Width:"));
		m_rectCard.add(m_rectWField);
		m_rectCard.add(new JLabel("Height:"));
		m_rectCard.add(m_rectHField);

		m_cards = new JPanel(new CardLayout());
		m_cards.add(m_rectCard, "Rectangle");

		pane.add(m_cards, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		m_okButton = new JButton("OK");
		m_okButton.addActionListener(this);
		m_cancelButton = new JButton("Cancel");
		m_cancelButton.addActionListener(this);

		buttonPanel.add(m_okButton);
		buttonPanel.add(m_cancelButton);

		pane.add(buttonPanel, BorderLayout.PAGE_END);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_comboBox) {
			if (m_comboBox.getSelectedItem().toString() == "Rectangle") {
				CardLayout cl = (CardLayout)(m_cards.getLayout());
				cl.show(m_cards, "Rectangle");
			}
		} else if (e.getSource() == m_okButton) {
			Solid new_solid;
			if (m_comboBox.getSelectedItem().toString() == "Rectangle") {
				new_solid = new Rectangle(0, 0, Integer.parseInt(m_rectWField.getText()), Integer.parseInt(m_rectHField.getText()));
			} else {
				return;
			}

			m_mainWindow.addSolid(new_solid);
			setVisible(false);
		} else if (e.getSource() == m_cancelButton) {
			setVisible(false);
		}
	}
}
