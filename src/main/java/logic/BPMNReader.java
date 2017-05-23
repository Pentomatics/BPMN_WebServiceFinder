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
public class BPMNReader {

    public static String[] getTasks(String filename) {
        String[] tasks = new String[0];
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = domFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            Element rootElement = document.getDocumentElement();

            NodeList taskNodes = rootElement.getElementsByTagName("bpmn:task");
            tasks = new String[taskNodes.getLength()];

            for (int i = 0; i < taskNodes.getLength(); i++) {
                tasks[i] = taskNodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static String[] getKeyWordsFromTask(String task){
        return task.split("\\s+");
    }
}
