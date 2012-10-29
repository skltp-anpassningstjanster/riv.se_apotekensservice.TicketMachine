package se.inera.pascal.ticket.core;

public class ApseInfoAttributes {
	private String requestID = "";
	private String systemNamn = "";
	private String systemVersion = "";
	private String systemIP = "";

	public ApseInfoAttributes() {
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setSystemNamn(String systemNamn) {
		this.systemNamn = systemNamn;
	}

	public String getSystemNamn() {
		return systemNamn;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemIP(String systemIP) {
		this.systemIP = systemIP;
	}

	public String getSystemIP() {
		return systemIP;
	}
}
