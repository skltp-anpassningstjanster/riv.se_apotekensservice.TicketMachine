package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.StatusDetail;
import org.opensaml.saml2.core.impl.StatusDetailBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2StatusDetailBuilder implements Builder<StatusDetail> {
	
	
	public StatusDetail build() {
		StatusDetail statusDetail = new StatusDetailBuilder().buildObject();
		return statusDetail;
	}

}
