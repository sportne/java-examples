package my.security.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.google.common.base.Charsets;

public class SymmetricEncryptBytes {

	private static final String CIPHER_ALGORITHM = "AES";
	private static final Charset ENCRYPTION_CHAR_SET = Charsets.UTF_8;

	public static void decrypt(Key key, InputStream is, OutputStream os) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		CipherOutputStream cos = new CipherOutputStream(os, cipher);
		doCopy(is, cos);
	}

	public static void doCopy(InputStream is, OutputStream os) throws Exception {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}

	public static void encrypt(Key key, InputStream is, OutputStream os) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		doCopy(cis, os);
	}

	public static void main(String[] args) throws Exception {

		SecretKey key = KeyGenerator.getInstance(CIPHER_ALGORITHM).generateKey();

		String theOriginalString = "The thing to be encrypted";
		System.out.println(theOriginalString);

		byte[] inputBytes = theOriginalString.getBytes(ENCRYPTION_CHAR_SET);
		InputStream is = new ByteArrayInputStream(inputBytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		encrypt(key, is, os);

		byte[] encryptedBytes = os.toByteArray();
		System.out.println(new String(encryptedBytes, ENCRYPTION_CHAR_SET));

		InputStream encryptedStream = new ByteArrayInputStream(encryptedBytes);
		ByteArrayOutputStream decryptedStream = new ByteArrayOutputStream();
		decrypt(key, encryptedStream, decryptedStream);

		System.out.println(new String(decryptedStream.toByteArray(), ENCRYPTION_CHAR_SET));
	}

}
