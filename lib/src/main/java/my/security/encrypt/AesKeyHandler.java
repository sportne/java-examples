package my.security.encrypt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

public class AesKeyHandler {

	private static final String ALGORITHM_STRING = "AES";

	public static Key loadKey(String keyFile) throws Exception {
		return loadKey(keyFile, ALGORITHM_STRING);
	}

	public static Key loadKey(String keyFile, String keyAlgorithm) throws Exception {
		try (FileInputStream keyStream = new FileInputStream(keyFile)) {
			byte[] rawKey = new byte[keyStream.available()];
			keyStream.read(rawKey);
			return new SecretKeySpec(rawKey, keyAlgorithm);
		}
	}

	public static void saveKey(Key key, String keyFile) throws Exception {
		try (FileOutputStream keyStream = new FileOutputStream(keyFile)) {
			keyStream.write(key.getEncoded());
		}
	}

}
