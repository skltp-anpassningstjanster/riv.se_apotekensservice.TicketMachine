package se.inera.pascal.ticket.core.impl;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.DefaultValues;
import se.inera.pascal.ticket.core.SAML2AssertionAttributeSet;
import se.inera.pascal.ticket.core.SAML2AssertionTicketGenerator;
import se.inera.pascal.ticket.core.SAML2Attribute;
import se.inera.pascal.ticket.core.StringConstants;
import se.inera.pascal.ticket.core.xml.XMLParser;
import se.inera.pascal.ticket.core.xml.XMLReader;

public class SAML2AssertionTicketGeneratorLauncher extends SAML2AssertionTicketGeneratorLauncherSupport {

	private static final Logger logger = LoggerFactory.getLogger(SAML2AssertionTicketGeneratorLauncher.class);

	private ApplicationContext appCtx = null;
	private SAML2AssertionTicketGenerator atg = null;
	private DefaultValues defVal = null;
	private SAML2AssertionAttributeSet attributeSet;
	private BasicParserPool parser;
	private ApseAuthorizationAttributes apseAuthorization;
	private ApseAuthenticationAttributes apseAuthentication;
	private ApseInfoAttributes apseInfo;
	private String errorString = "";

	// starta applikationen och konfigurera alla beans
	public SAML2AssertionTicketGeneratorLauncher() throws Exception {
		attributeSet = new SAML2AssertionAttributeSet();
		logger.debug("Loading ApplicationContext");
		// commandtool.xml innehller alla beans som springframework skall
		// starta upp
		try {
			appCtx = new ClassPathXmlApplicationContext(new String[] { "commandtool.xml" });
			logger.debug("Loading Ticket Generator");
			atg = (SAML2AssertionTicketGenerator) appCtx.getBean("saml2AssertionTicketGenerator");
			logger.debug("Loading Default Values");
			// DefaultValues r en "container" fr samtliga standard vrden som
			// vi anvnder i applikationen
			// vissa av dessa vrden br normalt ndras av inkommande biljetter
			defVal = (DefaultValues) appCtx.getBean("defaultValues");
			parser = (BasicParserPool) appCtx.getBean("parser");
			apseAuthorization = defVal.getApseAuthorizationAttributes();
			apseAuthentication = defVal.getApseAuthenticationAttributes();
			apseInfo = defVal.getApseInfoAttributes();
		} catch (Exception e) {
			errorString = "Failed initialize launcher application!";
			logger.error(errorString, e);
			throw new Exception(errorString);
		}
	}

