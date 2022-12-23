package my.security.tls;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class TrustStoreExample {
	public static void main(String[] args) throws Exception {
		// Load the server's public certificate
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		FileInputStream certificateInputStream = new FileInputStream("server.crt");
		X509Certificate certificate = (X509Certificate) certificateFactory
				.generateCertificate(certificateInputStream);
		certificateInputStream.close();

		// Create a truststore and add the server's public certificate
		KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(null, null);
		trustStore.setCertificateEntry("server", certificate);

		// Write the truststore to a file
		trustStore.store(new FileOutputStream("truststore.jks"), "storepwd".toCharArray());
	}
}
