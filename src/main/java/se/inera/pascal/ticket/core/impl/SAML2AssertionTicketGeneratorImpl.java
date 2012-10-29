package se.inera.pascal.ticket.core.impl;

import java.util.List;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.soap.client.SOAPFaultException;
import org.opensaml.xml.signature.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import se.inera.pascal.ticket.core.DefaultValues;
import se.inera.pascal.ticket.core.SAML2AssertionAttributeSet;
import se.inera.pascal.ticket.core.SAML2AssertionGenerator;
import se.inera.pascal.ticket.core.SAML2AssertionStringUtil;
import se.inera.pascal.ticket.core.SAML2AssertionTicketGenerator;
import se.inera.pascal.ticket.core.SAML2Attribute;
import se.inera.pascal.ticket.core.StringConstants;
import se.inera.pascal.ticket.core.xml.signature.SAML2XMLObjectSigner;

public class SAML2AssertionTicketGeneratorImpl implements SAML2AssertionTicketGenerator{
	
	private static final Logger logger = LoggerFactory.getLogger(SAML2AssertionTicketGeneratorImpl.class);
	
	private SAML2AssertionGenerator saml2AssertionGenerator;
	private SAML2XMLObjectSigner saml2XmlObjectSigner;
	private SAML2AssertionStringUtil saml2AssertionStringUtil;
	private DefaultValues defaultValues;
	private String validTo="";
		
