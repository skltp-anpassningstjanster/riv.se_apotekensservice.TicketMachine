package se.inera.pascal.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.impl.SAML2AssertionTicketGeneratorLauncher;

public class ArgosTicket{
	
	private static Logger logger = LoggerFactory.getLogger(GenerateTicket.class);
	
	private SAML2AssertionTicketGeneratorLauncher launcher = null;
	private String launcherErrorString = "";

	public ArgosTicket(){
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}

	public String getTicket(String forskrivarkod, 
							String legitimationskod, 
							String fornamn,
							String efternamn,
							String Yrkesgrupp, 
							String befattningskod, 
							String arbetsplatskod,
							String arbetsplatsnamn,
							String postort,
							String postadress,
							String postnummer,
							String telefonnummer,
							String requestId,
							String rollnamn,
							String hsaID,
							String katalog,
							String organisationsnummer, 
							String systemnamn,
							String systemversion, 
							String systemIp)
	{
		String retval="";
		if ( launcher != null){
			ApseAuthorizationAttributes authoAttr = 
				new ApseAuthorizationAttributes(rollnamn, hsaID, katalog, forskrivarkod, legitimationskod,
												Yrkesgrupp, befattningskod, fornamn, efternamn, arbetsplatskod, 
												arbetsplatsnamn, postadress, postnummer, postort, telefonnummer);
			
			ApseAuthenticationAttributes authnAttr = new ApseAuthenticationAttributes(hsaID,organisationsnummer);
			
			ApseInfoAttributes infoAttr = new ApseInfoAttributes(requestId,systemnamn,systemversion,systemIp);

			launcher.setIncomingAuthorizationAttributes(authoAttr);
			launcher.setIncomingAuthenticationAttributes(authnAttr);
			launcher.setIncomingInfoAttributes(infoAttr);
			
			launcher.configureAttributes();

			retval = launcher.getTicket(true);
		}else{
			retval = launcherErrorString;
		}
		return retval;
	}
}
