import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**

AddIngredient is a class that creates a JFrame for adding new ingredients to a database.
The class includes a constructor, main method, and various components such as JTextFields,
JButtons, and JLabels for user input and interaction. The class also includes a listener for
the "Add" button, which, when clicked, adds the inputted ingredient information to the database
and updates the ingredient comboBox in the Dashboard class.
@author Diaconu Mihai Alexandru
*/

public class AddIngredient extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JLabel name;
	private JLabel calories;
	private JLabel protein;
	private JLabel sugar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddIngredient frame = new AddIngredient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddIngredient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(191, 29, 116, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(191, 71, 116, 20);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(191, 111, 116, 20);
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(191, 150, 116, 20);
		contentPane.add(textField_3);
		
		JButton add = new JButton("Add");
		add.setBounds(168, 227, 89, 23);
		contentPane.add(add);
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
				Statement st;
				st = con.createStatement();
				String sql2 = "INSERT INTO `ingredients`(`ingredientName`, `ingredientCalories`, `protein`, `sugar`) VALUES ('"+textField.getText()+"', '"+textField_1.getText() +"', '"+textField_2.getText()+"','"+textField_3.getText()+"')";
				int res2 = st.executeUpdate(sql2);
				} catch (ClassNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Dashboard.ingredientName.addItem(textField.getText());
				Dashboard.ingredientName.updateUI();
				dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.setBounds(335, 227, 89, 23);
		contentPane.add(cancel);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dashboard.ingredientName.updateUI();
				dispose();
			}
		});
		
		name = new JLabel("Name");
		name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		name.setBounds(10, 31, 46, 14);
		contentPane.add(name);
		
		calories = new JLabel("Calories (per 100 grams)");
		calories.setFont(new Font("Tahoma", Font.PLAIN, 13));
		calories.setBounds(10, 73, 153, 18);
		contentPane.add(calories);
		
		protein = new JLabel("Protein (per 100 grams)");
		protein.setFont(new Font("Tahoma", Font.PLAIN, 13));
		protein.setBounds(10, 114, 153, 17);
		contentPane.add(protein);
		
		sugar = new JLabel("Sugar (per 100 grams)");
		sugar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		sugar.setBounds(10, 153, 153, 17);
		contentPane.add(sugar);
	}
	
}
