package se.inera.pascal.ticket.core.builder;

import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.impl.SignatureBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2SignatureBuilder implements Builder<Signature> {
	
	private Credential credential;
	private String canonicalizationAlgorithm = SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
	private String signatureAlgoritm = SignatureConstants.ALGO_ID_SIGNATURE_RSA;
	private KeyInfo keyInfo;
	
	public SAML2SignatureBuilder() {}
	
	public SAML2SignatureBuilder(Credential credential,
			String canonicalizationAlgorithm, String signatureAlgoritm) {
		this.credential = credential;
		this.canonicalizationAlgorithm = canonicalizationAlgorithm;
		this.signatureAlgoritm = signatureAlgoritm;
	}
	
	public SAML2SignatureBuilder(Credential credential,
			String canonicalizationAlgorithm, String signatureAlgoritm, KeyInfo keyInfo) {
		this.credential = credential;
		this.canonicalizationAlgorithm = canonicalizationAlgorithm;
		this.signatureAlgoritm = signatureAlgoritm;
		this.keyInfo = keyInfo;
	}
	
	public SAML2SignatureBuilder setSignatureAlgoritm(String signatureAlgoritm) {
		this.signatureAlgoritm = signatureAlgoritm;
		return this;
	}

	public SAML2SignatureBuilder setCredential(BasicCredential credential) {
		this.credential = credential;
		return this;
	}

	public SAML2SignatureBuilder setKeyInfo(KeyInfo keyInfo) {
		this.keyInfo = keyInfo;
		return this;
	}

	public SAML2SignatureBuilder setCanonicalizationAlgorithm(String canonicalizationAlgorithm) {
		this.canonicalizationAlgorithm = canonicalizationAlgorithm;
		return this;
	}

	@Override
	public Signature build() {
		Signature signature = new SignatureBuilder().buildObject();		
		signature.setSigningCredential(credential);
		signature.setCanonicalizationAlgorithm(canonicalizationAlgorithm);
		signature.setSignatureAlgorithm(signatureAlgoritm);
		signature.setKeyInfo(keyInfo);		
		return signature;
	}
}
