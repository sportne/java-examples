package my.security.password;

public class User {

	private final long id;
	private final String username;
	private final PasswordData passData;

	/**
	 * Constructor.
	 *
	 * @param id       the unique identifying number of this user.
	 * @param passData the PasswordData used to authenticate this user.
	 */
	public User(long id, String username, PasswordData passData) {
		this.id = id;
		this.username = username;
		this.passData = passData;
	}

	/**
	 * Authenticates that the provided password matches this user.
	 *
	 * @param password the String password to check.
	 * @return true if the password was correct and false otherwise.
	 */
	public boolean authenticate(char[] password) {
		return passData.authenticate(password);
	}

	/**
	 * @return the unique ID of this User.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the username for this user.
	 */
	public String getUsername() {
		return username;
	}

}
