package my.security.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordData {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final int ITERATIONS = 10000;

	private static final int KEY_LENGTH = 256;

	/**
	 * Generates a password hash based on a char array representation of a password
	 * and an array of bytes representing a salt.
	 *
	 * @param password the char array representing the provided password
	 * @param salt     the byte array salt that goes with the password. the salt is
	 *                 tied to a particular user.
	 * @return a byte array hash of the provided password.
	 */
	public static byte[] generateHash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} finally {
			spec.clearPassword();
			// also clear out the provided password, upon creation of the hash there is no
			// need to keep the password in memory
			System.arraycopy(new char[password.length], 0, password, 0, password.length);
		}
	}

	/**
	 * Generates a new salt for use in creation of a password hash.
	 *
	 * @return a byte array representing the password salt.
	 */
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private final byte[] passwordHash;
	private final byte[] passwordSalt;

	PasswordData(byte[] passwordHash, byte[] passwordSalt) {
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
	}

	public boolean authenticate(char[] password) {
		return Arrays.equals(passwordHash, generateHash(password, passwordSalt));
	}

}
