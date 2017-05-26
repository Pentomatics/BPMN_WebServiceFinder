package logic;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Created by Steffen on 19.05.2017.
 */
public class WebServiceFinder {

	/**
	 * Evaluates all given WebServices and returns a list of WebServiceResults that contains information about the
	 * matching quality of each WebService.
	 * @param task The task that WebServices are searched for
	 * @param selectedWsdlFiles The URLs of all WebServices that should be evaluated (all active WebServices)
	 * @return A ordered list of WebServiceResults.
	 */  
    public static LinkedList<WebServiceResult> findAppropriateWebServicesForTask(String task, String[] selectedWsdlFiles) {
        String[] keyWords = BPMNReader.getKeyWordsFromTask(task);
        LinkedList<WebServiceResult> webServiceResults = new LinkedList<WebServiceResult>();

        countKeywordHits(webServiceResults, keyWords, selectedWsdlFiles);       
        webServiceResults.sort(getWebServiceRankingComparator());        
        rankWebSericeResults(webServiceResults, keyWords.length, selectedWsdlFiles.length);
                
        return webServiceResults;
    }
    
    /**
     * Ranks the WebServices contained in the given list by calculating recall, precision and f-measure for them.
     * Because only up to 3 best results should be displayed, all other WebServiceResults are removed from the list.
     * @param webServiceResults The list of WebServiceResults that is ranked
     * @param amountOfKeywords The amount of keywords that were used to search for WebServices. Used to calculate precision.
     * @param amountOfSearchedWebServices The amount of WebServices that were searched. Used to calculate recall.
     */
    private static void rankWebSericeResults(LinkedList<WebServiceResult> webServiceResults, int amountOfKeywords, int amountOfSearchedWebServices){
    	int validResults = 0; 
    	int displayableResults = 0;     
    	
        for(WebServiceResult webServiceResult : webServiceResults){
        	if(webServiceResult.getHits()>0){
        		validResults++;  
        		if(displayableResults<3)
        			displayableResults++;
        	}
        } 
        
        if(displayableResults==0){
        	JOptionPane.showMessageDialog(null, "Es wurden keine passenden Webdienste gefunden!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
        }
        
        while(webServiceResults.size()> displayableResults){
        	webServiceResults.removeLast();
        }
        
        for(WebServiceResult webServiceResult : webServiceResults){
        	webServiceResult.setRecall(validResults/amountOfSearchedWebServices);
        	webServiceResult.setPrecision(webServiceResult.getHits()/amountOfKeywords);
        	webServiceResult.setFmeasure(2*((webServiceResult.getPrecision()*webServiceResult.getRecall())/(webServiceResult.getPrecision()+webServiceResult.getRecall())));
        }
    }
    
    /**
     * Iterates through all given wsdl files and counts their keywords hits.
     * For each wsdl file, a WebServiceresult is added into the given WebServiceResult list.
     * @param webServiceResults The list of WebServiceResults where the results are added to.
     * @param keyWords The keyWords used to compare the WebService description.
     * @param selectedWsdlFiles The list of URLs to the wsdl files.
     */
    private static void countKeywordHits(LinkedList<WebServiceResult> webServiceResults, String[] keyWords, String[] selectedWsdlFiles){    	
        for (String wsdlFile : selectedWsdlFiles) {
        	try{
	            int hits = 0;	            
	            String description = WSDLReader.getWebServiceDescription(wsdlFile);					
				for(String keyword:keyWords){
					if(description.contains(keyword))
						hits++;
				}	
	            webServiceResults.add(new WebServiceResult(wsdlFile, hits));
	            
        	}catch (ParserConfigurationException | SAXException | IOException e) {
				JOptionPane.showMessageDialog(null, "Unter \""+wsdlFile+"\" findet sich keine WSDL-Datei. Der Eintrag wird ignoriert!", "Fehlerhafter Webdienst", JOptionPane.INFORMATION_MESSAGE);
			}
        } 
    }
    
    /**	 
	 * @return A Comparator to sort the WebService-Ranking Vector. 
	 * Technically, the amount of matching keywords (hits) are compared.
	 */
	private static Comparator<WebServiceResult> getWebServiceRankingComparator(){
		return new Comparator<WebServiceResult>(){
			@Override
			public int compare(WebServiceResult first, WebServiceResult second) {				
				return first.getHits()-second.getHits();
			}			
		};
	}        
}
