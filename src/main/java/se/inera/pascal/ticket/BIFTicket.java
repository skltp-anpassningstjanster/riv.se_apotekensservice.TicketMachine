package se.inera.pascal.ticket;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;

import java.util.Base64;

public class BIFTicket implements ServiceLifecycle {

	private GenerateTicket gt;
	private String requestIP = "";

	public BIFTicket() {
		gt = new GenerateTicket();
	}

	
	public void destroy() {
	}

	
	public void init(Object obj) throws ServiceException {
		ServletEndpointContext ctx = (ServletEndpointContext) obj;
		requestIP = (String) ctx.getMessageContext().getProperty("remoteaddr");
	}

	public WSReturn getTicket(String b64BIFTicket, String roll, String katalogId, String katalog,
			String forskrivarkod, String legitimationskod, String yrkeskod, String befattningskod, String fornamn,
			String efternamn, String arbetsplatskod, String arbetsplats, String postadress, String postnummer,
			String postort, String telefonnummer) {
		WSReturn retVal = null;
		gt.setIP(requestIP);
		if (b64BIFTicket == null) {
			b64BIFTicket = "";
		}

		String bifTicket = new String(Base64.getDecoder().decode(b64BIFTicket));

		if (roll == null) {
			roll = "";
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
		authoAttr.setArbetsplats(null);
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

		gt.setIncomingAuthorizationAttributes(authoAttr);
		retVal = gt.getTicket(bifTicket, true);
		return retVal;
	}
}
