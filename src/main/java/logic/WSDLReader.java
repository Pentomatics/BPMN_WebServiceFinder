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
     * Iterates over all operations in a wsdl file to get a list of their names.
     * Those names / descriptions can be used to decide which WebService fits best to a give task.
     * @param url The URL to the webservice to be searched
     * @return An array containing the names of all operations from this WebService
     * @throws ParserConfigurationException
     * @throws MalformedURLException
     * @throws SAXException
     * @throws IOException
     */
	public static String[] getWebServiceOperations(String url) throws ParserConfigurationException, MalformedURLException, SAXException, IOException{			
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(url).openStream());
		Element rootElement = doc.getDocumentElement();		
		NodeList taskNodes = rootElement.getElementsByTagName("wsdl:operation");	
		String[] descriptions = new String[taskNodes.getLength()]; 
		for(int i = 0; i < taskNodes.getLength(); i++){
			Element e = (Element)taskNodes.item(i);
            descriptions[i] = e.getAttribute("name");            
		}

		return descriptions;		
	}
}
