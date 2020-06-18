
import java.awt.Component;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial") 
public class CSVPopup extends JFrame implements Popup, ActionListener{

	//swing variables
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
	
	/**
	 * The constructor for the CSVPopup
	 * 
	 * @param sites
	 */
	public CSVPopup(SiteData sites) {
		currSites = sites;
		createAndShowPopup();
	}

	/**
	 * Creates the main frame
	 */
	public void createAndShowPopup() {
		//frame properties
		setTitle("Import/Export CSV File");
		
		add(panel);
		addComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initializes all of the components
	 */
	public void addComponents() {
		//component properties
		importLab.setText("Import a CSV File to populate site data");
		exportLab.setText("Export a CSV File from the current site data");
		importLab.setAlignmentX(Component.LEFT_ALIGNMENT);
		impButton.addActionListener(this);
		expButton.addActionListener(this);
		
		//merge/replace frame
		mrFrame = new JFrame();
		JPanel panel = new JPanel();
		mButton = new JButton("Merge");
		rButton = new JButton("Replace");
		JLabel question1 = new JLabel("Would you like to merge or replace the old file with this one?");
		panel.add(mButton);
		panel.add(rButton);
		mrFrame.add(question1);
		mrFrame.add(panel);
		mrFrame.pack();
		mrFrame.setLocationRelativeTo(null);
		mrFrame.setVisible(false);

		//add to panels
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

		//action for when import button is pushed
		if(e.getSource() == impButton) importFile();
		
		//action for when export button is pushed
		if(e.getSource() == expButton) exportFile();
		
		mButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processImport(false);
				mrFrame.setVisible(false);	
				setVisible(false);
		}});
		
		rButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processImport(true);
				mrFrame.setVisible(false);	
		}});		
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
			mrFrame.setVisible(true);
			File selected = chooser.getSelectedFile();
			path = selected.getAbsolutePath();
			pack();
		}
	}
	
	/**
	 * Processes the imported file with the current SiteData instance
	 * 
	 * @param replace	Replaces the file if True and merges if False
	 */
	private void processImport(boolean replace) {
		if (replace) {								// replace the current csv
			new GUIFrame(path);
		} else {									// merge the 2 csv files
			SiteData localSites = new SiteData(path);
			localSites.sites.forEach((key,value) -> {
				try {
					if (!currSites.keyExists(key)) currSites.addSite(key, value);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			new GUIFrame(currSites.getCurrentFile());
		}
	}
	
	/**
	 * Exports the current CSV file to the users device
	 */
	private void exportFile()	{
		chooser = new JFileChooser();
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
	//Testing------------------------------------------------------------------------------------------------------//

	public static void main(String[] args) {
		//new CSVPopup(currSites);
	}
}
