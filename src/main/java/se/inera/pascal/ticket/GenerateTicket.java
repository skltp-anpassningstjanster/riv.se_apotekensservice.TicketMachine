package se.inera.pascal.ticket;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.impl.SAML2AssertionTicketGeneratorLauncher;

//utöka implementationen av ServiceLifecycle för att pp ett enkelt sätt
//logga inkommande/klientens IP-nummer
public class GenerateTicket implements ServiceLifecycle {

	private static Logger logger = LoggerFactory.getLogger(GenerateTicket.class);

	private SAML2AssertionTicketGeneratorLauncher launcher = null;
	private String launcherErrorString = "";
	private String requestIP = "";

	public GenerateTicket() {
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}

	
	public void destroy() {
	}


	public void init(Object obj) throws ServiceException {
		ServletEndpointContext ctx = (ServletEndpointContext) obj;
		requestIP = (String) ctx.getMessageContext().getProperty("remoteaddr");
	}

	// anvnds bara för BIF/LkTj-ticket för att sätta klientens IP
	protected void setIP(String ip) {
		requestIP = ip;
		// TODO: anvnda som infoAttribut?
	}

	// TODO: den skall tas bort som exponerad webservice före release
	// denna metod/webservice anvnds enbart i utvecklings- och testsyfte,
	public WSReturn getTicket(String roll, String katalogId, String katalog, String forskrivarkod,
			String legitimationskod, String yrkeskod, String befattningskod, String fornamn, String efternamn,
			String arbetsplatskod, String arbetsplats, String postadress, String postnummer, String postort,
			String telefonnummer) {
		WSReturn retVal = null;
		String logMess = "Incoming IP: " + requestIP;
		logger.info(logMess);

		// fil anvnds enbart i testsyfte
		if (roll.startsWith("file:")) {
			String file = roll.substring(5);
			analyzeXML(file, false, true);
		} else {
			ApseAuthorizationAttributes authoAttr = new ApseAuthorizationAttributes();
			authoAttr.setArbetsplats(arbetsplats);
			authoAttr.setArbetsplatskod(arbetsplatskod);
			authoAttr.setBefattningskod(befattningskod);
			authoAttr.setEfternamn(efternamn);
			authoAttr.setFornamn(fornamn);
			authoAttr.setForskrivarkod(forskrivarkod);
			authoAttr.setKatalog(katalog);
			authoAttr.setKatalogId(katalogId);
			authoAttr.setLegitimationskod(legitimationskod);
			authoAttr.setPostadress(postadress);
			authoAttr.setPostnummer(postnummer);
			authoAttr.setPostort(postort);
			authoAttr.setRoll(roll);
			authoAttr.setTelefonnummer(telefonnummer);
			authoAttr.setYrkeskod(yrkeskod);

			setIncomingAuthorizationAttributes(authoAttr);
		}
		if (launcher != null) {
			launcher.configureAttributes();
		}
		retVal = getReturnValue();
		return retVal;
	}

	// anropas av LkTjTicket/BIFTicket
	protected WSReturn getTicket(String incomingTicket, boolean isBIF) {
		String logMess = "Incoming IP: " + requestIP;
		logger.info(logMess);
		logger.info(incomingTicket);
		analyzeXML(incomingTicket, isBIF);
		if (launcher != null) {
			launcher.configureAttributes();
		}
		return getReturnValue();
	}

	// anropas av LkTjTicket/BIFTicket
	// sätter authorization-attributens default-vrden. ndras eventuell av
	// vrdena i biljetten
	protected void setIncomingAuthorizationAttributes(ApseAuthorizationAttributes attrs) {
		if (launcher != null) {
			launcher.setIncomingAuthorizationAttributes(attrs);
		}
	}

	protected void setIncomingAuthenticationAttributes(ApseAuthenticationAttributes attrs) {
		if (launcher != null) {
			launcher.setIncomingAuthenticationAttributes(attrs);
		}
	}

	protected void setIncomingInfoAttributes(ApseInfoAttributes attrs) {
		if (launcher != null) {
			launcher.setIncomingInfoAttributes(attrs);
		}
	}

	// analysera inkommande xml-strng
	private void analyzeXML(String xml, boolean isBIF) {
		analyzeXML(xml, isBIF, false);
	}

	private void analyzeXML(String xml, boolean isBIF, boolean isFile) {
		try {
			if (launcher != null) {
				launcher.analyzeXML(xml, isBIF, isFile);
			}
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}

	// Hmtar datat frn launcher-objektet och skapar retur-vrdet
	private WSReturn getReturnValue() {
		String ticket;
		String validTo;
		if (launcher != null) {
			ticket = launcher.getTicket(true);
			validTo = launcher.getValidToString();
		} else {
			ticket = launcherErrorString;
			validTo = "";
		}
		return new WSReturn(ticket, validTo);
	}
}
