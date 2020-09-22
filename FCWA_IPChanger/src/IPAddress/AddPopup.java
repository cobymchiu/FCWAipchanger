package IPAddress;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Popup window for when the "add" button is pressed
 */
@SuppressWarnings("serial")
public class AddPopup extends JFrame implements Popup, ActionListener{

	private JPanel panel = new JPanel();
	private JLabel instructions1 = new JLabel();
	private JLabel nameLab = new JLabel("Name");
	private JLabel ipLab = new JLabel("IP Address");
	private JTextField nameAdd = new JTextField();
	private JTextField ipAdd = new JTextField();
	private JButton confirm = new JButton("Confirm");
	private SiteData sites;

	public AddPopup() {
		sites = GUI.getSites();
		createAndShowPopup();
	}

	public void createAndShowPopup() {
		setTitle("Add a new site");
		addComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void addComponents() {
		//properties
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		instructions1.setText("Enter the name and IP address of the new location.");
		
		//adding
		panel.add(instructions1);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(nameLab);
		panel.add(nameAdd);
		panel.add(ipLab);
		panel.add(ipAdd);
		panel.add(confirm);

		confirm.addActionListener(this);
		
		add(panel);

		SwingUtilities.getRootPane(confirm).setDefaultButton(confirm);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == confirm) {
			String name = nameAdd.getText();
			String ip = ipAdd.getText();
			
			JLabel lab = new JLabel();
			lab.setHorizontalAlignment(SwingConstants.CENTER);

			//input validation
			if(!sites.isValidIP(ip)) {
				lab.setText("Invalid IP. Please try again.");
				JOptionPane.showMessageDialog(null, lab, "Error", JOptionPane.PLAIN_MESSAGE);
			} else {

				//verifies whether site already exists or not
				try {
					if(sites.keyExists(name)) {	//site already exists: try again
						lab.setText("Site already exists. Please try again.");
						JOptionPane.showMessageDialog(null, lab, "Error", JOptionPane.PLAIN_MESSAGE);
						
					} else {	//site is new: added to list
						sites.addSite(name, ip);
						GUI.refreshDropdown();
						
						lab.setText("Data successfully added.");
						JOptionPane.showMessageDialog(null, lab, "Success", JOptionPane.PLAIN_MESSAGE);
						
						dispose();
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}


	//main method testing
	public static void main(String[] args) {
		new AddPopup();
	}
}