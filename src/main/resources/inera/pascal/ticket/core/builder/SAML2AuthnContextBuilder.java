package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.impl.AuthnContextBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2AuthnContextBuilder implements Builder<AuthnContext> {
	
	private AuthnContextClassRef authnContextClassRef;
	
	public SAML2AuthnContextBuilder(AuthnContextClassRef authnContextClassRef){
		this.authnContextClassRef = authnContextClassRef;
	}

	@Override
	public AuthnContext build() {
		AuthnContext authnContext = new AuthnContextBuilder().buildObject();
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		return authnContext;
	}
}