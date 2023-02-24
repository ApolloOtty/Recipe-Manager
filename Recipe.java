import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Recipe class contains the recipe details and allows for insertion and viewing of recipes from a database.
 * @author Diaconu Mihai Alexandru
 * @version 1.0
 * @since 28/01/2023
 */

public class Recipe {
	/**
	 * The name of the recipe
	 */
	String name;
	/**
	 * The ingredients of the recipe
	 */
	String ingredients;
	/**
	 * The instructions for the recipe
	 */
	String instructions;
	/**
	 * The category of the recipe
	 */
	String category;
	/**
	 * The time required to make the recipe
	 */
	String timp;

	/**
	 * Constructor for creating a new recipe
	 *
	 * @param UserID    the user ID of the user creating the recipe
	 * @param name      the name of the recipe
	 * @param category  the category of the recipe
	 * @param instructions the instructions for the recipe
	 * @param time      the time required to make the recipe
	 */
	Recipe(int UserID, String name, String category, String instructions, String time){
		this.name=name;
		this.instructions=instructions;
		this.category=category;
		this.timp=time;
	}

	/**
	 * Inserts the recipe into the database and returns the ID of the inserted recipe
	 *
	 * @param UserID    the user ID of the user creating the recipe
	 * @param name      the name of the recipe
	 * @param category  the category of the recipe
	 * @param instructions the instructions for the recipe
	 * @param time      the time required to make the recipe
	 * @return the ID of the inserted recipe
	 * @throws SQLException if there is an error with the SQL query
	 */
	int insertRecipeAndGetID(int UserID, String name, String category, String instructions, String time) throws SQLException{
		this.name=name;
		this.instructions=instructions;
		this.timp=time;
		time=time.replaceAll("[^\\d]", "");
		if (time.equals("130")) time="90";
		else if (time.equals("2")) time="120";
		else if (time.equals("1")) time="60";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			PreparedStatement stmt = null;
			st = con.createStatement();
			stmt = con.prepareStatement("INSERT INTO `recipe`(`userID`, `name`, `category`, `instructions`, `time`) VALUES "
					+ "('"+UserID+"', '"+name+"', '"+category+"','"+instructions+"','"+time+"')", Statement.RETURN_GENERATED_KEYS);
			stmt.execute();
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				int id = keys.getInt(1);
				return id;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	Recipe(){

	}

	/**
	 * This method retrieves the ingredients for a recipe and returns them as a string.
	 * @param selectedItem - The selected recipe to view ingredients for.
	 * @param userID - The user ID of the user who owns the recipe.
	 * @return - A string containing the ingredients and their weights in grams.
	 * @throws SQLException - If there is an error with the SQL query.
	 */
	public String viewIngredients(Object selectedItem, int userID) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql = "SELECT recipeingredientName, weight FROM recipeingredients " + 
					"WHERE recipeID = (SELECT recipeID FROM recipe WHERE userID = '"+userID+"' AND name = '"+selectedItem+"') " + 
					"AND userID = '"+userID+"'";
			ResultSet res = st.executeQuery(sql);
			String text1 = "";

			while (res.next()) {
				text1 += "• "+res.getString("recipeingredientName")+" "+res.getString("weight")+" grams"+"\n";
			}
			return text1;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}

	/**
	 * This method retrieves the instructions for a recipe and returns them as a string.
	 * @param selectedItem - The selected recipe to view instructions for.
	 * @return - A string containing the instructions for the recipe.
	 * @throws SQLException - If there is an error with the SQL query.
	 */
	public String viewInstructions(Object selectedItem) throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "Select instructions from recipe where name='"+selectedItem+"'";
			ResultSet res2 = st.executeQuery(sql2);

			String text = "";

