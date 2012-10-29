package se.inera.pascal.ticket.core;

public class ApseInfoAttributes {
	private String requestID="";
	private String systemNamn="";
	private String systemVersion="";
	private String systemIP="";
	
	public ApseInfoAttributes(){
	}
	public ApseInfoAttributes(String requestID,String systemNamn,String systemVersion,String systemIP){
		this.requestID = requestID;
		this.systemNamn = systemNamn;
		this.systemVersion = systemVersion;
		this.systemIP = systemIP;
	}
	
	public void setRequestID(String requestID){
		this.requestID = requestID;
	}
	public String getRequestID(){
		return requestID;
	}
	public void setSystemNamn(String systemNamn){
		this.systemNamn = systemNamn;
	}
	public String getSystemNamn(){
		return systemNamn;
	}
	public void setSystemVersion(String systemVersion){
		this.systemVersion = systemVersion;
	}
	public String getSystemVersion(){
		return systemVersion;
	}
	public void setSystemIP(String systemIP){
		this.systemIP = systemIP;
	}
	public String getSystemIP(){
		return systemIP;
	}
}
