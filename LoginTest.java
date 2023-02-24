import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LoginTest {
  @Test
  public void testLoginTrue() {
    // Set up test data
    String email = "alex";
    String password = "123";

    // Set up test dependencies
    User user = new User();

    // Perform the test
    boolean result = user.verifyLogin(email, password);

    // Verify the result
    assertTrue(result);
  }
    @Test
    public void testLoginFalse() {
	    // Set up test data
	   String email2 = "exemplu.user@yahoo.com";
	   String password2 = "incorect";

	    // Set up test dependencies
	    User user2 = new User();

	    // Perform the test
	    boolean result2 = user2.verifyLogin(email2, password2);

	    // Verify the result
	    assertFalse(result2);
	  }
}