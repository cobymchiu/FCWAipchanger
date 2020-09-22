package IPAddress;

import java.awt.Color; 
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * driver for IP address program
 * 
 */
public class IPAddressDriver {
	
	public static void main(String[] args) {
		setAppearances();
		
		//new GUIFrame("Database.csv");
		new GUI("Database.csv");
	}
	
	
	private static void setAppearances() {		
		
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Color lightBlue = new Color(220, 238, 250);
		Color blueGreen = new Color(79, 154, 174);
		Color darkBlue = new Color(123, 172, 220);
		
		UIManager.put("OptionPane.background", lightBlue);
		UIManager.put("Panel.background", lightBlue);
		UIManager.put("control", lightBlue);
		UIManager.getLookAndFeelDefaults().put("Button.background", blueGreen);
		UIManager.put("controlHighlight", darkBlue);
		UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Roboto", Font.PLAIN, 12));	//not sure if we want to change the default, but we can
		
	}
	
}