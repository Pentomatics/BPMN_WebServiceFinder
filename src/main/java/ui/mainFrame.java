package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import logic.BPMNReader;
import net.iharder.dnd.FileDrop;

public class mainFrame extends JFrame {

	/**
	 * auto generated uid
	 */
	private static final long serialVersionUID = -2240642325129824098L;
	
	private WebServicesPanel webServicesPanel;
	
	
	public mainFrame(){
		JPanel mainContentPanel = new JPanel();		
		mainContentPanel.setLayout(null);	
		mainContentPanel.setBackground(new Color(255,253,242));
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(Color.DARK_GRAY);		
		separator.setBounds(275, 5, 2, 750);
		mainContentPanel.add(separator);
			
		webServicesPanel = new WebServicesPanel();
		webServicesPanel.setBounds(0, 0, 275, 800);
		mainContentPanel.add(webServicesPanel);			
		
		mainContentPanel.add(getDropPanel());		
		mainContentPanel.add(getInstructionsPanel());
				
		setSize(800,800);
		setTitle("WebService Finder");
		add(mainContentPanel);		
		
		setLocationRelativeTo(null);		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Creates the JPanel that is used to list all activities of the .bpmn file
	 * that is dragged into it.
	 * All required handling (such as button clicks etc.)  is added to this JPanel.
	 * @return
	 */
	private JPanel getDropPanel(){
		JPanel dropPanel = new JPanel();
		dropPanel.setBackground(Color.WHITE);
		dropPanel.setBounds(280, 300, 500, 457);
		dropPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		
		new  FileDrop( dropPanel, new FileDrop.Listener()
		  {   public void  filesDropped( java.io.File[] files )
		      { 
			  	if(!files[0].getName().endsWith(".bpmn") && !files[0].getName().endsWith(".xml")){
			  		JOptionPane.showMessageDialog(null, "Es können nur .bpmn oder .xml Dateien verarbeitet werden !", "Ungültiges Dateiformat !", JOptionPane.INFORMATION_MESSAGE);
			  	}else{			 
			  		dropPanel.removeAll();
			  		String[] activities = BPMNReader.getTasks(files[0].getAbsolutePath());

			  		for(int i = 0; i < activities.length ; i++){
			  			JButton b = new JButton(activities[i]);
			  			b.setBackground(Color.WHITE);
			  			b.setForeground(Color.DARK_GRAY);
			  			final String task = activities[i];
			  			b.addActionListener(new ActionListener(){
			  				public void actionPerformed(ActionEvent e){			  					
								webServicesPanel.searchForWebservice(task);								
			  				}
			  			});
			  			dropPanel.add(b);
			  			b.setPreferredSize(new Dimension(400,50));
			  			b.setLocation(10, 10+(i*60));
			  		}
			  	}		          
		      }   
		  }); 
		return dropPanel;
	}
	
	/**
	 * Creates the instructions as a JPanel.
	 * Internally those instructions are splitted into a JLabel for the title,
	 * a JLabel for the actual instructions as well as a JLabel for the credits.
	 * @return The created JPanel
	 */
	private JPanel getInstructionsPanel(){
		JPanel instructionsPanel = new JPanel();
		instructionsPanel.setLayout(null);
		instructionsPanel.setBackground(new Color(255,253,242));		
		instructionsPanel.add(getInternalInstructionsLabel("Anleitung", 30, Font.PLAIN, 15, 15, 500, 40));	
		instructionsPanel.add(getInternalInstructionsLabel("<html><body>1. Ziehe eine .bpmn oder eine entsprechende.xml Datei in das untere Feld. <br><br>2. Wähle links die zu durchsuchenden WebServices aus. <br><br>3. Wähle unten die Aktivität aus, zu der WebServices gesucht werden sollen.</body></html>", 14, Font.PLAIN, 20, 50, 400, 200));	
		instructionsPanel.add(getInternalInstructionsLabel("<html><body>Steffen Brehm, Max Soest, Rico Zieger <br> Umgesetzt im Rahmen von IGT, der wahrscheinlich zweitbesten Vorlesung der Welt</body></html>", 10, Font.BOLD, 20, 260, 450, 40));		
		instructionsPanel.setBounds(290, 3, 500, 300);
		return instructionsPanel;
	}
	
	/**
	 * Helper method to create the instruction-panel with much less redundant code	
	 */
	private JLabel getInternalInstructionsLabel(String text, int fontsize, int fontstyle, int x, int y, int width, int height){
		JLabel label = new JLabel(text);
		label.setForeground(Color.DARK_GRAY);		
		label.setFont(new Font("FELIXTI", fontstyle, fontsize));
		label.setBackground(new Color(255,253,242));		
		label.setBounds(x, y, width, height);
		return label;
	}

}
