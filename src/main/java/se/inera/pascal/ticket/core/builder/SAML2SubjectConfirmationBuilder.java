package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2SubjectConfirmationBuilder implements Builder<SubjectConfirmation> {
	
	private SubjectConfirmationData subjectConfirmationData;
	private String method = SubjectConfirmation.METHOD_HOLDER_OF_KEY;
	
	public SAML2SubjectConfirmationBuilder(SubjectConfirmationData subjectConfirmationData){
		this.subjectConfirmationData = subjectConfirmationData;		
	}

	public SAML2SubjectConfirmationBuilder setMethod(String method) {
		this.method = method;
		return this;
	}

	
	public SubjectConfirmation build() {
		SubjectConfirmation subjectConfirmation = new SubjectConfirmationBuilder().buildObject();
		subjectConfirmation.setMethod(method);
		subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
		return subjectConfirmation;
	}

}
