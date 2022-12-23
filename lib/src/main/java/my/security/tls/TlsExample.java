package my.security.tls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.google.common.base.Charsets;

public class TlsExample {

	private static final String KEYSTORE_TYPE = "JKS";

	private static final String TLS_PROTOCOL = "TLSv1.3";
	private static final String TLS_CIPHER = "TLS_AES_128_GCM_SHA256";

	private static final String HOSTNAME = "localhost";
	private static final int PORT = 8000;

	private static SSLContext sslContext;
	private static SSLSocketFactory sslSocketFactory;
	
	private static final Charset ENCRYPTION_CHAR_SET = Charsets.UTF_8;

	public static void closeSocket(SSLSocket socket) throws IOException {
		// Close the SSL socket
		socket.close();
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

	public static void init(String clientKeyStoreFile, char[] clientKeyStorePassword,
			String clientKeyPassword, String trustStoreFile, char[] trustStorePassword)
			throws Exception {
		// Load the keystore containing the client's private key and certificate
		KeyStore clientKeyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		System.out.println(new File(clientKeyStoreFile).getAbsolutePath());
		clientKeyStore.load(new FileInputStream(clientKeyStoreFile), clientKeyStorePassword);

		// Create a key manager factory to use the client's private key and certificate
		KeyManagerFactory kmf = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientKeyStore, clientKeyPassword.toCharArray());

		// Load the truststore containing the server's public certificate
		KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);
		trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);

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
		// Initialize the SSL context and socket factory
		init("client.jks", "clientpwd".toCharArray(), "clientpwd", "server.jks",
				"serverpwd".toCharArray());

		// Create an SSL socket to connect to the server
		SSLSocket sslSocket = createSocket(HOSTNAME, PORT);

		// Send a message to the server
		sendMessage(sslSocket, "Hello, Server!");

		// Receive a message from the server
		String message = receiveMessage(sslSocket);
		System.out.println("Received from server: " + message);

		// Close the SSL socket
		closeSocket(sslSocket);
	}

	public static String receiveMessage(SSLSocket socket) throws IOException {
		// Receive a message from the server
		InputStream inputStream = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String message = reader.readLine();
		return message;
	}

	public static void sendMessage(SSLSocket socket, String message) throws IOException {
		// Send a message to the server
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(message.getBytes(ENCRYPTION_CHAR_SET));
		outputStream.flush();
	}

	/**
	 * This code loads the client's private key and certificate from a keystore
	 * file, and the server's public certificate from a truststore file. It then
	 * initializes an SSL context and creates an SSL socket factory using these
	 * certificates. Finally, it creates an SSL socket and establishes a secure
	 * connection to the server using the TLS 1.3 protocol and the AES 128 GCM
	 * cipher suite. After the handshake is completed, the client can send a message
	 * to the server and receive a response.
	 *
	 * @param args unused arguments
	 * @throws Exception a generic throwing of exceptions
	 */
	public static void singleMethod(String[] args) throws Exception {
		// Load the keystore containing the client's private key and certificate
		KeyStore clientKeyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		clientKeyStore.load(new FileInputStream("client.jks"), "clientpwd".toCharArray());

		// Create a key manager factory to use the client's private key and certificate
		KeyManagerFactory kmf = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientKeyStore, "clientpwd".toCharArray());

		// Load the truststore containing the server's public certificate
		KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);
		trustStore.load(new FileInputStream("server.jks"), "serverpwd".toCharArray());

		// Create a trust manager factory to use the server's public certificate
		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		// Initialize SSL context
		SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		// Create an SSL socket factory
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

		// Create an SSL socket to connect to the server
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(HOSTNAME, PORT);

		// Set the enabled TLS protocols and cipher suites
		sslSocket.setEnabledProtocols(new String[] { TLS_PROTOCOL });
		sslSocket.setEnabledCipherSuites(new String[] { TLS_CIPHER });

		// Start the handshake
		sslSocket.startHandshake();

		// Send a message to the server
		OutputStream outputStream = sslSocket.getOutputStream();
		outputStream.write("Hello, Server!".getBytes(ENCRYPTION_CHAR_SET));
		outputStream.flush();

		// Receive a message from the server
		InputStream inputStream = sslSocket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String message = reader.readLine();
		System.out.println("Received from server: " + message);

		// Close the SSL socket
		sslSocket.close();
	}

}
