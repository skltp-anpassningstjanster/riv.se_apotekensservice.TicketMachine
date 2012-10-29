package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.impl.StatusMessageBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2StatusMessageBuilder implements Builder<StatusMessage> {

	
	public StatusMessage build() {
		StatusMessage statusMessage = new StatusMessageBuilder().buildObject();
		return statusMessage;
	}

}
