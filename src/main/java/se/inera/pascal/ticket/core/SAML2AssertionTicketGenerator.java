package se.inera.pascal.ticket.core;

import java.util.List;

import javax.xml.soap.SOAPMessage;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.soap.client.SOAPFaultException;
import org.w3c.dom.Node;

/**
 * This interface serve as a Ticket Server simulator. I addition to the returning
 * Ticket it is placed in a Security Header and attached to a SOAPMessage with or
 * without payload. Primarly a formatted string is generated. The SOAPMessage can also
 * be written to file if needed. The usergroup of this tool i typically a Developer
 * or a Tester. The generated string can the pasted to any desired SOAP testing tool.
 * Here SOAPUi, which is the one used at Apotekens Service. Or the string can be used
 * in testautomation, adding a util to generate testdata for request and append it to
 * the SOAPMessage string and den excute the call to the Web Service.
 * 
 */
public interface SAML2AssertionTicketGenerator {
	
	public String generateSOAPMessageWithoutPayload(SAML2AssertionAttributeSet saml2AssertionAttributeSet,boolean getSecurityOnly);
	public String getValidToString();
	/**
	 * Default SOAPMessage, includes a Security Header with valid Signature(s) for each SAML2 Assertion(s).
	 * The SOAPMessage string contains 2 placeHolder: $NameSpacePlaceHolder $RequestXMLPlaceHolder. The reason
	 * is that this string easily can be used as template while generating specific requests, for testautomation.
	 * The SAML2 Assertion(s) are only valid 30 minutes as default
	 * @param saml2AssertionAttributeSet SAML2AttributeSet contains desired attributes for each security block.
	 * @param fomatted Indicates whether the string should be formatted or not
	 * @return Returns a string representing the SOAPMessage.
	 */	
	public String generateSOAPMessageWithoutPayload(SAML2AssertionAttributeSet saml2AssertionAttributeSet,Boolean formatted,boolean getSecurityOnly);
		
	/**
	 * Default SOAPMessage, includes a Security Header with valid Signature(s) for each SAML2 Assertion(s).
	 * The SOAPMessage string contains 2 placeHolder: $NameSpacePlaceHolder $RequestXMLPlaceHolder. The reason
	 * is that this string easily can be used as template while generating specific requests, for testautomation.
     * @param saml2AssertionAttributeSet SAML2AttributeSet contains desired attributes for each security block.
     * @param fomatted Indicates whether the string should be formatted or not
	 * @param samlAssertionValidityInMinutes The SAML2 Assertion(s) validity in minutes 
	 * @return Returns a string representing the SOAPMessage.
	 */
	public String generateSOAPMessageWithoutPayload(SAML2AssertionAttributeSet saml2AssertionAttributeSet,Boolean formatted,Integer samlAssertionValidityInMinutes);
	
	/**
	 * @param authnAssertion SAML2 Assertion representening the authentication block
	 * @param authorizationAssertion SAML2 Assertion representening the authorization block
	 * @param auditingAssertion SAML2 Assertion representening the auditing block
	 * @return Returns a SOAPMessageTemplate
	 */
	public SOAPMessage getSoapMessageTemplate(Assertion authnAssertion, Assertion authorizationAssertion, Assertion auditingAssertion);
	
	/**
	 * @param authnAssertion SAML2 Assertion representening the authentication block
	 * @param authorizationAssertion SAML2 Assertion representening the authorization block
	 * @param auditingAssertion SAML2 Assertion representening the auditing block
	 * @return Returns a SOAPMessageTemplate
	 */
	public SOAPMessage getSoapMessageTemplateWSS(Assertion authnAssertion, Assertion authorizationAssertion, Assertion auditingAssertion);
	
	/**
	 * @param saml2AuthnAttributes SAML2 Attributes
	 * @param samlAssertionValidityInMinutes The SAML2 Assertion(s) validity in minutes
	 * @return Returns a signed SAML2 Assertion for the authentication block
	 */
	public Assertion getSignedAuthnAssertion(List<SAML2Attribute> saml2AuthnAttributes,Integer samlAssertionValidityInMinutes);
	
	/**
	 * @param saml2AuthorizationAttributes SAML2 attributes
	 * @param authnAssertion The SAML2 Assertion for the authentication block
	 * @return Returns a signed SAML2 Assertion for the autorization block
	 */
	public Assertion getSignedAuthorizationAssertion(List<SAML2Attribute> saml2AuthorizationAttributes,Assertion authnAssertion);
	
	/**
	 * @param saml2AuditingAttributes SAML2 Attributes aimed for the auditing block
	 * @param authnAssertion The SAML2 Assertion for the autentication block
	 * @return Returns a signed SAML2 Assertion for the auditing block
	 */
	public Assertion getSignedAuditingAssertion(List<SAML2Attribute> saml2AuditingAttributes,Assertion authnAssertion);
	
	/**
	 * @param soapMessage The SOAP Message
	 * @return Extracts the Security Header from the SOAP Message
	 * @throws SOAPFaultException
	 */
	public Node getSecurityHeader(SOAPMessage soapMessage) throws SOAPFaultException;
	
	
	/**
	 * @param soapMessage The SOAP Message
	 * @return Extracts the Security Header from the SOAP Message
	 * @throws SOAPFaultException
	 */
	public Node getSecurityHeaderWSS(SOAPMessage soapMessage) throws SOAPFaultException;
	
}
