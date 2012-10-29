package se.inera.pascal.ticket.core;

public class ApseAuthorizationAttributes {
	private String rollnamn="";
	private String katalogId="";
	private String katalog="";
	private String forskrivarkod="";
	private String legitimationskod="";
	private String yrkeskod="";
	private String befattningskod="";
	private String fornamn="";
	private String efternamn="";
	private String arbetsplatskod="";
	private String arbetsplats="";
	private String postadress="";
	private String postnummer="";
	private String postort="";
	private String telefonnummer="";

	public ApseAuthorizationAttributes(){		
	}
	public ApseAuthorizationAttributes(String rollnamn,
			String katalogId,
			String katalog,
			String forskrivarkod, 
			String legitimationskod, 
			String yrkeskod, 
			String befattningskod, 
			String fornamn,
			String efternamn,
			String arbetsplatskod,
			String arbetsplats,
			String postadress,
			String postnummer,
			String postort,
			String telefonnummer){
		this.rollnamn = rollnamn;
		this.katalogId = katalogId;
		this.katalog = katalog;
		this.forskrivarkod = forskrivarkod;
		this.legitimationskod = legitimationskod;
		this.yrkeskod = yrkeskod;
		this.befattningskod = befattningskod;
		this.fornamn = fornamn;
		this.efternamn = efternamn;
		this.arbetsplatskod = arbetsplatskod;
		this.arbetsplats = arbetsplats;
		this.postadress = postadress;
		this.postnummer = postnummer;
		this.postort = postort;
		this.telefonnummer = telefonnummer; 
	}
	
	public void setRollnamn(String rollnamn){
		this.rollnamn = rollnamn;
	}
	public String getRollnamn(){
		return rollnamn;
	}
	public void setKatalogId(String katalogId){
		this.katalogId = katalogId;
	}
	public String getKatalogId(){
		return katalogId;
	}
	public void setKatalog(String katalog){
		this.katalog = katalog;
	}
	public String getKatalog(){
		return katalog;
	}
	public void setForskrivarkod(String forskrivarkod){
		this.forskrivarkod = forskrivarkod;
	}
	public String getForskrivarkod(){
		return forskrivarkod;
	}
	public void setLegitimationskod(String legitimationskod){
		this.legitimationskod = legitimationskod;
	}
	public String getLegitimationskod(){
		return legitimationskod;
	}
	public void setYrkeskod(String yrkeskod){
		this.yrkeskod = yrkeskod;
	}
	public String getYrkeskod(){
		return yrkeskod;
	}
	public void setBefattningskod(String befattningskod){
		this.befattningskod = befattningskod;
	}
	public String getBefattningskod(){
		return befattningskod;
	}
	public void setFornamn(String fornamn){
		this.fornamn = fornamn;
	}
	public String getFornamn(){
		return fornamn;
	}
	public void setEfternamn(String efternamn){
		this.efternamn = efternamn;
	}
	public String getEfternamn(){
		return efternamn;
	}
	public void setArbetsplatskod(String arbetsplatskod){
		this.arbetsplatskod = arbetsplatskod;
	}
	public String getArbetsplatskod(){
		return arbetsplatskod;
	}
	public void setArbetsplats(String arbetsplats){
		this.arbetsplats = arbetsplats;
	}
	public String getArbetsplats(){
		return arbetsplats;
	}
	public void setPostadress(String postadress){
		this.postadress = postadress;
	}
	public String getPostadress(){
		return postadress;
	}
	public void setPostnummer(String postnummer){
		this.postnummer = postnummer;
	}
	public String getPostnummer(){
		return postnummer;
	}
	public void setPostort(String postort){
		this.postort = postort;
	}
	public String getPostort(){
		return postort;
	}
	public void setTelefonnummer(String telefonnummer){
		this.telefonnummer = telefonnummer;
	}
	public String getTelefonnummer(){
		return telefonnummer;
	}
}
