package logic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WSDLReader {    
    
    /**
	 * Retrieves the WebService description from the wsdl file at the given url.
	 */
	public static String getWebServiceDescription(String url) throws ParserConfigurationException, MalformedURLException, SAXException, IOException{			
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(url).openStream());
		Element rootElement = doc.getDocumentElement();		
		NodeList taskNodes = rootElement.getElementsByTagName("wsdl:documentation");		

		return taskNodes.item(0).getTextContent();		
	}
}
