package IPAddress;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class GUI extends JFrame implements ActionListener{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -619757644716801017L;
	//private InputStream is;
	public static SiteData sites;
	private static JComboBox<Object> dropdown;
	private boolean editable;
	private Dimension screenSize;

	// Swing Variables
	private JLabel plcAddress = new JLabel();
	private JLabel currentIPLabel = new JLabel();
	private JButton refresh;
	private JButton help;
	private JButton csvButton;
	private static JButton addButton;
	private static JButton removeButton;
	private static JButton editButton;
	private JButton changeButton;
	private JButton pingButton;
	private JButton routerButton;

	/**
	 * Create the frame.
	 */
	public GUI(String fileName) {		
		//Access CSV file
		//is = getClass().getResourceAsStream(fileName);
		sites = new SiteData(fileName);
		dropdown = new JComboBox<Object>(sites.locationToArray());
		dropdown.setSelectedIndex(-1);	//blank first entry

		this.editable = sites.isEditable();

		//Create GUI
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		createAndShowWindow();
	}

	/**
	 * Formats and creates the JFrame
	 */
	public void createAndShowWindow() {
		setTitle("IP Address Lookup");
		getContentPane().setBackground(new Color(234, 245, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int pWidth = (int)screenSize.getWidth()/2;			// Default Width of frame
		int pHeight = (int)screenSize.getHeight()/3*2;		// Default height of frame
		setPreferredSize(new Dimension(pWidth,pHeight));
		setLocation((int)screenSize.getWidth()/2-pWidth/2,(int)screenSize.getHeight()/2-pHeight/2);

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
			currentIPLabel.setText("IP address: " + hostIP2+ "    "); 
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}			

		// formats the 4 sides of the BorderLayout
		northSide();
		southSide();
		westSide();
		eastSide();

		//checks if the buttons should be disabled
		if(!editable) {
			disableEditButtons();
		}

		//adding action listeners
		dropdown.addActionListener(this);
		refresh.addActionListener(this);
		help.addActionListener(this);
		changeButton.addActionListener(this);
		pingButton.addActionListener(this);
		routerButton.addActionListener(this);
		editButton.addActionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		csvButton.addActionListener(this);

		pack();
		setMinimumSize(new Dimension(547, 386));
		setVisible(true);
		toBack();
	}

	/**
	 * Top Part of the GUI
	 */
	public void northSide() {
		// Icon logo on the header
		JLabel header = new JLabel();
		header.setIcon(new ImageIcon(GUI.class.getResource("/IPAddress/icons/header.png")));
		header.setBackground(new Color(3, 123, 195));
		header.setOpaque(true);
		header.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));

		// top buttons on the header
		refresh = new JButton();
		refresh.setIcon(new ImageIcon(GUI.class.getResource("/IPAddress/icons/refresh.png")));
		//refresh.setBackground(new Color(79, 154, 174));
		help = new JButton();
		help.setIcon(new ImageIcon(GUI.class.getResource("/IPAddress/icons/help.png")));
		//help.setBackground(new Color(79, 154, 174));
		header.add(refresh);
		header.add(help);
		getContentPane().add(header, BorderLayout.NORTH);
	}

	/**
	 * Bottom part of the GUI
	 */
	public void southSide() {
		JPanel footer = new JPanel();
		footer.setBackground(new Color(234, 245, 255));
		getContentPane().add(footer, BorderLayout.SOUTH);
		footer.setLayout(new BorderLayout(10, 10));
		JPanel footer2 = new JPanel();
		footer2.setBackground(new Color(234, 245, 255));
		footer2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		footer.add(footer2, BorderLayout.WEST);
		JPanel footer3 = new JPanel();
		footer3.setBackground(new Color(234, 245, 255));
		footer3.setLayout(new BoxLayout(footer3, BoxLayout.Y_AXIS));
		footer.add(footer3, BorderLayout.EAST);

		// Import/Export CSV button
		csvButton = new JButton("Import/Export CSV");
		csvButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		csvButton.setForeground(Color.BLACK);
		//csvButton.setBackground(new Color(79, 154, 174));
		footer2.add(csvButton);

		// PLC Address
		plcAddress.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		plcAddress.setForeground(Color.BLACK);
		plcAddress.setBackground(new Color(234, 245, 255));
		footer3.add(plcAddress);

		// Current IP Address
		currentIPLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		currentIPLabel.setForeground(Color.BLACK);
		currentIPLabel.setBackground(new Color(234, 245, 255));
		footer3.add(currentIPLabel);
	}

	/**
	 * The left side of the GUI
	 */
	public void westSide() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(234, 245, 255));
		getContentPane().add(leftPanel, BorderLayout.WEST);

		JLabel siteLabel = new JLabel("Site:");
		siteLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));

		// Add button
		addButton = new JButton("Add");
		addButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		addButton.setForeground(Color.BLACK);
		//addButton.setBackground(new Color(79, 154, 174));

		// Edit button
		editButton = new JButton("Edit");
		editButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		editButton.setForeground(Color.BLACK);
		//editButton.setBackground(new Color(79, 154, 174));

		// Remove button
		removeButton = new JButton("Remove");
		removeButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		removeButton.setForeground(Color.BLACK);
		//removeButton.setBackground(new Color(79, 154, 174));
		
