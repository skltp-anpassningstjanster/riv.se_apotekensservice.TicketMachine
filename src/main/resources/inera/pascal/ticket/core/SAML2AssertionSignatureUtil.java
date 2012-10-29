package se.inera.pascal.ticket.core;

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.validation.ValidationException;

public interface SAML2AssertionSignatureUtil {
	
	/**
	 * @param signableSAMLObject Any SignableSAMLObject
	 * @param credential The X509 certificate credential
	 * @return Returns the validity of the SignableSAMLObject
	 */
	public Boolean validateXMLSignatureReference(SignableSAMLObject signableSAMLObject) throws ValidationException;
	
	/**
	 * @param signableSAMLObject Any SignableSAMLObject
	 * @param credential The X509 certificate credential
	 * @return Returns the validity of the SignableSAMLObject
	 * 
	 */	
	public Boolean validateXMLSignature(SignableSAMLObject signableSAMLObject) throws ValidationException;
	
	/**
	 * @return Returns the verification credential used as the verification key to
	 * cryptograhically evaluate the XML signature
	 */
	public Credential getVerificationCredential();	

}
