package my.security.tls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.google.common.base.Charsets;

import my.security.encrypt.AsymmetricEncryptionBytes;

public class TlsExample {

	private static final String TLS_PROTOCOL = "TLSv1.3";
	private static final String TLS_CIPHER = "TLS_AES_128_GCM_SHA256";
	private static final String KEYSTORE_TYPE = "PKCS12";
	private static final String SIGNATURE_ALG = "SHA256WithRSA";
	private static final String KEY_ALGORITHM = "RSA";

	private static final String HOSTNAME = "localhost";

	private static SSLContext sslContext;
	private static SSLSocketFactory sslSocketFactory;

	private static final Charset ENCRYPTION_CHAR_SET = Charsets.UTF_8;

	public static void closeSocket(SSLSocket socket) throws IOException {
		// Close the SSL socket
		socket.close();
	}

	public static KeyStore createKeyStore(KeyPair keyPair, Certificate certificate,
			File keyStoreFile, char[] keyStorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStoreExample.createEmptyKeyStoreFile(KEYSTORE_TYPE, keyStoreFile, keyStorePassword);
		KeyStore keyStore = KeyStoreExample.loadKeyStore(KEYSTORE_TYPE, keyStoreFile,
				keyStorePassword);

		KeyStoreExample.storeKey(keyPair, certificate, keyStore, keyStorePassword, keyStoreFile);

		return keyStore;
	}

	public static SSLSocket createSocket(String host, int port) throws IOException {
		// Create an SSL socket to connect to the server
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);

		// Set the enabled TLS protocols and cipher suites
		sslSocket.setEnabledProtocols(new String[] { TLS_PROTOCOL });
		sslSocket.setEnabledCipherSuites(new String[] { TLS_CIPHER });

		// Start the handshake
		sslSocket.startHandshake();

		return sslSocket;
	}

	public static KeyStore createTrustStore(Certificate certificate, File trustStoreFile,
			char[] trustStorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStoreExample.createEmptyKeyStoreFile(KEYSTORE_TYPE, trustStoreFile, trustStorePassword);
		KeyStore trustStore = KeyStoreExample.loadKeyStore(KEYSTORE_TYPE, trustStoreFile,
				trustStorePassword);

		trustStore.load(null, null);
		trustStore.setCertificateEntry("server", certificate);

		return trustStore;
	}

	public static void init(File keyStoreFile, char[] keyStorePassword, File trustStoreFile,
			char[] trustStorePassword) throws Exception {

		// create some keys and certificates to use
		KeyPair keyPair = AsymmetricEncryptionBytes.generateKeyPair(KEY_ALGORITHM);
		Certificate certificate = KeyStoreExample.createSelfSignedCertificate(SIGNATURE_ALG,
				keyPair);

		// create a key store to use
		KeyStore keyStore = createKeyStore(keyPair, certificate, keyStoreFile, keyStorePassword);
		System.out.println(keyStoreFile.getAbsolutePath());

		// Create a key manager factory to use the client's private key and certificate
		KeyManagerFactory kmf = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keyStorePassword);

		// Create a trust store for the certificates
		KeyStore trustStore = createTrustStore(certificate, trustStoreFile, trustStorePassword);
		System.out.println(trustStoreFile.getAbsolutePath());

		// Create a trust manager factory to use the server's public certificate
		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		// Initialize SSL context
		sslContext = SSLContext.getInstance(TLS_PROTOCOL);
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		// Create an SSL socket factory
		sslSocketFactory = sslContext.getSocketFactory();
	}

	/**
	 * To use these methods in your own code, you will need to call the init method
	 * to initialize the SSL context and socket factory, using your own keystore and
	 * truststore files. Then, you can use the createSocket method to create an SSL
	 * socket and establish a secure connection to the server. You can use the
	 * sendMessage and receiveMessage methods to send and receive messages over the
	 * secure connection, and finally, you can use the closeSocket method to close
	 * the connection when you are done.
	 *
	 * @param args unused arguments
	 * @throws Exception general thrown exception
	 */
	public static void main(String[] args) throws Exception {

		File keyStoreFile = new File("keystore.pkcs");
		File trustStoreFile = new File("truststore.pkcs");

		// Initialize the SSL context and socket factory
		init(keyStoreFile, "keyPwd".toCharArray(), trustStoreFile, "trustPwd".toCharArray());

		try (TlsEchoServer server = TlsEchoServer.create(0, sslContext,
				new String[] { TLS_PROTOCOL }, new String[] { TLS_CIPHER })) {
			new Thread(server).start();
			Thread.sleep(1000);

			// Create an SSL socket to connect to the server
			SSLSocket sslSocketOnClient = createSocket(HOSTNAME, server.port());

			// Send a message to the server
			sendMessage(sslSocketOnClient, "Hello, Server!");

			// Receive a message from the server
			String message = receiveMessage(sslSocketOnClient);
			System.out.println(message);

			// Close the SSL socket
			closeSocket(sslSocketOnClient);

			keyStoreFile.delete();
			trustStoreFile.delete();
		}
	}

	public static String receiveMessage(SSLSocket socket) throws IOException {
		// Receive a message from the server
		InputStream inputStream = new BufferedInputStream(socket.getInputStream());
		byte[] data = new byte[2048];
		int len = inputStream.read(data);
		if (len <= 0) {
			throw new IOException("no data received");
		}
		System.out.printf("client received %d bytes: ", len);
		String message = new String(data, 0, data.length);
		return message;
	}

	public static void sendMessage(SSLSocket socket, String message) throws IOException {
		// Send a message to the server
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(message.getBytes(ENCRYPTION_CHAR_SET));
		outputStream.flush();
	}

}
