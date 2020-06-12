
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class GUIFrame extends JFrame implements ActionListener {

	InputStream is = getClass().getResourceAsStream("list.csv");
	static SiteData sites = new SiteData("list.csv");	//this is where the current csv file should be changed - make it a file location instead of name
	//swing variables
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
	private static JComboBox<Object> dropdown = new JComboBox<Object>(sites.locationToArray());

	/**
	 * constructor for frame object
	 */
	public GUIFrame() {
		createAndShowWindow();
	}

	/**
	 * method to create and show GUI
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

	private void setComponentProperties() {
		//instructions
		instructionLabel.setText("<HTML>Instructions<BR>---------------------------------<BR>1.Plug into network switch.<BR>2.Choose site.<BR>3.Change Computer IP.<BR>4.Refresh IP address.</HTML>");

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
	
	
	private void addComponentsToPanels() {
		//add buttons to right panel
		rightPanel.setLayout(new GridLayout(4,0,1,20));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		rightPanel.add(plcAddress);
		rightPanel.add(changeButton);
		rightPanel.add(pingPLCButton);
		rightPanel.add(pingRouterButton);
		
		//add and format left side
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

		//add top part
		topPanel.add(currentIPLabel);
		topPanel.add(refreshButton);
	}
	
	/**
	 * action events for the dropdown and buttons
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		//shows plc address of selected location
		if(e.getSource() == dropdown) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			plcAddress.setText("PLC Address: " + sites.getValue(data)); 
		}
		//refreshes current ip address
		if(e.getSource() == refreshButton) {
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
			openCMD(third); //method that does the changing	
		}
		//pings the selected site plc
		if(e.getSource() == pingPLCButton) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
				String siteIp = new String();
				siteIp = sites.getValue(data);
				pingCMD(siteIp);  //the method that does stuff 
		}
		//pings the selected router
		if(e.getSource() == pingRouterButton) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			String siteIp = new String();
			siteIp = sites.getValue(data);
			String third = "";
			third = getSubnet(siteIp);
			routerCMD(third); //the method that does stuff 
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
			new CSVPopup();
		}
		
	}

	//main method testing
	public static void main(String[] args) {
		new GUIFrame();
	}

	//helper methods-----------------------------------------------------------------------------------------------//
	
	/**
	 * unsure of use -- seems to find position of character
	 * @param data
	 * @param substr
	 * @param n
	 * @return
	 */
	public static int ordinalIndexOf(String data, String substr, int n) {

		int pos = data.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = data.indexOf(substr, pos + 1);
		return pos;
	}
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String getSubnet(String data) {
		String num = data;
		int pt1 = ordinalIndexOf(num,".",2);
		int pt2 = ordinalIndexOf(num,".",3);
		return num.substring(pt1+1, pt2);
	}

	//command prompt methods i haven't touched from original code
	private String error = "An error has occurred.";
	public void openCMD(String octet3) {
		try
		{
			Runtime runTime = Runtime.getRuntime();
			String argCommand = new String();
			String netmask = "255.255.255.0";
			String gw = "172.16." + octet3 + ".1";
			argCommand = "cmd.exe /c start cmd /k netsh interface ipv4 set address \"Local Area Connection\" static 172.16." + octet3 + ".19 " + netmask + " " + gw + " 1";	 
			runTime.exec(argCommand);
		}	
		catch (Exception e)
		{
			System.out.println(error);
			e.printStackTrace();
		}
	}

	public void pingCMD(String data) {
		try
		{
			Runtime runTime = Runtime.getRuntime();
			String argCommand = new String();
			argCommand = "cmd.exe /c start cmd /k ping " + data;	 
			runTime.exec(argCommand);
			System.out.println(argCommand);
		}	
		catch (Exception e)
		{
			System.out.println(error);
			e.printStackTrace();
		}	
	}

	public void routerCMD(String octet3) {
		try
		{
			Runtime runTime = Runtime.getRuntime();
			String argCommand = new String();
			argCommand = "cmd.exe /c start cmd /k ping 172.16." + octet3 + ".1";	 
			runTime.exec(argCommand);
			System.out.println(argCommand);
		}	
		catch (Exception e)
		{
			System.out.println(error);
			e.printStackTrace();
		}
	}

	public static JComboBox<Object> getDropdown() {
		return dropdown;
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