			if (res2.next()) {
				text = res2.getString("instructions");
			}
			return text;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * This method verifies if the given category exists in the recipe table in the database
	 * @param category The category to be verified
	 * @return true if the category exists in the recipe table, false otherwise
	 * @throws SQLException if there is an error in connecting to the database or executing the query
	 */
	public boolean verifyCategory(String category) throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			ResultSet rs1 = st.executeQuery("SELECT category from recipe where category='"+category+"'");
			if(rs1.next())
				return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * This method inserts ingredients into the recipeingredients table in the database
	 * @param userID The ID of the user who is adding the ingredients
	 * @param RecipeID The ID of the recipe to which the ingredients are being added
	 * @param ingredientNamesArray The list of ingredient names
	 * @param weightArray The list of weights for each ingredient
	 * @throws SQLException if there is an error in connecting to the database or executing the query
	 */
	public void insertIngredients(int userID, int RecipeID, List<String> ingredientNamesArray, List<Float> weightArray) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			for (int i = 0; i < ingredientNamesArray.size(); i++) {
				String names = ingredientNamesArray.get(i);
				Float weights = weightArray.get(i);
				String rs1 = "INSERT INTO `recipeingredients`(`userID`, `recipeID`, `recipeIngredientName`, `weight`) VALUES "
						+ "('"+userID+"', '"+RecipeID+"', '"+names+"','"+weights+"')";
				st.executeUpdate(rs1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the total calories for a recipe
	 * @param userID The ID of the user who owns the recipe
	 * @param selectedItem The name of the recipe
	 * @return The total calories for the recipe as a string
	 * @throws SQLException if there is an error in connecting to the database or executing the query
	 */
	public String getCalories(int userID, Object selectedItem) throws SQLException {
		try {
			float calories=0;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT SUM(ingredients.ingredientCalories * recipeingredients.weight/100) as totalCalories " +
					"FROM recipe " +
					"JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID AND recipe.userID = recipeingredients.userID " +
					"JOIN ingredients ON recipeingredients.recipeingredientName = ingredients.ingredientName " +
					"WHERE recipe.name = '"+selectedItem+"' AND recipe.userID = '"+userID +"'";
			ResultSet res2 = st.executeQuery(sql2);


			if (res2.next()) {
				calories += Float.parseFloat(res2.getString("totalCalories"));
			}return  String.valueOf(calories);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * This method is used to get the protein content of a selected recipe for a specific user.
	 * @param userID  the userID of the user who is searching for the recipe
	 * @param selectedItem  the name of the selected recipe
	 * @return the protein content of the recipe as a string
	 * @throws SQLException if there is an error in the SQL query
	 */
	public String getProtein(int userID, Object selectedItem) throws SQLException {
		try {
			float calories=0;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT SUM(ingredients.protein * recipeingredients.weight/100) as totalProtein " +
					"FROM recipe " +
					"JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID AND recipe.userID = recipeingredients.userID " +
					"JOIN ingredients ON recipeingredients.recipeingredientName = ingredients.ingredientName " +
					"WHERE recipe.name = '"+selectedItem+"' AND recipe.userID = '"+userID +"'";
			ResultSet res2 = st.executeQuery(sql2);


			if (res2.next()) {
				calories += Float.parseFloat(res2.getString("totalProtein"));
			}return  String.valueOf(calories);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * This method is used to get the sugar content of a selected recipe for a specific user.
	 * @param userID  the userID of the user who is searching for the recipe
	 * @param selectedItem  the name of the selected recipe
	 * @return the sugar content of the recipe as a string
	 * @throws SQLException if there is an error in the SQL query
	 */
	public String getSugar(int userID, Object selectedItem) throws SQLException {
		try {
			float calories=0;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT SUM(ingredients.sugar * recipeingredients.weight/100) as totalSugar " +
					"FROM recipe " +
					"JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID AND recipe.userID = recipeingredients.userID " +
					"JOIN ingredients ON recipeingredients.recipeingredientName = ingredients.ingredientName " +
					"WHERE recipe.name = '"+selectedItem+"' AND recipe.userID = '"+userID +"'";
			ResultSet res2 = st.executeQuery(sql2);


			if (res2.next()) {
				calories += Float.parseFloat(res2.getString("totalSugar"));
			}return  String.valueOf(calories);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	/**

	checkIfFavorite method is used to check if a recipe is already marked as favorite by a user.
	@param object - recipe name
	@param userID - user id of the user
	@return boolean - returns true if the recipe is already marked as favorite, otherwise false
	 */
	public boolean checkIfFavorite(Object object, int userID) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT recipeName FROM favorite WHERE userID='"+userID+"' and recipeName='"+object+"'";
			ResultSet res2 = st.executeQuery(sql2);
			if (res2.next()) {
				return true;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**

	insertFavorite method is used to insert a recipe as favorite for a user.
	@param object - recipe name
	@param userID - user id of the user
	@throws SQLException - if an error occurs while interacting with the database
	 */
	public void insertFavorite(Object object, int userID) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "INSERT INTO favorite (recipeName, userID) VALUES ('"+object+"', '"+userID+"')";
			st.executeUpdate(sql2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**

	getFavoriteRecipes method is used to get the list of favorite recipes for a user.
	@param userID - user id of the user
	@return DefaultListModel - returns a DefaultListModel containing the list of favorite recipes
	@throws SQLException - if an error occurs while interacting with the database
	 */
	public DefaultListModel getFavoriteRecipes(int userID) throws SQLException {
		DefaultListModel model = new DefaultListModel();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT recipeName as name from favorite where userID='"+userID+"'";
			ResultSet res = st.executeQuery(sql2);

			while(res.next()) {
				model.addElement(res.getString("name"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}


	/**

	The deleteRecipe method deletes a recipe from the database based on the name and user ID provided.
	@param name the name of the recipe to be deleted
	@param userID the ID of the user who owns the recipe
	@throws SQLException if there is an error in the SQL query
	 */
	public void deleteRecipe(Object name, int userID) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "DELETE FROM recipe where userID='"+userID+"' AND name='"+name+"'";
			String sql3 = "DELETE FROM favorite where userID='"+userID+"' AND recipeName='"+name+"'";
			st.executeUpdate(sql2);
			st.executeUpdate(sql3);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**

	The getRecipeNames method retrieves all the names of the recipes belonging to a specific user from the database.
	@param userID the ID of the user whose recipes are being retrieved
	@return a DefaultComboBoxModel containing the names of the recipes belonging to the user
	@throws SQLException if there is an error in the SQL query
	 */
	public DefaultComboBoxModel getRecipeNames(int userID) throws SQLException{
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT distinct name FROM recipe where userID='" + User.UserID + "'";
			ResultSet res = st.executeQuery(sql2);
			while(res.next()) {
				model.addElement(res.getString("name"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}


	/**

	The getRecipeTime method retrieves the time required to prepare a specific recipe from the database.
	@param selectedItem the name of the recipe for which the time is being retrieved
	@param userID the ID of the user who owns the recipe
	@return a string representing the time required to prepare the recipe in the format "X minutes" or "X hours"
	@throws SQLException if there is an error in the SQL query
	 */
	public String getRecipeTime(Object selectedItem, int userID) throws SQLException {
		String timp="";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "SELECT time FROM recipe where userID='" + User.UserID + "' and name='"+selectedItem+"'";
			ResultSet res = st.executeQuery(sql2);
			while(res.next()) {
				timp=(res.getString("time"));
			}
			if (timp.equals("60"))
				timp="1 hour";
			else if(timp.equals("90"))
				timp="1 hour and 30 minutes";
			else if(timp.equals("120"))
				timp="2 hours";
			else timp+=" minutes";
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timp;
	}

	public void deleteFavorite(Object selectedItem, int userID) throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st;
			st = con.createStatement();
			String sql2 = "DELETE FROM favorite where userID='" + User.UserID + "' and recipeName='"+selectedItem+"'";
			st.executeUpdate(sql2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

