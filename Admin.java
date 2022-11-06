/*
 * Admin class created to store login information for admin users of the program.
 */

public class Admin {
	
	private String username;		// stores the user name of the admin
	private String password;		// stores the password of the admin
	
	// default constructor not used
	public Admin() {}
	
	 // arg constructor using username and password to generate an admin account
	public Admin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/***************************************************
	 *               Getters and Setters               *
	 ***************************************************/
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
