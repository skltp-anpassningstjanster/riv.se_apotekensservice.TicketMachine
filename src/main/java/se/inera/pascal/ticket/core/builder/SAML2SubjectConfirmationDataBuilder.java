package se.inera.pascal.ticket.core.builder;

import org.opensaml.saml2.core.KeyInfoConfirmationDataType;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.KeyInfoConfirmationDataTypeBuilder;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.signature.impl.KeyNameBuilder;

import se.inera.pascal.ticket.core.Builder;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2SubjectConfirmationDataBuilder implements Builder<SubjectConfirmationData> {
	
	private String keyNameValue = StringConstants.KEYNAME_VALUE;

	public SAML2SubjectConfirmationDataBuilder setKeyNameValue(String keyNameValue) {
		this.keyNameValue = keyNameValue;
		return this;
	}

	
	public SubjectConfirmationData build() {		
		KeyInfoConfirmationDataType keyInfoConfirmationDataType = new KeyInfoConfirmationDataTypeBuilder().buildObject(KeyInfoConfirmationDataType.DEFAULT_ELEMENT_NAME, KeyInfoConfirmationDataType.TYPE_NAME);
		KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
		KeyName keyName = new KeyNameBuilder().buildObject();
		keyName.setValue(keyNameValue);
		keyInfo.getKeyNames().add(keyName);
		keyInfoConfirmationDataType.getKeyInfos().add(keyInfo);				
		return keyInfoConfirmationDataType;
	}
}
