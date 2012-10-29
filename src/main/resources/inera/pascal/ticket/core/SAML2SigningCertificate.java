package se.inera.pascal.ticket.core;


public class SAML2SigningCertificate {	
	
	private String keystorePath="";
	private String keystoreFilename="";
	private String keystorePassword="";
	private String keystoreAlias="";
	private String keystoreAliasPassword="";
	private String keystoreType="";
	private String credentialType="";	
	
	public String getKeystorePath() {
		return keystorePath;
	}
	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}
	public String getKeystoreFilename() {
		return keystoreFilename;
	}
	public void setKeystoreFilename(String keystoreFilename) {
		this.keystoreFilename = keystoreFilename;
	}
	public String getKeystorePassword() {
		return keystorePassword;
	}
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}
	public String getKeystoreAlias() {
		return keystoreAlias;
	}
	public void setKeystoreAlias(String keystoreAlias) {
		this.keystoreAlias = keystoreAlias;
	}
	public String getKeystoreAliasPassword() {
		return keystoreAliasPassword;
	}
	public void setKeystoreAliasPassword(String keystoreAliasPassword) {
		this.keystoreAliasPassword = keystoreAliasPassword;
	}
	public String getKeystoreType() {
		return keystoreType;
	}
	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}
	public String getCredentialType() {
		return credentialType;
	}
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
}
