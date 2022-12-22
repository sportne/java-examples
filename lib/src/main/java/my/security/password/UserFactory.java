package my.security.password;

public class UserFactory {

	static class UserIdProvider {
		private static long nextUserId = 0;

		public long getNewUserId() {
			return nextUserId++;
		}
	}

	private final UserIdProvider idProvider;

	public UserFactory() {
		idProvider = new UserIdProvider();
	}

	public UserFactory(UserIdProvider idProvider) {
		this.idProvider = idProvider;
	}

	/**
	 * Constructs a new User.
	 *
	 * @param username the desired user name
	 * @param password the user password
	 * @return a new User
	 */
	public User createUser(String username, char[] password) {
		long id = idProvider.getNewUserId();

		byte[] salt = PasswordData.generateSalt();
		byte[] passwordHash = PasswordData.generateHash(password, salt);
		PasswordData passwordData = new PasswordData(passwordHash, salt);

		return new User(id, username, passwordData);
	}

}
