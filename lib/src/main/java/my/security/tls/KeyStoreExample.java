package my.security.tls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import my.security.encrypt.AsymmetricEncryptionBytes;

public class KeyStoreExample {

	private static final String KEYSTORE_TYPE = "PKCS12";
	private static final String SIGNATURE_ALG = "SHA256WithRSA";

	public static void createEmptyKeyStoreFile(File newKeyStoreFile, char[] keystorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		ks.load(null, keystorePassword);

		// Store away the keystore.
		try (FileOutputStream fos = new FileOutputStream(newKeyStoreFile)) {
			ks.store(fos, keystorePassword);
		}
	}

	public static KeyStore loadKeyStore(File keyStoreFile, char[] keystorePassword)
			throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException,
			CertificateException {
		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);

		try (FileInputStream fis = new FileInputStream(keyStoreFile)) {
			ks.load(fis, keystorePassword);
		}

		return ks;
	}

	public static Certificate createSelfSignedCertificate(KeyPair keyPair)
			throws OperatorCreationException, CertificateException {
		// Generate a self-signed certificate
		X500Name issuer = new X500Name("CN=localhost");
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 365 * 24 * 60 * 60 * 1000);
		X500Name subject = new X500Name("CN=localhost");
		SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
				.getInstance(keyPair.getPublic().getEncoded());

		X509v3CertificateBuilder certificateGenerator = new X509v3CertificateBuilder(issuer,
				serialNumber, startDate, endDate, subject, subjectPublicKeyInfo);

		// Use appropriate signature algorithm based on your keyPair algorithm.
		BouncyCastleProvider bcProvider = new BouncyCastleProvider();
		ContentSigner contentSigner = new JcaContentSignerBuilder(SIGNATURE_ALG)
				.setProvider(bcProvider).build(keyPair.getPrivate());

		X509CertificateHolder certificateHolder = certificateGenerator.build(contentSigner);
		return new JcaX509CertificateConverter().getCertificate(certificateHolder);
	}

	public static void storeKey(KeyPair keyPair, Certificate certificate, KeyStore keyStore,
			char[] keyStorePassword, File keyStoreFile) throws KeyStoreException,
			FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore.Entry entry = new PrivateKeyEntry(keyPair.getPrivate(),
				new Certificate[] { certificate });
		KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(keyStorePassword);

		keyStore.setEntry("oneKeyEntry", entry, param);

		try (FileOutputStream fos = new FileOutputStream(keyStoreFile)) {
			keyStore.store(fos, keyStorePassword);
		}
	}

	public static KeyPair loadFromKeyStore(KeyStore keyStore, File keyStoreFile, char[] password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, UnrecoverableEntryException {

		try (FileInputStream fis = new FileInputStream(keyStoreFile);) {
			keyStore.load(fis, password);
		}

		KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(password);
		Entry entry = keyStore.getEntry("oneKeyEntry", param);
		if (!(entry instanceof PrivateKeyEntry)) {
			throw new KeyStoreException("That's not a private key!");
		}
		PrivateKeyEntry privKeyEntry = (PrivateKeyEntry) entry;
		PublicKey publicKey = privKeyEntry.getCertificate().getPublicKey();
		PrivateKey privateKey = privKeyEntry.getPrivateKey();
		return new KeyPair(publicKey, privateKey);
	}

	public static void main(String[] args) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		char[] keyStorePassword = "password".toCharArray();
		File newKeyStoreFile = new File("newKeyStoreFileName");

		createEmptyKeyStoreFile(newKeyStoreFile, keyStorePassword);

		KeyStore keyStore = loadKeyStore(newKeyStoreFile, keyStorePassword);
		System.out.println("Created a key store with " + keyStore.size() + " entries.");

		KeyPair keyPair = AsymmetricEncryptionBytes.generateKeyPair();
		Certificate certificate = createSelfSignedCertificate(keyPair);

		storeKey(keyPair, certificate, keyStore, keyStorePassword, newKeyStoreFile);

		KeyPair loadedKeyPair = loadFromKeyStore(keyStore, newKeyStoreFile, keyStorePassword);
		System.out.println("Key store now has " + keyStore.size() + " entries.");

		if (!Arrays.equals(loadedKeyPair.getPublic().getEncoded(),
				keyPair.getPublic().getEncoded()) &&
				!Arrays.equals(loadedKeyPair.getPrivate().getEncoded(),
						keyPair.getPrivate().getEncoded())) {
			System.out.println("Loaded key does NOT equal the created key!");
		} else {
			System.out.println("Loaded key does equal the created key!");
		}
		
		newKeyStoreFile.delete();
	}

}
