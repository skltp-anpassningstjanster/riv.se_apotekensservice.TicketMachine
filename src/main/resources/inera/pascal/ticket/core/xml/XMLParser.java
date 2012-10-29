package se.inera.pascal.ticket.core.xml;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Response;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.SAML2Attribute;

public class XMLParser {
	private static final Logger logger = LoggerFactory.getLogger(XMLParser.class);
	
	//skapa en lista av Assertions
	//denna lista kommer enbart innehålla en post
	private List<Assertion> assertions = null;
	private boolean isBIF = true;
	
	public XMLParser(XMLObject xmldata) throws ClassNotFoundException{
		config(xmldata);
	}
	public XMLParser(XMLObject xmldata, boolean isbif) throws ClassNotFoundException{
		isBIF = isbif;
		config(xmldata);
	}
	private void config(XMLObject xmldata) throws ClassNotFoundException{
		if (xmldata instanceof Assertion) {
			assertions = new ArrayList<Assertion>();
			assertions.add((Assertion) xmldata);
		}else if (xmldata instanceof Response){
			assertions = ((Response) xmldata).getAssertions();
		}else {
			throw new ClassNotFoundException();
		}
	}
	
	public void validate() throws CertificateException, ValidationException {
		for (Assertion assertion : assertions){
			validateDateTime(assertion);
			validateSignature(assertion);
		}
	}
	public List<SAML2Attribute> parse(){
		List<SAML2Attribute> ret = new ArrayList<SAML2Attribute>();
		String logMess = "===== Incoming ticket name/value-list =====";
		logger.info(logMess);
		for (Assertion assertion : assertions){
			ret = parseAttributes(assertion);
			String name;
			String value;
			if ( !isBIF ){
				//LkTj-biljetten sätter förskrivarkoden som NameID
				name = assertion.getSubject().getNameID().getFormat();
				value = assertion.getSubject().getNameID().getValue();
				logMess = " " + name + " : " + value;
				logger.info(logMess);
				SAML2Attribute nameid = new SAML2Attribute(name,value);
				ret.add(nameid);
			}
			name = "AssertionID"; //TODO: remove hard coded string?
			value = assertion.getID();
			logMess = " " + name + " : " + value;
			logger.info(logMess);
			SAML2Attribute assertID = new SAML2Attribute(name,value);
			ret.add(assertID);
		}
		return ret;
	}
	
	private void validateDateTime(Assertion assertion) throws ValidationException{
		DateTime now = new DateTime();
		Conditions conditions = assertion.getConditions();
		DateTime notBefore = conditions.getNotBefore();
		DateTime notAfter = conditions.getNotOnOrAfter();
		if (now.getMillis() < notBefore.getMillis()){
			throw new ValidationException("notBefore validation failed!");
		}
		if (now.getMillis() > notAfter.getMillis()){
			throw new ValidationException("notOnOrAfter validation failed!");
		}		
	}
	
	private void validateSignature(Assertion assertion) throws ValidationException, CertificateException{
        assertion.validate(true);
        Signature signature = assertion.getSignature();
        KeyInfo inf = signature.getKeyInfo();
		List<X509Certificate> certs = KeyInfoHelper.getCertificates(inf);
		if (certs == null || certs.isEmpty() ){
			throw new CertificateException("KeyInfoHelper contains no certificates, unable to validate signature!");
		}
		X509Certificate cert = certs.get(0);
		//TODO: verify certificate issuer/subject?
//		Principal pr = cert.getIssuerDN();
//		pr = cert.getSubjectDN();

		SAMLSignatureProfileValidator pv = new SAMLSignatureProfileValidator();
        pv.validate(signature);
        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(cert);

        SignatureValidator sigValidator = new SignatureValidator(credential);
        sigValidator.validate(signature);
        
    }

	private List<SAML2Attribute> parseAttributes(Assertion assertion){
		List<AttributeStatement> attribStatmts = assertion.getAttributeStatements();
		List<SAML2Attribute> saml2Attributes = new ArrayList<SAML2Attribute>();
		for ( AttributeStatement attrStatement : attribStatmts )
		{	
			List<Attribute> attributes = attrStatement.getAttributes();
			for ( Attribute attr : attributes ){
				List<XMLObject> vals = attr.getAttributeValues();
				String value = vals.get(0).getDOM().getFirstChild().getNodeValue();
				String name = attr.getName();
				String logMess = " " + name + " : " + value;
				logger.info(logMess);
				SAML2Attribute sa2attr = new SAML2Attribute(name,value);
				saml2Attributes.add(sa2attr);				
			}
		}
		return saml2Attributes;
	}
}
