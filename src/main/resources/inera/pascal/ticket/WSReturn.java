package se.inera.pascal.ticket;

public class WSReturn {
	String ticket="";
	String validTo="";
	
	public WSReturn(){		
	}
	
	public WSReturn(String ticket, String validTo) {
		this.ticket = ticket;
		this.validTo = validTo;
	}

	public String getTicket(){
		return ticket;
	}
	public String getValidTo(){
		return validTo;
	}
}
