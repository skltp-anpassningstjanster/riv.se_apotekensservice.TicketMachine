package se.inera.pascal.ticket.core.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.DefaultValues;
import se.inera.pascal.ticket.core.SAML2AssertionGenerator;
import se.inera.pascal.ticket.core.SAML2Attribute;
import se.inera.pascal.ticket.core.StringConstants;
import se.inera.pascal.ticket.core.builder.SAML2AssertionBuilder;
import se.inera.pascal.ticket.core.builder.SAML2AttributeBuilder;
import se.inera.pascal.ticket.core.builder.SAML2AttributeStatementBuilder;
import se.inera.pascal.ticket.core.builder.SAML2AuthnContextBuilder;
import se.inera.pascal.ticket.core.builder.SAML2AuthnContextClassRefBuilder;
import se.inera.pascal.ticket.core.builder.SAML2AuthnStatementBuilder;
import se.inera.pascal.ticket.core.builder.SAML2ConditionsBuilder;
import se.inera.pascal.ticket.core.builder.SAML2IssuerBuilder;
import se.inera.pascal.ticket.core.builder.SAML2NameIDBuilder;
import se.inera.pascal.ticket.core.builder.SAML2SubjectBuilder;
import se.inera.pascal.ticket.core.builder.SAML2SubjectConfirmationBuilder;
import se.inera.pascal.ticket.core.builder.SAML2SubjectConfirmationDataBuilder;

public class SAML2AssertionGeneratorImpl implements SAML2AssertionGenerator {
	
	static private final Logger logger = LoggerFactory.getLogger(SAML2AssertionGeneratorImpl.class);

