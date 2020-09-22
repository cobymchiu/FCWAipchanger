package IPAddress;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SiteData {
	
	//site data storage
	public HashMap<String,String> sites;
	public HashMap<String,String> newSites = new HashMap<String,String>();
	private String currentFile;
	private boolean editable = true;

	/**
	 * constructor sets name of current file and reads in the csv file 
	 * @param fileName name of current file to read/write data to
	 */
	public SiteData(String fileName) {
		sites = new HashMap<String, String>();
		this.currentFile = fileName;
		readCSV(currentFile);
	}

	
	/**
	 * reads in csv file
	 */
	public void readCSV(String filename) {
		BufferedReader reader = null;
		String line = "";
		
		//reads each non-null line of the file then adds the data to the hashmap as key-value pairs	
		try {
			reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if(data.length>0) {
					sites.put(data[0], data[1]);
				}
			}
			reader.close();
		} catch (FileNotFoundException ef) {
			fileNotFound();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writes data to csv file
	 * 
	 * @param filename name of file writing to
	 * if filename already exists, it overwrites it. otherwise creates a new file with that name
	 */
	public void writeCSV(String filename) {
		String eol = System.getProperty("line.separator");
		
		try(FileWriter writer = new FileWriter(filename)) {
			for(Map.Entry<String, String> entry: sites.entrySet()) {
				writer.append(entry.getKey())
					  .append(',')
					  .append(entry.getValue())
					  .append(eol);
			}
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
	
	/**
	 * adds new site to end of csv file and then to the main hashmap
	 * 
	 * @param filename name of file to be appended to
	 * @throws IOException
	 */
	public void appendCSV(String filename) throws IOException{
		String eol = System.getProperty("line.separator");
		FileWriter writer = new FileWriter(filename, true);
		
		if(!newSites.isEmpty()) {
			for(Map.Entry<String, String> entry: newSites.entrySet()) {
				writer.append(entry.getKey())
					  .append(',')
					  .append(entry.getValue())
					  .append(eol);
				sites.put(entry.getKey(), entry.getValue());
			}
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * validates a csv file
	 * checks for # of columns and valid IP address
	 * 
	 * @param filepath	CSV file absolute path
	 */
	public boolean isValidCSV(String filepath) {
		BufferedReader reader = null;
		String line = "";
		
		try {
			reader = new BufferedReader(new FileReader(filepath));
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if(data.length != 2) {		//check for only 2 columns
					return false;
				} else
					if(!isValidIP(data[1])) {
						return false;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	/**
	 * If the file path is not found, then the default list
	 * will be used and the user
	 */
	private void fileNotFound() {
		System.out.println("File Not Found");
		sites = getDefaultList();
		editable = false;
		
		// Alert the user that the file was not found
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame alert = new JFrame();
		alert.setUndecorated(true); 
		alert.setPreferredSize(new Dimension(450,110));
		alert.setLocation((int)screenSize.getWidth()/2-225, (int)screenSize.getHeight()/2-55);
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		alert.add(top, BorderLayout.NORTH);
		alert.add(bottom, BorderLayout.SOUTH);
		JLabel message = new JLabel("The file "+ currentFile +" was not found "
				+ "and the default sites have been applied.");
		JLabel message2 = new JLabel("You can manually import a .csv file if necessary.");
		message2.setHorizontalAlignment(JLabel.CENTER);
		JButton okButton = new JButton("OK");
		top.add(message);
		alert.add(message2);
		bottom.add(okButton);
		//alert.setLocationRelativeTo(null);
		alert.pack();
		alert.setVisible(true);
		
		// OK button to close the alert
		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alert.setVisible(false);
		}});
	}

	/**
	 * gets a list of the default site data
	 * @return hashmap of site locations and ip addresses
	 */
	private HashMap<String,String> getDefaultList(){
		
		HashMap<String,String> list = new HashMap<String,String>();
		list.put("Pohick Vault", "172.16.28.3");
		list.put("Pohick PS", "172.16.28.13");
		list.put("Stenhouse PRV", "172.16.29.3");
		list.put("Kidwell PS", "172.16.30.3");
		list.put("Bull Run PRV", "172.16.31.3");
		list.put("Gum Springs PS", "172.16.32.3");
		list.put("Backlick PS", "172.16.33.3");
		list.put("Central Area Vault", "172.16.33.13");
		list.put("Central Area Tank", "172.16.33.23");
		list.put("Fox Mill PS", "172.16.34.3");
		list.put("Tyson Corner PS", "172.16.35.3");
		list.put("Fair Oak Tank", "172.16.36.3");
		list.put("Rose Hill PRV", "172.16.37.3");
		list.put("Franconia PRV", "172.16.38.3");
		list.put("Hospital Tank", "172.16.39.3");
		list.put("Beacon Hill Tank", "172.16.40.3");
		list.put("Penderwood 2nd Tank", "172.16.41.3");
		list.put("Fair Oak PS", "172.16.42.3");
		list.put("Fairfax Circle PS", "172.16.43.3");
		list.put("Fordson PS", "172.16.44.3");
		list.put("Fort Belvoir PS", "172.16.45.3");
		list.put("Kings Park PS", "172.16.46.3");
		list.put("Buckley PRV", "172.16.48.3");
		list.put("Reston PS", "172.16.49.3");
		list.put("Beulah PRV", "172.16.52.3");
		list.put("Pope Head PS", "172.16.53.3");
		list.put("Lawyers PRV", "172.16.54.3");
		list.put("Pole Road PRV", "172.16.55.3");
		list.put("South Kings PRV", "172.16.57.3");
		list.put("Dulles South Meter Vault", "172.16.58.3");
		list.put("Huntington Meter Vault", "172.16.59.3");
		list.put("North Chambliss Meter Vault", "172.16.60.3");
		list.put("Oasis Meter Vault", "172.16.61.3");
		list.put("Pole Road Meter Vault", "172.16.62.3");
		list.put("Dale City PS", "172.16.63.3");
		list.put("Southington PS / Reston PRV", "172.16.64.3");
		list.put("Telegraph PS", "172.16.65.3");
		list.put("Chain Brlistge PS", "172.16.66.3");
		list.put("Live Oak PS", "172.16.67.3");
		list.put("Dalecarlia TP", "172.16.68.3");
		list.put("Willston Tank", "172.16.69.3");
		list.put("Willston PS", "172.16.70.3");
		list.put("Seven Corner Tank", "172.16.71.3");
		list.put("Prout Hill Tanks", "172.16.72.3");
		list.put("Falls Hill Tank", "172.16.73.3");
		list.put("Chesterbrook PS", "172.16.74.3");
		list.put("McLean Lewinsvill Tank", "172.16.77.3");
		list.put("McLean PS", "172.16.76.3");
		list.put("Scott Run PS", "172.16.78.3");
		list.put("George Mason PS", "172.16.79.3");
		list.put("Poplar Tank", "172.16.80.3");
		list.put("Dunn Loring PS / Tank", "172.16.82.3");
		list.put("Tyson Route 7 Tank", "172.16.85.3");
		list.put("Williams Tank", "172.16.101.3");
		list.put("GMU Tank", "172.16.102.3");
		list.put("Lyndhurst Tank", "172.16.103.3");
		list.put("Wall Street PRV/Tank", "172.16.104.3"); 
	
		return list;
	}
	 /** rewrites csv file and refreshes the sitedata
	 * @param filename
	 * @throws IOException
	 */
	public void refreshData(String filename) throws IOException{
		writeCSV(filename);
		new SiteData(currentFile);
	}
	/**
	 * checks if ip address is valid
	 * @param ip
	 * @return
	 */
	public boolean isValidIP(String ip) {
		//breaks ip address into parts, returns false if not enough terms
		if(ip.indexOf('.') == -1) {
			return false;
		}
		String p1,p2,p3,p4,a = "";
		try {
			p1 = ip.substring(0,ip.indexOf('.'));
			a = ip.substring(ip.indexOf('.')+1);
			p2 = a.substring(0,a.indexOf('.'));
			a = a.substring(a.indexOf('.')+1);
			p3 = a.substring(0,a.indexOf('.'));
			a = a.substring(a.indexOf('.')+1);
			p4 = a.substring(a.indexOf('.') + 1);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

		//checks the ranges of each part and if they are numbers
		String[] arr = {p1,p2,p3,p4};
		try {
			for(String s: arr) {
				int num = Integer.parseInt(s);

				if(num < 0 || num > 255) {
					return false;
				}
			}
		} catch(NumberFormatException e) {
			return false;
		}

		//checks if first term is 0
		if(Integer.parseInt(p1) == 0) {
			return false;
		}
		return true;
	}
	
	//methods used for changing the list of sites
	/**
	 * adds a new site to the hashmap
	 * @param name name of new site
	 * @param ip new ip address
	 * @throws IOException 
	 */
	public boolean addSite(String name, String ip) throws IOException {
		String result = newSites.put(name, ip);
		appendCSV(currentFile);
		
		if(result != null) return false;
		return true;
		//refreshData(currentFile);
	}
	/**
	 * removes a site from the hashmap
	 * @param name name of site to be removed
	 * @throws IOException 
	 */
	public boolean removeSite(String name) throws IOException {
		String result = sites.remove(name);
		refreshData(currentFile);
		
		if(result == null) return false;	
		return true;
	}
	/**
	 * edits the existing sites
	 * @param oldName
	 * @param oldIp
	 * @param newName
	 * @param newIp
	 * @return
	 * @throws IOException 
	 */
	public boolean editSite(String oldName, String oldIp, String newName, String newIp) throws IOException {
		if(oldName.equals(newName)) {
			if(oldIp.equals(newIp)) {
				return false;
			} else {
				sites.replace(oldName, newIp);
				refreshData(currentFile);
			}
		} else {
			sites.remove(oldName);
			sites.put(newName, newIp);
			refreshData(currentFile);
		}
		
		return true;
	}
	/**
	 * completely rewrites the database file
	 */
	public void replaceSites(String filename) {
		sites = new HashMap<String,String>();
		readCSV(filename);
		writeCSV(currentFile);
	}
	/**
	 * exports the current list of sites to a specified location
	 * @param filePath The path to the exported csv
	 * @return true if successful, false otherwise
	 */
	public boolean exportFile(String filePath) {
		try {
			writeCSV(filePath);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	//getters and setters
	/**
	 * gets site location
	 * @return site location
	 */
	public ArrayList<String> locationToArrayList() {
		ArrayList<String> places = new ArrayList<String>();
		sites.forEach((k,v) -> places.add(k));
		Collections.sort(places);
		return places;
	}
	/**
	 * turns string arraylist into string array
	 * @param list ArrayList of strings
	 * @return array of strings
	 */
	public String[] locationToArray() {
		ArrayList<String> list = locationToArrayList();
		String[] arr = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			arr[i] = list.get(i);
		}
		return arr;
	}
	/**
	 * gets site IP address
	 * @return site IP address
	 */
	public ArrayList<String> getIpAddresses(){
		ArrayList<String> ips = new ArrayList<String>();
		sites.forEach((k,v) -> ips.add(v));
		return ips;
	}
	/**
	 * gets value of hashmap associated with a key
	 * @param key the key 
	 * @return value associated with a
	 */
	public String getValue(String key) {
		return sites.get(key);
	}
	/**
	 * determines if key is in mapping already
	 * @param key
	 * @return
	 */
	public boolean keyExists(String key) {
		return sites.containsKey(key);
	}
	/**
	 * gets name of current csv file
	 * @return filename
	 */
	public String getCurrentFile() {
		return currentFile;
	}
	/**
	 * changes the current csv file
	 * @param filename filename
	 */
	public void setCurrentFile(String filename) {
		this.currentFile = filename;
	}
	/**
	 * obtains ip address associated with location
	 * @param key
	 * @return
	 */
	public String getIP(String key) {
		return sites.get(key);
	}
	/**
	 * modifies editable value
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	/**
	 * gives a boolean value to determine if the list should be editable or not
	 * @return True if it should be, False if not
	 */
	public boolean isEditable() {
		return editable;
	}
	
	//Testing------------------------------------------------------------------------------------------------------//
	
	public static void main(String[] args) throws IOException {
		/*SiteData test =*/ new SiteData("list.csv");
		//test.readCSV();
//		System.out.println(test.getIpAddresses());
//		System.out.println(test.locationToArrayList());
		
		//test.writeCSV("list.csv");
	}

}

