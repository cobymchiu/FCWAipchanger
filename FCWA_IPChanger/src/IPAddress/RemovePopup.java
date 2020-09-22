package IPAddress;


import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
import java.io.IOException;
/**
 * Popup window for when "remove" button is pressed
 */
@SuppressWarnings("serial")
public class RemovePopup extends JFrame implements Popup, ActionListener{
	
	private JPanel panel = new JPanel();
	private JLabel instruction1 = new JLabel();
	private JButton confirm = new JButton("Confirm");
	//private JComboBox<Object> list = new JComboBox<Object>(GUIFrame.sites.locationToArray());
	private JComboBox<Object> list = new JComboBox<Object>(GUI.sites.locationToArray());
	
	private String selected = "";	//selected dropdown option
	
	public RemovePopup() {
		createAndShowPopup();
	}
	
	public void createAndShowPopup() {
		setTitle("Remove a site");

		addComponents();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void addComponents() {
		//main popup properties
		instruction1.setText("Select a site to delete:");
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		list.setSelectedIndex(-1);	//blank first entry
		
		//adding to main panel
		panel.add(instruction1);
		panel.add(list);
		panel.add(confirm);
		add(panel);
		
		//action listeners
		confirm.addActionListener(this);	//initial confirmation
		
		SwingUtilities.getRootPane(confirm).setDefaultButton(confirm);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== confirm) {
			JLabel lab = new JLabel();
			lab.setHorizontalAlignment(SwingConstants.CENTER);
			
			selected = (String)list.getSelectedItem();

			if(selected == null) {
				lab.setText("Please choose a site.");
				JOptionPane.showMessageDialog(null, lab, "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				Object[] options = {"Yes","Cancel"};
				lab.setText("Are you sure you want to remove this site? This action is permanent.");
				int choice = JOptionPane.showOptionDialog(null, lab, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[1]);
				if(choice == JOptionPane.YES_OPTION) {	//data is removed 
					try {
						GUI.sites.removeSite(selected);
						GUI.refreshDropdown();
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					GUI.refreshDropdown();
					lab.setText("Changes have been made.");
					JOptionPane.showMessageDialog(null, lab, "Success", JOptionPane.PLAIN_MESSAGE);

					dispose();
				} else { 	//data is not removed
					lab.setText("Removal cancelled. No changes made.");
					JOptionPane.showMessageDialog(null, lab, "No Change",JOptionPane.PLAIN_MESSAGE);
				}
			}
		}

	}

	public static void main(String[] args) {
		new RemovePopup();
	}
}