package logic;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        
        for(WebServiceResult webServiceResult : webServiceResults){
        	webServiceResult.setRecall((double)validResults/(double)amountOfSearchedWebServices);
        	webServiceResult.setPrecision((double)webServiceResult.getHits()/(double)amountOfKeywords);
        	webServiceResult.setFmeasure(2.0d*((webServiceResult.getPrecision()*webServiceResult.getRecall())/(webServiceResult.getPrecision()+webServiceResult.getRecall())));
        }
        
        webServiceResults.sort(getWebServiceRankingComparator());        
        
        while(webServiceResults.size()> displayableResults){
        	webServiceResults.removeLast();
        }        
        
    }
    
    /**
     * Iterates through all operations of all given wsdl files and counts their keywords hits.
     * For each wsdl file, a WebServiceResult is added into the given WebServiceResult list.
     * Therefore the operations with the most hits is used to represent this WebService.
     * @param webServiceResults The list of WebServiceResults where the results are added to.
     * @param keyWords The keyWords used to compare the WebService description.
     * @param selectedWsdlFiles The list of URLs to the wsdl files.
     */
    private static void countKeywordHits(LinkedList<WebServiceResult> webServiceResults, String[] keyWords, String[] selectedWsdlFiles){    	
        for (String wsdlFile : selectedWsdlFiles) {
        	try{           
	            
	            String[] descriptions = WSDLReader.getWebServiceOperations(wsdlFile);	
	            WebServiceResult[] services = new WebServiceResult[descriptions.length];
	            
	            //count keyword occurrences for all operations of this WebService
	            for(int i = 0; i < descriptions.length; i++){
	            	int hits = 0;
	            	for(String keyword:keyWords){
	            		if(containsWholeWord(descriptions[i], keyword))
	            			hits++;
	            	}
	            	services[i] = new WebServiceResult(wsdlFile, hits);
	            }
				
	            //use the operation with the most hits for this WebService
	            int index = 0;
	            for(int x = 0; x < services.length; x++){
	            	if(services[x].getHits()>services[index].getHits())
	            		index=x;
	            }
	            webServiceResults.add(services[index]);
	            
        	}catch (ParserConfigurationException | SAXException | IOException e) {
				JOptionPane.showMessageDialog(null, "Unter \""+wsdlFile+"\" findet sich keine WSDL-Datei. Der Eintrag wird ignoriert!", "Fehlerhafter Webdienst", JOptionPane.INFORMATION_MESSAGE);
			}
        } 
    }
    
    /**
     * Checks if a given word occurs in a given string. 
     * Other than string.contains() this will only return true if the entire
     * word as it is is contained. 
     * If sentence is "Hello" and word is "ello", this method return false while
     * String.contains() would return true
     * @param sentence The string to be searched
     * @param word The word to be searched
     * @return true, if there is any occurrence of this word in the string and false if not
     */
    private static boolean containsWholeWord(String sentence, String word){
        String pattern = word;
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(sentence);
        return m.find();
   }
    
    /**	 
	 * @return A Comparator to sort the WebService-Ranking Vector. 
	 * Technically, the f-measure of WebServiceResults are compared.
	 * NOTE THAT this sorts in reverse order to create a ranking with
	 * the WebServiceResult holding the biggest f-measure in first place
	 */
	private static Comparator<WebServiceResult> getWebServiceRankingComparator(){
		return new Comparator<WebServiceResult>(){
			@Override
			public int compare(WebServiceResult first, WebServiceResult second) {	
				if(first.getFmeasure()<second.getFmeasure())
					return 1;
				else if(first.getFmeasure()>second.getFmeasure())
					return -1;
				else 
					return 0;				
			}			
		};
	}        
}
