package se.inera.pascal.ticket;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.encoding.Base64;

import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;

// ut�ka implementationen av ServiceLifecycle f�r att p� ett enkelt s�tt
// logga inkommande/klientens IP-nummer
public class LkTjTicket implements ServiceLifecycle {

	private GenerateTicket gt;
	private String requestIP = "";

	public LkTjTicket() {
		gt = new GenerateTicket();
	}


	public void destroy() {
	}

	
	public void init(Object obj) throws ServiceException {
		ServletEndpointContext ctx = (ServletEndpointContext) obj;
		requestIP = (String) ctx.getMessageContext().getProperty("remoteaddr");
	}

	// Inkommande biljett m�ste vara Base64-enkodad f�r att minimera risk med
	// valideringsfel. Det beh�vs ocks� f�r att web-tj�nsten inte skall parsa
	// biljetten.
	public WSReturn getTicket(String b64LkTjTicket, String rollnamn, String katalogId, String katalog,
			String forskrivarkod, String legitimationskod, String yrkeskod, String befattningskod, String fornamn,
			String efternamn, String arbetsplatskod, String arbetsplats, String postadress, String postnummer,
			String postort, String telefonnummer) {
		WSReturn retVal = null;
		gt.setIP(requestIP);
		if (b64LkTjTicket == null) {
			b64LkTjTicket = "";
		}
		String lktjTicket = new String(Base64.decode(b64LkTjTicket));
		if (rollnamn == null) {
			rollnamn = "";
		}
		if (katalogId == null) {
			katalogId = "";
		}
		if (katalog == null) {
			katalog = "";
		}
		if (forskrivarkod == null) {
			forskrivarkod = "";
		}
		if (legitimationskod == null) {
			legitimationskod = "";
		}
		if (befattningskod == null) {
			befattningskod = "";
		}
		if (fornamn == null) {
			fornamn = "";
		}
		if (efternamn == null) {
			efternamn = "";
		}
		if (arbetsplatskod == null) {
			arbetsplatskod = "";
		}
		if (arbetsplats == null) {
			arbetsplats = "";
		}
		if (postadress == null) {
			postadress = "";
		}
		if (postort == null) {
			postort = "";
		}
		if (postnummer == null) {
			postnummer = "";
		}
		if (telefonnummer == null) {
			telefonnummer = "";
		}

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
		authoAttr.setRollnamn(rollnamn);
		authoAttr.setTelefonnummer(telefonnummer);
		authoAttr.setYrkeskod(yrkeskod);

		gt.setIncomingAuthorizationAttributes(authoAttr);
		retVal = gt.getTicket(lktjTicket, false);
		return retVal;
	}
}
