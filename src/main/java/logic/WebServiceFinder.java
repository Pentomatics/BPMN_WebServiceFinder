package logic;
import java.util.LinkedList;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WebServiceFinder {

    public static LinkedList<WebServiceResult> findAppropriateWebServicesForTask(String task, String[] wsdlFilenames) {
        String[] keyWords = BPMNReader.getKeyWordsFromTask(task);
        LinkedList<WebServiceResult> webServiceResults = new LinkedList<WebServiceResult>();

        for (String wsdlFilename : wsdlFilenames) {
            int hits = 0;
            String[] serviceMethods = WSDLReader.getServiceMethods(wsdlFilename);

            for (String keyWord : keyWords) {
                for (String serviceMethod : serviceMethods) {
                    if (serviceMethod.contains(keyWord)) {
                        hits++;
                    }
                }
            }

            webServiceResults.add(new WebServiceResult(wsdlFilename, hits));
        }


        //precision = richtige treffer / (richtige treffer + falsche treffer);
        //recall = richtige treffer / (richtige treffer + übersehene treffer);
        //fmeasure = 2 * ((precision * recall) / (precision + recall));


        return webServiceResults;
    }
}
