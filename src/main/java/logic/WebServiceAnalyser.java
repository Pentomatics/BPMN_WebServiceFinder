package logic;
import java.util.LinkedList;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WebServiceAnalyser {

    public static WebServiceAnalysisResult analyseWebService(LinkedList<String> keyWords, String wsdlFilename) {
        LinkedList<String> serviceMethods = WSDLReader.getServiceMethods(wsdlFilename);

        int hits = 0;

        for (String keyWord : keyWords) {
            for (String serviceMethod : serviceMethods) {
                if (serviceMethod.contains(keyWord)) {
                    hits++;
                }
            }
        }

        WebServiceAnalysisResult analysisResult = new WebServiceAnalysisResult(hits);
        return analysisResult;
    }
}
