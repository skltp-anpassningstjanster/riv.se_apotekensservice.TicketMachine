package se.inera.pascal.ticket.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.xml.signature.Signature;

import se.inera.pascal.ticket.core.Builder;

public class SAML2ResponseBuilder implements Builder<Response> {
	
//	private DateTime issueInstant = new DateTime(2010,11,5,9,0,0,50,DateTimeZone.UTC);
	private DateTime issueInstant = new DateTime(DateTimeZone.UTC);
	private String id = UUID.randomUUID().toString();
	private String inResponseTo = UUID.randomUUID().toString();
	private Issuer issuer;
	private Status status;
	private Signature signature;
	private List<Assertion> assertions = new ArrayList<Assertion>();
	
	public SAML2ResponseBuilder(Issuer issuer, Status status){
		this.issuer = issuer;
		this.status = status;		
	}
	
	public SAML2ResponseBuilder(Issuer issuer, Status status, Signature signature){
		this.issuer = issuer;
		this.status = status;
		this.signature = signature;
	}

	public SAML2ResponseBuilder setIssueInstant(DateTime issueInstant) {
		this.issueInstant = issueInstant;
		return this;
	}
	
	public SAML2ResponseBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public SAML2ResponseBuilder setInResponseTo(String inResponseTo) {
		this.inResponseTo = inResponseTo;
		return this;
	}
	
	public SAML2ResponseBuilder addAssertion(Assertion assertion){
		assertions.add(assertion);
		return this;
	}
	
	public SAML2ResponseBuilder removeAssertion(Assertion assertion){
		assertions.remove(assertion);
		return this;
	}

	
	public Response build() {
		Response saml2Response = new ResponseBuilder().buildObject();
		saml2Response.setIssueInstant(issueInstant);
		saml2Response.setVersion(SAMLVersion.VERSION_20);
		saml2Response.setID(id);
		saml2Response.setInResponseTo(inResponseTo);
		saml2Response.setIssuer(issuer);
		saml2Response.setStatus(status);
		saml2Response.setSignature(signature);
		if(assertions.size() >0){
			for (Assertion assertion : assertions) {
				saml2Response.getAssertions().add(assertion);
			}
		}
		return saml2Response;
	}
}
