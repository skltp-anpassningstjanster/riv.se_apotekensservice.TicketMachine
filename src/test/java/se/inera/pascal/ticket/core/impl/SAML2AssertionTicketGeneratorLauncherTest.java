package se.inera.pascal.ticket.core.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;

public class SAML2AssertionTicketGeneratorLauncherTest {

	@Test
	public void allFieldsAreCopiedCorrect() throws Exception {
		
	 SAML2AssertionTicketGeneratorLauncher launcher = new SAML2AssertionTicketGeneratorLauncher();

		ApseAuthorizationAttributes expected = new ApseAuthorizationAttributes();
		expected.setArbetsplats("Sjukhuset");
		expected.setArbetsplatskod("SJUKHUS 1");
		expected.setBefattningskod("BK 1");
		expected.setEfternamn("Kula");
		expected.setFornamn("Kalle");
		expected.setForskrivarkod("DR");
		expected.setKatalog("HSA");
		expected.setKatalogId("HSA ID");
		expected.setLegitimationskod("LK");
		expected.setPostadress("Fabriskgatan 13");
		expected.setPostnummer("44266");
		expected.setPostort("Stockholm");
		expected.setRoll("EN ROLL");
		expected.setTelefonnummer("08 1234567890");
		expected.setYrkeskod("YK");
		launcher.setIncomingAuthorizationAttributes(expected);
		launcher.configureAttributes();
		ApseAuthorizationAttributes actual = launcher.getApseAuthorizationAttributes();

		assertNotNull(actual);
		assertEquals(expected.getArbetsplats(), actual.getArbetsplats());
		assertEquals(expected.getArbetsplatskod(), actual.getArbetsplatskod());
		assertEquals(expected.getBefattningskod(), actual.getBefattningskod());
		assertEquals(expected.getEfternamn(), actual.getEfternamn());
		assertEquals(expected.getFornamn(), actual.getFornamn());
		assertEquals(expected.getForskrivarkod(), actual.getForskrivarkod());
		assertEquals(expected.getKatalog(), actual.getKatalog());
		assertEquals(expected.getKatalogId(), actual.getKatalogId());
		assertEquals(expected.getLegitimationskod(), actual.getLegitimationskod());
		assertEquals(expected.getPostadress(), actual.getPostadress());
		assertEquals(expected.getPostnummer(), actual.getPostnummer());
		assertEquals(expected.getPostort(), actual.getPostort());
		assertEquals(expected.getRoll(), actual.getRoll());
		assertEquals(expected.getTelefonnummer(), actual.getTelefonnummer());
		assertEquals(expected.getYrkeskod(), actual.getYrkeskod());

	}
	
	@Test
	public void defaultValuesShouldBeKeptWhenValuesAreMissing() throws Exception {

		//Defaultvalues are found in apse_authorization.properties
		/*
			roll=
			katalogId=
			katalog=
			forskrivarkod=
			legitimationskod=
			yrkeskod=
			befattningskod=
			fornamn=
			efternamn=
			arbetsplatskod=
			arbetsplats=
			postadress=
			postnummer=
			postort=
			telefonnummer=
		*/
		
		SAML2AssertionTicketGeneratorLauncher launcher = new SAML2AssertionTicketGeneratorLauncher();
		
		ApseAuthorizationAttributes expected = new ApseAuthorizationAttributes();

		launcher.setIncomingAuthorizationAttributes(expected);
		launcher.configureAttributes();
		ApseAuthorizationAttributes actual = launcher.getApseAuthorizationAttributes();

		assertNotNull(actual);
		assertEquals("", actual.getArbetsplats());
		assertEquals("", actual.getArbetsplatskod());
		assertEquals("", actual.getBefattningskod());
		assertEquals("", actual.getEfternamn());
		assertEquals("", actual.getFornamn());
		assertEquals("", actual.getForskrivarkod());
		assertEquals("", actual.getKatalog());
		assertEquals("", actual.getKatalogId());
		assertEquals("", actual.getLegitimationskod());
		assertEquals("", actual.getPostadress());
		assertEquals("", actual.getPostnummer());
		assertEquals("", actual.getPostort());
		assertEquals("", actual.getRoll());
		assertEquals("", actual.getTelefonnummer());
		assertEquals("", actual.getYrkeskod());
		assertEquals("", actual.getPersonnummer());
	}

	@Test
	public void setAuthorizationAttributesShouldNotFailIfStringIsEmptyOrNull() throws Exception {

		ApseAuthorizationAttributes expected = new ApseAuthorizationAttributes();
		expected.setArbetsplats(null);
		expected.setArbetsplatskod(null);
		expected.setBefattningskod(null);
		expected.setEfternamn(null);
		expected.setFornamn(null);
		expected.setForskrivarkod(null);
		expected.setKatalog(null);
		expected.setKatalogId(null);
		expected.setLegitimationskod(null);
		expected.setPostadress(null);
		expected.setPostnummer(null);
		expected.setPostort(null);
		expected.setRoll(null);
		expected.setTelefonnummer(null);
		expected.setYrkeskod(null);

		SAML2AssertionTicketGeneratorLauncher launcher = new SAML2AssertionTicketGeneratorLauncher();

		launcher.setIncomingAuthorizationAttributes(expected);
		launcher.configureAttributes();
		ApseAuthorizationAttributes actual = launcher.getApseAuthorizationAttributes();
		

		assertNotNull(actual);
	}

}
