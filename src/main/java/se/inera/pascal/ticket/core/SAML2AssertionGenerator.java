package se.inera.pascal.ticket.core;

import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;

public interface SAML2AssertionGenerator {
	
	/**
	 * @param parentAssertionId The parent/owning assertionId
	 * @param assertionType The of the Assertion, referred to as block
	 * @param issueInstant The time when the SAML2 assertion is issued
	 * @param nameQualifier The name qualifier
	 * @param nameIdValue The value for the NameID
	 * @param validFrom The DateTime from which SAML2 Assertion is valid
	 * @param validTo   The DateTime from which SAML2 Assertion will expire
	 * @param authnInstant The Datetime when the authentication was performed
	 * @param saml2Attributes A collection of SAML Attributes
	 * @return Return a SAML2 Assertion
	 */
	public Assertion generateSAML2Assertion(String parentAssertionId,
											String assertionType,
			                                DateTime issueInstant,										 
			  							    DateTime validFrom, 
			  							    DateTime validTo,										  
			  							    List<SAML2Attribute> saml2Attributes);
	/**
	 * @param issueInstant The time when the SAML2 assertion is issued
	 * @param nameQualifier The name qualifier
	 * @param nameIdValue The value for the NameID
	 * @param validFrom The DateTime from which SAML2 Assertion is valid
	 * @param validTo   The DateTime from which SAML2 Assertion will expire
	 * @param authnInstant The Datetime when the authentication was performed
	 * @param saml2Attributes A collection of SAML Attributes
	 * @return Return a SAML2 Assertion
	 */
	public Assertion generateSAML2Assertion(DateTime issueInstant,
			  							  String nameQualifier,
			  							  String nameIdValue,
			  							  DateTime validFrom, 
			  							  DateTime validTo,
			  							  DateTime authnInstant,
			  							  List<SAML2Attribute> saml2Attributes);
	

	/**
	 * @param algorithm The specified algorithm
	 * @return Returns identifier which is compatible with the xsd:ID format
	 */
	public String generateSAML2AssertionId(String algorithm);	

}
