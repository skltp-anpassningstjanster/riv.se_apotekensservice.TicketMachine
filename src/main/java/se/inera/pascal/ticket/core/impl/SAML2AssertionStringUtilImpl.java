package se.inera.pascal.ticket.core.impl;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.inera.pascal.ticket.core.SAML2AssertionStringUtil;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2AssertionStringUtilImpl implements SAML2AssertionStringUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SAML2AssertionStringUtilImpl.class);
	
	private  ResponseMarshaller responseMarshaller;	
	private  AssertionMarshaller assertionMarshaller;	
	
	/**
	 * {@inheritDoc}
	 */
	public String xmlDocumentToUnformattedString(Document document) {
		Element element = null;
		String unformatted = null;			

		if(document !=null){
			if(document.getDocumentElement() !=null){
				element = document.getDocumentElement();			
				if(element !=null){
					unformatted = XMLHelper.nodeToString(element);
					unformatted = unformatted.replace(StringConstants.XML_OMIT_STRING, "");
				}
			}
		}
		return unformatted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String xmlDocumentToFormattedString(Document document) {
		Element element = null;
		String formatted = null;
		if(document !=null){
			if(document.getDocumentElement() !=null){				
					element = document.getDocumentElement();			
					if(element !=null){
						formatted = XMLHelper.prettyPrintXML(element);
						formatted = formatted.replace(StringConstants.XML_OMIT_STRING, "");
					}
			}				
		}
		return formatted;
	}

	/**
	 * {@inheritDoc}
	 */
	public String soapMessageToUnFormattedString(SOAPMessage soapMessage) {
		Element soapElement = null;
		String unformatted = null;
		try {
			if(soapMessage != null){
				soapElement = soapMessage.getSOAPPart().getEnvelope().getOwnerDocument().getDocumentElement();
				if(soapElement != null){
					unformatted = XMLHelper.nodeToString(soapElement);
					unformatted = unformatted.replace(StringConstants.SOAP_REPLACE_FROM,StringConstants.SOAP_REPLACE_TO);
				}else{
					unformatted = "SOAP Element is null!";
					logger.error(unformatted);
				}
			}else{
				unformatted = "SOAPMessage is null!";
				logger.error(unformatted);
			}
		}catch(SOAPException e){
			unformatted = "Formatting failed: ";
			logger.error(unformatted,e);
		}
		return unformatted;
	}

	/**
	 * {@inheritDoc}
	 */
	public String soapMessageToFormattedString(SOAPMessage soapMessage){
		Element soapElement = null;
		String formatted = null;
		try {
			if(soapMessage !=null){
				soapElement = soapMessage.getSOAPPart().getEnvelope().getOwnerDocument().getDocumentElement();
			}	
			if(soapElement !=null){
				formatted = XMLHelper.prettyPrintXML(soapElement);
				formatted = formatted.replace(StringConstants.SOAP_REPLACE_FROM,StringConstants.SOAP_REPLACE_TO);
			}			
		}catch(SOAPException e){
			String message = "Failed to get owning document from SOAPMessage : ";
			logger.error(message,e);	
		}
		return formatted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String saml2ResponseToFormattedString(Response response) {
		Element saml2ResponseElement = null;
		String saml2ResponseString = null;
		try {
			if(response !=null){
				saml2ResponseElement = responseMarshaller.marshall(response);
			}				
			if(saml2ResponseElement !=null) {				
				saml2ResponseString = XMLHelper.prettyPrintXML(saml2ResponseElement);							
			}
		}catch(MarshallingException e){
			String message ="saml2ResponseToString(Response), Marshalling failed";
			logger.error(message,e);
		}
		return saml2ResponseString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String saml2ResponseToUnFormattedString(Response response) {
		Element saml2ResponseElement = null;
		String saml2ResponseString = null;
		try {
			if(response !=null){
				saml2ResponseElement = responseMarshaller.marshall(response);
			}	
			if(saml2ResponseElement !=null) {				
				saml2ResponseString = XMLHelper.nodeToString(saml2ResponseElement);							
			}
		}catch(MarshallingException e){
			String message ="saml2ResponseToString(Response), Marshalling failed";
			logger.error(message,e);
		}
		return saml2ResponseString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String saml2AssertionToFormattedString(Assertion assertion) {
		Element saml2AssertionElement = null;
		String saml2AssertionString = null;
		try {
			if(assertion !=null){
				saml2AssertionElement = assertionMarshaller.marshall(assertion);
			}	
			if(saml2AssertionElement !=null) {				
				saml2AssertionString = XMLHelper.prettyPrintXML(saml2AssertionElement);				
			}
		}catch(MarshallingException e){
			String message ="saml2AssertionToUnFormattedString(Assertion), Marshalling failed";
			logger.error(message,e);
		}
		return saml2AssertionString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String saml2AssertionToUnFormattedString(Assertion assertion) {
		Element saml2AssertionElement = null;
		String saml2AssertionString = null;
		try {
			if(assertion != null) {
				saml2AssertionElement = assertionMarshaller.marshall(assertion);
				if(saml2AssertionElement != null) {				
					saml2AssertionString = XMLHelper.nodeToString(saml2AssertionElement);				
				}else{
					saml2AssertionString ="AssertionElement is null! ";
					logger.error(saml2AssertionString);
				}
			}else{
				saml2AssertionString ="Assertion is null! ";
				logger.error(saml2AssertionString);
			}
		}catch(MarshallingException e){
			saml2AssertionString ="saml2AssertionToUnFormattedString(Assertion), Marshalling failed";
			logger.error(saml2AssertionString,e);
		}
		return saml2AssertionString;
	}

	public AssertionMarshaller getAssertionMarshaller() {
		return assertionMarshaller;
	}

	public void setAssertionMarshaller(AssertionMarshaller assertionMarshaller) {
		this.assertionMarshaller = assertionMarshaller;
	}

	public ResponseMarshaller getResponseMarshaller() {
		return responseMarshaller;
	}

	public void setResponseMarshaller(ResponseMarshaller responseMarshaller) {
		this.responseMarshaller = responseMarshaller;
	}	
}
