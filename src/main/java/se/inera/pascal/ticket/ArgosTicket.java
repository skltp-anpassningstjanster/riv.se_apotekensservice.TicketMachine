package se.inera.pascal.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.impl.SAML2AssertionTicketGeneratorLauncher;

public class ArgosTicket {

	private static Logger logger = LoggerFactory.getLogger(ArgosTicket.class);

	/**
	 * Use this when creating a ticket for an organization.
	 * 
	 * @param forskrivarkod
	 * @param legitimationskod
	 * @param fornamn
	 * @param efternamn
	 * @param Yrkesgrupp
	 * @param befattningskod
	 * @param arbetsplatskod
	 * @param arbetsplatsnamn
	 * @param postort
	 * @param postadress
	 * @param postnummer
	 * @param telefonnummer
	 * @param requestId
	 * @param rollnamn
	 * @param hsaID
	 * @param katalog
	 * @param organisationsnummer
	 * @param systemnamn
	 * @param systemversion
	 * @param systemIp
	 * @return
	 */
	public String getTicketForOrganization(String forskrivarkod, String legitimationskod, String fornamn, String efternamn,
			String Yrkesgrupp, String befattningskod, String arbetsplatskod, String arbetsplatsnamn, String postort,
			String postadress, String postnummer, String telefonnummer, String requestId, String rollnamn,
			String hsaID, String katalog, String organisationsnummer, String systemnamn, String systemversion,
			String systemIp) {
		String retval = "";
		SAML2AssertionTicketGeneratorLauncher launcher;
		String launcherErrorString = "";
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
		if (launcher != null) {
			ApseAuthorizationAttributes authoAttr = new ApseAuthorizationAttributes();
			authoAttr.setArbetsplats(arbetsplatsnamn);
			authoAttr.setArbetsplatskod(arbetsplatskod);
			authoAttr.setBefattningskod(befattningskod);
			authoAttr.setEfternamn(efternamn);
			authoAttr.setFornamn(fornamn);
			authoAttr.setForskrivarkod(forskrivarkod);
			authoAttr.setKatalog(katalog);
			authoAttr.setKatalogId(hsaID);
			authoAttr.setLegitimationskod(legitimationskod);
			authoAttr.setPostadress(postadress);
			authoAttr.setPostnummer(postnummer);
			authoAttr.setPostort(postort);
			authoAttr.setRollnamn(rollnamn);
			authoAttr.setTelefonnummer(telefonnummer);
			authoAttr.setYrkeskod(Yrkesgrupp);

			ApseAuthenticationAttributes authnAttr = new ApseAuthenticationAttributes();
			authnAttr.setDirectoryID(hsaID);
			authnAttr.setOrganisationID(organisationsnummer);

			ApseInfoAttributes infoAttr = new ApseInfoAttributes();
			infoAttr.setRequestID(requestId);
			infoAttr.setSystemIP(systemIp);
			infoAttr.setSystemNamn(systemnamn);
			infoAttr.setSystemVersion(systemversion);

			launcher.setIncomingAuthorizationAttributes(authoAttr);
			launcher.setIncomingAuthenticationAttributes(authnAttr);
			launcher.setIncomingInfoAttributes(infoAttr);

			launcher.configureAttributes();

			retval = launcher.getTicket(true);
			logger.debug(retval);
		} else {
			retval = launcherErrorString;
		}
		return retval;
	}

	/**
	 * Use this when creating a ticket a citizen.
	 * 
	 * @param fornamn
	 * @param efternamn
	 * @param personnummer
	 * @param rollnamn
	 * @param organisationsnummer
	 * @param requestId
	 * @param systemIp
	 * @param systemnamn
	 * @param systemversion
	 * @return
	 */
	public String getTicketForCitizen(String fornamn, String efternamn,
			String personnummer, String rollnamn, String organisationsnummer,
			String requestId, String systemIp, String systemnamn, String systemversion) {
		String retval = "";
		SAML2AssertionTicketGeneratorLauncher launcher;
		String launcherErrorString = "";
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
		if (launcher != null) {
			ApseAuthorizationAttributes authoAttr = new ApseAuthorizationAttributes();
			authoAttr.setEfternamn(efternamn);
			authoAttr.setFornamn(fornamn);
			
			/*
			 * Obs! Roll är det attribut i säkerhetsheadern som ska användas enligt dokumentet
			 * Säkerhetsheader v1.0 från Apotekens service. Vi behåller dock det parallella spåret
			 * med rollnamn för organisationsbiljetter för att befintlig kod i produktion ej ska drabbas.
			 * Se https://skl-tp.atlassian.net/browse/SKLTP-346
			 */
			authoAttr.setRoll(rollnamn); 
			authoAttr.setPersonnummer(personnummer);
			authoAttr.setOrganisationsnummer(organisationsnummer);
			
			ApseAuthenticationAttributes authnAttr = new ApseAuthenticationAttributes();
			authnAttr.setDirectoryID(personnummer);
			authnAttr.setOrganisationID(organisationsnummer);

			ApseInfoAttributes infoAttr = new ApseInfoAttributes();
			infoAttr.setSystemNamn(systemnamn);
			infoAttr.setSystemVersion(systemversion);
			infoAttr.setSystemIP(systemIp);
			infoAttr.setRequestID(requestId);

			launcher.setIncomingAuthorizationAttributesForCitizen(authoAttr);
			launcher.setIncomingAuthenticationAttributesForCitizen(authnAttr);
			launcher.setIncomingInfoAttributesForCitizen(infoAttr);

			launcher.configureAttributesForCitizen();

			retval = launcher.getTicket(true);
			logger.debug(retval);
		} else {
			retval = launcherErrorString;
		}
		return retval;
	}
}
