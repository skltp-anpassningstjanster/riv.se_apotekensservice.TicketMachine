package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.impl.NameIDBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2NameIDBuilder implements Builder<NameID> {
	
	/**
	 * This format is hard coded, cause OpenSAML, translates its format to SAML 1.1, instead of 2.0.
	 * And considered a defect or bug.
	 */
	private static String X509_SUBJECT = StringConstants.NAME_ID_X509;	
	
	private String format = X509_SUBJECT;
	private String nameQualifier = "subject fqn or serial";
	private String value = "SubjectName";	
	
	public SAML2NameIDBuilder setFormat(String format) {
		this.format = format;
		return this;
	}
	
	public SAML2NameIDBuilder setNameQualifier(String nameQualifier) {
		this.nameQualifier = nameQualifier;
		return this;
	}	

	public SAML2NameIDBuilder setValue(String value) {
		this.value = value;
		return this;
	}
	
	@Override
	public NameID build() {
		NameID nameId = new NameIDBuilder().buildObject();
		nameId.setFormat(format);
		nameId.setNameQualifier(nameQualifier);		
		nameId.setValue(value);
		return nameId;
	}
}
