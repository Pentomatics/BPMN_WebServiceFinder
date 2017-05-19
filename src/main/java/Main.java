import java.util.LinkedList;

/**
 * Created by Steffen on 19.05.2017.
 */
public class Main {

    public static void main(String [ ] args) {
        BPMNReader bpmnReader = new BPMNReader();
        LinkedList<String> tasks = bpmnReader.getTasks("C:\\Users\\Steffen\\Desktop\\bpmn.xml");

        WSDLReader wsdlReader = new WSDLReader();
        LinkedList<String> serviceMethods = wsdlReader.getServiceMethods("C:\\Users\\Steffen\\Desktop\\wsdl.xml");

        printTasks(tasks);
        printServiceMethods(serviceMethods);
    }

    public static void printTasks(LinkedList<String> tasks) {
        for (String task : tasks) {
            System.out.println(task);
        }
    }

    public static void printServiceMethods(LinkedList<String> serviceMethods) {
        for (String method : serviceMethods) {
            System.out.println(method);
        }
    }
}
