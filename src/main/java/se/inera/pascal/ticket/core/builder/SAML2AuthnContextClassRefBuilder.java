package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2AuthnContextClassRefBuilder implements Builder<AuthnContextClassRef> {
	
//	private String authnContextClassReference="urn:oasis:names:tc:SAML:2.0:ac:classes:SmartcardPKI";
	private String authnContextClassReference= StringConstants.AUTHN_CONTEXT_CLASS_REFERENCE;

	public SAML2AuthnContextClassRefBuilder setAuthnContextClassRef(String authnContextClassReference) {
		this.authnContextClassReference = authnContextClassReference;
		return this;
	}


	public AuthnContextClassRef build() {
		AuthnContextClassRef authnContextClassRef = new AuthnContextClassRefBuilder().buildObject();
		authnContextClassRef.setAuthnContextClassRef(authnContextClassReference);
		return authnContextClassRef;
	}

}
