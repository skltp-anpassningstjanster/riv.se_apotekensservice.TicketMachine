package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.impl.IssuerBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2IssuerBuilder implements Builder<Issuer> {
	
//	private String value = "https://federation.apotekensservice.se";	
	private String value = StringConstants.DEFAULT_ISSUER;	

	public SAML2IssuerBuilder setValue(String value) {
		this.value = value;
		return this;
	}

	
	public Issuer build() {
		Issuer issuer = new IssuerBuilder().buildObject();
		issuer.setValue(value);
		return issuer;
	}

}
