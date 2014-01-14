package se.inera.pascal.ticket;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

public class ArgosTicketLoadTest {
	private static ArgosTicket argosTicket = new ArgosTicket();
	
	@Ignore
	@Test
	public void loadTest() {
		int numberOfIterations = 100;
		List<Long> testData = new ArrayList();
		List<Long> testDataStatic = new ArrayList();
		for(int i = 1; i < numberOfIterations; i ++) {
			long startTime = System.nanoTime();    
			getArgosTicketAsString();
			long estimatedTime = System.nanoTime() - startTime;
			testData.add(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));
		}
		
		for(int i = 1; i < numberOfIterations; i ++) {
			long startTime = System.nanoTime();    
			getArgosTicketStaticAsString();
			long estimatedTime = System.nanoTime() - startTime;
			testDataStatic.add(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));
		}
		createReport( testData, testDataStatic, numberOfIterations );
		assertTrue(true);
	}
	
	private void createReport(List<Long> testData, List<Long> testDataStatic, int numberOfIterations) {
		System.out.println("Not static | Static");
		for(int i = 0; i < numberOfIterations - 1; i++) {
			System.out.println(testData.get(i) + "             " + testDataStatic.get(i) );
		}
	}
	
	private String getArgosTicketAsString() {
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

		return new ArgosTicket().getTicketForOrganization(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
					befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
					requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

	}
	
	private String getArgosTicketStaticAsString() {
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

		return argosTicket.getTicketForOrganization(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
					befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
					requestId, rollnamn, hsaID, katalog, organisationsnummer, systemnamn, systemversion, systemIp);

	}

}
