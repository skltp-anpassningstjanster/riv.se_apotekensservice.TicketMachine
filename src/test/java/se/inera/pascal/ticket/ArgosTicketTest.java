package se.inera.pascal.ticket;

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsAnything.*;

import org.hamcrest.core.IsNot;
import org.junit.Test;

public class ArgosTicketTest {
	private static String samlAttributeTemplate = "<saml2:Attribute Name=\"%s\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">%s</saml2:AttributeValue></saml2:Attribute>";
	
	@Test
	public void happyCaseToSeeHowTicketGeneratorWorks() {

		String forskrivarkod = "1111152";
		String legitimationskod = "1111111";
		String fornamn = "Lars";
		String efternamn = "Lkare";
		String yrkesgrupp = "FORSKRIVARE";
		String befattningskod = "1111111";
		String arbetsplatskod = "4000000000001";
		String arbetsplatsnamn = "VC Test";
		String postort = "Staden";
		String postadress = "Vgen 1";
		String postnummer = "11111";
		String telefonnummer = "0987654321";
		String requestId = "12345676";
		String rollnamn = "LK";
		// String directoryID = "SE1111111111-1003";
		String hsaID = "SE1111111111-1003";
		String katalog = "HSA";
		String organisationsnummer = "111111111";
		String systemnamn = "Pascal";
		String systemversion = "1.0";
		String systemIp = "192.168.1.1";

		String ticket = new ArgosTicket().getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));

	}

	@Test
	public void ineraCodeDoesNotGiveErrorWhenOneFieldIsNull() {

		String forskrivarkod = "1111129";
		String legitimationskod = null;
		String fornamn = "Lars";
		String efternamn = "Lkare";
		String yrkesgrupp = "Lkare";
		String befattningskod = "123456";
		String arbetsplatskod = "1234567890";
		String arbetsplatsnamn = "Sjukhuset";
		String postort = "Staden";
		String postadress = "Vgen 1";
		String postnummer = "11111";
		String telefonnummer = "08-1234567";
		String requestId = "123456";
		String rollnamn = "FORSKRIVARE";
		String directoryID = "TSE6565656565-1003";
		String hsaID = "TSE6565656565-1003";
		String katalog = "HSA";
		String organisationsnummer = "1234567890";
		String systemnamn = "Melior";
		String systemversion = "1.0";
		String systemIp = "192.0.0.1";

		String ticket = new ArgosTicket().getTicket(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
				befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
				requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

		assertNotNull(ticket);

	}
	
	@Test
	public void a_ticket_for_usage_in_individual_based_services_can_be_produced() {
		//given
		String fornamn = "Lars";
		String efternamn = "Larsson";
		String rollnamn = "PRIVATPERSON";
		String personnummer = "197109231234";
		String organisationsnummer = "MVK_111111111";
		String systemnamn = "Mina vårdkontakter";
		String systemversion = "1.0";

		String ticket = new ArgosTicket().getTicket(fornamn, efternamn, personnummer, rollnamn, organisationsnummer, systemnamn, systemversion) ;

		//Autentiseringsintyget
		//directoryId
		String expectedDirectoryIDSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:DirectoryID",
				personnummer);
		assertThat("Ticket should contain directoryID and personnummer", ticket, containsString(expectedDirectoryIDSamlAttribute));
		
		//organizationId
		String expectedOrganizationIdSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:OrganizationID",
				organisationsnummer);
		assertThat("Ticket should contain organizationID and Organisationsnummer",
				ticket, containsString(expectedOrganizationIdSamlAttribute));
		
		//Auktorisationsintyget
		//assertionType måste innehålla värdet AuthorizationData
		String expectedAssertionTypeSamlAttribute = format(samlAttributeTemplate, "urn:apotekensservice:names:federation:attributeName:assertionType", "AuthorizationData");
		assertThat("Ticket should contain urn for assertionType and the value AuthorizationData",
				ticket, containsString(expectedAssertionTypeSamlAttribute));

		//connectedAssertionId
		assertThat("Ticket should contain urn for connectedAssertionID",
				ticket, containsString("urn:apotekensservice:names:federation:attributeName:connectedAssertionId"));
		assertNotNull("connectedAssertionID was not found in ticket", getConnectedAssertionIDFromTicket(ticket));
		
		//efternamn
		String expectedEfternamnSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:efternamn",
				efternamn);
		assertThat("Ticket should contain attribute efternamn and its attributevalue",
				ticket, containsString(expectedEfternamnSamlAttribute));

		//förnamn
		String expectedFornamnSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:fornamn",
				fornamn);
		assertThat("Ticket should contain attribute fornamn and its attributevalue",
				ticket, containsString(expectedFornamnSamlAttribute));
		
		//organisationsnummer
		String expectedOrganisationsnummerSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:organisationsnummer",
				organisationsnummer);
		assertThat("Ticket should contain attribute organisationsnummer and its attributevalue",
				ticket, containsString(expectedOrganisationsnummerSamlAttribute));
		
		//rollnamn I ApSe säkerhetsheader står det att detta attribut ska heta roll och inte rollnamn
		String expectedRollSamlAttribute = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:rollnamn",
				rollnamn);
		assertThat("Ticket should contain attribute roll and its attributevalue PRIVATPERSON",
				ticket, containsString(expectedRollSamlAttribute));
		
		//Infodata
		//Hur göra asserts på att rätt data kommer ut här. Detta överlappar ju med AuktorisationsIntyget i flera delar
		
	}
	private String getConnectedAssertionIDFromTicket(String ticket) {
		String connectedAssertionId = null;
		
		String connectedAssertionIDRegexPattern = format(samlAttributeTemplate,
				"urn:apotekensservice:names:federation:attributeName:connectedAssertionId",
				"(_[a-z,A-Z,0-9]*)");
		
		Pattern pattern = Pattern.compile(connectedAssertionIDRegexPattern);
		Matcher matcher = pattern.matcher(ticket);
		if(matcher.find()){
			connectedAssertionId = matcher.group(1);
		}
		
		return connectedAssertionId;
	}
}