	public void analyzeXML(String xml, boolean isBIF, boolean isFile) throws Exception {
		// isFile skall bara vara true i utveckling/test
		try {
			XMLObject xObj = null;
			XMLReader xRead = new XMLReader(parser);
			Document doc;
			if (isFile) {
				File xmlf = new File(xml);
				doc = xRead.parseFile(xmlf);
			} else {
				doc = xRead.parseString(xml);
			}
			Element element = doc.getDocumentElement();

			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
			xObj = unmarshaller.unmarshall(element);

			// Skapa ett XMLParser-objekt av inkommande biljett, validera (om
			// konfigurerat)
			// och parsa sedan ut attributen
			XMLParser xp = new XMLParser(xObj, isBIF);
			if (isBIF) {
				if (defVal.getValidateBIF().equalsIgnoreCase("true") || defVal.getValidateBIF().equalsIgnoreCase("yes")) {
					xp.validate();
				}
			} else {
				if (defVal.getValidateLkTj().equalsIgnoreCase("true")
						|| defVal.getValidateLkTj().equalsIgnoreCase("yes")) {
					xp.validate();
				}
			}
			List<SAML2Attribute> attribs = xp.parse();
			// iterera igenom samtliga attribut som parsern hittat och koppla
			// dem till rtt typ av ApSe-attribut (authentication,
			// authorization, info)
			for (SAML2Attribute att : attribs) {
				String name = att.getName().toLowerCase();
				String value = att.getValue();
				// assertionID r den inkommande biljettens unika ID, vi
				// teranvnder det vrdet
				// och stter det som unikt id mot ApSe
				if (name.contains(("AssertionID").toLowerCase())) { // TODO:
																	// remove
																	// hard
																	// coded
																	// string?
					if (value.length() > 20) {
						value = value.substring(0, 19);
					}
					apseInfo.setRequestID(value);
				}
				if (isBIF) {
					if (name.contains(StringConstants.BIF_TICKET_PRESCRIBERCODE)) {
						apseAuthorization.setForskrivarkod(value);
					} else if (name.contains(StringConstants.BIF_TICKET_FIRSTNAME)) {
						apseAuthorization.setFornamn(value);
					} else if (name.contains(StringConstants.BIF_TICKET_LASTNAME)) {
						apseAuthorization.setEfternamn(value);
					} else if (name.contains(StringConstants.BIF_TICKET_POSITIONCODE)) {
						apseAuthorization.setBefattningskod(value);
					}
				} else {
					if (name.contains(StringConstants.LKTJ_TICKET_PRESCRIBERCODE)) {
						apseAuthorization.setForskrivarkod(value);
					} else if (name.contains(StringConstants.LKTJ_TICKET_WORKPLACECODE)) {
						apseAuthorization.setArbetsplatskod(value);
					} else if (name.contains(StringConstants.LKTJ_TICKET_HSAID)) {
						apseAuthorization.setKatalogId(value);
						apseAuthentication.setDirectoryID(value);
					} else if (name.contains(StringConstants.LKTJ_TICKET_PRODUCT)) {
						apseInfo.setSystemNamn(value);
						apseInfo.setSystemVersion(value);
						// personnummer anvnds inte
						// }else if
						// (name.contains(StringConstants.LKTJ_TICKET_SSN)){
					} else if (name.contains(StringConstants.LKTJ_TICKET_SYSTEMIP)) {
						apseInfo.setSystemIP(value);
						// SystemName/Identifier anvnds inte, r
						// biljettserverns namn
						// }else if
						// (name.contains(StringConstants.LKTJ_TICKET_SYSTEMNAME)){
					}
				}
			}
		} catch (UnmarshallingException e) {
			errorString = "Unable to unmarshall xml";
			logger.error(errorString, e);
			throw new Exception(errorString);
		} catch (ClassNotFoundException e) {
			errorString = "XML data is neither Assertion nor Response";
			logger.error(errorString, e);
			throw new Exception(errorString);
		} catch (CertificateException e) {
			errorString = "Error reading certificate information";
			logger.error(errorString, e);
			throw new Exception(errorString);
		} catch (ValidationException e) {
			errorString = "Error validating ticket";
			logger.error(errorString, e);
			throw new Exception(errorString);
		} catch (Exception e) {
			errorString = "Unknown error, please contact administrator";
			logger.error(errorString, e);
			throw new Exception(errorString);
		}
	}

	public void configureAttributes() {
		configureAttributes(this.apseAuthorization);
	}

	public void configureAttributes(ApseAuthorizationAttributes autho) {
		configureAttributes(autho, this.apseAuthentication, this.apseInfo);
	}

	private void configureAttributes(ApseAuthorizationAttributes autho, ApseAuthenticationAttributes authn,
			ApseInfoAttributes info) {
		configureAuthoAttributes(autho.getRollnamn(), autho.getKatalogId(), autho.getKatalog(),
				autho.getForskrivarkod(), autho.getLegitimationskod(), autho.getYrkeskod(), autho.getBefattningskod(),
				autho.getFornamn(), autho.getEfternamn(), autho.getArbetsplatskod(), autho.getArbetsplats(),
				autho.getPostadress(), autho.getPostnummer(), autho.getPostort(), autho.getTelefonnummer(), autho.getPersonnummer(), autho.getOrganisationsnummer());

		configureAuthnAttributes(authn.getDirectoryID(), authn.getOrganisationID());

		configureInfoAttributes(info.getRequestID(), info.getSystemNamn(), info.getSystemVersion(), info.getSystemIP());
	}

	public String getTicket() {
		return getTicket(false);
	}

	public String getTicket(boolean getSecurityOnly) {
		logger.debug("Launching application");
		String message = null;
		if (attributeSet == null) {
			configureAttributes();
		}
		message = getMessageAsString(atg, attributeSet, getSecurityOnly);
		if (defVal.getRemoveInitialXMLString().equalsIgnoreCase("true")
				|| defVal.getRemoveInitialXMLString().equalsIgnoreCase("yes")) {
			message = message.replace(defVal.getXmlStringToRemove(), "");
		}
		return message;
	}

