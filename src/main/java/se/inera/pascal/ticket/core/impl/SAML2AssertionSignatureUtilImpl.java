package se.inera.pascal.ticket.core.impl;

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

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.CredentialType;
import se.inera.pascal.ticket.core.SAML2AssertionSignatureUtil;
import se.inera.pascal.ticket.core.SAML2SigningCertificate;

public class SAML2AssertionSignatureUtilImpl implements SAML2AssertionSignatureUtil {

static private final Logger logger = LoggerFactory.getLogger(SAML2AssertionSignatureUtilImpl.class);
	
	private  SAML2SigningCertificate saml2SigningCertificate;
	private  SAMLSignatureProfileValidator  samlSignatureProfileValidator;	
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean validateXMLSignatureReference(SignableSAMLObject signableSAMLObject) throws ValidationException{
		boolean validationResult = false;
		try {
			if(signableSAMLObject !=null) {
				samlSignatureProfileValidator.validate(signableSAMLObject.getSignature());
				validationResult=true;
			}
		}catch(ValidationException e) {
			throw e;			
		}		
		return validationResult;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean validateXMLSignature(SignableSAMLObject signableSAMLObject) throws ValidationException{
		boolean validationResult = false;		
		SignatureValidator signatureValidator = new SignatureValidator(getVerificationCredential());
		try{
			if(signableSAMLObject !=null) {
				signatureValidator.validate(signableSAMLObject.getSignature());
				validationResult=true;
			}	
		}catch(ValidationException e){
			throw e;
		}	
		return validationResult;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Credential getVerificationCredential(){
		Credential credential;
		X509Certificate x509cert;
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

			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());	
			keystoreStream = getClass().getClassLoader().getResourceAsStream(saml2SigningCertificate.getKeystorePath() + saml2SigningCertificate.getKeystoreFilename());
			keystore.load(keystoreStream, keystorePassword);

			if (!keystoreAlias.isEmpty() && keystore.containsAlias(keystoreAlias)){
				x509cert = (X509Certificate) keystore.getCertificate(keystoreAlias);
			}else{
				Enumeration <String> aliases = keystore.aliases();
				keystoreAlias = aliases.nextElement();
				x509cert = (X509Certificate) keystore.getCertificate(keystoreAlias);
			}

			logger.debug("Loading X509 certificate from keystore");
			
			
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
				logger.error(e.getMessage());
				throw new IllegalStateException(e);
		} catch (KeyStoreException e) {
				logger.error(e.getMessage());
				throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
				throw new IllegalStateException(e);
		} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage());
				throw new IllegalStateException(e);
		} catch (CertificateException e) {
				logger.error(e.getMessage());
				throw new IllegalStateException(e);
		} catch (IOException e) {
				logger.error(e.getMessage());
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
				logger.error(e.getMessage());
			}
		}
		return credential;		
	}

	public SAML2SigningCertificate getSaml2SigningCertificate() {
		return saml2SigningCertificate;
	}

	public void setSaml2SigningCertificate(
			SAML2SigningCertificate saml2SigningCertificate) {
		this.saml2SigningCertificate = saml2SigningCertificate;
	}

	public SAMLSignatureProfileValidator getSamlSignatureProfileValidator() {
		return samlSignatureProfileValidator;
	}

	public void setSamlSignatureProfileValidator(
			SAMLSignatureProfileValidator samlSignatureProfileValidator) {
		this.samlSignatureProfileValidator = samlSignatureProfileValidator;
	}	
}
