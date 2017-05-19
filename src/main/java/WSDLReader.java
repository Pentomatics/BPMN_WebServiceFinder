import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WSDLReader {

    public LinkedList<String> getServiceMethods(String filename) {
        LinkedList<String> methods = new LinkedList<String>();
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = domFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            Element rootElement = document.getDocumentElement();

            NodeList methodNodes = rootElement.getElementsByTagName("wsdl:message");
            System.out.println("Length " + methodNodes.getLength());
            for (int i = 0; i < methodNodes.getLength(); i++) {
                methods.add(methodNodes.item(i).getAttributes().getNamedItem("name").getNodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return methods;
    }
}
