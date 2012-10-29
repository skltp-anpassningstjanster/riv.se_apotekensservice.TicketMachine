package se.inera.pascal.ticket.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustedKeyStoreLoader {
	
	private final static Logger logger = LoggerFactory.getLogger(TrustedKeyStoreLoader.class);
	
	private String path;
	private char[] password;
	private KeyStore keyStore;
	private File keyStoreFile;
	
	public TrustedKeyStoreLoader(String path, String password) {
		if(StringUtils.isBlank(path)){
        	String message= "Path to keystore cannot be null or empty!";
        	logger.info(message);
        	throw new IllegalArgumentException(message);
        }
		if(StringUtils.isBlank(password)){
			String message= "Password must be assigned !";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}	
		this.path = path;
		this.password = password.toCharArray();		
	}	
	
	/**
	 * @return Loads the the trustedkeystore into memory
	 */
	public void loadTrustedKeyStore() throws KeyStoreException, IOException,
			                                 NoSuchAlgorithmException, CertificateException {
		FileInputStream fis = null;
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStoreFile = new File(path);
		
		if(!keyStoreFile.exists() || !keyStoreFile.canRead()){
			String message= "Truststore file must exist and be readable !";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}
		
		if(StringUtils.isBlank(password.toString())){
			String message= "Password must be assigned !";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}
		
		try {
			fis = new FileInputStream(keyStoreFile);
			keyStore.load(fis,password);
		}finally {
			if(fis !=null){
				fis.close();
			}			
		}
	}
	
	/**
	 * @return Returns the trusted keystore
	 */
	public KeyStore getTrustedKeyStore(){
		return keyStore;
	}
}
