import java.util.LinkedList;

import logic.BPMNReader;
import logic.WSDLReader;
import logic.WebServiceFinder;
import logic.WebServiceResult;
import ui.mainFrame;

/**
 * Created by Steffen on 19.05.2017.
 */
public class Main {

    public static void main(String [ ] args) {
        String bpmnFilename = "src/main/resources/KundenRabattSystem.bpmn";
        String[] wsdlFilenames = {"src/main/resources/TestService1.wsdl"};

        String[] tasks = BPMNReader.getTasks(bpmnFilename);
        String[] serviceMethods = WSDLReader.getServiceMethods(wsdlFilenames[0]);

        printTasks(tasks);
        printServiceMethods(serviceMethods);


        System.out.println("\nTask results: ");
        for (String task : tasks) {
            LinkedList<WebServiceResult> results = WebServiceFinder.findAppropriateWebServicesForTask(task, wsdlFilenames);
            printResultsForTask(task, results);
        }
    	
    	new mainFrame();
    }

    public static void printTasks(String[] tasks) {
        System.out.println("\nBPMN tasks: ");

        for (String task : tasks) {
            System.out.println(task);
        }
    }

    public static void printServiceMethods(String[] serviceMethods) {
        System.out.println("\nWebService methods: ");

        for (String method : serviceMethods) {
            System.out.println(method);
        }
    }

    public static void printResultsForTask(String task, LinkedList<WebServiceResult> results) {
        for (WebServiceResult result : results) {
            System.out.println("Task name: " + task + " - WebService: " + result.getName() + ", Hits: " + result.getHits());
        }
    }
}
