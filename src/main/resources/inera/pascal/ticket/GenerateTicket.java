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

//utška implementationen av ServiceLifecycle för att på ett enkelt sätt
//logga inkommande/klientens IP-nummer
public class GenerateTicket implements ServiceLifecycle{
	
	private static Logger logger = LoggerFactory.getLogger(GenerateTicket.class);
	
	private SAML2AssertionTicketGeneratorLauncher launcher = null;
	private String launcherErrorString = "";
	private String requestIP = "";

	public GenerateTicket(){
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}
	@Override
	public void destroy() {
	}
	@Override
	public void init(Object obj) throws ServiceException {
		ServletEndpointContext ctx = (ServletEndpointContext) obj;
		requestIP = (String) ctx.getMessageContext().getProperty("remoteaddr");
	}
	//anvŠnds bara för BIF/LkTj-ticket för att sätta klientens IP
	protected void setIP(String ip){
		requestIP = ip;
		//TODO: anvŠnda som infoAttribut?
	}
	//TODO: den skall tas bort som exponerad webservice före release 
	//denna metod/webservice anvŠnds enbart i utvecklings- och testsyfte,
	public WSReturn getTicket(String rollnamn,
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
		WSReturn retVal=null;
		String logMess = "Incoming IP: " + requestIP;
		logger.info(logMess);
		
		//fil anvŠnds enbart i testsyfte
		if( rollnamn.startsWith("file:") ){
			String file = rollnamn.substring(5);
			analyzeXML(file, false, true);
		}else{
			ApseAuthorizationAttributes authoAttr = 
				new ApseAuthorizationAttributes(rollnamn, katalogId, katalog, forskrivarkod, legitimationskod,
												yrkeskod, befattningskod, fornamn, efternamn, arbetsplatskod, 
												arbetsplats, postadress, postnummer, postort, telefonnummer);
			setIncomingAuthorizationAttributes(authoAttr);
		}
		if ( launcher != null){
			launcher.configureAttributes();
		}
		retVal = getReturnValue();
		return retVal;
	}
	
	//anropas av LkTjTicket/BIFTicket 
	protected WSReturn getTicket(String incomingTicket, boolean isBIF){
		String logMess = "Incoming IP: " + requestIP;
		logger.info(logMess);
		logger.info(incomingTicket);
		analyzeXML(incomingTicket, isBIF);
		if ( launcher != null){
			launcher.configureAttributes();
		}
		return getReturnValue();
	}
	
	//anropas av LkTjTicket/BIFTicket
	//sätter authorization-attributens default-vŠrden. €ndras eventuell av vŠrdena i biljetten
	protected void setIncomingAuthorizationAttributes(ApseAuthorizationAttributes attrs){
		if ( launcher != null){
			launcher.setIncomingAuthorizationAttributes(attrs);
		}
	}
	protected void setIncomingAuthenticationAttributes(ApseAuthenticationAttributes attrs){
		if ( launcher != null){
			launcher.setIncomingAuthenticationAttributes(attrs);
		}
	}
	protected void setIncomingInfoAttributes(ApseInfoAttributes attrs){
		if ( launcher != null){
			launcher.setIncomingInfoAttributes(attrs);
		}
	}
	//analysera inkommande xml-strŠng
	private void analyzeXML(String xml, boolean isBIF){
		analyzeXML(xml,isBIF,false);
	}
	private void analyzeXML(String xml, boolean isBIF,boolean isFile){
		try {
			if ( launcher != null){
				launcher.analyzeXML(xml,isBIF,isFile);
			}
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
		}
	}
	//HŠmtar datat frŒn launcher-objektet och skapar retur-vŠrdet
	private WSReturn getReturnValue(){
		String ticket;
		String validTo;
		if ( launcher != null){
			ticket = launcher.getTicket(true);
			validTo = launcher.getValidToString();
		}else{
			ticket = launcherErrorString;
			validTo = "";
		}
		return new WSReturn(ticket,validTo);
	}
}