	public String getValidToString() {
		return getValidToString(atg);
	}

	ApseAuthorizationAttributes getApseAuthorizationAttributes() {
		return this.apseAuthorization;
	}

	ApseAuthenticationAttributes getApseAuthenticationAttributes() {
		return this.apseAuthentication;
	}

	// stt default-vrden, om de inte innehller ngot.
	public void setIncomingAuthorizationAttributes(ApseAuthorizationAttributes attribs) {
		if (StringUtils.isNotEmpty(attribs.getArbetsplats())) {
			apseAuthorization.setArbetsplats(attribs.getArbetsplats());
		}
		if (StringUtils.isNotEmpty(attribs.getArbetsplatskod())) {
			apseAuthorization.setArbetsplatskod(attribs.getArbetsplatskod());
		}
		if (StringUtils.isNotEmpty(attribs.getBefattningskod())) {
			apseAuthorization.setBefattningskod(attribs.getBefattningskod());
		}
		if (StringUtils.isNotEmpty(attribs.getEfternamn())) {
			apseAuthorization.setEfternamn(attribs.getEfternamn());
		}
		if (StringUtils.isNotEmpty(attribs.getFornamn())) {
			apseAuthorization.setFornamn(attribs.getFornamn());
		}
		if (StringUtils.isNotEmpty(attribs.getForskrivarkod())) {
			apseAuthorization.setForskrivarkod(attribs.getForskrivarkod());
		}
		if (StringUtils.isNotEmpty(attribs.getKatalog())) {
			apseAuthorization.setKatalog(attribs.getKatalog());
		}
		if (StringUtils.isNotEmpty(attribs.getKatalogId())) {
			apseAuthorization.setKatalogId(attribs.getKatalogId());
		}
		if (StringUtils.isNotEmpty(attribs.getLegitimationskod())) {
			apseAuthorization.setLegitimationskod(attribs.getLegitimationskod());
		}
		if (StringUtils.isNotEmpty(attribs.getPostadress())) {
			apseAuthorization.setPostadress(attribs.getPostadress());
		}
		if (StringUtils.isNotEmpty(attribs.getPostnummer())) {
			apseAuthorization.setPostnummer(attribs.getPostnummer());
		}
		if (StringUtils.isNotEmpty(attribs.getPostort())) {
			apseAuthorization.setPostort(attribs.getPostort());
		}
		if (StringUtils.isNotEmpty(attribs.getRollnamn())) {
			apseAuthorization.setRollnamn(attribs.getRollnamn());
		}
		if (StringUtils.isNotEmpty(attribs.getTelefonnummer())) {
			apseAuthorization.setTelefonnummer(attribs.getTelefonnummer());
		}
		if (StringUtils.isNotEmpty(attribs.getYrkeskod())) {
			apseAuthorization.setYrkeskod(attribs.getYrkeskod());
		}
		if (StringUtils.isNotEmpty(attribs.getPersonnummer())) {
			apseAuthorization.setPersonnummer(attribs.getPersonnummer());
		}
		
		if (StringUtils.isNotEmpty(attribs.getPersonnummer())) {
			apseAuthorization.setOrganisationsnummer(attribs.getOrganisationsnummer());
		}
	}

	public void setIncomingAuthenticationAttributes(ApseAuthenticationAttributes attribs) {
		if (StringUtils.isNotEmpty(attribs.getDirectoryID())) {
			apseAuthentication.setDirectoryID(attribs.getDirectoryID());
		}
		if (StringUtils.isNotEmpty(attribs.getOrganisationID())) {
			apseAuthentication.setOrganisationID(attribs.getOrganisationID());
		}
	}

	public void setIncomingInfoAttributes(ApseInfoAttributes attribs) {
		if (StringUtils.isNotEmpty(attribs.getRequestID())) {
			apseInfo.setRequestID(attribs.getRequestID());
		}
		if (StringUtils.isNotEmpty(attribs.getSystemIP())) {
			apseInfo.setSystemIP(attribs.getSystemIP());
		}
		if (StringUtils.isNotEmpty(attribs.getSystemNamn())) {
			apseInfo.setSystemNamn(attribs.getSystemNamn());
		}
		if (StringUtils.isNotEmpty(attribs.getSystemVersion())) {
			apseInfo.setSystemVersion(attribs.getSystemVersion());
		}
	}

