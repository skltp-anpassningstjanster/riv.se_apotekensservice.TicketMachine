package se.inera.pascal.ticket.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2AttributeStatementBuilder implements Builder<AttributeStatement> {
	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public SAML2AttributeStatementBuilder addAttribute(Attribute attribute) {
		attributes.add(attribute);
		return this;
	}
	@Override
	public AttributeStatement build() {
		AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
		if(attributes.size() >0){
			for (Attribute attribute : attributes) {
				attributeStatement.getAttributes().add(attribute);			
			}
		}
		return attributeStatement;
	}
}
