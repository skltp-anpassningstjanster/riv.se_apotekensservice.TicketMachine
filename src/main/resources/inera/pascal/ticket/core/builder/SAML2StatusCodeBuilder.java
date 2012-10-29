package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2StatusCodeBuilder implements Builder<StatusCode> {
//	private String value="urn:oasis:names:tc:SAML:2.0:status:Success";
	private String value=StringConstants.STATUS_CODE;
	private StatusCode statusCodePointer;
	
	public SAML2StatusCodeBuilder() {}
	
	public SAML2StatusCodeBuilder(StatusCode statusCodePointer) {
		this.statusCodePointer = statusCodePointer;
	}
	
	public SAML2StatusCodeBuilder setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public StatusCode build() {
		StatusCode statusCode = new StatusCodeBuilder().buildObject();
		statusCode.setValue(value);
		statusCode.setStatusCode(statusCodePointer);
		return statusCode;
	}

	

	

	
}