	/**
	 * {@inheritDoc}
	 */
	public String generateSOAPMessageWithoutPayload(
			SAML2AssertionAttributeSet saml2AssertionAttributeSet,
			Boolean formatted, boolean getSecurityOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateSOAPMessageWithoutPayload(SAML2AssertionAttributeSet saml2AssertionAttributeSet,boolean getSecurityOnly) {
		String soapMessageString = null;
		preConditionSAML2AssertionAttributeSet(saml2AssertionAttributeSet);
		Assertion authnAssertion = getSignedAuthnAssertion(saml2AssertionAttributeSet.getAuthnAttributes(),defaultValues.getTicketValidity());
		Assertion authorizationAssertion = getSignedAuthorizationAssertion(saml2AssertionAttributeSet.getAuthorizationAttributes(),authnAssertion);
		Assertion auditingAssertion = getSignedAuditingAssertion(saml2AssertionAttributeSet.getAuditingAttributes(),authnAssertion);
		if (getSecurityOnly){
			soapMessageString = saml2AssertionStringUtil.saml2AssertionToUnFormattedString(authnAssertion);
			soapMessageString += saml2AssertionStringUtil.saml2AssertionToUnFormattedString(authorizationAssertion);
			soapMessageString += saml2AssertionStringUtil.saml2AssertionToUnFormattedString(auditingAssertion);
		}else{
			SOAPMessage soapMessageTemplate = getSoapMessageTemplateWSS(authnAssertion, authorizationAssertion, auditingAssertion);
			soapMessageString = saml2AssertionStringUtil.soapMessageToUnFormattedString(soapMessageTemplate);
		}
		return soapMessageString;
	}
	
	public String getValidToString(){
		return validTo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String generateSOAPMessageWithoutPayload(SAML2AssertionAttributeSet saml2AssertionAttributeSet,Boolean formatted, Integer samlAssertionValidityInMinutes) {
		String soapMessageString = null;		
		preConditionSAML2AssertionAttributeSet(saml2AssertionAttributeSet);		
		Assertion authnAssertion = getSignedAuthnAssertion(saml2AssertionAttributeSet.getAuthnAttributes(),samlAssertionValidityInMinutes);
		Assertion authorizationAssertion = getSignedAuthorizationAssertion(saml2AssertionAttributeSet.getAuthorizationAttributes(),authnAssertion);
		Assertion auditingAssertion = getSignedAuditingAssertion(saml2AssertionAttributeSet.getAuditingAttributes(),authnAssertion);
		SOAPMessage soapMessageTemplate = getSoapMessageTemplateWSS(authnAssertion, authorizationAssertion, auditingAssertion);
		
		if(formatted){
			soapMessageString = saml2AssertionStringUtil.soapMessageToFormattedString(soapMessageTemplate);
		}else {
			soapMessageString = saml2AssertionStringUtil.soapMessageToUnFormattedString(soapMessageTemplate);
		}		
		return soapMessageString;
	}

	/**
	 * {@inheritDoc}
	 */
	public Node getSecurityHeader(SOAPMessage soapMessage) throws SOAPFaultException {
		final String SEC_HEADER="urn:apotekensservice:security:1";
		SOAPHeader soapHeader = null;
		Node securityHeader = null;
		try {
			soapHeader = soapMessage.getSOAPHeader();
			if(soapHeader == null){
				String message = "SOAP Header must not be null";
				throw new SOAPFaultException(message);
			}
			securityHeader = soapHeader.getFirstChild();			
			if(securityHeader == null || 
			   !securityHeader.getLocalName().equals("Security") ||
			   !securityHeader.getNamespaceURI().equals(SEC_HEADER)){
				String message = "Missing or invalid Security Header";
				throw new SOAPFaultException(message);
			}
			
		}catch(Exception e){
			throw new SOAPFaultException(e);
		}
		return securityHeader;		 
	}	
	
	public Node getSecurityHeaderWSS(SOAPMessage soapMessage) throws SOAPFaultException {
		final String WSS_SEC_HEADER="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPHeader soapHeader = null;
		Node securityHeader = null;
		try {
			soapHeader = soapMessage.getSOAPHeader();
			if(soapHeader == null){
				String message = "SOAP Header must not be null";
				throw new SOAPFaultException(message);
			}
			securityHeader = soapHeader.getFirstChild();			
			if(securityHeader == null || 
			   !securityHeader.getLocalName().equals("Security") ||
			   !securityHeader.getNamespaceURI().equals(WSS_SEC_HEADER)){
				String message = "Missing or invalid Security Header";
				throw new SOAPFaultException(message);
			}
			
		}catch(Exception e){
			throw new SOAPFaultException(e);
		}
		return securityHeader;		
	}

	/**
	 * {@inheritDoc}
	 */
	public SOAPMessage getSoapMessageTemplateWSS(Assertion authnAssertion, Assertion authorizationAssertion, Assertion auditingAssertion){
		final String WSS_SEC_HEADER="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPMessage soapMessage = null;
		
		try {

			soapMessage = MessageFactory.newInstance().createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();	

			soapEnvelope.addNamespaceDeclaration("", "$NameSpacePlaceHolder");

			Name secHeaderName = soapEnvelope.createName("Security", "wsse", WSS_SEC_HEADER);			

			if(soapEnvelope.getHeader() == null){
				soapEnvelope.addHeader();
			}		

			SOAPHeaderElement secElement = soapEnvelope.getHeader().addHeaderElement(secHeaderName);
			secElement.addNamespaceDeclaration("wsse", WSS_SEC_HEADER);
			
			secElement.appendChild(soapPart.importNode(authnAssertion.getDOM(), true));
			secElement.appendChild(soapPart.importNode(authorizationAssertion.getDOM(), true));
			secElement.appendChild(soapPart.importNode(auditingAssertion.getDOM(), true));
			soapBody.addTextNode("$RequestXMLPlaceHolder");

		}catch(SOAPException e){
			String message = "Error while creating SoapMessage";
			logger.error(message,e);
		}		
		return soapMessage;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public SOAPMessage getSoapMessageTemplate(Assertion authnAssertion, Assertion authorizationAssertion, Assertion auditingAssertion){
		final String SEC_HEADER="urn:apotekensservice:security:1";
		SOAPMessage soapMessage = null;
		
		try {

			soapMessage = MessageFactory.newInstance().createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();	

			soapEnvelope.addNamespaceDeclaration("", "$NameSpacePlaceHolder");

			Name secHeaderName = soapEnvelope.createName("Security", "sec", SEC_HEADER);			
			Name authorizationHeaderName = soapEnvelope.createName("AuthorizationData","sec", SEC_HEADER);
			Name auditingHeaderName = soapEnvelope.createName("InfoData","sec", SEC_HEADER);

			if(soapEnvelope.getHeader() == null){
				soapEnvelope.addHeader();
			}		

			SOAPHeaderElement secElement = soapEnvelope.getHeader().addHeaderElement(secHeaderName);
			secElement.addNamespaceDeclaration("sec", SEC_HEADER);
			
			secElement.appendChild(soapPart.importNode(authnAssertion.getDOM(), true));
			secElement.addChildElement(authorizationHeaderName).appendChild(soapPart.importNode(authorizationAssertion.getDOM(), true));
			secElement.addChildElement(auditingHeaderName).appendChild(soapPart.importNode(auditingAssertion.getDOM(), true));
			soapBody.addTextNode("$RequestXMLPlaceHolder");

		}catch(SOAPException e){
			String message = "Error while creating SoapMessage";
			logger.error(message,e);
		}		
		return soapMessage;
	}	

	/**
	 * {@inheritDoc}
	 */
	public Assertion getSignedAuthnAssertion(List<SAML2Attribute> saml2AuthnAttributes,Integer samlAssertionValidityInMinutes) {
//		String nameQualifier = "Serial Number= SE2321000131-P000000068832, CN=SAML2 Test tool, O=Apotekens Service, L=Stockholm, C=SE";
//		String nameIdValue = "SAML2 Test tool";
		String nameQualifier = "Serial Number=" + defaultValues.getCertificateSerial() +","+ defaultValues.getCertificateSubject();
		String nameIdValue = defaultValues.getCertificateSubjectName();
		DateTime now = new DateTime();
		DateTime from = new DateTime().minusMinutes(5);
		DateTime to = new DateTime().plusMinutes(samlAssertionValidityInMinutes).minusMinutes(5);
		validTo = to.toString();
		Assertion authnAssertion = saml2AssertionGenerator.generateSAML2Assertion(new DateTime(), 
															  nameQualifier, 
															  nameIdValue, 
															  from, 
															  to, 
															  now, 
															  saml2AuthnAttributes);
		try {
			authnAssertion = (Assertion)saml2XmlObjectSigner.sign(authnAssertion);
		}catch(SignatureException e){
			String message ="SAML2 assertion signing failed : ";
			logger.error(message,e);
		}
		return authnAssertion;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public Assertion getSignedAuthorizationAssertion(List<SAML2Attribute> saml2AuthorizationAttributes,Assertion authnAssertion) {
		
		Assertion authorizationAssertion = saml2AssertionGenerator.generateSAML2Assertion(authnAssertion.getID(),
				StringConstants.ATTRIBUTE_AUTHORIZATION_DATA,
				new DateTime(), 
				authnAssertion.getConditions().getNotBefore(), 
				authnAssertion.getConditions().getNotOnOrAfter().minusMinutes(1), 
				saml2AuthorizationAttributes);		
		try {
			authorizationAssertion = (Assertion)saml2XmlObjectSigner.sign(authorizationAssertion);
		}catch(SignatureException e){
			String message ="SAML2 assertion signing failed : ";
			logger.error(message,e);
		}
		return authorizationAssertion;
	}
	
	/**
	 * {@inheritDoc}	 
	 */
	public Assertion getSignedAuditingAssertion(List<SAML2Attribute> saml2AuditingAttributes,Assertion authnAssertion){
		Assertion auditingAssertion = saml2AssertionGenerator.generateSAML2Assertion(authnAssertion.getID(),
				StringConstants.ATTRIBUTE_INFO_DATA,
				new DateTime(), 
				authnAssertion.getConditions().getNotBefore(), 
				authnAssertion.getConditions().getNotOnOrAfter().minusMinutes(1), 
				saml2AuditingAttributes);		
		try {
			auditingAssertion = (Assertion)saml2XmlObjectSigner.sign(auditingAssertion);
		}catch(SignatureException e){
			String message ="SAML2 assertion signing failed : ";
			logger.error(message,e);
		}
		return auditingAssertion;
	}	

	public SAML2AssertionGenerator getSaml2AssertionGenerator() {
		return saml2AssertionGenerator;
	}	

	public void setSaml2AssertionGenerator(
			SAML2AssertionGenerator saml2AssertionGenerator) {
		this.saml2AssertionGenerator = saml2AssertionGenerator;
	}	
	
	public SAML2XMLObjectSigner getSaml2XmlObjectSigner() {
		return saml2XmlObjectSigner;
	}

	public void setSaml2XmlObjectSigner(SAML2XMLObjectSigner saml2XmlObjectSigner) {
		this.saml2XmlObjectSigner = saml2XmlObjectSigner;
	}

	public SAML2AssertionStringUtil getSaml2AssertionStringUtil() {
		return saml2AssertionStringUtil;
	}

	public void setSaml2AssertionStringUtil(
			SAML2AssertionStringUtil saml2AssertionStringUtil) {
		this.saml2AssertionStringUtil = saml2AssertionStringUtil;
	}	

	public DefaultValues getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(DefaultValues defaultValues) {
		this.defaultValues = defaultValues;
	}	

	

//	public XMLDocumentReaderUtil getXmlDocumentReaderUtil() {
//		return xmlDocumentReaderUtil;
//	}
//
//	public void setXmlDocumentReaderUtil(XMLDocumentReaderUtil xmlDocumentReaderUtil) {
//		this.xmlDocumentReaderUtil = xmlDocumentReaderUtil;
//	}
	
	private void preConditionSAML2AssertionAttributeSet(
			SAML2AssertionAttributeSet saml2AssertionAttributeSet) {
		if(saml2AssertionAttributeSet == null || 
		   saml2AssertionAttributeSet.getAuthnAttributes() == null || 
		   saml2AssertionAttributeSet.getAuthorizationAttributes() == null ||
		   saml2AssertionAttributeSet.getAuditingAttributes() == null ||
		   saml2AssertionAttributeSet.getAuthnAttributes().size() ==0 ||
		   saml2AssertionAttributeSet.getAuthorizationAttributes().size() ==0 ||
		   saml2AssertionAttributeSet.getAuditingAttributes().size() ==0){
			String message = "SAML2AssertionAttributeSet can not be null or empty";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}
	}
}
