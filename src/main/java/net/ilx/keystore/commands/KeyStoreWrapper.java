package net.ilx.keystore.commands;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.ilx.keystore.util.StreamUtils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.ResourceUtils;

public class KeyStoreWrapper {
	public static final String JCEKS = "jceks";
	public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	protected String fileName;
	protected String kspass;
	protected KeyStore ks;

	protected KeyStoreWrapper(String p_file, String p_keystorePass) {
		this.fileName = p_file;
		this.kspass = p_keystorePass;
	}

	public static KeyStoreWrapper load(final String p_file, final String p_keystorePass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStoreWrapper wrapper = new KeyStoreWrapper(p_file, p_keystorePass);
		wrapper.load();

		return wrapper;
	}

	public static KeyStoreWrapper create(String p_file, String p_keystorePass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStoreWrapper wrapper = new KeyStoreWrapper(p_file, p_keystorePass);
		wrapper.create();
		return wrapper;
	}

	protected void load() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		URL keystoreUrl = ResourceUtils.getURL(fileName);
		InputStream inputStream = keystoreUrl.openStream();
		try {
			KeyStore ks = KeyStore.getInstance(JCEKS);
			ks.load(inputStream, kspass.toCharArray());
		} finally {
			StreamUtils.close(inputStream);
		}
	}

	protected void create() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        ks = KeyStore.getInstance(JCEKS);
        ks.load(null, kspass.toCharArray());
	}


	public void save() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		try {
			ks.store(fos, kspass.toCharArray());
		}
		finally {
			StreamUtils.close(fos);
		}
	}


	public String content() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {

		StringBuilder sb = new StringBuilder();

		String type = ks.getType();
		sb.append("keystore '").append(fileName).append("'").append(" of type:").append(type);
		sb.append(" contains:");

		Enumeration<String> aliases = ks.aliases();
		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			Key key = ks.getKey(alias, kspass.toCharArray());

			if (null != key) {
				sb.append("\nkey alias:'").append(alias).append("'");
				sb.append(" algorithm: '").append(key.getAlgorithm()).append("'").append(" format: '").append(key.getFormat()).append("'");
			}
		}

		return sb.toString();
	}

	/**
	 *
	 * @param alias
	 * @param base64EncodedPassword base64 encoded key
	 * @throws UnsupportedEncodingException
	 * @throws KeyStoreException
	 */
	public void addPassword(String alias, String base64EncodedPassword) throws UnsupportedEncodingException, KeyStoreException {
		if (!Base64.isBase64(base64EncodedPassword)) {
			String msg = String.format("Value '%s' is not base64 encoded!", base64EncodedPassword);
			throw new IllegalStateException(msg);
		}
		byte[] bytes = Base64.decodeBase64(base64EncodedPassword);

        SecretKey secretKey = new SecretKeySpec(bytes, HMAC_SHA1_ALGORITHM);
        Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        String keystorePassword = kspass;
		ProtectionParameter param = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        ks.setEntry(alias, entry, param);
	}

	public void deleteEntry(String alias) throws KeyStoreException {
		this.ks.deleteEntry(alias);
	}

	public String retrievePassword(final String alias) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		ProtectionParameter protParam = new KeyStore.PasswordProtection(kspass.toCharArray());
		Entry entry = this.ks.getEntry(alias, protParam);

		String retval = "";
		if (entry instanceof SecretKey) {
			SecretKey secretKey = (SecretKey) entry;
			byte[] encoded = secretKey.getEncoded();
			retval = Base64.encodeBase64URLSafeString(encoded);
		}
		else {
			String msg = String.format("Alias '%s' is not secret key.", alias);
			throw new IllegalStateException(msg);
		}

		return retval;
	}

}