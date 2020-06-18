
import java.awt.BorderLayout; 
import java.awt.Component;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class CSVPopup extends JFrame implements Popup, ActionListener{

	//swing variables
	private JPanel panel = new JPanel();
	private JPanel impPanel = new JPanel();
	private JPanel expPanel = new JPanel();
	private JLabel importLab = new JLabel();
	private JLabel exportLab = new JLabel();
	private JButton impButton = new JButton("Import");
	private JButton expButton = new JButton("Export");
	private JLabel filename1 = new JLabel();
	private JLabel filename2 = new JLabel();
	private JFileChooser chooser;
	private String name, path;
	private SiteData currSites;
	
	
	public CSVPopup(SiteData sites) {
		currSites = sites;
		createAndShowPopup();
	}

	public void createAndShowPopup() {
		//frame properties
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

		//add to panels
		//to do: format it nicely
		impPanel.add(importLab);
		impPanel.add(impButton);
		impPanel.add(filename1);
		impPanel.setAlignmentX(LEFT_ALIGNMENT);

		expPanel.add(exportLab);
		expPanel.add(expButton);
		expPanel.add(filename2);
		expPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(impPanel);
		panel.add(expPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

	}


	/**
	 * action events of the buttons
	 */
	public void actionPerformed(ActionEvent e) {
		chooser = new JFileChooser();
		JButton m;
		JButton r;

		//action for when import button is pushed
		//to do: add the extra popup
		if(e.getSource() == impButton) {

			//file chooser properties
			chooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv","csv");
			chooser.addChoosableFileFilter(filter);
			chooser.setDialogTitle("Select a file to import");

			//choosing a file 
			int returnVal = chooser.showOpenDialog(CSVPopup.this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				//add popup that asks if user wants to merge or replace, then a popup asking if they're sure
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				m = new JButton("Merge");
				r = new JButton("Replace");
				JLabel question1 = new JLabel("Would you like to merge or replace the old file with this one?");
				panel.add(m);
				panel.add(r);
				frame.add(question1);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				//maybe add a popup class to just create these things
				
				File selected = chooser.getSelectedFile();
				path = selected.getAbsolutePath();
				name = selected.getName();
				filename1.setText(name);
				pack();
			}
		}
		//action for when export button is pushed
		if(e.getSource() == expButton) {
			BufferedWriter bw = null;
			FileWriter fw = null;
			//file chooser properties
			chooser.setDialogTitle("Select file location and name");

			//choosing where to put file + file name
			int returnVal = chooser.showSaveDialog(CSVPopup.this);
			//action for if "save" is clicked
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File directory = chooser.getSelectedFile();
				path = directory.getAbsolutePath();
				currSites.exportFile(path);
			}
		}
	}
	
	//Testing------------------------------------------------------------------------------------------------------//

	public static void main(String[] args) {
		//new CSVPopup(currSites);
	}
}
