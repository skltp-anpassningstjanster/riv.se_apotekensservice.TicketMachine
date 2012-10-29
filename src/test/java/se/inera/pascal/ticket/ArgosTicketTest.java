package se.inera.pascal.ticket;

import static org.junit.matchers.JUnitMatchers.containsString;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ArgosTicketTest extends TestCase {

	@Test
	public void testHappyCaseToSeeHowTicketGeneratorWorks() {

		String forskrivarkod = "1111152";
		String legitimationskod = "1111111";
		String fornamn = "Lars";
		String efternamn = "LŠkare";
		String yrkesgrupp = "FORSKRIVARE";
		String befattningskod = "1111111";
		String arbetsplatskod = "4000000000001";
		String arbetsplatsnamn = "VC Test";
		String postort = "Staden";
		String postadress = "VŠgen 1";
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

		Assert.assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));

	}

	@Test
	public void ineraCodeDoesNotGiveErrorWhenOneFieldIsNull() {

		String forskrivarkod = "1111129";
		String legitimationskod = null;
		String fornamn = "Lars";
		String efternamn = "LŠkare";
		String yrkesgrupp = "LŠkare";
		String befattningskod = "123456";
		String arbetsplatskod = "1234567890";
		String arbetsplatsnamn = "Sjukhuset";
		String postort = "Staden";
		String postadress = "VŠgen 1";
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

		Assert.assertNotNull(ticket);

	}
}
