import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Steffen on 19.05.2017.
 */
public class Main {

    public static void main(String [ ] args) {
        String bpmnFilename = "C:\\Users\\Steffen\\Desktop\\bpmn.xml";
        String wsdlFilename = "C:\\Users\\Steffen\\Desktop\\wsdl.xml";

        BPMNReader bpmnReader = new BPMNReader();
        LinkedList<String> tasks = bpmnReader.getTasks(bpmnFilename);

        WSDLReader wsdlReader = new WSDLReader();
        LinkedList<String> serviceMethods = wsdlReader.getServiceMethods(wsdlFilename);

        printTasks(tasks);
        printServiceMethods(serviceMethods);

        LinkedList<String> keyWords = BPMNReader.getKeyWordsFromTasks(tasks);
        WebServiceAnalysisResult result = WebServiceAnalyser.analyseWebService(keyWords, wsdlFilename);
        System.out.println("\nHits: " + result.getHits());
    }

    public static void printTasks(LinkedList<String> tasks) {
        System.out.println("\nBPMN tasks: ");

        for (String task : tasks) {
            System.out.println(task);
        }
    }

    public static void printServiceMethods(LinkedList<String> serviceMethods) {
        System.out.println("\nWebService methods: ");

        for (String method : serviceMethods) {
            System.out.println(method);
        }
    }
}
