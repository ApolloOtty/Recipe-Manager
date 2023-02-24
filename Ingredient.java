import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
/**

Ingredient class represents an ingredient in a recipe
@author Diaconu Mihai Alexandru
@version 1.0
@since 28/01/2023
*/

public class Ingredient {
	String name;
	float weight;
	int calories;
	
	Ingredient(){
		
	}
	/**

	constructor that takes name and weight
	@param name name of ingredient
	@param weight weight of ingredient
	*/
	Ingredient(String name, float weight){
		this.name=name;
		this.weight=weight;
	}
	/**

	method that verifies if ingredient exists in database
	@param name name of ingredient
	@param weight weight of ingredient
	@return true if ingredient exists in database, false otherwise
	*/
	static boolean verifyIngredient(String name, float weight) {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
		Statement st = con.createStatement();
		String sql = "Select * from ingredients where ingredientName='"+name+"'";
		ResultSet res = st.executeQuery(sql);
		if(res.next()) {
			return true;
		}
		}catch(Exception err) {System.out.print(err);}
		return false;
	}
}