//		editButton.setToolTipText("Edit an existing entry");
//		addButton.setToolTipText("Add a new entry");
//		removeButton.setToolTipText("Remove an existing entry");
//		csvButton.setToolTipText("Import or export a comma-separated values file");

		// Dropdown
		//dropdown.setBackground(new Color(123, 172, 220));

		// Layout of the components
		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
				gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(dropdown, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				.addGroup(gl_leftPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(siteLabel)
						.addContainerGap(257, Short.MAX_VALUE))
				.addGroup(gl_leftPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(addButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(removeButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(editButton))
				//						.addContainerGap(114, Short.MAX_VALUE))
				//				.addGroup(gl_leftPanel.createSequentialGroup()
				//						
				//						.addContainerGap(229, Short.MAX_VALUE))
				);
		gl_leftPanel.setVerticalGroup(
				gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
						.addGap(44)
						.addComponent(siteLabel)
						.addGap(18)
						.addComponent(dropdown, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_leftPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(addButton)
								.addComponent(removeButton)
								.addComponent(editButton)))
						.addGap(18)
				);
		leftPanel.setLayout(gl_leftPanel);
	}

	/**
	 * Right side of the GUI
	 */
	public void eastSide() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(new Color(234, 245, 255));
		getContentPane().add(rightPanel, BorderLayout.EAST);

		// Change IP Address button
		changeButton = new JButton("Change IP Address");
		changeButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		changeButton.setForeground(Color.BLACK);
		//changeButton.setBackground(new Color(79, 154, 174));

		// Ping TLC or Telnet button
		pingButton = new JButton("Ping TLC or Telnet");
		pingButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		pingButton.setForeground(Color.BLACK);
		//pingButton.setBackground(new Color(79, 154, 174));

		// Ping router button
		routerButton = new JButton("Ping Router");
		routerButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		routerButton.setForeground(Color.BLACK);
		//routerButton.setBackground(new Color(79, 154, 174));

		// Layout of the components
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
				gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_rightPanel.createSequentialGroup()
						.addContainerGap(347, Short.MAX_VALUE)
						.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(changeButton)
								.addGroup(gl_rightPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(routerButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(pingButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap())
				);
		gl_rightPanel.setVerticalGroup(
				gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_rightPanel.createSequentialGroup()
						.addContainerGap(90, Short.MAX_VALUE)
						.addComponent(changeButton)
						.addGap(18)
						.addComponent(pingButton)
						.addGap(18)
						.addComponent(routerButton)
						.addGap(121))
				);
		rightPanel.setLayout(gl_rightPanel);
	}

	/**
	 * action events for the dropdown and buttons
	 * @param e The ActionEvent performed
	 */
	public void actionPerformed(ActionEvent e) {
		//shows plc address of selected location
		if(e.getSource() == dropdown) {
			String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
			plcAddress.setText("PLC Address: " + sites.getValue(data)+"  "); 
			if(dropdown.getSelectedIndex() == -1) {
				plcAddress.setVisible(false);
			} else {
				plcAddress.setVisible(true);
			}
		}
		//refreshes current ip address
		if(e.getSource() == refresh) {			
			try {
				refreshIP();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//displays help window
		if(e.getSource() == help) {			
			new helpPopup();
		}

		//data from selected location
		String data = dropdown.getItemAt(dropdown.getSelectedIndex()) + "";
		String siteIp = sites.getValue(data);

		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);

		//changes user's ip address
		if(e.getSource() == changeButton) {
			try {
				String third = getSubnet(siteIp);

				if(siteIp == null) {	//error message if location is not selected
					label.setText("Please select a location.");
					JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					openCMD(third);
					refreshIP();
				}

			} catch(NullPointerException n) {	//catches subnet error if no location selected
				label.setText("Please select a location.");
				JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//pings the selected site plc
		if(e.getSource() == pingButton) {
			try {
				if(siteIp == null) {	//error message if location is not selected
					label.setText("Please select a location.");
					JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					pingCMD(siteIp);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		//pings the selected router
		if(e.getSource() == routerButton) {
			try {
				String third = getSubnet(siteIp);

				if(siteIp == null) {	//error message if location is not selected
					label.setText("Please select a location.");
					JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					routerCMD(third);
				}
			} catch(NullPointerException n) {	//catches subnet error if no location selected
				label.setText("Please select a location.");
				JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e1) {
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
		new GUI("Database.csv");
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
	 * Disables the editing buttons
	 */
	private void disableEditButtons() {
		addButton.setEnabled(false);
		editButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
	/**
	 * Enables the editing buttons
	 */
	public static void enableEditButtons() {
		addButton.setEnabled(true);
		editButton.setEnabled(true);
		removeButton.setEnabled(true);
	}
	/**
	 * Returns the dropdown list
	 * 
	 * @return	The dropdown created prior
	 */
	public static JComboBox<Object> getDropdown() {
		return dropdown;
	}

	/**
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
		dropdown.setSelectedIndex(-1);	//blank first entry
		
	}
	
	private void refreshIP() {
		String ip;
		try {
			ip = InetAddress.getLocalHost().toString();
			int slashPos = ordinalIndexOf(ip,"/",1);
			String newIp = ip.substring(slashPos + 1, ip.length());
			currentIPLabel.setText("IP address: " + newIp+"    ");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


}
