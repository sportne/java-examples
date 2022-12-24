package my.security.encrypt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.common.base.Charsets;

public class AsymmetricEncryptionBytes {

	private static final String KEY_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 2048;

	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

	private static final Charset ENCRYPTION_CHAR_SET = Charsets.UTF_8;

	public static byte[] decrypt(byte[] encryptedBytes, PrivateKey privateKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		return decrypt(encryptedBytes, privateKey, CIPHER_ALGORITHM);
	}

	public static byte[] decrypt(byte[] encryptedBytes, PrivateKey privateKey, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher with the private key and the decryption mode
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		// Decrypt the encrypted bytes
		return cipher.doFinal(encryptedBytes);
	}

	public static byte[] encrypt(byte[] inputBytes, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		return encrypt(inputBytes, key, CIPHER_ALGORITHM);
	}

	public static byte[] encrypt(byte[] inputBytes, Key key, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher with the public key and the encryption mode
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		// Encrypt the input bytes
		return cipher.doFinal(inputBytes);
	}

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair(KEY_ALGORITHM);
	}

	public static KeyPair generateKeyPair(String algorithm) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
		keyGen.initialize(KEY_SIZE);
		return keyGen.generateKeyPair();
	}

	public static PrivateKey loadPrivateKey(String pathToKey)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read the private key file into a byte array
		Path privateKeyFile = Paths.get(pathToKey);
		byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile);

		// Create a KeyFactory for the RSA algorithm
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// Generate the private key from the key bytes
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		return keyFactory.generatePrivate(keySpec);
	}

	public static PublicKey loadPublicKey(String pathToKey)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read the public key file into a byte array
		Path publicKeyFile = Paths.get("path/to/public/key/file.key");
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile);

		// Create a KeyFactory for the RSA algorithm
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// Generate the public key from the key bytes
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		return keyFactory.generatePublic(keySpec);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		KeyPair keys = generateKeyPair();

		PrivateKey privateKey = keys.getPrivate();
		PublicKey publicKey = keys.getPublic();

		String original = "Message to encrypt";
		System.out.println("Original message: " + original);

		byte[] encryptedBytes = encrypt(original.getBytes(ENCRYPTION_CHAR_SET), publicKey);
		System.out.println("Encrypted message: " + new String(encryptedBytes, ENCRYPTION_CHAR_SET));

		byte[] decryptedBytes = decrypt(encryptedBytes, privateKey);
		System.out.println("Decrypted message: " + new String(decryptedBytes, ENCRYPTION_CHAR_SET));
	}

}
