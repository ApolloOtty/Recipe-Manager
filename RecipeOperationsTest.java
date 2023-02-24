import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
	import java.sql.SQLException;
class RecipeOperationsTest {
	Recipe recipe = new Recipe();

	    @Test
	    public void testGetCaloriesWrong() throws SQLException {
	        int userID = 1;
	        Object selectedItem = "Chocolate Cake";
	        String expectedCalories = "500.0";
	        String actualCalories = recipe.getCalories(userID, selectedItem);
	        assertEquals(expectedCalories, actualCalories);
	    }
	        
	       @Test
	      public void testGetCaloriesRight() throws SQLException {
	        int userID = 3;
	        Object selectedItem = "Chicken Tortilla";
	        String expectedCalories = "410.3";
	        String actualCalories = recipe.getCalories(userID, selectedItem);
	        assertEquals(expectedCalories, actualCalories);
	    }
	       @Test
	       public void testGetProteinWrong() throws SQLException {
		        int userID = 1;
		        Object selectedItem = "Chocolate Cake";
		        String expectedCalories = "500.0";
		        String actualCalories = recipe.getProtein(userID, selectedItem);
		        assertEquals(expectedCalories, actualCalories);
		    }
	       @Test
	       public void testGetProteinRight() throws SQLException {
		        int userID = 3;
		        Object selectedItem = "Chicken Tortilla";
		        String expectedCalories = "38.3";
		        String actualCalories = recipe.getProtein(userID, selectedItem);
		        assertEquals(expectedCalories, actualCalories);
		    }
	       
	       @Test
	       public void testGetSugarWrong() throws SQLException {
		        int userID = 2;
		        Object selectedItem = "Chicken Tortilla";
		        String expectedCalories = "38.3";
		        String actualCalories = recipe.getSugar(userID, selectedItem);
		        assertEquals(expectedCalories, actualCalories);
		    }
	       
	       @Test
	       public void testGetSugarRight() throws SQLException {
		        int userID = 3;
		        Object selectedItem = "Chicken Tortilla";
		        String expectedCalories = "6.2";
		        String actualCalories = recipe.getSugar(userID, selectedItem);
		        assertEquals(expectedCalories, actualCalories);
		    }
	       
	       @Test
	       public void testCheckIfFavoriteWrong() throws SQLException {
	           // Arrange
	           Object object = "Spaghetti";
	           int userID = 3;
	           
	           // Act
	           boolean result = recipe.checkIfFavorite(object, userID);
	           
	           // Assert
	           assertTrue(result);
	       }
	       
	       @Test
	       public void testCheckIfFavoriteRight() throws SQLException {
	           // Arrange
	           Object object = "Chicken Tortilla";
	           int userID = 3;
	           
	           // Act
	           boolean result = recipe.checkIfFavorite(object, userID);
	           
	           // Assert
	           assertTrue(result);
	       }
	   }


