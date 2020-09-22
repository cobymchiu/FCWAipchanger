package IPAddress;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.*;
/**
 * Popup window for when the "edit" button is pressed
 */
@SuppressWarnings("serial")
public class EditPopup extends JFrame implements Popup, ActionListener{

	//initial screen
	private JPanel selectionPane = new JPanel();
	private JLabel instruction1 = new JLabel();
	private SiteData sites;
	//private JComboBox<Object> list = new JComboBox<Object>(GUIFrame.sites.locationToArray());
	private JComboBox<Object> list = new JComboBox<Object>(GUI.sites.locationToArray());

	//editing screen
	private JPanel editingPane = new JPanel();
	private JPanel inputPane = new JPanel();
	private JLabel instruction2 = new JLabel();
	private JButton confirm = new JButton("Confirm");
	private JTextField nameField = new JTextField();
	private JTextField ipField = new JTextField();

	private String selected = "";	//selected item from dropdown

	/**
	 * constructor
	 */
	public EditPopup() {
		sites = GUI.getSites();
		createAndShowPopup();
	}

	public final void createAndShowPopup() {
		setTitle("Edit list of sites");

		addComponents();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void addComponents() {
		//properties
		instruction1.setText("Select a location to edit:");
		instruction2.setText("Edit values");
		instruction2.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		editingPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		editingPane.setLayout(new BoxLayout(editingPane, BoxLayout.Y_AXIS));
		nameField.setColumns(10);
		ipField.setColumns(10);
		list.setSelectedIndex(-1);	//blank first entry

		//adding first panel
		selectionPane.add(instruction1);
		selectionPane.add(list);

		//adding second panel
		editingPane.add(instruction2);
		inputPane.add(new JLabel("Location name:"));
		inputPane.add(nameField);
		inputPane.add(new JLabel("IP Address:"));
		inputPane.add(ipField);
		editingPane.add(inputPane);
		editingPane.add(confirm);

		add(selectionPane);

		confirm.addActionListener(this);
		
		//gets the selected item in the list
		list.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					add(editingPane, BorderLayout.SOUTH);
					selected = (String)list.getSelectedItem();
					nameField.setText(selected);
					ipField.setText(sites.getIP(selected));
					pack();
					setLocationRelativeTo(null);
				}
			}
		});
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//editing confirm
		if(e.getSource() == confirm) {
			String newName = nameField.getText();
			String newIp = ipField.getText();
			String oldIp = sites.getValue(selected);
			
			JLabel lab = new JLabel();
			lab.setHorizontalAlignment(SwingConstants.CENTER);
			
			//input validation
			if(!sites.isValidIP(newIp)) {
				lab.setText("Invalid IP. Try again.");
				JOptionPane.showMessageDialog(null, lab, "Error", JOptionPane.PLAIN_MESSAGE);
			} else {
				//checks if name already exists and if user wants to overwrite
				if(newName.equals(selected) && !oldIp.equals(newIp)) {
					lab.setText("This location already exists. Would you like to overwrite?");
					int choice = JOptionPane.showConfirmDialog(null, lab, "Overwriting", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {	//data is overwritten
						try {
							sites.editSite(selected, oldIp, newName, newIp);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						GUI.refreshDropdown();
						lab.setText("Changes have been made.");
						JOptionPane.showMessageDialog(null, lab, "Success", JOptionPane.PLAIN_MESSAGE);
						
						dispose();
					} else { 	//data is not overwritten
						lab.setText("Data was not overwritten. No changes made.");
						JOptionPane.showMessageDialog(null, lab, "No Change",JOptionPane.PLAIN_MESSAGE);
					}
					
				} else {	//ip is valid and location name is unique
					
					boolean worked = false;
					try {
						worked = sites.editSite(selected, oldIp, newName, newIp);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//checks if that same data point already exists
					if(!worked) {
						lab.setText("Input is same as original data or already exists. No changes have been made.");
						JOptionPane.showMessageDialog(null, lab, "No Change", JOptionPane.PLAIN_MESSAGE);
					} else {
						GUI.refreshDropdown();
						lab.setText("Changes have been made.");
						JOptionPane.showMessageDialog(null, lab, "Success", JOptionPane.PLAIN_MESSAGE);
						
						dispose();
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		//new EditPopup();
	}
}
