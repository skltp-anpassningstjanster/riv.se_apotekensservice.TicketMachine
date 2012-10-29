package se.inera.pascal.ticket;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import org.apache.axis.encoding.Base64;

// utöka implementationen av ServiceLifecycle för att på ett enkelt sätt
// logga inkommande/klientens IP-nummer
public class LkTjTicket implements ServiceLifecycle{
	
	private GenerateTicket gt;
	private String requestIP = "";
	
	public LkTjTicket(){
		gt = new GenerateTicket();
	}
	@Override
	public void destroy() {
	}

	@Override
	public void init(Object obj) throws ServiceException {
		ServletEndpointContext ctx = (ServletEndpointContext) obj;
		requestIP = (String) ctx.getMessageContext().getProperty("remoteaddr");
	}

	// Inkommande biljett måste vara Base64-enkodad för att minimera risk med
	// valideringsfel. Det behövs också för att web-tjänsten inte skall parsa
	// biljetten.
	public WSReturn getTicket(String b64LkTjTicket,
			String rollnamn,
			String katalogId,
			String katalog,
			String forskrivarkod, 
			String legitimationskod, 
			String yrkeskod, 
			String befattningskod, 
			String fornamn,
			String efternamn,
			String arbetsplatskod,
			String arbetsplats,
			String postadress,
			String postnummer,
			String postort,
			String telefonnummer)
{
		WSReturn retVal = null;
		gt.setIP(requestIP);
		if ( b64LkTjTicket == null ){ b64LkTjTicket="";}
		String lktjTicket = new String(Base64.decode(b64LkTjTicket));
		if ( rollnamn == null ){ rollnamn="";}
		if ( katalogId == null ){ katalogId="";}
		if ( katalog == null ){ katalog="";}
		if ( forskrivarkod == null ){ forskrivarkod="";}
		if ( legitimationskod == null ){ legitimationskod="";}
		if ( befattningskod == null ){ befattningskod="";}
		if ( fornamn == null ){ fornamn="";}
		if ( efternamn == null ){ efternamn="";}
		if ( arbetsplatskod == null ){ arbetsplatskod="";}
		if ( arbetsplats == null ){ arbetsplats="";}
		if ( postadress == null ){ postadress="";}
		if ( postort == null ){ postort="";}
		if ( postnummer == null ){ postnummer="";}
		if ( telefonnummer == null ){ telefonnummer="";}
		ApseAuthorizationAttributes authoAttr = 
			new ApseAuthorizationAttributes(rollnamn, katalogId, katalog, forskrivarkod, legitimationskod,
											yrkeskod, befattningskod, fornamn, efternamn, arbetsplatskod, 
											arbetsplats, postadress, postnummer, postort, telefonnummer);
		gt.setIncomingAuthorizationAttributes(authoAttr);
		retVal =  gt.getTicket(lktjTicket, false);
		return retVal;
	}
}
