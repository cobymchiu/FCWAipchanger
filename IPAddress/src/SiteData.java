
/**
 * class that houses the data and methods for modifying data
 */
import java.io.*;
import java.util.*;

public class SiteData {

//	InputStream is = getClass().getResourceAsStream("list.csv");
//	InputStreamReader isr = new InputStreamReader(is);
	
	//site data storage
	private HashMap<String,String> sites = new HashMap<String,String>();
	private HashMap<String,String> newSites = new HashMap<String,String>();
	private String currentFile;	

	/**
	 * constructor sets name of current file and reads in the csv file 
	 * @param currentFile name of current file to read/write data to
	 */
	public SiteData(String currentFile) {
		this.currentFile = currentFile;
		readCSV();
	}

	
	/**
	 * reads in csv file
	 */
	public void readCSV() {
		//InputStream input = getClass().getResourceAsStream(currentFile);
		BufferedReader reader = null;
		String line = "";
		
		//reads each non-null line of the file then adds the data to the hashmap as key-value pairs	
		try {
			
			reader = new BufferedReader(new FileReader(currentFile));
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				System.out.println(data[0] + " " +data[1]);
				if(data.length>0) {
					sites.put(data[0], data[1]);
				}
			}
		} catch (FileNotFoundException f) {
			//add message saying file wasn't found
			
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * writes data to csv file
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
	 * csv reader for importing
	 * @param filepath
	 * @param merge
	 */
	public void readCSV(String filepath, boolean merge) {
		BufferedReader reader = null;
		String line = "";
		
		//reads each non-null line of the file then adds the data to the hashmap as key-value pairs	
		try {
			reader = new BufferedReader(new FileReader(filepath));
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				System.out.println(data[0] + " " +data[1]);
				if(data.length > 0) {	//checks if there was data on that line
					newSites.put(data[0], data[1]);
					if(merge) {		//determines whether to merge or replace the old and new file
						appendCSV(currentFile);
					} else {
						//to do: clear the current file then add these values
						writeCSV(currentFile);
					}
				}
			}
			reader.close();
		} catch (NullPointerException n) {
			System.out.println("null pointer exception");
			n.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
		
		if(result != null) {
			return false;
		} else {
			return true;
		}
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
		
		if(result == null) {
			return false;
		} else {
			return true;
		}
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
				System.out.println("same values");
				return false;
			} else {
				System.out.println("same name");
				sites.replace(oldName, newIp);
				refreshData(currentFile);
			}
		} else {
			System.out.println("changing data");
			sites.remove(oldName);
			sites.put(newName, newIp);
			refreshData(currentFile);
		}
		
		return true;
	}
	/**
	 * imports a given file and either merges or replaces the original csv file
	 * @param filename name of file to import
	 * @param merge states whether the new and old file will be merged or not
	 * @return true if successful, false otherwise
	 */
	public boolean importFile(String filename, boolean merge) {
		if(merge) {
			
		} else {
			
		}
		return true;
	}
	/**
	 * exports the current list of sites to a specified location
	 * @param location directory location of file
	 * @param name name to be given to file
	 * @return true if successful, false otherwise
	 */
	public boolean exportFile(String location, String name) {
		
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
	 * obtains ip address associated wiht location
	 * @param key
	 * @return
	 */
	public String getIP(String key) {
		return sites.get(key);
	}

	
	
	public static void main(String[] args) throws IOException {
		/*SiteData test =*/ new SiteData("list.csv");
		//test.readCSV();
//		System.out.println(test.getIpAddresses());
//		System.out.println(test.locationToArrayList());
		
		//test.writeCSV("list.csv");
	}

}
