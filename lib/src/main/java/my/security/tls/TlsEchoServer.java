package my.security.tls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import com.google.common.base.Charsets;

public class TlsEchoServer implements Runnable, AutoCloseable {

	private static final String TLS_PROTOCOL = "TLSv1.3";
	private static final String TLS_CIPHER = "TLS_AES_128_GCM_SHA256";

	private static final int FREE_PORT = 0;

	public static TlsEchoServer create() throws IOException {
		return create(FREE_PORT);
	}

	public static TlsEchoServer create(int port) throws IOException {
		return create(port, null, new String[] { TLS_PROTOCOL }, new String[] { TLS_CIPHER });
	}

	public static TlsEchoServer create(int port, SSLContext context, String[] protocols,
			String[] cipher_suites) throws IOException {
		SSLServerSocket socket;
		if (context == null) {
			socket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(port);
		} else {
			socket = (SSLServerSocket) context.getServerSocketFactory().createServerSocket(port);
		}
		socket.setEnabledProtocols(protocols);
		socket.setEnabledCipherSuites(cipher_suites);
		return new TlsEchoServer(socket);
	}

	private final SSLServerSocket sslServerSocket;

	private TlsEchoServer(SSLServerSocket sslServerSocket) {
		this.sslServerSocket = sslServerSocket;
	}

	@Override
	public void close() throws IOException {
		if (sslServerSocket != null && !sslServerSocket.isClosed()) {
			sslServerSocket.close();
		}
	}

	public int port() {
		return sslServerSocket.getLocalPort();
	}

	@Override
	public void run() {
		System.out.printf("server started on port %d%n", port());

		try (SSLSocket socket = (SSLSocket) sslServerSocket.accept()) {
			System.out.println("accepted");
			InputStream is = new BufferedInputStream(socket.getInputStream());
			OutputStream os = new BufferedOutputStream(socket.getOutputStream());
			byte[] data = new byte[2048];
			int len = is.read(data);
			if (len <= 0) {
				throw new IOException("no data received");
			}
			String sentMessage = new String(data, 0, len, Charsets.UTF_8);
			System.out.printf("server received %d bytes: %s%n", len, sentMessage);

			os.write(data, 0, len);
			os.flush();
		} catch (Exception e) {
			System.out.printf("exception: %s%n", e.getMessage());
		}

		System.out.println("server stopped");
	}
}
