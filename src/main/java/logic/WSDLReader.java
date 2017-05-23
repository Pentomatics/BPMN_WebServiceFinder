package logic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WSDLReader {

    public static String[] getServiceMethods(String filename) {
        String[] serviceMethods = new String[0];
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = domFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            Element rootElement = document.getDocumentElement();

            NodeList methodNodes = rootElement.getElementsByTagName("wsdl:message");
            serviceMethods = new String[methodNodes.getLength()];

            for (int i = 0; i < methodNodes.getLength(); i++) {
                serviceMethods[i] = methodNodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serviceMethods;
    }
}
