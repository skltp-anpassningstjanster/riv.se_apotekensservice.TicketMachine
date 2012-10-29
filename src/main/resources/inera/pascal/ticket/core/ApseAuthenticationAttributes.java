package se.inera.pascal.ticket.core;

public class ApseAuthenticationAttributes {
	private String directoryID="";
	private String organisationID="";

	public ApseAuthenticationAttributes(){
	}
	public ApseAuthenticationAttributes(String directoryID,String organisationID){
		this.directoryID = directoryID;
		this.organisationID = organisationID;
	}

	public void setDirectoryID(String directoryID){
		this.directoryID = directoryID;
	}
	public String getDirectoryID(){
		return directoryID;
	}
	public void setOrganisationID(String organisationID){
		this.organisationID = organisationID;
	}
	public String getOrganisationID(){
		return organisationID;
	}
}