	// konfigurera ApSe-attributen och spara i attribut-settet
	private void configureAuthnAttributes(String dirid, String orgid) {
		List<SAML2Attribute> authnAttributes = new ArrayList<SAML2Attribute>();
		SAML2Attribute dir = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHENTICATION_DIRECTORYID, dirid);
		authnAttributes.add(dir);
		SAML2Attribute org = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHENTICATION_ORGANIZATIONID, orgid);
		authnAttributes.add(org);

		attributeSet.setAuthnAttributes(authnAttributes);
	}

	private void configureInfoAttributes(String reqId, String sysName, String sysVersion, String sysIp) {
		List<SAML2Attribute> infoAttributes = new ArrayList<SAML2Attribute>();
		SAML2Attribute reqid = new SAML2Attribute(StringConstants.ATTRIBUTE_INFO_REQUESTID, reqId);
		infoAttributes.add(reqid);
		SAML2Attribute sysname = new SAML2Attribute(StringConstants.ATTRIBUTE_INFO_SYSTEMNAME, sysName);
		infoAttributes.add(sysname);
		SAML2Attribute sysver = new SAML2Attribute(StringConstants.ATTRIBUTE_INFO_SYSTEMVERSION, sysVersion);
		infoAttributes.add(sysver);
		SAML2Attribute sysip = new SAML2Attribute(StringConstants.ATTRIBUTE_INFO_SYSTEMIP, sysIp);
		infoAttributes.add(sysip);

		attributeSet.setAuditingAttributes(infoAttributes);

	}

	private void configureAuthoAttributes(String rollnamn, String katalogId, String katalog, String forskrivarkod,
			String legitimationskod, String yrkeskod, String befattningskod, String fornamn, String efternamn,
			String arbetsplatskod, String arbetsplats, String postadress, String postnummer, String postort,
			String telefonnummer, String personnummer, String organisationsnummer) {
		List<SAML2Attribute> authoAttributes = new ArrayList<SAML2Attribute>();
		SAML2Attribute roll = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_ROLE, rollnamn);
		authoAttributes.add(roll);
		SAML2Attribute dirid = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_DIRECTORY_ID, katalogId);
		authoAttributes.add(dirid);
		SAML2Attribute dir = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_DIRECTORY, katalog);
		authoAttributes.add(dir);

		if (forskrivarkod != null && forskrivarkod.length() > 0) {
			SAML2Attribute fkod = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_PRESCIBERCODE,
					forskrivarkod);
			authoAttributes.add(fkod);
		}

		SAML2Attribute lkod = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_LEGITIMATIONCODE,
				legitimationskod);
		authoAttributes.add(lkod);
		SAML2Attribute ykod = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKCODE, yrkeskod);
		authoAttributes.add(ykod);
		SAML2Attribute bkod = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_POSITIONCODE, befattningskod);
		authoAttributes.add(bkod);
		SAML2Attribute fnamn = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_GIVENNAME, fornamn);
		authoAttributes.add(fnamn);
		SAML2Attribute enamn = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_FAMILYNAME, efternamn);
		authoAttributes.add(enamn);
		SAML2Attribute arbplatskod = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKPLACECODE,
				arbetsplatskod);
		authoAttributes.add(arbplatskod);
		SAML2Attribute arbplats = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKPLACE, arbetsplats);
		authoAttributes.add(arbplats);
		SAML2Attribute padr = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_ADDRESS, postadress);
		authoAttributes.add(padr);
		SAML2Attribute pnmr = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_ZIPCODE, postnummer);
		authoAttributes.add(pnmr);
		SAML2Attribute port = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_POSTAREA, postort);
		authoAttributes.add(port);
		SAML2Attribute tnmr = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_TELEPHONE, telefonnummer);
		authoAttributes.add(tnmr);
		if (StringUtils.isNotEmpty(personnummer)) {
			SAML2Attribute pnr = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_SSN, personnummer);
			authoAttributes.add(pnr);
		}
		if (StringUtils.isNotEmpty(organisationsnummer)) {
			SAML2Attribute org = new SAML2Attribute(StringConstants.ATTRIBUTE_AUTHORIZATION_ORGANIZATION_ID, organisationsnummer);
			authoAttributes.add(org);
		}

		attributeSet.setAuthorizationAttributes(authoAttributes);
	}
}
