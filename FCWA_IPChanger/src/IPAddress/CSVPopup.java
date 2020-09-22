package IPAddress;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
//import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * popup window for the import/export csv functionality
 */
@SuppressWarnings("serial") 
public class CSVPopup extends JFrame implements Popup, ActionListener{

	private JFrame mrFrame;
	private JPanel panel = new JPanel();
	private JPanel impPanel = new JPanel();
	private JPanel expPanel = new JPanel();
	private JLabel importLab = new JLabel();
	private JLabel exportLab = new JLabel();
	private JButton impButton = new JButton("Import");
	private JButton expButton = new JButton("Export");
	private JButton mButton = new JButton("Merge");
	private JButton rButton = new JButton("Replace");
	private JLabel filename1 = new JLabel();
	private JLabel filename2 = new JLabel();
	private JFileChooser chooser;
	private String path;
	private SiteData currSites;
	
	
	public CSVPopup(SiteData sites) {
		currSites = sites;
		createAndShowPopup();
	}


	public void createAndShowPopup() {
		setTitle("Import/Export CSV File");
		
		add(panel);
		addComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void addComponents() {
		//component properties
		importLab.setText("Import a CSV File to populate site data");
		exportLab.setText("Export a CSV File from the current site data");
		importLab.setAlignmentX(Component.LEFT_ALIGNMENT);
		impButton.addActionListener(this);
		expButton.addActionListener(this);
		
		//merge/replace frame
		mrFrame = new JFrame();
		JPanel mrPanel = new JPanel();
		mButton = new JButton("Merge");
		rButton = new JButton("Replace");
		JLabel question1 = new JLabel("Would you like to merge or replace the old file with this one?");
		question1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		mrPanel.add(mButton);
		mrPanel.add(rButton);
		mrFrame.add(question1, BorderLayout.NORTH);
		mrFrame.add(mrPanel);
		mrFrame.pack();
		mrFrame.setLocationRelativeTo(null);
		mrFrame.setVisible(false);

		//import panel
		impPanel.add(importLab);
		impPanel.add(impButton);
		impPanel.add(filename1);
		impPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		//export panel
		expPanel.add(exportLab);
		expPanel.add(expButton);
		expPanel.add(filename2);
		expPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//parent panel
		panel.add(impPanel);
		panel.add(expPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	/**
	 * Import a new CSV file
	 */
	private void importFile() {
		chooser = new JFileChooser();
		
		//file chooser properties
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv","csv");
		chooser.addChoosableFileFilter(filter);
		chooser.setDialogTitle("Select a file to import");

		//choosing a file 
		int returnVal = chooser.showOpenDialog(CSVPopup.this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			path = selected.getAbsolutePath();
			
			if(currSites.isEditable()) {	//if the file doesn't exist, the merge/replace screen is bypassed
				mrFrame.setVisible(true);
			} else { 	//file is imported as a replacement
				processImport(true);
			}
		}
	}
	
	/**
	 * Processes the imported file with the current SiteData instance
	 * 
	 * @param replace	Replaces the file if True and merges if False
	 */
	private void processImport(boolean replace) {
		if(!currSites.isValidCSV(path)) {
			JOptionPane.showMessageDialog(null, "CSV file is invalid. Please check data and try again.", "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
		} else {
			if (replace) {								// replace the current csv
				currSites.replaceSites(path);

			} else {									// merge the 2 csv files
				SiteData localSites = new SiteData(path);
				localSites.sites.forEach((key,value) -> {
					try {
						if (!currSites.keyExists(key)) currSites.addSite(key, value);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
			
			currSites.setEditable(true);
			GUI.enableEditButtons();
			GUI.refreshDropdown();
			
			//import confirmation screen
			JLabel lab = new JLabel("File has been imported.");
			lab.setHorizontalAlignment(SwingConstants.CENTER);
			JOptionPane.showMessageDialog(null, lab, "Import confirmation", JOptionPane.PLAIN_MESSAGE);

			dispose();	//closes this popup
		}
	}
	
	/**
	 * Exports the current CSV file to the users device
	 */
	private void exportFile()	{
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select file location and name");

		//choosing where to put file + file name
		int returnVal = chooser.showSaveDialog(CSVPopup.this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File directory = chooser.getSelectedFile();
			path = directory.getAbsolutePath();
			currSites.exportFile(path);
			//export confirmation screen
			JLabel lab = new JLabel("File has been exported.");
			lab.setHorizontalAlignment(SwingConstants.CENTER);
			JOptionPane.showMessageDialog(null, lab, "Export confirmation", JOptionPane.PLAIN_MESSAGE);
			
			dispose();	//closes this popup
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		chooser = new JFileChooser();
	
		//action for when import button is pushed
		if(e.getSource() == impButton) importFile();
		
		//action for when export button is pushed
		if(e.getSource() == expButton) exportFile();
		
		mButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processImport(false);
				mrFrame.setVisible(false);
		}});
		
		rButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processImport(true);
				mrFrame.setVisible(false);	
				
		}});		
	}


	//Testing------------------------------------------------------------------------------------------------------//

	public static void main(String[] args) {
		
		new CSVPopup(new SiteData("list.csv"));
	}
}
