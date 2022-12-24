package my.security.tls;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Arrays;

import my.security.encrypt.AsymmetricEncryptionBytes;

public class TrustStoreExample {
	public static void main(String[] args) throws Exception {

		KeyPair keyPair = AsymmetricEncryptionBytes.generateKeyPair();
		Certificate certificate = KeyStoreExample.createSelfSignedCertificate(keyPair);

		// Create a truststore and add the server's public certificate
		File trustStoreFile = new File("truststore.pkcs");
		char[] trustStorePassword = "here's a password".toCharArray();
		KeyStoreExample.createEmptyKeyStoreFile(trustStoreFile, trustStorePassword);
		KeyStore trustStore = KeyStoreExample.loadKeyStore(trustStoreFile, trustStorePassword);

		trustStore.load(null, null);
		trustStore.setCertificateEntry("server", certificate);

		// Write the truststore to a file
		try (FileOutputStream fos = new FileOutputStream(trustStoreFile)) {
			trustStore.store(fos, trustStorePassword);
		}

		System.out.println("Wrote a trust store holding " + trustStore.size() + " certificates");

		KeyStore loadedTrustStore = KeyStoreExample.loadKeyStore(trustStoreFile,
				trustStorePassword);
		System.out.println(
				"Loaded a trust store holding " + loadedTrustStore.size() + " certificates");

		Certificate loadedCert = loadedTrustStore.getCertificate("server");

		if (Arrays.equals(loadedCert.getEncoded(), certificate.getEncoded())) {
			System.out.println("Loaded certificate does equal the created certificate!");
		} else {
			System.out.println("Loaded certificate does NOT equal the created certificate!");
		}

		trustStoreFile.delete();
	}
}
