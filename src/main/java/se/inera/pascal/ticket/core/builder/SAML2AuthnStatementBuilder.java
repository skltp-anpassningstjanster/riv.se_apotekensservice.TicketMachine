package se.inera.pascal.ticket.core.builder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2AuthnStatementBuilder implements Builder<AuthnStatement> {

	private AuthnContext authnContext;
//	private DateTime authnInstant = new DateTime(2010,11,5,9,0,0,50,DateTimeZone.UTC);
	private DateTime authnInstant = new DateTime(DateTimeZone.UTC);
	
	public SAML2AuthnStatementBuilder(AuthnContext authnContext) {
		this.authnContext = authnContext;		
	}
	
	public SAML2AuthnStatementBuilder(DateTime authnInstant,AuthnContext authnContext) {
		this.authnInstant = authnInstant;
		this.authnContext = authnContext;		
	}	

	
	public AuthnStatement build() {
		AuthnStatement authnStatement = new AuthnStatementBuilder().buildObject();	
		authnStatement.setAuthnContext(authnContext);
		authnStatement.setAuthnInstant(authnInstant);
		return authnStatement;
	}

}
