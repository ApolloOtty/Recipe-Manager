import java.awt.EventQueue;




import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.sql.*;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

The RegisterWindow class creates a GUI for user registration, including email, password, and password verification fields.
It also includes methods for input validation, database connection, and error handling.
@author Diaconu Mihai Alexandru
@version 1.0
@since 28/01/2023
*/

public class RegisterWindow {
	/**
	 * Declare the JFrame for the register window and the text fields for email, password, and password verification.
	 */
	JFrame register;
	private JTextField textEmail;
	private JTextField textPassword;
	private JTextField textVerifyPassword;

	/**
	 * Launch the application.
	 */
	public static void registerwin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterWindow window = new RegisterWindow();
					window.register.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RegisterWindow() {
		initialize();
	}
	/**
	 * Create the frame.
	 */
	private void initialize() {
		register=new JFrame();
		register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		register.setBounds(100, 100, 450, 300);

		register.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Register");
		lblNewLabel.setForeground(SystemColor.window);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 30));
		lblNewLabel.setBounds(25, 11, 382, 36);
		register.getContentPane().add(lblNewLabel);
		
		textVerifyPassword = new JPasswordField();
		textVerifyPassword.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		textVerifyPassword.setBounds(25, 143, 187, 20);
		register.getContentPane().add(textVerifyPassword);
		textVerifyPassword.setColumns(10);
		
		textPassword = new JPasswordField();
		textPassword.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		textPassword.setBounds(25, 195, 187, 20);
		register.getContentPane().add(textPassword);
		textPassword.setColumns(10);
		
		textEmail = new JTextField();
		textEmail.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		textEmail.setColumns(10);
		textEmail.setBounds(25, 87, 187, 20);
		register.getContentPane().add(textEmail);
		
		JLabel lblNewLabel_1 = new JLabel("Email:");
		lblNewLabel_1.setForeground(SystemColor.window);
		lblNewLabel_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(25, 62, 87, 14);
		register.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password:");
		lblNewLabel_2.setForeground(SystemColor.window);
		lblNewLabel_2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(25, 118, 133, 14);
		register.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Verify Password:");
		lblNewLabel_2_1.setForeground(SystemColor.window);
		lblNewLabel_2_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_2_1.setBounds(25, 160, 167, 36);
		register.getContentPane().add(lblNewLabel_2_1);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					  Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
						        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
					  Matcher matcher = pattern.matcher(textEmail.getText());
					if((textPassword.getText()).equals(textVerifyPassword.getText()) && matcher.find()) {
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
					Statement st = con.createStatement();
					String sql = "Select * from user where email='"+textEmail.getText()+"' and password='"+textPassword.getText().toString()+"'";
					ResultSet res = st.executeQuery(sql);
					if(res.next()) {
						JOptionPane.showMessageDialog(null, "User already exists");
					}
					else {
						register.dispose();
						LoginWindow reg = new LoginWindow();
						reg.login.setVisible(true);
						JOptionPane.showMessageDialog(null, "Account successfully created!");
						//Creare user si inserare in baza de date
						User user = new User(textEmail.getText(), textPassword.getText());
						
					}
					}else JOptionPane.showMessageDialog(null, "Passwords do not match or email is not valid");
				}catch(Exception err) {System.out.print(err);}
			}
		});
		btnNewButton.setBounds(160, 227, 89, 23);
		register.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\photo_2023-01-14_15-40-21_434x261.jpg"));
		lblNewLabel_3.setBounds(0, 0, 434, 261);
		register.getContentPane().add(lblNewLabel_3);
	}
}
