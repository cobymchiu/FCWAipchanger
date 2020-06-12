
import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
import java.io.IOException;
/**
 * Popup window for when "remove" button is pressed
 * @author cchiu
 */
@SuppressWarnings("serial")
public class RemovePopup extends JFrame{
	
	//main window variables
	private JPanel panel = new JPanel();
	private JLabel instruction1 = new JLabel();
	private JLabel instruction2 = new JLabel();
	private JButton confirm = new JButton("Confirm");
	private JComboBox<Object> list = new JComboBox<Object>(GUIFrame.sites.locationToArray());
	//popup window variables
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JButton confirm2 = new JButton("Confirm");
	private JButton cancel = new JButton("Cancel");
	private JFrame popup = new JFrame();
	
	private String selected = "";	//selected dropdown option
	
	public RemovePopup() {
		createAndShowPopup();
	}
	
	/**
	 * creates and shows frame
	 */
	private void createAndShowPopup() {
		setTitle("Remove a site");

		addComponents();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * adds components to panels then adds panels to frame
	 */
	private void addComponents() {
		//main popup properties
		instruction1.setText("Select a site to delete:");
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//dialog box properties
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		confirm2.setAlignmentX(Component.CENTER_ALIGNMENT);
		instruction2.setText("Are you sure you want to remove this site? This action is permanent.");
		instruction2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//adding to main panel
		panel.add(instruction1);
		panel.add(list);
		panel.add(confirm);
		add(panel);
		
		//adding to dialog box
		panel2.add(instruction2);
		panel3.add(confirm2);
		panel3.add(cancel);
		panel2.add(panel3);
		popup.add(panel2);
		
		//action listener for confirm button on dialog box
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.setTitle("Confirmation");
				popup.pack();
				popup.setLocationRelativeTo(null);
				popup.setVisible(true);
			}
		});
		
		//action listener for final confirmation
		confirm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = (String)list.getSelectedItem();
				//System.out.println(selected);
				try {
					GUIFrame.sites.removeSite(selected);
					GUIFrame.refreshDropdown();
					popup.dispose();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//action listener for cancel button
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.dispose();
			}
		});
		
		SwingUtilities.getRootPane(confirm).setDefaultButton(confirm);
		popup.getRootPane().setDefaultButton(confirm2);
	}
	
	public static void main(String[] args) {
		new RemovePopup();
	}
}