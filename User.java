import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

/**

The User class represents a user in the system and includes information such as email and password.

It contains constructors for creating a new user and logging in an existing user.

It also has a method for verifying the user's login credentials.
*/

public class User {
	String email;
	String password;
	static int UserID;
	/**

	Default constructor for the User class
	*/
	User(){
		
	}
	/**

	Constructor for creating a User object with only an email
	@param email the email of the user
	*/
	User (String email){
		this.email=email;
	}
	/**

	Constructor for creating a User object with only a userID
	@param UserID the unique ID of the user
	*/
	User (int UserID){
		this.UserID=UserID;
	}
	/**

	Constructor for creating a new user with email and password

	@param email the email of the user

	@param password the password of the user
	*/
	User(String email, String password){
		this.email=email;
		this.password=password;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st = con.createStatement();
			String sqlinsert = "Insert into user (email, password) Values ('"+email+"', '"+password+"')";
			st.executeUpdate(sqlinsert);
			}catch(Exception err) {System.out.print(err);}
		}
	/**

	Method for verifying the user's login credentials
	@param email the email of the user
	@param password the password of the user
	@return true if the login is successful, false otherwise
	*/
	static boolean verifyLogin(String email, String password) {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
		Statement st = con.createStatement();
		String sql = "Select * from user where email='"+email+"' and password='"+password+"'";
		ResultSet res = st.executeQuery(sql);
		if(res.next()) {
			UserID=res.getInt("userID");
			return true;
		}
		}catch(Exception err) {System.out.print(err);}
		return false;
	}
	/**

	Overrides the toString method to return the email of the user
	@return the email of the user
	*/
	@Override
	public String toString() {
		return email;
	}
}
