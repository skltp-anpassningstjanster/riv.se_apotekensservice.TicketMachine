package se.inera.pascal.ticket.core;

public class ApseAuthorizationAttributes {
	private String katalogId = "";
	private String katalog = "";
	private String forskrivarkod = "";
	private String legitimationskod = "";
	private String yrkeskod = "";
	private String befattningskod = "";
	private String fornamn = "";
	private String efternamn = "";
	private String arbetsplatskod = "";
	private String arbetsplats = "";
	private String postadress = "";
	private String postnummer = "";
	private String postort = "";
	private String telefonnummer = "";
	private String personnummer = "";
	private String organisationsnummer = "";
	private String roll = "";

	public ApseAuthorizationAttributes() {
	}

	public void setKatalogId(String katalogId) {
		this.katalogId = katalogId;
	}

	public String getKatalogId() {
		return katalogId;
	}

	public void setKatalog(String katalog) {
		this.katalog = katalog;
	}

	public String getKatalog() {
		return katalog;
	}

	public void setForskrivarkod(String forskrivarkod) {
		this.forskrivarkod = forskrivarkod;
	}

	public String getForskrivarkod() {
		return forskrivarkod;
	}

	public void setLegitimationskod(String legitimationskod) {
		this.legitimationskod = legitimationskod;
	}

	public String getLegitimationskod() {
		return legitimationskod;
	}

	public void setYrkeskod(String yrkeskod) {
		this.yrkeskod = yrkeskod;
	}

	public String getYrkeskod() {
		return yrkeskod;
	}

	public void setBefattningskod(String befattningskod) {
		this.befattningskod = befattningskod;
	}

	public String getBefattningskod() {
		return befattningskod;
	}

	public void setFornamn(String fornamn) {
		this.fornamn = fornamn;
	}

	public String getFornamn() {
		return fornamn;
	}

	public void setEfternamn(String efternamn) {
		this.efternamn = efternamn;
	}

	public String getEfternamn() {
		return efternamn;
	}

	public void setArbetsplatskod(String arbetsplatskod) {
		this.arbetsplatskod = arbetsplatskod;
	}

	public String getArbetsplatskod() {
		return arbetsplatskod;
	}

	public void setArbetsplats(String arbetsplats) {
		this.arbetsplats = arbetsplats;
	}

	public String getArbetsplats() {
		return arbetsplats;
	}

	public void setPostadress(String postadress) {
		this.postadress = postadress;
	}

	public String getPostadress() {
		return postadress;
	}

	public void setPostnummer(String postnummer) {
		this.postnummer = postnummer;
	}

	public String getPostnummer() {
		return postnummer;
	}

	public void setPostort(String postort) {
		this.postort = postort;
	}

	public String getPostort() {
		return postort;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}
	
	public String getPersonnummer() {
		return personnummer;
	}
	
	public void setPersonnummer(String personnummer) {
		this.personnummer = personnummer;
	}

	public void setOrganisationsnummer(String organisationsnummer) {
		this.organisationsnummer = organisationsnummer; 
	}
	
	public String getOrganisationsnummer() {
		return organisationsnummer;
	}

	/**
	 * Obs! Roll är det attribut i säkerhetsheadern som ska användas enligt dokumentet
	 * Säkerhetsheader v1.0 från Apotekens service. Tidigare fanns även rollnamn felaktigt
	 * i biljettautomaten, men har rättats i NTP-12.
	 * 
	 * @param roll
	 */
	public void setRoll(String roll) {
		this.roll = roll;
	}
	
	/**
	 * Obs! Roll är det attribut i säkerhetsheadern som ska användas enligt dokumentet
	 * Säkerhetsheader v1.0 från Apotekens service. Tidigare fanns även rollnamn felaktigt
	 * i biljettautomaten, men har rättats i NTP-12.
	 * 
	 * @return roll
	 */
	public String getRoll() {
		return roll;
	}
}
