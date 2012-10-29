package se.inera.pascal.ticket.core.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;


public class XMLReader {
	private static final Logger logger = LoggerFactory.getLogger(XMLReader.class);
	
	private Document document = null;
	private BasicParserPool parser = null;
	
	public XMLReader(BasicParserPool parser){
		this.parser = parser;
	}
	public Document parseFile(File xmlf){
		try {
			FileReader fileReader = new FileReader(xmlf);
			document = parser.parse(fileReader);				 
		} catch (FileNotFoundException e) {
			String mess = "Error reading file";
			logger.error(mess,e);
		} catch (XMLParserException e) {
			String mess = "XMLParser of file failed";
			logger.error(mess,e);
		}
		return document;
	}
	public Document parseString(String xmls) throws Exception{
		try {
			InputStream in = new ByteArrayInputStream(xmls.getBytes());
			document = parser.parse(in);
		} catch (XMLParserException e) {
			String mess = "XMLParser of string failed";
			logger.error(mess,e);
			throw new Exception(mess);
		}
		return document;
	}
}
