package se.inera.pascal.ticket.core;

import java.util.List;

import se.inera.pascal.ticket.core.SAML2Attribute;

public class SAML2AssertionAttributeSet {
	
	private List<SAML2Attribute> authnAttributes;
	private List<SAML2Attribute> authorizationAttributes;
	private List<SAML2Attribute> auditingAttributes;
	public List<SAML2Attribute> getAuthnAttributes() {
		return authnAttributes;
	}
	public void setAuthnAttributes(List<SAML2Attribute> authnAttributes) {
		this.authnAttributes = authnAttributes;
	}
	public List<SAML2Attribute> getAuthorizationAttributes() {
		return authorizationAttributes;
	}
	public void setAuthorizationAttributes(
			List<SAML2Attribute> authorizationAttributes) {
		this.authorizationAttributes = authorizationAttributes;
	}
	public List<SAML2Attribute> getAuditingAttributes() {
		return auditingAttributes;
	}
	public void setAuditingAttributes(List<SAML2Attribute> auditingAttributes) {
		this.auditingAttributes = auditingAttributes;
	}
}
