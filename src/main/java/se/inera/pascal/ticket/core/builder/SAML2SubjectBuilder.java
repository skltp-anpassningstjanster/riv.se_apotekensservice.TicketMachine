package se.inera.pascal.ticket.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.impl.SubjectBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2SubjectBuilder implements Builder<Subject> {
	
	private NameID nameId;
	private List<SubjectConfirmation> subjectConfermations = new ArrayList<SubjectConfirmation>();
	
	public SAML2SubjectBuilder(NameID nameId){
		this.nameId = nameId;
	}
	
	public SAML2SubjectBuilder addSubjectConfirmation(SubjectConfirmation subjectConfirmation){
		subjectConfermations.add(subjectConfirmation);
		return this;
	}

	
	public Subject build() {
		Subject subject = new SubjectBuilder().buildObject();
		subject.setNameID(nameId);		
		if(subjectConfermations.size() >0){
			for (SubjectConfirmation subjectConfirmation : subjectConfermations) {
				subject.getSubjectConfirmations().add(subjectConfirmation);
			}
		}
		return subject;
	}
}
