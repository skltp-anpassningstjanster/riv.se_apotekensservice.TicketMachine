package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusDetail;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.impl.StatusBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2StatusBuilder implements Builder<Status> {
	
	private StatusCode statusCode;
	private StatusDetail statusDetail;
	private StatusMessage statusMessage;
	
	
	public SAML2StatusBuilder(StatusCode statusCode){
		this.statusCode = statusCode;
	}
	
	public SAML2StatusBuilder(StatusCode statusCode, StatusDetail statusDetail){
		this.statusCode = statusCode;
		this.statusDetail = statusDetail;
	}
	
	public SAML2StatusBuilder(StatusCode statusCode, StatusDetail statusDetail, StatusMessage statusMessage){
		this.statusCode = statusCode;
		this.statusDetail = statusDetail;
		this.statusMessage = statusMessage;
	}

	@Override
	public Status build() {
		Status status = new StatusBuilder().buildObject();
		status.setStatusCode(statusCode);
		status.setStatusDetail(statusDetail);
		status.setStatusMessage(statusMessage);
		return status;
	}
}
