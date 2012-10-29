package se.inera.pascal.ticket.core.impl;

import se.inera.pascal.ticket.core.SAML2AssertionAttributeSet;
import se.inera.pascal.ticket.core.SAML2AssertionTicketGenerator;

public abstract class SAML2AssertionTicketGeneratorLauncherSupport {
	
	final static Boolean DEFAULT_FORMATTING=false;
	
	protected static String getMessageAsString(SAML2AssertionTicketGenerator cmdTool, 
											   SAML2AssertionAttributeSet attributes,
											   boolean getSecurityOnly) {
		String message = null;
		message = cmdTool.generateSOAPMessageWithoutPayload(attributes,getSecurityOnly);
		return message;
	}
	protected static String getValidToString(SAML2AssertionTicketGenerator cmdTool){
		String validTo = null;
		validTo = cmdTool.getValidToString();
		return validTo;
	}
}