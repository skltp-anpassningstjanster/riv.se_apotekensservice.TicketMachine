package se.inera.pascal.ticket.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.signature.Signature;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.impl.SAML2AssertionGeneratorImpl;

public class SAML2AssertionBuilder implements Builder<Assertion> {
	
	private String id = new SAML2AssertionGeneratorImpl().generateSAML2AssertionId(null);
//	private DateTime issueInstant = new DateTime(2010,11,5,9,0,0,50,DateTimeZone.UTC);
	private DateTime issueInstant = new DateTime(DateTimeZone.UTC);
	private Issuer issuer;
	private Subject subject;
	private Conditions conditions;
	private XMLObjectBuilderFactory xmlObjectBuilderFactory;
	private Signature signature;
	
	private List<AttributeStatement> attributeStatements = new ArrayList<AttributeStatement>();
	private List<AuthnStatement> authnStatements = new ArrayList<AuthnStatement>();

	public SAML2AssertionBuilder setId(String id) {
		this.id = id;
		return this;
	}
	
	public SAML2AssertionBuilder setIssueInstant(DateTime issueInstant) {
		this.issueInstant = issueInstant;
		return this;
	}
	
	public SAML2AssertionBuilder(XMLObjectBuilderFactory xmlObjectBuilderFactory,Issuer issuer, Conditions conditions){
		this.xmlObjectBuilderFactory = xmlObjectBuilderFactory;
		this.issuer = issuer;		
		this.conditions = conditions;
	}
	
	public SAML2AssertionBuilder(XMLObjectBuilderFactory xmlObjectBuilderFactory,Issuer issuer, Subject subject, Conditions conditions){
		this.xmlObjectBuilderFactory = xmlObjectBuilderFactory;
		this.issuer = issuer;
		this.subject = subject;
		this.conditions = conditions;
	}
	
	public SAML2AssertionBuilder(XMLObjectBuilderFactory xmlObjectBuilderFactory,Issuer issuer, Subject subject, Conditions conditions, Signature signature){
		this.xmlObjectBuilderFactory = xmlObjectBuilderFactory;
		this.issuer = issuer;
		this.subject = subject;
		this.conditions = conditions;
		this.signature = signature;
	}
	
	public SAML2AssertionBuilder addAuthnStatement(AuthnStatement authnStatement){
		authnStatements.add(authnStatement);
		return this;
	}
	
	public SAML2AssertionBuilder addAttributStatement(AttributeStatement attributeStatement){
		attributeStatements.add(attributeStatement);
		return this;
	}

	public Assertion build() {
		AssertionBuilder assertionBuilder = (AssertionBuilder) xmlObjectBuilderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
		Assertion assertion = assertionBuilder.buildObject();		
		assertion.setID(id);
		assertion.setIssueInstant(issueInstant);
		assertion.setVersion(SAMLVersion.VERSION_20);
		assertion.setIssuer(issuer);
		assertion.setSubject(subject);
		assertion.setConditions(conditions);
		if(signature !=null){
			assertion.setSignature(signature);
		}
		if(authnStatements.size() > 0){
			for (AuthnStatement authnStatement : authnStatements) {
				assertion.getAuthnStatements().add(authnStatement);
			}
		}
		if(attributeStatements.size() > 0){
			for (AttributeStatement attributeStatement : attributeStatements) {
				assertion.getAttributeStatements().add(attributeStatement);
			}
		}
		
		return assertion;
	}

}
