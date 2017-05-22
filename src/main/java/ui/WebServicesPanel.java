package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GrayFilter;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
			//for test purposes
			//have to be replaced with our webservices
			{"http://www.webservicex.com/globalweather.asmx?WSDL",new Boolean(true)},
			{"http://ws.cdyne.com/ip2geo/ip2geo.asmx?wsdl",new Boolean(true)}
	};
	
	private Object[] tableHeader = {
		"Webservice-URL",
		"Aktiv"
	};
	
	@SuppressWarnings("serial")
	public WebServicesPanel(){		
		setLayout(null);
		
		tableTitle = new JLabel("Durchsuchte Webdienste");		
		DefaultTableModel model = new DefaultTableModel(predefinedTableData, tableHeader);
        wsdlTable = new JTable(model) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
		tableScrollPane = new JScrollPane(wsdlTable);
		webServiceResultsTitle = new JLabel("Passende Webdienste");
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
		
	}

	public void searchForWebservice(String[] keywords){
		Vector<int[]> webServiceRanking = new Vector<int[]>();
		
		for(int i =0; i<wsdlTable.getRowCount();i++){
			if((boolean) wsdlTable.getValueAt(i, 1)){
				try {					
					int[] ranking = {i,0};
					String description = getWebServiceDescription((String)wsdlTable.getValueAt(i, 0));					
					for(String keyword:keywords){
						if(description.contains(keyword))
							ranking[1]++;
					}
					webServiceRanking.add(ranking);
				} catch (ParserConfigurationException | SAXException | IOException e) {
					JOptionPane.showMessageDialog(null, "Unter \""+(String)wsdlTable.getValueAt(i, 0)+"\" findet sich keine WSDL-Datei. Der Eintrag wird ignoriert!", "Fehlerhafter Webdienst", JOptionPane.INFORMATION_MESSAGE);
				}
			}			
		}
		
		Comparator<int[]> c = new Comparator<int[]>(){
			@Override
			public int compare(int[] first, int[] second) {				
				return first[1]-second[1];
			}			
		};
		webServiceRanking.sort(c);
		
		webServiceResults.removeAll();
		//a maximum of 3 best fitting webservices are displayed
		for(int i=0;i<webServiceRanking.size() && i<3;i++){			
			double recall = (webServiceRanking.get(i)[1])/keywords.length;
			double precision = (webServiceRanking.get(i)[1])/keywords.length;
			double fmeasure = 2*((precision*recall)/(precision+recall));
			String webserviceName = wsdlTable.getValueAt((webServiceRanking.get(i)[0]), 0).toString().substring(0, wsdlTable.getValueAt((webServiceRanking.get(i)[0]), 0).toString().indexOf(".asmx"));
			webserviceName = webserviceName.substring(webserviceName.lastIndexOf("/")+1);
			JLabel webserviceLink = new JLabel("<html><body><a href=\""+wsdlTable.getValueAt((webServiceRanking.get(i)[0]), 0)+"\" >"+webserviceName+"</a></body></html>");
			JLabel webserviceResult = new JLabel("<html><body>Recall:"+recall+"<br>Precision:"+precision+"<br>F-measure:"+fmeasure+"</body></html>");
			JPanel webservicePanel = new JPanel();
			webservicePanel.setLayout(null);
			webserviceResult.setBackground(Color.WHITE);
			webserviceLink.setBackground(Color.WHITE);
			webservicePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			webserviceLink.setBounds(0,0,245,20);
			webserviceResult.setBounds(0,20,245,70);
			webservicePanel.add(webserviceLink);
			webservicePanel.add(webserviceResult);
			JScrollPane sp = new JScrollPane(webservicePanel);
			webservicePanel.setBounds(10, 10+(i*100), 245, 90);			
			sp.setBounds(10, 10+(i*100), 245, 90);
			webserviceResult.setOpaque(true);
			webserviceLink.setOpaque(true);
			webserviceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			String link = wsdlTable.getValueAt((webServiceRanking.get(i)[0]), 0).toString();
			webserviceLink.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			        try {
			        	Desktop.getDesktop().browse(new URI(link));
			        } catch (URISyntaxException | IOException ex) {
			            ex.printStackTrace();
			        }
			    }
			});
			webServiceResults.add(sp);
			webServiceResults.repaint();
			System.out.println("Platz "+(i+1)+" mit "+webServiceRanking.get(i)[1]+" Übereinstimmungen ist der Webdienst Nr. "+(webServiceRanking.get(i)[0]+1));
		}
		
	}
	
	private String getWebServiceDescription(String url) throws ParserConfigurationException, MalformedURLException, SAXException, IOException{			
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(url).openStream());
		Element rootElement = doc.getDocumentElement();		
		NodeList taskNodes = rootElement.getElementsByTagName("wsdl:documentation");		

		return taskNodes.item(0).getTextContent();
		
	}
	
}
