import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginWindow {

	JFrame login;
	private JTextField txtEmail;
	private JTextField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void loginwin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
					window.login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		login = new JFrame();
		login.getContentPane().setBackground(new Color(72, 61, 139));
		login.setBounds(100, 100, 415, 377);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
		lblNewLabel.setBounds(-88, 23, 414, 37);
		login.getContentPane().add(lblNewLabel);
		
		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		txtEmail.setColumns(10);
		txtEmail.setBounds(30, 97, 184, 20);
		login.getContentPane().add(txtEmail);
		
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		txtPassword.setColumns(10);
		txtPassword.setBounds(30, 153, 184, 20);
		login.getContentPane().add(txtPassword);
		
		JLabel lblNewLabel_1_2 = new JLabel("Email:");
		lblNewLabel_1_2.setForeground(SystemColor.desktop);
		lblNewLabel_1_2.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 18));
		lblNewLabel_1_2.setBounds(30, 72, 70, 14);
		login.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Password:");
		lblNewLabel_1_2_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_2_1.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 18));
		lblNewLabel_1_2_1.setBounds(30, 128, 113, 14);
		login.getContentPane().add(lblNewLabel_1_2_1);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBackground(new Color(123, 104, 238));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(User.verifyLogin(txtEmail.getText(), txtPassword.getText())) {
						User utilizator = new User(txtEmail.getText());
						login.dispose();
						Dashboard reg;
						try {
							reg = new Dashboard(utilizator);
							reg.dashboard.setVisible(true);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					else
						JOptionPane.showMessageDialog(null, "Wrong");
			}
		});
		btnNewButton.setBounds(82, 255, 89, 23);
		login.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\Screenshot_11_2_406x343.png"));
		lblNewLabel_1.setBounds(0, 0, 406, 344);
		login.getContentPane().add(lblNewLabel_1);
	}
}
