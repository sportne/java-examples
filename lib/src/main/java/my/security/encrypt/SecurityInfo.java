package my.security.encrypt;

import java.security.Provider;
import java.security.Security;

public class SecurityInfo {

	public static void listProvidersAndProperties() {
		for (Provider provider : Security.getProviders()) {
			System.out.println(provider.getName());
			for (String key : provider.stringPropertyNames())
				System.out.println("\t" + key + "\t" + provider.getProperty(key));
			System.out.println("----------------------------------");
		}

	}

	public static void listAlgorithms() {
		String[] cryptoServicesNames = { "SecureRandom", "Signature", "MessageDigest", "Cipher",
				"Mac", "KeyStore", "KeyGenerator", "KeyPairGenerator", "CertificateFactory",
				"KeyAgreement" };
		for (String service : cryptoServicesNames) {
			System.out.println(service + " Algorithms");
			for (String str : Security.getAlgorithms(service)) {
				System.out.println("   " + str);
			}
			System.out.println("----------------------------------");
		}

	}

	public static void main(String[] args) {
		listProvidersAndProperties();
		System.out.println("");
		listAlgorithms();
	}

}
