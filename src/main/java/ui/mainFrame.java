package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import logic.BPMNReader;
import net.iharder.dnd.FileDrop;

public class mainFrame extends JFrame {

	/**
	 * auto generated uid
	 */
	private static final long serialVersionUID = -2240642325129824098L;
	
	private JPanel mainContentPanel;
	private JScrollPane activitiesPanel;
	private JPanel dropPanel;
	private JLabel instructions;
	private JSeparator separator;
	
	private WebServicesPanel webServicesPanel;
	
	
	
	public mainFrame(){
		separator = new JSeparator(SwingConstants.VERTICAL);
		dropPanel = new JPanel();
		mainContentPanel = new JPanel();		
		mainContentPanel.setLayout(null);
		instructions = new JLabel();
		activitiesPanel = new JScrollPane(dropPanel);		
		webServicesPanel = new WebServicesPanel();
		
		activitiesPanel.setBounds(280, 300, 500, 457);		
		mainContentPanel.add(separator);
		separator.setBounds(275, 0, 2, 800);
		mainContentPanel.add(dropPanel);
		dropPanel.setBounds(280, 300, 500, 457);
		dropPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		mainContentPanel.add(instructions);
		instructions.setText("<html><body>Anleitung<br><br><br>1. Ziehe eine .bpmn oder .xml Datei in das untenstehende Feld. <br><br>2. Es werden dir im unteren Feld alle erkannten Aktivitäten vorgestellt. Klicke eine Aktivität an, um nach passenden Webdiensten zu suchen. <br><br>3. Im linken Fenster werden dir passende Webdienste angezeigt.</body></html>");
		instructions.setBounds(290, 3, 500, 200);
		webServicesPanel.setBounds(0, 0, 275, 800);
		mainContentPanel.add(webServicesPanel);		
		
		dropPanel.setBackground(Color.WHITE);
		new  FileDrop( dropPanel, new FileDrop.Listener()
		  {   public void  filesDropped( java.io.File[] files )
		      { 
			  	if(!files[0].getName().endsWith(".bpmn") && !files[0].getName().endsWith(".xml")){
			  		JOptionPane.showMessageDialog(null, "Es können nur .bpmn oder .xml Dateien verarbeitet werden !", "Ungültiges Dateiformat !", JOptionPane.INFORMATION_MESSAGE);
			  	}else{			 
			  		dropPanel.removeAll();
			  		LinkedList<String> activities = BPMNReader.getTasks(files[0].getAbsolutePath());			  		
			  		for(int i = 0;i<activities.size();i++){	
			  			JButton b = new JButton(activities.get(i));
			  			final String task = activities.get(i);
			  			b.addActionListener(new ActionListener(){
			  				public void actionPerformed(ActionEvent e){
								webServicesPanel.searchForWebservice(BPMNReader.getKeyWordsFromTask(task));								
			  				}
			  			});
			  			dropPanel.add(b);
			  			b.setPreferredSize(new Dimension(400,50));
			  			b.setLocation(10, 10+(i*60));
			  			
			  		}
			  	}		          
		      }   
		  }); 
		
		
		
		setSize(800,800);
		setTitle("WebService Finder");
		add(mainContentPanel);
		
		setLocationRelativeTo(null);		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