	private  XMLObjectBuilderFactory builderFactory;
	private  SecureRandomIdentifierGenerator secureRandomIdentifierGenerator;
	private DefaultValues defaultValues;

	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionGenerator#generateSAML2Assertion(java.lang.String, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, java.util.List)
	 */
	public Assertion generateSAML2Assertion(String parentAssertionId,String
			assertionTypeConstant,DateTime issueInstant, DateTime validFrom, DateTime validTo,
			List<SAML2Attribute> saml2Attributes) {
		
		if(StringUtils.isBlank(parentAssertionId) ||
		   StringUtils.isBlank(assertionTypeConstant) ||
		   issueInstant == null || 
		   validFrom == null ||
		   validTo == null || 
		   saml2Attributes == null || 
		   saml2Attributes.size() ==0){
			String message ="Paramters required to generate sub SAML2 Assertion is not assigned !";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}		
		
		SAML2ConditionsBuilder saml2ConditionsBuilder = new SAML2ConditionsBuilder();
		saml2ConditionsBuilder.setNotBefore(validFrom);
		saml2ConditionsBuilder.setNotOnOrAfter(validTo);
		Conditions conditions = saml2ConditionsBuilder.build();
		SAML2AttributeStatementBuilder saml2AttributeStatementBuilder = new SAML2AttributeStatementBuilder();
		SAML2Attribute connectedAssertionId = new SAML2Attribute(StringConstants.ATTRIBUTE_CONNECTED_ASSERTION_ID, parentAssertionId);
		SAML2Attribute assertionType = new SAML2Attribute(StringConstants.ATTRIBUTE_ASSERTION_TYPE, assertionTypeConstant);
		saml2Attributes.add(0,connectedAssertionId);
		saml2Attributes.add(1,assertionType);
		for (SAML2Attribute saml2Attribute : saml2Attributes) {
			Attribute attribute = new SAML2AttributeBuilder(saml2Attribute.getName(), saml2Attribute.getValue()).build();
			saml2AttributeStatementBuilder.addAttribute(attribute);
		}
		AttributeStatement attributeStatement = saml2AttributeStatementBuilder.build();
		SAML2IssuerBuilder issBuild = new SAML2IssuerBuilder();
		issBuild.setValue(defaultValues.getCertificateSubjectName());
		Issuer issuer = issBuild.build();		
		SAML2AssertionBuilder saml2AssertionBuilder = new SAML2AssertionBuilder(builderFactory,issuer,conditions);
		saml2AssertionBuilder.setIssueInstant(issueInstant);
		saml2AssertionBuilder.addAttributStatement(attributeStatement);
		Assertion assertion = saml2AssertionBuilder.build();		
		return assertion;
	}
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionGenerator#generateSAML2Assertion(org.joda.time.DateTime, java.lang.String, java.lang.String, org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.DateTime, java.util.List)
	 */
	public Assertion generateSAML2Assertion(DateTime issueInstant,
			String nameQualifier, String nameIdValue, DateTime validFrom,
			DateTime validTo, DateTime authnInstant,
			List<SAML2Attribute> saml2Attributes) {
		
		if(issueInstant == null || 
		   StringUtils.isBlank(nameQualifier) ||
		   StringUtils.isBlank(nameIdValue) ||
		   validFrom == null ||
		   validTo == null ||
		   authnInstant == null ||
		   saml2Attributes == null || saml2Attributes.size() == 0) {
			String message ="Paramters required to generate parent SAML2 Assertion is not assigned !";
			logger.info(message);
			throw new IllegalArgumentException(message);
		}
		SAML2NameIDBuilder saml2NameIDBuilder = new SAML2NameIDBuilder();
		saml2NameIDBuilder.setNameQualifier(nameQualifier);
		saml2NameIDBuilder.setValue(nameIdValue);
		NameID nameId = saml2NameIDBuilder.build();
		SAML2SubjectBuilder subjectBuilder  = new SAML2SubjectBuilder(nameId);
		//do we need the subject confirmation?
		if ( defaultValues.getBuildingSubjectConfirmation().equalsIgnoreCase("true") ||
				defaultValues.getBuildingSubjectConfirmation().equalsIgnoreCase("yes")	
		){
			SAML2SubjectConfirmationDataBuilder subjConfDataBuilder = new SAML2SubjectConfirmationDataBuilder();
			subjConfDataBuilder.setKeyNameValue(StringConstants.KEYNAME_VALUE);
			SubjectConfirmationData confirmationData = subjConfDataBuilder.build();				
			SubjectConfirmation subjectConfirmation = new SAML2SubjectConfirmationBuilder(confirmationData).build();
			subjectBuilder.addSubjectConfirmation(subjectConfirmation);
		}
		Subject subject = subjectBuilder.build();		
		AuthnContextClassRef authnContextClassRef = new SAML2AuthnContextClassRefBuilder().build();
		AuthnContext authnContext = new SAML2AuthnContextBuilder(authnContextClassRef).build();
		AuthnStatement authnStatement = new SAML2AuthnStatementBuilder(authnInstant,authnContext).build();
		SAML2ConditionsBuilder saml2ConditionsBuilder = new SAML2ConditionsBuilder();
		saml2ConditionsBuilder.setNotBefore(validFrom);
		saml2ConditionsBuilder.setNotOnOrAfter(validTo);
		Conditions conditions = saml2ConditionsBuilder.build();		
		SAML2AttributeStatementBuilder saml2AttributeStatementBuilder = new SAML2AttributeStatementBuilder();
		for (SAML2Attribute saml2Attribute : saml2Attributes) {
				Attribute attribute = new SAML2AttributeBuilder(saml2Attribute.getName(), saml2Attribute.getValue()).build();
				saml2AttributeStatementBuilder.addAttribute(attribute);
			}
		AttributeStatement attributeStatement = saml2AttributeStatementBuilder.build();
		SAML2IssuerBuilder issBuild = new SAML2IssuerBuilder();
		issBuild.setValue(defaultValues.getCertificateSubjectName());
		Issuer issuer = issBuild.build();		
		SAML2AssertionBuilder saml2AssertionBuilder = new SAML2AssertionBuilder(builderFactory,issuer, subject, conditions);
		saml2AssertionBuilder.setIssueInstant(issueInstant);
		saml2AssertionBuilder.addAttributStatement(attributeStatement);
		saml2AssertionBuilder.addAuthnStatement(authnStatement);
		Assertion assertion = saml2AssertionBuilder.build();		
		return assertion;		
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionGenerator#generateSAML2AssertionId(java.lang.String)
	 */
	public String generateSAML2AssertionId(String algorithm) {
		if(secureRandomIdentifierGenerator == null){
			try {
				if(StringUtils.isNotBlank(algorithm)) {
					secureRandomIdentifierGenerator = new SecureRandomIdentifierGenerator(algorithm);
				}else {
					secureRandomIdentifierGenerator = new SecureRandomIdentifierGenerator();
				}
			}catch(NoSuchAlgorithmException e) {
				String message="Algoritm not supporterd : ";
				logger.error(message,e);
			}
		}
		return secureRandomIdentifierGenerator.generateIdentifier();
	}	
	
	public SecureRandomIdentifierGenerator getSecureRandomIdentifierGenerator() {
		return secureRandomIdentifierGenerator;
	}
	
	public void setSecureRandomIdentifierGenerator(
			SecureRandomIdentifierGenerator secureRandomIdentifierGenerator) {
		this.secureRandomIdentifierGenerator = secureRandomIdentifierGenerator;
	}
	
	public XMLObjectBuilderFactory getBuilderFactory() {
		return builderFactory;
	}
	
	public void setBuilderFactory(XMLObjectBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}
	public DefaultValues getDefaultValues() {
		return defaultValues;
	}
	
	public void setDefaultValues(DefaultValues defaultValues) {
		this.defaultValues = defaultValues;
	}
}
