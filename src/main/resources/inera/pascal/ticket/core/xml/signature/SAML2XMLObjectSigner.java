package se.inera.pascal.ticket.core.xml.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.SignableXMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;

import se.inera.pascal.ticket.core.CredentialType;
import se.inera.pascal.ticket.core.SAML2SigningCertificate;


public class SAML2XMLObjectSigner {
	private static Logger logger = LoggerFactory.getLogger(SAML2XMLObjectSigner.class);

	private Credential credential;
	private X509Certificate x509cert;
	private String canonicalizationAlgorithm;
	private boolean includeKeyInfoInSignature;
	private XMLObjectBuilderFactory builderFactory;
	private MarshallerFactory marshallerFactory;
	private KeyInfoGeneratorManager keyInfoGeneratorManager;
	private SAML2SigningCertificate saml2SigningCertificate;
	
	/**
	 * Constructor which is responsible to load the certificate
	 * @param saml2SigningCertificate
	 */
	public SAML2XMLObjectSigner(SAML2SigningCertificate saml2SigningCertificate) {
		logger.debug("Initiating keystore");
		InputStream keystoreStream = null;
		char[] keystorePassword = null;
		String keystoreAlias = null;
		char[] keystoreAliasPassword = null;
		CredentialType credentialType = null;
		try {			
			keystorePassword = saml2SigningCertificate.getKeystorePassword().toCharArray();
			keystoreAlias = saml2SigningCertificate.getKeystoreAlias();
			keystoreAliasPassword = saml2SigningCertificate.getKeystoreAliasPassword().toCharArray();
			credentialType = CredentialType.valueOf(saml2SigningCertificate.getCredentialType());

			KeyStore keystore = KeyStore.getInstance(saml2SigningCertificate.getKeystoreType());
			String fi = saml2SigningCertificate.getKeystorePath() + saml2SigningCertificate.getKeystoreFilename();
			File f; 
			if (fi.startsWith("/") || fi.startsWith(":", 1)){
				//path is absolute
				f = new File(fi);
			}else{
				//path is relative
				f = new File( getClass().getClassLoader().getResource(fi).getPath().replaceAll("%20", " ") );
			}
			keystoreStream = new FileInputStream(f);
			keystore.load(keystoreStream, keystorePassword);

			logger.debug("Loading X509 certificate from keystore");
			if (!keystoreAlias.isEmpty() && keystore.containsAlias(keystoreAlias)){
				x509cert = (X509Certificate) keystore.getCertificate(keystoreAlias);
			}else{
				Enumeration <String> aliases = keystore.aliases();
				keystoreAlias = aliases.nextElement();
				x509cert = (X509Certificate) keystore.getCertificate(keystoreAlias);
			}
						
			logger.debug("Loading credential data from keystore");			
			switch (credentialType) {
			case BASIC_CREDENTIAL:
				BasicCredential basicCredential = new BasicCredential();
				basicCredential.setPrivateKey((PrivateKey) keystore.getKey(keystoreAlias, keystoreAliasPassword));
				basicCredential.setPublicKey(keystore.getCertificate(keystoreAlias).getPublicKey());
				credential = basicCredential;
				break;

			case X509_CREDENTIAL:
				BasicX509Credential basicX509Credential = new BasicX509Credential();
				basicX509Credential.setEntityCertificate(x509cert);
				basicX509Credential.setPrivateKey((PrivateKey) keystore.getKey(keystoreAlias, keystoreAliasPassword));
				basicX509Credential.setPublicKey(keystore.getCertificate(keystoreAlias).getPublicKey());
				credential = basicX509Credential;
				break;

			default:
				throw new IllegalStateException("Invalid credentialType");
			}

		} catch (UnrecoverableKeyException e) {
				String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
				logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (KeyStoreException e) {
			String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
			logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
			String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error! File not found.";
			logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (NoSuchAlgorithmException e) {
			String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
			logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (CertificateException e) {
			String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
			logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (IOException e) {
			String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
			logger.error(message,e);
				throw new IllegalStateException(e);
		} catch (NullPointerException e) {
			String message = "Could not load keystore. Check alias names. : ";
			logger.error(message,e);
			throw new IllegalStateException(e);
		} finally {
			try {
				logger.debug("Closing keystore");
				if (keystoreStream != null)
					keystoreStream.close();
			} catch (IOException e) {				
				String message = "Error closing keystore";
				logger.error(message,e);
			}
		}
	}
	
	/**
	 * Adds a signature to a SignableXMLObject
	 * @param signableXmlObject The SignableXMLObject
	 * @return Returns a SignableXMLObject containing the signature
	 * @throws SignatureException
	 */
	public SignableXMLObject sign(SignableXMLObject signableXmlObject) throws SignatureException {
		Signature signature = (Signature) builderFactory.getBuilder(Signature.DEFAULT_ELEMENT_NAME).buildObject(Signature.DEFAULT_ELEMENT_NAME);
		signature.setSigningCredential(credential);
		signature.setCanonicalizationAlgorithm(canonicalizationAlgorithm);
		

		if (includeKeyInfoInSignature) {
			KeyInfoGeneratorFactory keyInfoGeneratorFactory = keyInfoGeneratorManager.getFactory(credential);
			KeyInfo keyInfo = null;
			try {
				keyInfo = keyInfoGeneratorFactory.newInstance().generate(credential);
			} catch (SecurityException e) {
				String message = "SAML2XMLObjectSigner(SAML2SigningCertificate), Keystore error!";
				logger.error(message,e);
			}
			signature.setKeyInfo(keyInfo);
		}

		logger.debug("Trying to match signature Algorithm to public key algorithm: " + credential.getPublicKey().getAlgorithm());
		if (credential.getPublicKey().getAlgorithm().equalsIgnoreCase("DSA")) {
			signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_DSA);
		} else if (credential.getPublicKey().getAlgorithm().equalsIgnoreCase("RSA")) {
			signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA);
		} else {
			throw new SignatureException("Unknown public key algorithm. Signature algorithm not set.");
		}
		signableXmlObject.setSignature(signature);		
		Marshaller marshaller = marshallerFactory.getMarshaller(signableXmlObject);
		try {
			marshaller.marshall(signableXmlObject);
		} catch (MarshallingException e) {
			String message = "sign(SignableXMLObject), Marshalling failed : ";
			logger.error(message,e);
		}		
		Signer.signObject(signature);		
		return signableXmlObject;
	}

	public Credential getCredential() {
		return credential;
	}

	public String getCanonicalizationAlgorithm() {
		return canonicalizationAlgorithm;
	}

	public void setCanonicalizationAlgorithm(String canonicalizationAlgorithm) {
		this.canonicalizationAlgorithm = canonicalizationAlgorithm;
	}

	public boolean isIncludeKeyInfoInSignature() {
		return includeKeyInfoInSignature;
	}

	public void setIncludeKeyInfoInSignature(boolean includeKeyInfoInSignature) {
		this.includeKeyInfoInSignature = includeKeyInfoInSignature;
	}

	public XMLObjectBuilderFactory getBuilderFactory() {
		return builderFactory;
	}

	public SAML2SigningCertificate getSaml2SigningCertificate() {
		return saml2SigningCertificate;
	}

	public void setBuilderFactory(XMLObjectBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}

	public MarshallerFactory getMarshallerFactory() {
		return marshallerFactory;
	}

	public void setMarshallerFactory(MarshallerFactory marshallerFactory) {
		this.marshallerFactory = marshallerFactory;
	}

	public KeyInfoGeneratorManager getKeyInfoGeneratorManager() {
		return keyInfoGeneratorManager;
	}

	public void setKeyInfoGeneratorManager(
			KeyInfoGeneratorManager keyInfoGeneratorManager) {
		this.keyInfoGeneratorManager = keyInfoGeneratorManager;
	}	
}
