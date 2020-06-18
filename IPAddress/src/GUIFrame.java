
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class GUIFrame extends JFrame implements ActionListener {

	// Local fields
	InputStream is;
	public static SiteData sites;
	private static JComboBox<Object> dropdown;
	
	// Local Swing variables
	private JLabel instructionLabel = new JLabel();
	private JLabel currentIPLabel = new JLabel();
	private JLabel plcAddress = new JLabel();
	private JButton refreshButton = new JButton();
	private JButton changeButton = new JButton();
	private JButton pingPLCButton = new JButton();
	private JButton pingRouterButton = new JButton();
	private JButton editButton = new JButton();
	private JButton addButton = new JButton();
	private JButton removeButton = new JButton();
	private JButton csvButton = new JButton();
	private JPanel topPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();

	/**
	 * Constructor for frame object
	 */
	public GUIFrame(String fileName) {
		//Access CSV file
		is = getClass().getResourceAsStream(fileName);
		sites = new SiteData(fileName);
		dropdown = new JComboBox<Object>(sites.locationToArray());
		
		//Create GUI
		createAndShowWindow();
	}

	/**
	 * Initializes GUI and sets visibility
	 */
	public final void createAndShowWindow() {
		//window properties
		setTitle("IP Address Lookup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add parts
		setComponentProperties();
		addComponentsToPanels();
		add(topPanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initializes all the components
	 * 
	 * - Instructions, IP, buttons, ACtionListeners
	 */
	private void setComponentProperties() {
		//instructions
		instructionLabel.setText("<HTML>Instructions<BR>---------------------------------<BR>"
				+ "1.Plug into network switch.<BR>"
				+ "2.Choose site.<BR>"
				+ "3.Change Computer IP.<BR>"
				+ "4.Refresh IP address.</HTML>");

		//finding IP
		String hostIP; 
		String hostIP2;
		int slash_pos;
		InetAddress ip;
		//finding current IP address - gets inetaddress and turns into string
		try {
			ip = InetAddress.getLocalHost();
			hostIP = ip.toString();
			slash_pos = ordinalIndexOf(hostIP, "/", 1);
			hostIP2 = hostIP.substring(slash_pos+1, hostIP.length());
			currentIPLabel.setText("Your current IP address: " + hostIP2); 
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}

		//button formatting
		refreshButton.setText("Refresh IP");
		refreshButton.setSize(100, 20);
		editButton.setText("Edit");
		editButton.setToolTipText("Edit an existing entry");
		addButton.setText("Add");
		addButton.setToolTipText("Add a new entry");
		removeButton.setText("Remove");
		removeButton.setToolTipText("Remove an existing entry");
		csvButton.setText("Import/Export CSV");
		csvButton.setToolTipText("Import or export a comma-separated values file");
		changeButton.setText("Change Computer IP");
		pingPLCButton.setText("Ping PLC or Telnet");
		pingRouterButton.setText("Ping Router");
		
		
		//adding action listeners
		dropdown.addActionListener(this);
		refreshButton.addActionListener(this);
		changeButton.addActionListener(this);
		pingPLCButton.addActionListener(this);
		pingRouterButton.addActionListener(this);
		editButton.addActionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		csvButton.addActionListener(this);
	}
	
	/**
	 * Adds all the components to each panel
	 * 
	 * 	Right: PLCAddress, changePLC, pingPLC, pingRouter
	 * 	Left: Site, add, remove, edit, import/export, Instructions
	 * 	Top: IPAddress, refresh
	 */
	private void addComponentsToPanels() {
		//add and format right panel
		rightPanel.setLayout(new GridLayout(4,0,1,20));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		rightPanel.add(plcAddress);
		rightPanel.add(changeButton);
		rightPanel.add(pingPLCButton);
		rightPanel.add(pingRouterButton);
		
		//add and format left panel
		JPanel buttonPane = new JPanel();
		buttonPane.setMaximumSize(new Dimension(205, 30));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(editButton);
		buttonPane.add(addButton);
		buttonPane.add(removeButton);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		leftPanel.setMaximumSize(new Dimension(205,300));
		dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropdown.setMaximumSize(new Dimension(210,40));
		leftPanel.add(dropdown);
		leftPanel.add(buttonPane);
		leftPanel.add(csvButton);
		leftPanel.add(instructionLabel);

		//add and format top panel
		topPanel.add(currentIPLabel);
		topPanel.add(refreshButton);
	}
	
	/**
	 * action events for the dropdown and buttons
	 * @param e The ActionEvent performed
	 */
	public void actionPerformed(ActionEvent e) {
		//shows plc address of selected location
		if(e.getSource() == dropdown) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			plcAddress.setText("PLC Address: " + sites.getValue(data)); 
		}
		//refreshes current ip address
		else if(e.getSource() == refreshButton) {			
			try {
				String ip = InetAddress.getLocalHost().toString();
				int slashPos = ordinalIndexOf(ip,"/",1);
				String newIp = ip.substring(slashPos + 1, ip.length());
				currentIPLabel.setText("Your current IP address: " + newIp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//changes user's ip address
		if(e.getSource() == changeButton) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			String siteIp = new String();
			siteIp = sites.getValue(data);
			String third = "";
			third = getSubnet(siteIp);
			try {
				openCMD(third);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//pings the selected site plc
		if(e.getSource() == pingPLCButton) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
				String siteIp = new String();
				siteIp = sites.getValue(data);
				try {
					pingCMD(siteIp);
				} catch (IOException e1) {
					e1.printStackTrace();
				}  
		}
		//pings the selected router
		if(e.getSource() == pingRouterButton) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			String siteIp = new String();
			siteIp = sites.getValue(data);
			String third = "";
			third = getSubnet(siteIp);
			try {
				routerCMD(third);
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
		}
		//creates an edit popup
		if(e.getSource() == editButton) {
			new EditPopup();
		}
		//creates an add popup
		if(e.getSource() == addButton) {
			new AddPopup();
		}
		//creates remove popup
		if(e.getSource() == removeButton) {
			new RemovePopup();
		}
		//creates a csv popup
		if(e.getSource() == csvButton) {
			new CSVPopup(sites);
		}
		
	}
 
	//Testing------------------------------------------------------------------------------------------------------//
	public static void main(String[] args) {
		new GUIFrame("list.csv");
	}

	//Helper Methods-----------------------------------------------------------------------------------------------//
	
	/**
	 * Finds the nth occurrence of a substring within a string
	 * 
	 * @param data	
	 * 		The larger string
	 * @param substr	
	 * 		The encapsulated string
	 * @param n		
	 * 		The desired number of occurrences
	 * @return
	 * 		The index of the nth occurrence of a substring and
	 * 		-1 if substr is not within data
	 */
	public static int ordinalIndexOf(String data, String substr, int n) {

		int pos = data.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = data.indexOf(substr, pos + 1);
		return pos;
	}
	
	/**
	 * Counts the number of occurrences of a character within a string
	 * 
	 * @param str	String to be parsed through
	 * @param target	Character being searched for
	 * @return
	 * 		The number of occurrences of target within str
	 */
	private static int countOccurrences(String str, char target) {
		int count = 0;
		  
		for (int i = 0; i < str.length(); i++) {
		    if (str.charAt(i) == target) {
		        count++;
		    }
		}
		return count;
	}
	
	/**
	 * Determines the 3rd number within an IP Address
	 * Finds the substring between the 2nd and 3rd "."
	 * 
	 * @param data
	 * 		The IP Address
	 * @return
	 * 		The 3rd number of the IP Address and null if
	 * 		the IP is invalid.
	 */
	public static String getSubnet(String data) {
		// Test that the IP is valid
		if (countOccurrences(data, '.') != 3) return null;
		
		int pt1 = ordinalIndexOf(data,".",2);
		int pt2 = ordinalIndexOf(data,".",3);
		
		return data.substring(pt1+1, pt2);
	}

	/**
	 * Opens the command prompt and changes the device IP Address
	 * 
	 * @param octet3
	 * 		The third number of the IP Address
	 * @throws IOException 
	 */
	public void openCMD(String octet3) throws IOException {
		Runtime runTime = Runtime.getRuntime();
		String argCommand = new String();
		String netmask = "255.255.255.0";
		String gw = "172.16." + octet3 + ".1";
		argCommand = "cmd.exe /c start cmd /k netsh interface ipv4 set address "
				+ "\"Local Area Connection\" static 172.16." + octet3 + ".19 " + netmask + " " + gw + " 1";	 
		runTime.exec(argCommand);
	}

	/**
	 * runs the ping command in the command prompt
	 * 
	 * @param data
	 * 		The destination IP Address
	 * @throws IOException 
	 */
	public void pingCMD(String data) throws IOException {
		Runtime runTime = Runtime.getRuntime();
		String argCommand = new String();
		argCommand = "cmd.exe /c start cmd /k ping " + data;	 
		runTime.exec(argCommand);
	}

	/**
	 * runs the ping command in the command prompt with the router
	 * 
	 * @param octet3
	 * 		The destination IP Address
	 * @throws IOException 
	 */
	public void routerCMD(String octet3) throws IOException {
		Runtime runTime = Runtime.getRuntime();
		String argCommand = new String();
		argCommand = "cmd.exe /c start cmd /k ping 172.16." + octet3 + ".1";	 
		runTime.exec(argCommand);
	}

	/**
	 * Returns the dropdown list
	 * 
	 * @return	The dropdown created prior
	 */
	public static JComboBox<Object> getDropdown() {
		return dropdown;
	}
	
	/*
	 * Gets the current state of the sites
	 */
	public static SiteData getSites() {
		return sites;
	}

	/**
	 * refreshes the dropdown on the menu
	 */
	public static void refreshDropdown() {
		dropdown.removeAllItems();
		for(String s: sites.locationToArrayList()) {
			dropdown.addItem(s);
		}
	}
}
