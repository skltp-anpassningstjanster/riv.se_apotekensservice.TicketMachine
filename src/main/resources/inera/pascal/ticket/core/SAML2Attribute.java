package se.inera.pascal.ticket.core;


public class SAML2Attribute {
	
	private String name;
	private String value;
		
	public SAML2Attribute(String name, String value){
		this.name= name;
		this.value = value;
	}
	
	public String getName() {
		String retVal;
		if (name.contains(":")){
			retVal = name;
		}else{
			retVal = StringConstants.SAML2_ATTRIBUTE_PREFIX + name; 
		}
		return retVal;
	}
	public String getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
