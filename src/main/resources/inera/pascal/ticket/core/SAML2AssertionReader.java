package se.inera.pascal.ticket.core;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;

public interface SAML2AssertionReader {
	
	/**
	 * @param fileName The specified filename
	 * @return Returns an SAML2 Assertion Response read from file 
	 */
	public Response readSAML2ResponseFromFile(String fileName);
	
	/**
	 * @param fileName The specified filename
	 * @return Returns an SAML2 Assertion read from file 
	 */
	public Assertion readSAML2AssertionFromFile(String fileName);
	
	/**
	 * @param saml2AssertionResponseString The specified response string
	 * @return Returns an SAML2 Assertion Response read from SAML2 Assertion ResponseString  
	 */
	public Response readSAML2ResponseFromString(String saml2AssertionResponseString);
	
	/**
	 * @param saml2AssertionString The specified response string
	 * @return Returns an SAML2 Assertion Response read from SAML2 Assertion String  
	 */
	public Assertion readSAML2AssertionFromString(String saml2AssertionString);

}
