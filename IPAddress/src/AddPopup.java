
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Popup window for when the "add" button is pressed
 * @author cchiu
 */
@SuppressWarnings("serial")
public class AddPopup extends JFrame{

	//main panel
	private JPanel panel = new JPanel();
	private JLabel instructions1 = new JLabel();
	private JLabel instructions2 = new JLabel();
	private JLabel nameLab = new JLabel("Name");
	private JLabel ipLab = new JLabel("IP Address");
	private JTextField nameAdd = new JTextField();
	private JTextField ipAdd = new JTextField();
	private JButton confirm = new JButton("Confirm");

	//popup dialog
	private JFrame popup = new JFrame();
	private JPanel panel2 = new JPanel(); 
	private JLabel message = new JLabel();
	private JButton close = new JButton("Close");


	public AddPopup() {
		createAndShowPopup();
	}

	/**
	 * creates and displays the add frame
	 */
	private void createAndShowPopup() {
		setTitle("Add a new site");
		addComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * adds the components of the frame to the panels then adds the panels to the frame
	 */
	private void addComponents() {
		//main screen properties
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		instructions1.setText("Enter the name and IP address of the new location.");
		instructions2.setText("Or upload a comma separated file (CSV)");
		//adding main components
		panel.add(instructions1);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(nameLab);
		panel.add(nameAdd);
		panel.add(ipLab);
		panel.add(ipAdd);
		panel.add(confirm);
		//panel.add(Box.createRigidArea(new Dimension(0,20)));

		//properties of dialog popup
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		//adding to popup
		panel2.add(message);
		panel2.add(close);
		popup.add(panel2);
		close.setAlignmentX(Component.CENTER_ALIGNMENT);
		message.setAlignmentX(Component.CENTER_ALIGNMENT);


		//close button action listener
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				popup.dispose();
			}
		});

		//confirm button action listener - takes in submitted values
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String name = nameAdd.getText();
				String ip = ipAdd.getText();
				
				//input validation
				if(!isValidIP(ip)) {
					message.setText("Invalid IP. Please try again.");
					popup.setVisible(true);
					popup.pack();
					popup.setLocationRelativeTo(null);
				} else {

					//verifies whether site already exists or not
					try {
						if(GUIFrame.sites.keyExists(name)) {	//site already exists: try again
							message.setText("Site already exists. Please try again.");
							popup.setVisible(true);
							popup.pack();
							popup.setLocationRelativeTo(null);
						} else {	//site is new: added to list
							GUIFrame.sites.addSite(name, ip);
							GUIFrame.refreshDropdown();
							message.setText("Data successfully added.");
							popup.setVisible(true);
							popup.pack();
							popup.setLocationRelativeTo(null);
							dispose();
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		add(panel);
		
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
		new AddPopup();
	}
}