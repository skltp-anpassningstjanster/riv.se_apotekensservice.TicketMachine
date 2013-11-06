package se.inera.pascal.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.impl.SAML2AssertionTicketGeneratorLauncher;

public class ArgosTicket {

	private static Logger logger = LoggerFactory.getLogger(ArgosTicket.class);

	private SAML2AssertionTicketGeneratorLauncher launcher = null;
	private String launcherErrorString = "";

	public ArgosTicket() {
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}

	public String getTicket(String forskrivarkod, String legitimationskod, String fornamn, String efternamn,
			String Yrkesgrupp, String befattningskod, String arbetsplatskod, String arbetsplatsnamn, String postort,
			String postadress, String postnummer, String telefonnummer, String requestId, String rollnamn,
			String hsaID, String katalog, String organisationsnummer, String systemnamn, String systemversion,
			String systemIp) {
		String retval = "";
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
		} else {
			retval = launcherErrorString;
		}
		return retval;
	}

	public String getTicket(String fornamn, String efternamn,
			String personnummer, String rollnamn, String organisationsnummer,
			String systemnamn, String systemversion) {
		String retval = "";
		if (launcher != null) {
			ApseAuthorizationAttributes authoAttr = new ApseAuthorizationAttributes();
			authoAttr.setEfternamn(efternamn);
			authoAttr.setFornamn(fornamn);
			authoAttr.setRollnamn(rollnamn);
			authoAttr.setPersonnummer(personnummer);
			authoAttr.setOrganisationsnummer(organisationsnummer);
			
			ApseAuthenticationAttributes authnAttr = new ApseAuthenticationAttributes();
			authnAttr.setDirectoryID(personnummer);
			authnAttr.setOrganisationID(organisationsnummer);

			ApseInfoAttributes infoAttr = new ApseInfoAttributes();
			infoAttr.setSystemNamn(systemnamn);
			infoAttr.setSystemVersion(systemversion);

			launcher.setIncomingAuthorizationAttributes(authoAttr);
			launcher.setIncomingAuthenticationAttributes(authnAttr);
			launcher.setIncomingInfoAttributes(infoAttr);

			launcher.configureAttributes();

			retval = launcher.getTicket(true);
		} else {
			retval = launcherErrorString;
		}
		return retval;
	}
}
