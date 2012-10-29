package se.inera.pascal.ticket.core;

public class DefaultValues {
	
	private int ticketValidity;
	private ApseAuthenticationAttributes apseAuthenticationAttributes;
	private ApseAuthorizationAttributes apseAuthorizationAttributes;
	private ApseInfoAttributes apseInfoAttributes;
//	private String directoryID;
//	private String organisationID;
//	private String systemNamn;
//	private String systemVersion;
//	private String systemIP;
//	private String requestID;
	private String certificateSubjectName;
	private String certificateSubject;
	private String certificateSerial;
	private String buildingSubjectConfirmation;
	private String removeInitialXMLString;
	private String xmlStringToRemove;
	private String validateBIF;
	private String validateLkTj;
	
	public void setTicketValidity(int ticketValidity){
		this.ticketValidity = ticketValidity;
	}
	public int getTicketValidity(){
		return ticketValidity;
	}
	public void setApseAuthenticationAttributes(ApseAuthenticationAttributes apseAuthenticationAttributes){
		this.apseAuthenticationAttributes = apseAuthenticationAttributes;
	}
	public ApseAuthenticationAttributes getApseAuthenticationAttributes(){
		return apseAuthenticationAttributes;
	}
	public void setApseAuthorizationAttributes(ApseAuthorizationAttributes apseAuthorizationAttributes){
		this.apseAuthorizationAttributes = apseAuthorizationAttributes;
	}
	public ApseAuthorizationAttributes getApseAuthorizationAttributes(){
		return apseAuthorizationAttributes;
	}
	public void setApseInfoAttributes(ApseInfoAttributes apseInfoAttributes){
		this.apseInfoAttributes = apseInfoAttributes;
	}
	public ApseInfoAttributes getApseInfoAttributes(){
		return apseInfoAttributes;
	}
//	public void setDirectoryID(String directoryID){
//		this.directoryID = directoryID;
//	}
//	public String getDirectoryID(){
//		return directoryID;
//	}
//	public void setOrganisationID(String organisationID){
//		this.organisationID = organisationID;
//	}
//	public String getOrganisationID(){
//		return organisationID;
//	}
//	public void setSystemNamn(String systemNamn){
//		this.systemNamn = systemNamn;
//	}
//	public String getSystemNamn(){
//		return systemNamn;
//	}
//	public void setSystemVersion(String systemVersion){
//		this.systemVersion = systemVersion;
//	}
//	public String getSystemVersion(){
//		return systemVersion;
//	}
//	public void setSystemIP(String systemIP){
//		this.systemIP = systemIP;
//	}
//	public String getSystemIP(){
//		return systemIP;
//	}
//	public void setRequestID(String requestID){
//		this.requestID = requestID;
//	}
//	public String getRequestID(){
//		return requestID;
//	}
	public void setCertificateSubjectName(String certificateSubjectName){
		this.certificateSubjectName = certificateSubjectName;
	}
	public String getCertificateSubjectName(){
		return certificateSubjectName;
	}
	public void setCertificateSubject(String certificateSubject){
		this.certificateSubject = certificateSubject;
	}
	public String getCertificateSubject(){
		return certificateSubject;
	}
	public void setCertificateSerial(String certificateSerial){
		this.certificateSerial = certificateSerial;
	}
	public String getCertificateSerial(){
		return certificateSerial;
	}
	public void setBuildingSubjectConfirmation(String buildingSubjectConfirmation){
		this.buildingSubjectConfirmation = buildingSubjectConfirmation;
	}
	public String getBuildingSubjectConfirmation(){
		return buildingSubjectConfirmation;
	}
	public void setRemoveInitialXMLString(String removeInitialXMLString){
		this.removeInitialXMLString = removeInitialXMLString;
	}
	public String getRemoveInitialXMLString(){
		return removeInitialXMLString;
	}
	public void setXmlStringToRemove(String xmlStringToRemove){
		this.xmlStringToRemove = xmlStringToRemove;
	}
	public String getXmlStringToRemove(){
		return xmlStringToRemove;
	}
	public void setValidateBIF(String validateBIF){
		this.validateBIF = validateBIF;
	}
	public String getValidateBIF(){
		return validateBIF;
	}
	public void setValidateLkTj(String validateLkTj){
		this.validateLkTj = validateLkTj;
	}
	public String getValidateLkTj(){
		return validateLkTj;
	}
}
