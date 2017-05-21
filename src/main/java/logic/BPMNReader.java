package logic;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Steffen on 19.05.2017.
 */
public class BPMNReader {

    public static LinkedList<String> getTasks(String filename) {
        LinkedList<String> tasks = new LinkedList<String>();
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = domFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            Element rootElement = document.getDocumentElement();

            NodeList taskNodes = rootElement.getElementsByTagName("bpmn:task");

            for (int i = 0; i < taskNodes.getLength(); i++) {
                tasks.add(taskNodes.item(i).getAttributes().getNamedItem("name").getNodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static LinkedList<String> getKeyWordsFromTasks(LinkedList<String> tasks) {
        LinkedList<String> keyWordList = new LinkedList<String>();

        for (String task : tasks) {
            String[] keyWords = task.split("\\s+");

            for (String keyWord : keyWords) {
                keyWordList.add(keyWord);
            }
        }

        return keyWordList;
    }
}
