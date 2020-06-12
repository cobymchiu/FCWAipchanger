
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
/**
 * Popup window for when the "edit" button is pressed
 * @author cchiu
 */
@SuppressWarnings("serial")
public class EditPopup extends JFrame{

	//initial screen
	private JPanel selectionPane = new JPanel();
	private JLabel instruction1 = new JLabel();
	private JButton confirm = new JButton("Confirm");
	private JComboBox<Object> list = new JComboBox<Object>(GUIFrame.sites.locationToArray());

	//editing screen
	private JPanel editingPane = new JPanel();
	private JPanel inputPane = new JPanel();
	private JLabel instruction2 = new JLabel();
	private JButton confirm2 = new JButton("Confirm");
	private JTextField nameField = new JTextField();
	private JTextField ipField = new JTextField();

	//popup windows
	private JFrame popup = new JFrame();
	private JPanel popupPane = new JPanel();
	private JLabel message = new JLabel();
	private JButton closepop = new JButton("Close");
	private JFrame overridingPopup = new JFrame();
	private JPanel oPane = new JPanel();
	private JPanel buttonPane = new JPanel();
	private JLabel message2 = new JLabel();
	private JButton yes = new JButton("Yes");
	private JButton no = new JButton("No");


	private String selected = "";	//selected item from dropdown

	public EditPopup() {
		createAndShowPopup();
	}

	/**
	 * creates and displays the edit frame
	 */
	private final void createAndShowPopup() {
		setTitle("Edit list of sites");
		setLayout(new GridLayout(2,1,5,0));

		addComponents();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * adds components to panels then adds panels to frame
	 */
	private void addComponents() {
		//properties
		instruction1.setText("Select a location to edit:");
		instruction2.setText("Edit values");
		instruction2.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirm2.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		editingPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		editingPane.setLayout(new BoxLayout(editingPane, BoxLayout.Y_AXIS));
		nameField.setColumns(10);
		ipField.setColumns(10);
		
		//adding first panel
		selectionPane.add(instruction1);
		selectionPane.add(list);
		selectionPane.add(confirm);

		//adding second panel
		editingPane.add(instruction2);
		inputPane.add(new JLabel("Location name:"));
		inputPane.add(nameField);
		inputPane.add(new JLabel("IP Address:"));
		inputPane.add(ipField);
		editingPane.add(inputPane);
		editingPane.add(confirm2);

		//popup
		popupPane.setLayout(new BoxLayout(popupPane, BoxLayout.Y_AXIS));
		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		closepop.setAlignmentX(Component.CENTER_ALIGNMENT);
		popupPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		popupPane.add(message);
		popupPane.add(closepop);
		popup.add(popupPane);
		
		//override confirm popup
		oPane.setLayout(new BoxLayout(oPane, BoxLayout.Y_AXIS));
		message2.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPane.add(yes);
		buttonPane.add(no);
		oPane.add(message2);
		overridingPopup.add(oPane);
		oPane.add(buttonPane);
		
		add(selectionPane);


		//action listener for confirm button
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add(editingPane);
				selected = (String)list.getSelectedItem();
				nameField.setText(selected);
				ipField.setText(GUIFrame.sites.getIP(selected));
				pack();
			}
		});

		//action listener for editing confirmation
		confirm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newName = nameField.getText();
				String newIp = ipField.getText();
				String oldIp = GUIFrame.sites.getValue(selected);
				//checks if ip is valid
				if(!isValidIP(newIp)) {
					popup.setVisible(true);
					message.setText("Invalid IP. Try again.");
					popup.pack();
					popup.setLocationRelativeTo(null);
				} else {
					//checks if name already exists and if user wants to override
					if(newName.equals(selected) && !oldIp.equals(newIp)) {
						overridingPopup.setVisible(true);
						message2.setText("This location already exists. Would you like to override?");
						overridingPopup.pack();
						overridingPopup.setLocationRelativeTo(null);
					} else {	//ip is valid and location name is unique
						//checks if that same data point already exists
						boolean worked = false;
						try {
							worked = GUIFrame.sites.editSite(selected, oldIp, newName, newIp);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						if(!worked) {
							//System.out.println("nothing changed");
							popup.setVisible(true);
							message.setText("Input is same as original data or already exists. No changes have been made.");
							popup.pack();
							popup.setLocationRelativeTo(null);
						} else {
							//System.out.println("changed");
							GUIFrame.refreshDropdown();
							popup.setVisible(true);
							message.setText("Changes have been made.");
							popup.pack();
							popup.setLocationRelativeTo(null);
							dispose();
						}
					}
				}
			}
		});

		//action listener for popup close button
		closepop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.dispose();
			}
		});
		
		//actionlistener for yes no buttons
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newName = nameField.getText();
				String newIp = ipField.getText();
				String oldIp = GUIFrame.sites.getValue(selected);
				
				try {
					GUIFrame.sites.editSite(selected, oldIp, newName, newIp);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GUIFrame.refreshDropdown();
				
				overridingPopup.dispose();
				popup.setVisible(true);
				message.setText("Changes have been made.");
				popup.pack();
				popup.setLocationRelativeTo(null);
				dispose();
			}
		});
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				overridingPopup.dispose();
			}
		});
		
		SwingUtilities.getRootPane(confirm).setDefaultButton(confirm);
	}

	/**
	 * checks if ip address is valid
	 * @param ip
	 * @return
	 */
	private boolean isValidIP(String ip) {
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
		System.out.println("made it through . test");

		//checks the ranges of each part and if they are numbers
		String[] arr = {p1,p2,p3,p4};
		try {
			for(String s: arr) {
				int num = Integer.parseInt(s);

				if(num < 0 || num > 255) {
					System.out.println(num+ ": term out of bounds");
					return false;
				}
			}
		} catch(NumberFormatException e) {
			System.out.println("not a number");
			return false;
		}
		
		//checks if first term is 0
		if(Integer.parseInt(p1) == 0) {
			return false;
		}
		System.out.println(p1+p2+p3+p4);
		return true;
	}

	//main method testing
	public static void main(String[] args) {
		new EditPopup();
	}
}