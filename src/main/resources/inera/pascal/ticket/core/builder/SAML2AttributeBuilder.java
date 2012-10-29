package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2AttributeBuilder implements Builder<Attribute> {
	
//	private String attributeName = "urn:apotekensservice:names:federation:attributeName:DirectoryID";
//	private String attributeValue = "XYZ";
	private String attributeName = "";
	private String attributeValue = "";
	private static String attributeFormat = StringConstants.ATTRIBUTE_FORMAT;
	
	//public SAML2AttributeBuilder(){}
	public SAML2AttributeBuilder(String attributeName,String attributeValue) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;		
	}
	
	public SAML2AttributeBuilder setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}
	
	public SAML2AttributeBuilder setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
		return this;
	}
	@Override
	public Attribute build() {
		Attribute attribute = new AttributeBuilder().buildObject();
		XSString xsString = new XSStringBuilder().buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		attribute.setName(attributeName);
		attribute.setNameFormat(attributeFormat);
		xsString.setValue(attributeValue);
		attribute.getAttributeValues().add(xsString);		
		return attribute;
	}

}
