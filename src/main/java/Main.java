import java.util.LinkedList;

/**
 * Created by Steffen on 19.05.2017.
 */
public class Main {

    public static void main(String [ ] args) {
        BPMNReader bpmnReader = new BPMNReader();
        LinkedList<String> tasks = bpmnReader.getTasks("C:\\Users\\Steffen\\Desktop\\bpmn.xml");

        printTasks(tasks);
    }

    public static void printTasks(LinkedList<String> tasks) {
        for (String task : tasks) {
            System.out.println(task);
        }
    }
}
