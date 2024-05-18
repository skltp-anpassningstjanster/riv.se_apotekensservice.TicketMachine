package se.inera.pascal.ticket.core;

public interface StringConstants {
	
	public final static String SAML2_ATTRIBUTE_PREFIX = "urn:apotekensservice:names:federation:attributeName:";
	public final static String AUTHN_CONTEXT_CLASS_REFERENCE = "urn:oasis:names:tc:SAML:2.0:ac:classes:SmartcardPKI";
	public final static String ATTRIBUTE_FORMAT = "urn:oasis:names:tc:SAML:2.0:attrname-format:uri";
	public final static String DEFAULT_ISSUER = "Pascal Test";
	public final static String STATUS_CODE = "urn:oasis:names:tc:SAML:2.0:status:Success";
	public final static String NAME_ID_X509 = "urn:oasis:names:tc:SAML:2.0:nameid-format:X509SubjectName";
	public final static String KEYNAME_VALUE = "Pascal Key";
	public final static String XML_OMIT_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
													
	public final static String SOAP_REPLACE_FROM = "SOAP-ENV";
	public final static String SOAP_REPLACE_TO = "soapenv";

	public final static String ATTRIBUTE_CONNECTED_ASSERTION_ID = "connectedAssertionId";
	public final static String ATTRIBUTE_ASSERTION_TYPE = "assertionType";

	public final static String ATTRIBUTE_AUTHENTICATION_DIRECTORYID = "DirectoryID";
	public final static String ATTRIBUTE_AUTHENTICATION_ORGANIZATIONID = "OrganizationID";

	public final static String ATTRIBUTE_AUTHORIZATION_DATA = "AuthorizationData";
	public final static String ATTRIBUTE_AUTHORIZATION_DIRECTORY_ID = "katalogId";
	public final static String ATTRIBUTE_AUTHORIZATION_DIRECTORY = "katalog";
	public final static String ATTRIBUTE_AUTHORIZATION_PRESCIBERCODE = "forskrivarkod"; 
	public final static String ATTRIBUTE_AUTHORIZATION_LEGITIMATIONCODE = "legitimationskod"; 
	public final static String ATTRIBUTE_AUTHORIZATION_WORKCODE = "yrkeskod"; 
	public final static String ATTRIBUTE_AUTHORIZATION_POSITIONCODE = "befattningskod"; 
	public final static String ATTRIBUTE_AUTHORIZATION_GIVENNAME = "fornamn";
	public final static String ATTRIBUTE_AUTHORIZATION_FAMILYNAME = "efternamn";
	public final static String ATTRIBUTE_AUTHORIZATION_WORKPLACECODE = "arbetsplatskod";
	public final static String ATTRIBUTE_AUTHORIZATION_WORKPLACE = "arbetsplats";
	public final static String ATTRIBUTE_AUTHORIZATION_ADDRESS = "postadress";
	public final static String ATTRIBUTE_AUTHORIZATION_ZIPCODE = "postnummer";
	public final static String ATTRIBUTE_AUTHORIZATION_POSTAREA = "postort";
	public final static String ATTRIBUTE_AUTHORIZATION_TELEPHONE = "telefonnummer";
	public final static String ATTRIBUTE_AUTHORIZATION_SSN = "personnummer";
	public static final String ATTRIBUTE_AUTHORIZATION_ORGANIZATION_ID = "organisationsnummer";
	public static final String ATTRIBUTE_AUTHORIZATION_ROLE = "roll"; //se https://skl-tp.atlassian.net/browse/SKLTP-346

	public final static String ATTRIBUTE_INFO_DATA = "InfoData";
	public final static String ATTRIBUTE_INFO_REQUESTID = "requestId";
	public final static String ATTRIBUTE_INFO_SYSTEMNAME = "systemnamn";
	public final static String ATTRIBUTE_INFO_SYSTEMVERSION = "systemversion";
	public final static String ATTRIBUTE_INFO_SYSTEMIP = "systemIp";

	public final static String LKTJ_TICKET_PRESCRIBERCODE = "fskcode";
	public final static String LKTJ_TICKET_WORKPLACECODE = "workplacecode";
	public final static String LKTJ_TICKET_PRODUCT = "product";
	public final static String LKTJ_TICKET_SSN = "urn:carelink:bif:1.0:types:pId";
	public final static String LKTJ_TICKET_HSAID = "urn:oid:1.2.752.29.4.19";
	public final static String LKTJ_TICKET_SYSTEMIP = "systemlocality";
	public final static String LKTJ_TICKET_SYSTEMNAME = "systemidentifier";
	
	public final static String BIF_TICKET_PRESCRIBERCODE = "förskrivarkod";
	public final static String BIF_TICKET_FIRSTNAME = "förnamn";
	public final static String BIF_TICKET_LASTNAME = "efternamn";
	public final static String BIF_TICKET_POSITIONCODE = "befattningskod";
	
}
