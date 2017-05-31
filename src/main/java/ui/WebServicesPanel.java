package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logic.WebServiceFinder;
import logic.WebServiceResult;

public class WebServicesPanel extends JPanel {
	
	/**
	 * auto generated uid
	 */
	private static final long serialVersionUID = 6968306191509662997L;
	
	private JScrollPane tableScrollPane;
	private JTable wsdlTable;
	private JLabel tableTitle;
	private JLabel webServiceResultsTitle;
	private JPanel webServiceResults;
	private JButton addWebServiceUrl;
	
	private Object[][] predefinedTableData = {
			//For test purposes
			//TODO: Have to be replaced with our webservices
			{"http://www.webservicex.com/globalweather.asmx?WSDL",new Boolean(true)},
			{"http://ws.cdyne.com/ip2geo/ip2geo.asmx?wsdl",new Boolean(true)},
			{"http://localhost:8080/CustomerProvider_war_exploded/CustomerSalesService?wsdl", new Boolean(true)},
			{"http://localhost:8080/CustomerProvider_war_exploded/PeerGroupSalesService?wsdl", new Boolean(true)},
			{"http://localhost:8080/CustomerProvider_war_exploded/CustomerService?wsdl", new Boolean(true)}
	};
	
	private Object[] tableHeader = {
		"Webservice-URL",
		"Aktiv"
	};
	
	@SuppressWarnings("serial")
	public WebServicesPanel(){		
		setLayout(null);
		
		tableTitle = new JLabel("Durchsuchte Webdienste");
		tableTitle.setForeground(Color.DARK_GRAY);
		DefaultTableModel model = new DefaultTableModel(predefinedTableData, tableHeader);
        wsdlTable = new JTable(model) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        wsdlTable.setFont(new Font("FELIXTI", Font.PLAIN, 12));
        wsdlTable.setForeground(Color.DARK_GRAY);        
		tableScrollPane = new JScrollPane(wsdlTable);
		webServiceResultsTitle = new JLabel("Passende Webdienste");
		webServiceResultsTitle.setForeground(Color.DARK_GRAY);
		webServiceResults = new JPanel();
		webServiceResults.setLayout(null);
		addWebServiceUrl = new JButton("hinzufügen");		
		
		tableTitle.setBounds(70, 15, 200, 50);
		add(tableTitle);
		tableScrollPane.setBounds(5,50,265,180);	
		add(tableScrollPane);
		wsdlTable.setFillsViewportHeight(true);
		webServiceResultsTitle.setBounds(70, 260, 200, 50);
		add(webServiceResultsTitle);		
		webServiceResults.setBounds(5, 300, 265, 456);
		webServiceResults.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		add(webServiceResults);
		addWebServiceUrl.setBounds(169,230,100,20);
		add(addWebServiceUrl);
		
		addWebServiceUrl.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] newRow = {"URL hier eingeben", new Boolean(true)};
				model.addRow(newRow);				
			}	
		});
		
		setBackground(new Color(255,253,242));
		
	}

	public void searchForWebservice(String task){
		LinkedList<String> activeWebServices = new LinkedList<String>();
		for(int i =0; i<wsdlTable.getRowCount();i++){
			if((boolean) wsdlTable.getValueAt(i, 1)){
				activeWebServices.add(wsdlTable.getValueAt(i, 0).toString());
			}
		}
		String[] webServices = new String[activeWebServices.size()]; 
		LinkedList<WebServiceResult> webServicesResults = WebServiceFinder.findAppropriateWebServicesForTask(task, activeWebServices.toArray(webServices));		
		webServiceResults.removeAll();	
		webServiceResults.repaint();
		
		for (int i = 0; i < webServicesResults.size(); i++) {
			WebServiceResult webServiceResult = webServicesResults.get(i);
			displayWebServiceResult(webServiceResult.getName(), webServiceResult.getURL(), webServiceResult.getRecall(),
					webServiceResult.getPrecision(), webServiceResult.getFmeasure(), i);					
		}	
		webServiceResults.repaint();
	}
		
	/**
	 * Creates the upper half of the visual representation for a WebService result
	 * @param WebServiceName The name of the WebService to be displayed
	 * @param WebServiceUrl The URL to the wsdl-file
	 * @param rankingIndex The rank of this WebService (best match = 0 (first index position)
	 * @return
	 */
	private JLabel createLinkLabel(String WebServiceName, String WebServiceUrl, int rankingIndex){
		JLabel webserviceLink = new JLabel("<html><body><a href=\""+WebServiceUrl+"\" >"+WebServiceName+"</a></body></html>");
		webserviceLink.setBackground(Color.WHITE);
		webserviceLink.setBounds(10,0,235,20);		
		webserviceLink.setOpaque(true);
		webserviceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));			
		webserviceLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(WebServiceUrl));
				} catch (URISyntaxException | IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		return webserviceLink;
	}
	
	/**
	 * Creates the lower half of the visual representation for a WebService result
	 * @param recall The recall for this WebService
	 * @param precision The precision for this WebService
	 * @param fmeasure The F-measure for this WebService
	 * @param rankingIndex The rank of this WebService (best match = 0 (first index position)
	 * @return A formatted JLabel containing/displaying the given information
	 */
	private JLabel createResultLabel(double recall, double precision, double fmeasure, int rankingIndex){
		JLabel webserviceResult = new JLabel("<html><body>Recall:"+recall+"<br>Precision:"+precision+"<br>F-measure:"+fmeasure+"</body></html>");
		webserviceResult.setBackground(Color.WHITE);
		webserviceResult.setBounds(15,20,230,70);
		webserviceResult.setOpaque(true);
		return webserviceResult;
	}
	
	/**
	 * Creates a visual representation for the WebService result on the bottom left corner of the screen.
	 * @param WebServiceName The name of the WebService to be displayed
	 * @param WebServiceUrl The URL to the wsdl-file
	 * @param recall The recall for this WebService
	 * @param precision The precision for this WebService
	 * @param fmeasure The F-measure for this WebService
	 * @param rankingIndex The rank of this WebService (best match = 0 (first index position)
	 */
	private void displayWebServiceResult(String WebServiceName, String WebServiceUrl, double recall, double precision, double fmeasure, int rankingIndex){
		JPanel webservicePanel = new JPanel();
		webservicePanel.setLayout(null);			
		webservicePanel.add(createLinkLabel(WebServiceName, WebServiceUrl, rankingIndex));		
		webservicePanel.add(createResultLabel(recall, precision, fmeasure, rankingIndex));
		webservicePanel.setBackground(Color.WHITE);
		
		JScrollPane sp = new JScrollPane(webservicePanel);
		webservicePanel.setBounds(10, 10+(rankingIndex*100), 245, 90);			
		sp.setBounds(10, 10+(rankingIndex*100), 245, 90);		
		
		webServiceResults.add(sp);
	}	
}
