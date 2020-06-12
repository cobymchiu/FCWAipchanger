import java.awt.event.ActionEvent;

public interface Popup {
	/**
	 * creates the window and shows the popup
	 */
	void createAndShowPopup();
	/**
	 * adds the parts to panes and adds panes to window
	 */
	void addComponents();
	/**
	 * adds the necessary action listeners
	 */
	void actionPerformed(ActionEvent e);
}
