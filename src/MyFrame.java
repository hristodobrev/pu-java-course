import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MyFrame extends JFrame {
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet result = null;
	int id = -1;

	String[] genders = { "Мъж", "Жена" };
	
	JTabbedPane tab = new JTabbedPane();
	JPanel personPanel = new JPanel();
	JPanel carsPanel = new JPanel();
	
	JPanel topPanel = new JPanel();
	JPanel middlePanel = new JPanel();
	JPanel bottomPanel = new JPanel();

	JLabel firstNameLabel = new JLabel("Име");
	JLabel lastNameLabel = new JLabel("Фамилия");
	JLabel genderLabel = new JLabel("Пол");
	JLabel ageLabel = new JLabel("Години");
	JLabel salaryLabel = new JLabel("Заплата");

	JTextField firstNameField = new JTextField();
	JTextField lastNameField = new JTextField();
	JComboBox<String> genderComboBox = new JComboBox<String>(genders);
	JTextField ageField = new JTextField();
	JTextField salaryField = new JTextField();

	JButton addButton = new JButton("Добавяне");
	JButton deleteButton = new JButton("Триене");
	JButton editButton = new JButton("Промяна");

	JTable peopleTable = new JTable();
	JScrollPane peopleTableScroll = new JScrollPane(peopleTable);

	public MyFrame() {
		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.setLayout(new GridLayout(3, 1));

		topPanel.setLayout(new GridLayout(5, 2));
		topPanel.add(firstNameLabel);
		topPanel.add(firstNameField);
		topPanel.add(lastNameLabel);
		topPanel.add(lastNameField);
		topPanel.add(genderLabel);
		topPanel.add(genderComboBox);
		topPanel.add(ageLabel);
		topPanel.add(ageField);
		topPanel.add(salaryLabel);
		topPanel.add(salaryField);

		middlePanel.add(addButton);
		middlePanel.add(deleteButton);
		middlePanel.add(editButton);
		addButton.addActionListener(new AddAction());
		deleteButton.addActionListener(new RemoveAction());

		peopleTableScroll.setPreferredSize(new Dimension(350, 150));
		bottomPanel.add(peopleTableScroll);
		peopleTable.addMouseListener(new MouseAction());

		personPanel.add(topPanel);
		personPanel.add(middlePanel);
		personPanel.add(bottomPanel);
		
		tab.add(personPanel, "Persons");
		tab.add(carsPanel, "Cars");

		this.add(tab);
		
		refreshTable();

		this.setVisible(true);
	}

	private void refreshTable() {
		connection = DbConnection.getConnection();
		try {
			statement = connection.prepareStatement("SELECT * FROM PEOPLE");

			result = statement.executeQuery();

			peopleTable.setModel(new MyModel(result));
		} catch (SQLException ex) {
			System.out.println("Error while trying to get data from DB:");
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			System.out.println("Error while trying to bind data from DB:");
			System.out.println(ex.getMessage());
		}
	}

	private void clearForm() {
		firstNameField.setText("");
		lastNameField.setText("");
		ageField.setText("");
		salaryField.setText("");
	}

	class AddAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Person inserted - " + firstNameField.getText() + " " + lastNameField.getText() + " "
					+ genderComboBox.getSelectedItem().toString() + " " + ageField.getText() + " "
					+ salaryField.getText());

			connection = DbConnection.getConnection();
			String query = "INSERT INTO PEOPLE(FIRST_NAME, LAST_NAME, GENDER, AGE, SALARY) VALUES(?,?,?,?,?)";

			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, firstNameField.getText());
				statement.setString(2, lastNameField.getText());
				statement.setString(3, genderComboBox.getSelectedItem().toString());
				statement.setInt(4, Integer.parseInt(ageField.getText()));
				statement.setFloat(5, Float.parseFloat(salaryField.getText()));

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to insert person in DB:");
				System.out.println(ex.getMessage());
			}

			refreshTable();
			clearForm();
		}
	}

	class RemoveAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (id > -1) {
				System.out.println("Person deleted - " + firstNameField.getText() + " " + lastNameField.getText() + " "
						+ genderComboBox.getSelectedItem().toString() + " " + ageField.getText() + " "
						+ salaryField.getText());

				connection = DbConnection.getConnection();
				String query = "DELETE FROM PEOPLE WHERE ID = ?";

				try {
					statement = connection.prepareStatement(query);
					statement.setInt(1, id);

					statement.execute();
				} catch (SQLException ex) {
					System.out.println("Error while trying to delete person from DB:");
					System.out.println(ex.getMessage());
				}

				refreshTable();
				clearForm();
				id = -1;
			}
		}
	}

	class RefreshAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (id > -1) {
				System.out.println("Person deleted - " + firstNameField.getText() + " " + lastNameField.getText() + " "
						+ genderComboBox.getSelectedItem().toString() + " " + ageField.getText() + " "
						+ salaryField.getText());

				connection = DbConnection.getConnection();
				String query = "DELETE FROM PEOPLE WHERE ID = ?";

				try {
					statement = connection.prepareStatement(query);
					statement.setInt(1, id);

					statement.execute();
				} catch (SQLException ex) {
					System.out.println("Error while trying to delete person from DB:");
					System.out.println(ex.getMessage());
				}

				refreshTable();
				clearForm();
				id = -1;
			}
		}
	}

	class MouseAction implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = peopleTable.getSelectedRow();
			id = Integer.parseInt(peopleTable.getValueAt(row, 0).toString());
			firstNameField.setText(peopleTable.getValueAt(row, 1).toString());
			lastNameField.setText(peopleTable.getValueAt(row, 2).toString());
			genderComboBox.setSelectedItem(peopleTable.getValueAt(row, 3).toString());
			ageField.setText(peopleTable.getValueAt(row, 4).toString());
			salaryField.setText(peopleTable.getValueAt(row, 5).toString());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
