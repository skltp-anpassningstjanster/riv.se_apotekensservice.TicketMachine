package se.inera.pascal.ticket.core.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AssertionUnmarshaller;
import org.opensaml.saml2.core.impl.ResponseUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.inera.pascal.ticket.core.SAML2AssertionReader;

public class SAML2AssertionReaderImpl implements SAML2AssertionReader {
	
	static private final Logger logger = LoggerFactory.getLogger(SAML2AssertionReaderImpl.class);
	
	private  AssertionUnmarshaller assertionUnmarshaller;
	private  ResponseUnmarshaller responseUnmarshaller;
	private  BasicParserPool parser;
	private  String relativePath;
	
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionReader#readSAML2ResponseFromFile(java.lang.String)
	 */
	@Override
	public Response readSAML2ResponseFromFile(String fileName) {
		File file = null;
		FileReader fileReader = null;
		Document saml2ResponseDocument = null;
		Element saml2ResponseElement = null;
		Response response = null;

		if(!StringUtils.isBlank(relativePath)&& 
				   !StringUtils.isBlank(fileName)){			
					file = new File(relativePath, fileName);				
		}

		if(file !=null){	
			if(!file.exists() || !file.canRead()){
				String message = "File not exists or is read protected";
				logger.info(message);
				throw new IllegalArgumentException(message);
			}
			try {
				fileReader = new FileReader(file);
			}catch(FileNotFoundException e){
				String message ="readSAML2ResponseFromFile(String), Marshalling failed";
				logger.error(message,e);
			}       
		}

		if(fileReader !=null){
			try {
				saml2ResponseDocument = parser.parse(fileReader);
			}catch(XMLParserException e){
				String message ="readSAML2ResponseFromFile(String), Parsing failed";
				logger.error(message,e);
			}
			finally {
				try {
					if(fileReader !=null){
					   fileReader.close();
					}
				}catch(IOException e){
					String message ="readSAML2ResponseFromFile(String), Closing fileReader failed";
					logger.error(message,e);
				}
			}
		}
		if(saml2ResponseDocument !=null){
			saml2ResponseElement = saml2ResponseDocument.getDocumentElement();
		}
		if(saml2ResponseElement !=null){
			try {
				response = (Response)responseUnmarshaller.unmarshall(saml2ResponseElement);
			}catch(UnmarshallingException e){
				String message ="readSAML2ResponseFromFile(String), Unmarhalling failed";
				logger.error(message,e);
			}
		}		
		return response;
	}
	
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionReader#readSAML2AssertionFromFile(java.lang.String)
	 */
	@Override
	public Assertion readSAML2AssertionFromFile(String fileName) {
		File file = null;
		FileReader fileReader = null;
		Document assertionDocument = null;
		Element assertionElement = null;
		Assertion assertion = null;

		if(!StringUtils.isBlank(relativePath)&& 
		   !StringUtils.isBlank(fileName)){			
			file = new File(relativePath, fileName);				
		}

		if(file !=null){	
			if(!file.exists() || !file.canRead()){
				String message = "File not exists or is read protected";
				logger.info(message);
				throw new IllegalArgumentException(message);
			}
			try {
				fileReader = new FileReader(file);
			}catch(FileNotFoundException e){
				String message ="readSAML2AssertionFromFile(String), Marshalling failed";
				logger.error(message,e);
			}       
		}

		if(fileReader !=null){
			try {
				assertionDocument = parser.parse(fileReader);
			}catch(XMLParserException e){
				String message ="readSAML2AssertionFromFile(String), Parsing failed";
				logger.error(message,e);
			}
			finally {
				try {
					if(fileReader !=null){
						fileReader.close();
					}
				}catch(IOException e){
					String message ="readSAML2AssertionFromFile(String), Closing fileReader failed";
					logger.error(message,e);
				}
			}
		}
		if(assertionDocument !=null){
			assertionElement = assertionDocument.getDocumentElement();
		}
		if(assertionElement !=null){
			try {
				assertion = (Assertion)assertionUnmarshaller.unmarshall(assertionElement);
			}catch(UnmarshallingException e){
				String message ="readSAML2AssertionFromFile(String), Unmarhalling failed";
				logger.error(message,e);
			}
		}		
		return assertion;
	}
	
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionReader#readSAML2ResponseFromString(java.lang.String)
	 */
	@Override
	public Response readSAML2ResponseFromString(String saml2AssertionResponseString) {
		StringReader stringReader = null;
		Document responseDocument = null;
		Element responseElement = null;
		Response response = null;		

		if(!StringUtils.isBlank(saml2AssertionResponseString)){			
			stringReader = new StringReader(saml2AssertionResponseString);			
		}
		if(stringReader !=null){
			try {
				responseDocument = parser.parse(stringReader);
			}catch(XMLParserException e){
				String message ="readSAML2ResponseFromString(String), Parsing failed";
				logger.error(message,e);
			}
			finally {
				if(stringReader !=null){
					stringReader.close();
				}
			}
		}
		if(responseDocument !=null){
			responseElement = responseDocument.getDocumentElement();
		}
		if(responseElement !=null){
			try {
				response = (Response)responseUnmarshaller.unmarshall(responseElement);
			}catch(UnmarshallingException e){
				String message ="readSAML2ResponseFromString(String), Unmarhalling failed";
				logger.error(message,e);
			}
		}		
		return response;
	}
	
	/**
	 * {@inheritDoc}
	 * @see se.inera.pascal.ticket.core.SAML2AssertionReader#readSAML2AssertionFromString(java.lang.String)
	 */
	@Override
	public Assertion readSAML2AssertionFromString(String saml2AssertionString) {
		StringReader stringReader = null;
		Document assertionDocument = null;
		Element assertionElement = null;
		Assertion assertion = null;		

		if(!StringUtils.isBlank(saml2AssertionString)){			
			stringReader = new StringReader(saml2AssertionString);			
		}
		if(stringReader !=null){
			try {
				assertionDocument = parser.parse(stringReader);
			}catch(XMLParserException e){
				String message ="readSAML2AssertionFromString(String), Parsing failed";
				logger.error(message,e);
			}
			finally {
				if(stringReader !=null){
					stringReader.close();
				}
			}
		}
		if(assertionDocument !=null){
			assertionElement = assertionDocument.getDocumentElement();
		}
		if(assertionElement !=null){
			try {
				assertion = (Assertion)assertionUnmarshaller.unmarshall(assertionElement);
			}catch(UnmarshallingException e){
				String message ="readSAML2AssertionFromString(String), Unmarhalling failed";
				logger.error(message,e);
			}
		}		
		return assertion;
	}

	public AssertionUnmarshaller getAssertionUnmarshaller() {
		return assertionUnmarshaller;
	}

	public void setAssertionUnmarshaller(AssertionUnmarshaller assertionUnmarshaller) {
		this.assertionUnmarshaller = assertionUnmarshaller;
	}

	public ResponseUnmarshaller getResponseUnmarshaller() {
		return responseUnmarshaller;
	}

	public void setResponseUnmarshaller(ResponseUnmarshaller responseUnmarshaller) {
		this.responseUnmarshaller = responseUnmarshaller;
	}

	public BasicParserPool getParser() {
		return parser;
	}

	public void setParser(BasicParserPool parser) {
		this.parser = parser;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
}
