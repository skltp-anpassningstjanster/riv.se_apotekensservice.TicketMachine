package se.inera.pascal.ticket.core;

import javax.xml.soap.SOAPMessage;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.w3c.dom.Document;

public interface SAML2AssertionStringUtil {
	
	/**
	 * @param re The SAML2 Assertion Response
	 * @return Returns a SAML2 Assertion Response formatted string
	 */
	public String saml2ResponseToFormattedString(Response response);
	
	/**
	 * @param re The SAML2 Assertion Response
	 * @return Returns a SAML2 Assertion Response unformatted string
	 */
	public  String saml2ResponseToUnFormattedString(Response response);
	
	/**
	 * @param assertion The SAML2 Assertion
	 * @return Returns a SAML2 Assertion formatted string	 
	 */
	public String saml2AssertionToFormattedString(Assertion assertion);
	
	/**
	 * @param assertion The SAML2 Assertion
	 * @return Returns a SAML2 Assertion unformatted string	 
	 */
	public  String saml2AssertionToUnFormattedString(Assertion assertion);
	
	/**
	 * @param soapMessage The SOAPMessage
	 * @return Returns a formatted String
	 */
	public  String soapMessageToFormattedString(SOAPMessage soapMessage);
	
	/**
	 * @param soapMessage The SOAPMessage
	 * @return Returns a formatted String
	 */
	public  String soapMessageToUnFormattedString(SOAPMessage soapMessage);
	
	/**
	 * @param document
	 * @return Returns an unformatted XML string
	 */
	public  String xmlDocumentToUnformattedString(Document document); 
	
	/**
	 * @param document
	 * @return Returns a formatted XML string
	 */
	public  String xmlDocumentToFormattedString(Document document); 

}
